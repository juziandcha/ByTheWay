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
                listener.onDialogClick(mNameEditText.getText().toString(),mPasswordEditText.getText().toString());
                dismiss();
            }
        });

        builder.setView(view);

        return builder.create();
    }
}
