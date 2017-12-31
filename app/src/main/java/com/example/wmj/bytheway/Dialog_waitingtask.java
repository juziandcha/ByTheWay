package com.example.wmj.bytheway;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by wmj on 2017/12/31.
 */

public class Dialog_waitingtask extends DialogFragment{
    private ImageView image;
    private TextView release_user;
    private TextView title,content;
    private TextView start_address,end_address;
    private Button receive;

    private Dialog_waitingtask.DialogClickListener listener;

    public interface DialogClickListener{
        void onDialogClick(boolean ifreceive);
    }

    public void setOnDialogClick(Dialog_waitingtask.DialogClickListener listener){
        this.listener=listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_waitingtask, null);
        image=(ImageView)view.findViewById(R.id.image);
        release_user=(TextView)view.findViewById(R.id.release_user);
        title=(TextView)view.findViewById(R.id.title);
        content=(TextView)view.findViewById(R.id.content);
        start_address=(TextView) view.findViewById(R.id.start_address);
        end_address=(TextView)view.findViewById(R.id.end_address);
        receive=(Button)view.findViewById(R.id.receive);

        release_user.setText(bundle.getString("release_user"));
        title.setText(bundle.getString("title"));
        content.setText(bundle.getString("content"));
        start_address.setText(bundle.getString("start_address"));
        end_address.setText(bundle.getString("end_address"));
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
        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDialogClick(true);
            }
        });

        builder.setView(view);

        return builder.create();
    }
}
