package com.example.wmj.bytheway;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by wmj on 2017/12/31.
 */

public class Dialog_createtask extends DialogFragment{
    private TextView title,content;
    private TextView start_address,end_address;
    private Button task_create;

    private Dialog_createtask.DialogClickListener listener;

    public interface DialogClickListener{
        void onDialogClick(boolean ifcreate,String titlename,String contents,String s_address,String e_address);
    }

    public void setOnDialogClick(Dialog_createtask.DialogClickListener listener){
        this.listener=listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_createtask, null);

        title=(TextView)view.findViewById(R.id.title_create);
        content=(TextView)view.findViewById(R.id.content_create);
        start_address=(TextView)view.findViewById(R.id.saddress_create);
        end_address=(TextView)view.findViewById(R.id.eaddress_create);
        task_create=(Button)view.findViewById(R.id.create_task);
        //设为其它状态不可选中
        //setCancelable(false);

        start_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"star",Toast.LENGTH_SHORT).show();
            }
        });
        end_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"end",Toast.LENGTH_SHORT).show();
            }
        });
        task_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogClick(true,title.getText().toString(),content.getText().toString(),start_address.getText().toString(),end_address.getText().toString());
                dismiss();
            }
        });

        builder.setView(view);

        return builder.create();
    }
}
