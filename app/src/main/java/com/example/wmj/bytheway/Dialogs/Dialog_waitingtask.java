package com.example.wmj.bytheway.Dialogs;

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

import com.example.wmj.bytheway.Activities.ByTheWayActivity;
import com.example.wmj.bytheway.Activities.OnlyMapActivity;
import com.example.wmj.bytheway.InfoClass.AllOrders;
import com.example.wmj.bytheway.InfoClass.Order;
import com.example.wmj.bytheway.R;
import com.example.wmj.bytheway.Util.GetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by wmj on 2017/12/31.
 */

public class Dialog_waitingtask extends DialogFragment{
    private ImageView image;
    private TextView release_user;
    private TextView title,content;
    private TextView start_address,end_address;
    private TextView release_time;
    private Button receive;

    private String s_lat,s_lot;
    private String e_lat,e_lot;

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

        UUID uuid=(UUID)bundle.getSerializable("uuid");
        final Order order= AllOrders.get(getActivity()).getOrder(uuid);

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
        release_time=(TextView)view.findViewById(R.id.release_time);


        if(order.getReleaseUser().getName().equals(""))
            release_user.setText(order.getReleaseUser().getID());
        else
            release_user.setText(order.getReleaseUser().getName());
        title.setText(order.getTitle());
        release_time.setText(order.getReleaseTime());
        content.setText(order.getContent());
        if(order.getStartAddress()!=null){
            start_address.setText(order.getStartAddress().getAddress());
            s_lat=order.getStartAddress().getLatitude();
            s_lot=order.getStartAddress().getLongitude();
        }else {
            start_address.setText("暂无地址信息");
            s_lot="0";
            s_lat="0";
        }
        if(order.getTargetAddress()!=null){
            end_address.setText(order.getTargetAddress().getAddress());
            e_lat=order.getTargetAddress().getLatitude();
            e_lot=order.getTargetAddress().getLongitude();
        }else {
            end_address.setText("暂无地址信息");
            e_lot="0";
            e_lat="0";
        }

        //设置头像
        String gender=order.getReleaseUser().getGender();
        if(gender.equals("male"))
            image.setImageResource(R.drawable.boy);
        else if(gender.equals("female"))
            image.setImageResource(R.drawable.girl);
        else
            image.setImageResource(R.mipmap.app_logo);

        //设为其它状态不可选中
        //setCancelable(false);

        start_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //经纬度传输
                Intent intent=new Intent(getActivity().getApplicationContext(),OnlyMapActivity.class);
                Bundle location_bundle=new Bundle();
                location_bundle.putString("Latitude",s_lat);
                location_bundle.putString("Longitude",s_lot);
                intent.putExtras(location_bundle);
                startActivity(intent);
            }
        });

        end_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //经纬度传输
                Intent intent=new Intent(getActivity().getApplicationContext(),OnlyMapActivity.class);
                Bundle location_bundle=new Bundle();
                location_bundle.putString("Latitude",e_lat);
                location_bundle.putString("Longitude",e_lot);
                intent.putExtras(location_bundle);
                startActivity(intent);
            }
        });

        receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ByTheWayActivity.userData.getID().equals(order.getReleaseUser().getID())){
                    Toast.makeText(getActivity(), "不能接收自己发布的订单", Toast.LENGTH_SHORT).show();
                }else {
                    //接收订单
                    try {
                        String sql = "update Task set Status='doing' where TaskID=?;";
                        JSONObject keyValue = new JSONObject();
                        keyValue.put("1", order.getOrderID());//顺序放置要填充?部分的值，防止sql注入
                        GetData.runGetData(sql, "update", keyValue);
                        JSONObject jsObjStatus = new JSONObject(ByTheWayActivity.dataResult);
                        String status = jsObjStatus.getString("succeed");

                        sql = "insert into TaskUser values (?,?,?,null);";
                        keyValue = new JSONObject();
                        keyValue.put("1", order.getOrderID());//顺序放置要填充?部分的值，防止sql注入
                        keyValue.put("2", order.getReleaseUser().getID());
                        keyValue.put("3", ByTheWayActivity.userData.getID());

                        GetData.runGetData(sql, "update", keyValue);
                        JSONObject jsObjTaskUser = new JSONObject(ByTheWayActivity.dataResult);
                        String taskUser = jsObjTaskUser.getString("succeed");
                        if (!status.equals("true") || !taskUser.equals("true")) {
                            if (status.equals("false")) {
                                sql = "update Task set Status='waiting' where TaskID=?;";
                                keyValue = new JSONObject();
                                keyValue.put("1", order.getOrderID());//顺序放置要填充?部分的值，防止sql注入
                                GetData.runGetData(sql, "update", keyValue);
                            }
                            if (taskUser.equals("false")) {
                                sql = "delete from TaskUser where TaskID=?";
                                keyValue = new JSONObject();
                                keyValue.put("1", order.getOrderID());
                                GetData.runGetData(sql, "update", keyValue);
                            }
                        } else {
                            Toast.makeText(getActivity(), "接收订单成功", Toast.LENGTH_SHORT).show();
                        }

                        listener.onDialogClick(true);
                        dismiss();
                    } catch (Exception ex) {
                        Toast.makeText(getActivity(), "接受订单失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        builder.setView(view);

        return builder.create();
    }
}
