package com.example.wmj.bytheway.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmj.bytheway.Activities.ByTheWayActivity;
import com.example.wmj.bytheway.Activities.MapActivity;
import com.example.wmj.bytheway.ConnSup.MD5;
import com.example.wmj.bytheway.InfoClass.Address;
import com.example.wmj.bytheway.R;
import com.example.wmj.bytheway.Util.GetData;
import com.example.wmj.bytheway.Util.GetTime;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.KEYGUARD_SERVICE;

/**
 * Created by wmj on 2017/12/31.
 */

public class Dialog_createtask extends DialogFragment{
    private TextView title,content;
    private TextView start_address,end_address;
    private TextView locate_here;
    private Button task_create;
    private boolean text_start=false;

    private Address start=new Address("","","");
    private Address target=new Address("","","");

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
                String lat=b.getString("Latitude");
                String lng=b.getString("Longitude");
                if(text_start){
                    start_address.setText(Addr);
                    start=new Address(Addr,lat,lng);
                }
                else {
                    end_address.setText(Addr);
                    target=new Address(Addr,lat,lng);
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
        locate_here=(TextView)view.findViewById(R.id.locate_here);
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
                String myTitle=title.getText().toString().trim();
                String myContent=content.getText().toString().trim();

                if(myTitle.equals("")) {
                    Toast.makeText(getActivity(), "标题不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    //将数据存储进数据库
                    try{
                        String sql = "INSERT INTO Task VALUES(null, ?, ? , ? , null , null , ? , ? , ? , ? , ? , ? ,'waiting')";
                        JSONObject keyValue = new JSONObject();
                        keyValue.put("1",ByTheWayActivity.userData.getID());
                        keyValue.put("2", myTitle);//顺序放置要填充?部分的值，防止sql注入
                        keyValue.put("3", myContent);
                        keyValue.put("4",start.getLongitude());
                        keyValue.put("5",start.getLatitude());
                        keyValue.put("6",start.getAddress());
                        keyValue.put("7",target.getLongitude());
                        keyValue.put("8",target.getLatitude());
                        keyValue.put("9",target.getAddress());

                        //Todo: 可能要判断网络连接
                        GetData.runGetData(sql,"update",keyValue);
                        JSONObject jsonObject=new JSONObject(ByTheWayActivity.dataResult);

                        if(jsonObject.getString("succeed").equals("true")) {
                            Toast.makeText(getActivity(), "创建订单成功", Toast.LENGTH_SHORT).show();
                            listener.onDialogClick(true, title.getText().toString(), content.getText().toString(), start_address.getText().toString(), end_address.getText().toString());
                            dismiss();
                        }else{
                            Toast.makeText(getActivity(), "创建订单失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    }catch (JSONException ex){
                        ex.printStackTrace();
                    }
                }
            }
        });

        locate_here.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                start_address.setText("定位中...");
                //Todo: 自动定位到当前位置
            }
        });

        builder.setView(view);

        return builder.create();
    }
}
