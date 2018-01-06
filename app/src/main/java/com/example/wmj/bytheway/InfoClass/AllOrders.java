package com.example.wmj.bytheway.InfoClass;

import android.content.Context;

import com.example.wmj.bytheway.Activities.ByTheWayActivity;
import com.example.wmj.bytheway.Util.GetData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
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

    private AllOrders(Context context){
        mOrders=new ArrayList<>();
        //Todo: get add orders from database
        //获取用户个人信息并赋值
//        String sql="select * from Person where ID=?";//获取筛选的sql
//        JSONObject keyValue=new JSONObject();//
//
//        //获取用户信息并赋值
//        GetData.runGetData(sql,"query",keyValue);
//        JSONArray jsonArray=new JSONArray(ByTheWayActivity.dataResult);
//        JSONObject jsonResult=jsonArray.getJSONObject(0);
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
