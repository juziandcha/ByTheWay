package com.example.wmj.bytheway;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by wmj on 2018/1/2.
 */

public class Dialog_historytask extends DialogFragment{
    private TextView title,content;
    private TextView release_user,receive_user;
    private TextView start_address,end_address;
    private TextView task_status;

    private Dialog_historytask.DialogClickListener listener;

    public interface DialogClickListener{
        void onDialogClick(boolean ifreceive);
    }

    public void setOnDialogClick(Dialog_historytask.DialogClickListener listener){
        this.listener=listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_histotytask, null);
        title=(TextView)view.findViewById(R.id.title_history);
        content=(TextView)view.findViewById(R.id.content_history);
        release_user=(TextView)view.findViewById(R.id.release_user_history);
        receive_user=(TextView)view.findViewById(R.id.receive_user_history);
        start_address=(TextView)view.findViewById(R.id.saddress_history);
        end_address=(TextView)view.findViewById(R.id.eaddress_history);
        task_status=(TextView)view.findViewById(R.id.status_history);

        //获取预置信息
        /*
        title.setText(bundle.getString("title"));
        content.setText(bundle.getString("content"));
        release_user.setText(bundle.getString("release_user"));
        receive_user.setText(bundle.getString("receive_user"));
        start_address.setText(bundle.getString("start_address"));
        end_address.setText(bundle.getString("end_address"));
        task_status.setText(bundle.getString("task_status"));
        */
        //设为其它状态不可选中
        //setCancelable(false);

        start_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //还需加入经纬度信息
                Intent intent=new Intent(getActivity().getApplicationContext(),MapActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(),"star",Toast.LENGTH_SHORT).show();
            }
        });
        end_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //还需加入经纬度信息
                Intent intent=new Intent(getActivity().getApplicationContext(),MapActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(),"end",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setView(view);

        return builder.create();
    }

}
