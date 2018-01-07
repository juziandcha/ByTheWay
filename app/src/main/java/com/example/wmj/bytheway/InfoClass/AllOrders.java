package com.example.wmj.bytheway.InfoClass;

import android.content.Context;
import android.widget.Toast;

import com.example.wmj.bytheway.Activities.ByTheWayActivity;
import com.example.wmj.bytheway.Util.GetData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by st4rlight on 2017/12/28.
 */

public class AllOrders {
    private static AllOrders sAllOrders;
    private List<Order> mOrders;

    public static AllOrders get(Context context){
        if(sAllOrders ==null)
            sAllOrders =new AllOrders(context);
        return sAllOrders;
    }

    public AllOrders(Context context){
        mOrders=new ArrayList<>();

        try {
            //Todo: get add orders from database
            String sql = "select * from Task where Status='waiting'";
            JSONObject keyValue = new JSONObject();
            //获取用户个人信息并赋值
//        String sql="select * from Person where ID=?";//获取筛选的sql
//        JSONObject keyValue=new JSONObject();//
//
            //获取订单信息并赋值
            GetData.runGetData(sql, "query", keyValue);
            JSONArray jsonArray = new JSONArray(ByTheWayActivity.dataResult);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Order tempOrder = new Order();
                tempOrder.setOrderID(jsonObject.getString("TaskID"));
                tempOrder.setTitle(jsonObject.getString("Title"));
                tempOrder.setContent(jsonObject.getString("Content"));
                tempOrder.setReleaseTime(jsonObject.getString("StartTime"));

                    //修改usedata
                    sql="select * from Person where ID=?";
                    keyValue=new JSONObject();
                    keyValue.put("1",jsonObject.getString("UserID"));
                    GetData.runGetData(sql, "query", keyValue);
                    JSONArray jsonUserArr = new JSONArray(ByTheWayActivity.dataResult);
                    JSONObject jsonUser=jsonUserArr.getJSONObject(0);
                        UserData tempUser=new UserData();
                        tempUser.UserData(jsonUser.getString("ID"),jsonUser.getString("Name"),jsonUser.getString("Gender"),jsonUser.getString("PhoneNumber"));
                    tempOrder.setReleaseUser(tempUser);

                tempOrder.setStartAddress(new Address(jsonObject.getString("StartAddr"), jsonObject.getString("StartAdrLot"), jsonObject.getString("StartAdrLat")));
                tempOrder.setTargetAddress(new Address(jsonObject.getString("EndAddr"), jsonObject.getString("EndAdrLot"), jsonObject.getString("EndAdrLat")));
                tempOrder.setStatus(jsonObject.getString("Status"));

                mOrders.add(tempOrder);
            }

            sAllOrders=this;
        }catch (JSONException ex){
            ex.printStackTrace();
        }catch(Exception ex){
            //Todo: may cause error here;
            Toast.makeText(ByTheWayActivity.getByTheWayActivity().getApplication(), "网络连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    public List<Order> getOrders(){
        return mOrders;
    }
    public Order getOrder(UUID id){
        for(Order order:mOrders){
            if(order.getUUID().equals(id)){
                return order;
            }
        }
        return null;
    }

}
