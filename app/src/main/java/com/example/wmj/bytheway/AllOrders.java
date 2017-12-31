package com.example.wmj.bytheway;

import android.content.Context;

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
        for(int i=0;i<10;i++){
            Order order=new Order();
            order.setTitle("Order #"+i);
            mOrders.add(order);
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
