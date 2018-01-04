package com.example.wmj.bytheway;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wmj.bytheway.ConnSup.JsonContent;
import com.example.wmj.bytheway.ConnSup.MD5;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by wmj on 2017/12/31.
 */

public class LoginDialogFragment extends DialogFragment{
    private EditText mNameEditText;
    private EditText mPasswordEditText;
    private Button bcancel,blogin;

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
                        keyValue.put("ID", name);//顺序放置要填充?部分的值，防止sql注入
                        keyValue.put("Password", md5);

                        JsonContent JsonObj = new JsonContent(sql, "query", keyValue);

//                        //Todo: 可能要判断网络连接
//                        //if (session != null && session.isConnected())
                        ByTheWayActivity.session.write(JsonObj.getJsonStr());

                        Toast.makeText(getActivity(), ByTheWayActivity.dataResult, Toast.LENGTH_SHORT).show();
                        String result=ByTheWayActivity.dataResult;

                        JSONArray tempJsArr=new JSONArray(result);

                        if(tempJsArr.length()!=0){
                            Toast.makeText(getActivity(), "登陆成功", Toast.LENGTH_SHORT).show();
                            ByTheWayActivity.dataResult=null;
                            dismiss();
                        } else{
                            Toast.makeText(getActivity(), "账户名或密码错误", Toast.LENGTH_SHORT).show();
                            ByTheWayActivity.dataResult=null;
                        }
                    }catch (Exception ex){
                        Toast.makeText(getActivity(), "出错", Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                }

//                listener.onDialogClick(mNameEditText.getText().toString(),mPasswordEditText.getText().toString());
            }
        });

        builder.setView(view);

        return builder.create();
    }
}
