package com.example.wmj.bytheway;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                String name=Edit_name.getText().toString();
                String password1=Edit_password.getText().toString();
                String password2=Edit_password2.getText().toString();

                if(password1.equals(password2)){
                    //数据库通信
                    Toast.makeText(getActivity(), Edit_name.getText().toString()+Edit_password.getText().toString(), Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                else {
                    Toast.makeText(getActivity(), "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                }


            }
        });

        builder.setView(view);

        return builder.create();
    }
}
