package com.example.wmj.bytheway.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmj.bytheway.Activities.ByTheWayActivity;
import com.example.wmj.bytheway.ConnSup.MD5;
import com.example.wmj.bytheway.R;
import com.example.wmj.bytheway.Util.GetData;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by wmj on 2017/12/31.
 */

public class Dialog_login extends DialogFragment{
    private EditText mNameEditText;
    private EditText mPasswordEditText;
    private Button bcancel,blogin,bregister,bfoget;

    private DialogClickListener listener;

    public interface DialogClickListener{
        void onDialogClick(String Name,String Password);
    }

    public void setOnDialogClick(DialogClickListener listener){
        this.listener=listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View view = inflater.inflate(R.layout.dialog_login, null);

        mNameEditText=(EditText)view.findViewById(R.id.user);
        mPasswordEditText=(EditText)view.findViewById(R.id.key);
        bcancel=(Button)view.findViewById(R.id.cancel);
        blogin=(Button)view.findViewById(R.id.login);
        bfoget=(Button)view.findViewById(R.id.btn_forget);
        bregister=(Button)view.findViewById(R.id.btn_register);

        //设为其它状态不可选中
        setCancelable(false);

        bcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogClick("cancel","cancel");
                dismiss();
            }
        });
        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=mNameEditText.getText().toString().trim();
                String passwd=mPasswordEditText.getText().toString().trim();

                if(name.equals("")||passwd.equals("")){
                    Toast.makeText(getActivity(),"用户名或者密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(name.length()>20||passwd.length()>20){
                    Toast.makeText(getActivity(), "用户名或者密码过长", Toast.LENGTH_SHORT).show();
                }else{
                    try{
                        String md5 = MD5.getMD5(passwd);
                        String sql = "select * from User where ID=? and Password=?";
                        JSONObject keyValue = new JSONObject();
                        keyValue.put("1", name);//顺序放置要填充?部分的值，防止sql注入
                        keyValue.put("2", md5);

                        //Todo: 可能要判断网络连接
                        //if (session != null && session.isConnected())
                        GetData.runGetData(sql,"query",keyValue);
                        JSONArray tempJsArr=new JSONArray(ByTheWayActivity.dataResult);

                        if(tempJsArr.length()!=0){
                            Toast.makeText(getActivity(), "登陆成功", Toast.LENGTH_SHORT).show();

                            //获取用户个人信息并赋值
                            sql="select * from Person where ID=?";//获取用户信息的sql
                            keyValue=new JSONObject();//清空keyValue
                            keyValue.put("1",name);

                            //获取用户信息并赋值
                            GetData.runGetData(sql,"query",keyValue);
                            JSONArray jsonArray=new JSONArray(ByTheWayActivity.dataResult);
                            JSONObject jsonResult=jsonArray.getJSONObject(0);

                            //显示用户名字
                            TextView user_name=getActivity().findViewById(R.id.user_name);
                            if(jsonResult.getString("Name").equals(""))
                                user_name.setText(jsonResult.getString("ID"));
                            else
                                user_name.setText(jsonResult.getString("Name"));
                            ByTheWayActivity.userData.UserData(jsonResult.getString("ID"),jsonResult.getString("Name"),jsonResult.getString("Gender"),jsonResult.getString("PhoneNumber"));

                            dismiss();
                        } else{
                            Toast.makeText(getActivity(), "账户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception ex){
                        Toast.makeText(getActivity(), "网络连接失败，请检擦网络连接", Toast.LENGTH_SHORT).show();
                    }
                }

//                listener.onDialogClick(mNameEditText.getText().toString(),mPasswordEditText.getText().toString());
            }
        });

        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_createuser dialog_createuser=new Dialog_createuser();
                dialog_createuser.show(getFragmentManager(),"dialog_createuser");

            }
        });

        builder.setView(view);

        return builder.create();
    }
}
