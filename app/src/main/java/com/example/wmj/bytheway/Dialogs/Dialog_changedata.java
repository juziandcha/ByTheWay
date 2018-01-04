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

import com.example.wmj.bytheway.R;

/**
 * Created by wmj on 2018/1/4.
 */

public class Dialog_changedata extends DialogFragment {
    private EditText edit_name;
    private EditText edit_password;
    private EditText edit_gender;
    private EditText edit_phone;
    private Button btn_change;

    private Dialog_changedata.DialogClickListener listener;

    public interface DialogClickListener{
        void onDialogClick(String Name,String Password);
    }

    public void setOnDialogClick(Dialog_changedata.DialogClickListener listener){
        this.listener=listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        View view = inflater.inflate(R.layout.dialog_changedata, null);

        edit_name=(EditText)view.findViewById(R.id.new_user_name);
        edit_password=(EditText)view.findViewById(R.id.new_password);
        edit_gender=(EditText)view.findViewById(R.id.new_gender);
        edit_phone=(EditText)view.findViewById(R.id.new_phone_number);
        btn_change=(Button)view.findViewById(R.id.change_data);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=edit_name.getText().toString();
                String password=edit_password.getText().toString();
                String gender=edit_gender.getText().toString();
                String phone=edit_phone.getText().toString();

                if(!name.equals("")){
                    //修改数据
                    Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                if(!password.equals("")){
                    //
                    dismiss();
                }
                if(!gender.equals("")){
                    //
                    dismiss();
                }
                if(!phone.equals("")){
                    //
                    dismiss();
                }
            }
        });

        builder.setView(view);

        return builder.create();
    }
}
