package com.example.wmj.bytheway.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmj.bytheway.Activities.MapActivity;
import com.example.wmj.bytheway.R;

import static android.app.Activity.RESULT_OK;

/**
 * Created by wmj on 2017/12/31.
 */

public class Dialog_createtask extends DialogFragment{
    private TextView title,content;
    private TextView start_address,end_address;
    private Button task_create;
    private boolean text_start=false;

    private Dialog_createtask.DialogClickListener listener;

    public interface DialogClickListener{
        void onDialogClick(boolean ifcreate,String titlename,String contents,String s_address,String e_address);
    }

    public void setOnDialogClick(Dialog_createtask.DialogClickListener listener){
        this.listener=listener;
    }

    //map数据回传
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle b=data.getExtras();
                String Addr=b.getString("address");
                if(text_start){
                    start_address.setText(Addr);
                }
                else {
                    end_address.setText(Addr);
                }
                break;
            default:
                break;
        }
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
                //定位
                text_start=true;
                Intent intent=new Intent(getActivity().getApplicationContext(),MapActivity.class);
                startActivityForResult(intent,0);
            }
        });
        end_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定位
                text_start=false;
                Intent intent=new Intent(getActivity().getApplicationContext(),MapActivity.class);
                startActivityForResult(intent,0);
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
