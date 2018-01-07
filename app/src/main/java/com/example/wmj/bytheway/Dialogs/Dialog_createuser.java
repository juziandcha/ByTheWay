package com.example.wmj.bytheway.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wmj.bytheway.Activities.ByTheWayActivity;
import com.example.wmj.bytheway.ConnSup.MD5;
import com.example.wmj.bytheway.R;
import com.example.wmj.bytheway.Util.GetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wmj on 2018/1/4.
 */

public class Dialog_createuser extends DialogFragment{
    private EditText Edit_name;
    private EditText Edit_password;
    private EditText Edit_password2;
    private Button btn_create;

    private Dialog_createuser.DialogClickListener listener;

    public interface DialogClickListener{
        void onDialogClick(String Name,String Password);
    }

    public void setOnDialogClick(Dialog_createuser.DialogClickListener listener){
        this.listener=listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View view = inflater.inflate(R.layout.dialog_createuser, null);

        Edit_name=(EditText)view.findViewById(R.id.user_create);
        Edit_password=(EditText)view.findViewById(R.id.password_create);
        Edit_password2=(EditText)view.findViewById(R.id.password_create_again);
        btn_create=(Button)view.findViewById(R.id.create_new_user);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=Edit_name.getText().toString().trim();
                String password1=Edit_password.getText().toString().trim();
                String password2=Edit_password2.getText().toString().trim();

                if(name.equals("")||password1.equals("")||password2.equals("")){
                    Toast.makeText(getActivity(), "输入不能为空", Toast.LENGTH_SHORT).show();
                } else if(name.length()>20){
                    Toast.makeText(getActivity(), "用户名过长", Toast.LENGTH_SHORT).show();
                } else if(password1.length()>20||password2.length()>20){
                    Toast.makeText(getActivity(), "密码过长", Toast.LENGTH_SHORT).show();
                } else if(!password1.equals(password2)){
                    Toast.makeText(getActivity(), "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        //数据库通信
                        //判断是否已存在此用户名
                        String sql="SELECT * FROM User WHERE ID=?";
                        JSONObject keyValue=new JSONObject();
                        keyValue.put("1",name);
                        GetData.runGetData(sql,"query",keyValue);
                        JSONArray jsonArray=new JSONArray(ByTheWayActivity.dataResult);
                        if(jsonArray.length()!=0){
                            Toast.makeText(getActivity(), "当前账户名已存在", Toast.LENGTH_SHORT).show();
                        }else {
                            //注意JsonObject内部定义是没有顺序的

                            //先插入到User表中
                            sql = "INSERT INTO User VALUES (?,?);";
                            String md5 = MD5.getMD5(password1);
                            keyValue = new JSONObject();
                            keyValue.put("1", name);
                            keyValue.put("2", md5);

                            //获取插入到User表中的结果
                            GetData.runGetData(sql, "update", keyValue);
                            JSONObject jsonObjUser = new JSONObject(ByTheWayActivity.dataResult);

                            //之后插入数据到Person表中
                            sql="INSERT INTO Person VALUES(?,null,null,null);";
                            keyValue=new JSONObject();
                            keyValue.put("1",name);

                            //获取插入到Person表中的结果
                            GetData.runGetData(sql, "update", keyValue);
                            JSONObject jsonObjPerson = new JSONObject(ByTheWayActivity.dataResult);


                            if (jsonObjUser.getString("succeed").equals("false")||jsonObjPerson.getString("succeed").equals("false")) {
                                Toast.makeText(getActivity(), "注册失败，请检查网络连接", Toast.LENGTH_SHORT).show();

                                //如果只是一部分插入失败了，那么另外一部分要撤销
                                //判断User表是否插入成功，成功则撤销
                                while(jsonObjUser.getString("succeed").equals("true")){
                                    sql="DELETE FROM User WHERE ID=?;";
                                    keyValue=new JSONObject();
                                    keyValue.put("1",name);

                                    GetData.runGetData(sql, "update", keyValue);
                                    jsonObjUser = new JSONObject(ByTheWayActivity.dataResult);
                                }

                                //判断Person表是否插入成功，成功则撤销
                                while(jsonObjPerson.getString("succeed").equals("true")){
                                    sql="DELETE FROM Person WHERE ID=?;";
                                    keyValue=new JSONObject();
                                    keyValue.put("1",name);

                                    GetData.runGetData(sql, "update", keyValue);
                                    jsonObjPerson = new JSONObject(ByTheWayActivity.dataResult);
                                }
                            } else {
                                Toast.makeText(getActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        }
                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }catch (Exception ex){
                        Toast.makeText(getActivity(), "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        builder.setView(view);

        return builder.create();
    }
}
