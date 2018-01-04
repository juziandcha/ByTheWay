package com.example.wmj.bytheway.InfoClass;

import java.util.Date;
import java.util.UUID;

/**
 * Created by st4rlight on 2017/12/28.
 */

public class Order {
    private UUID mUUID;//uuid
    //    private int mOrderID;//订单id
    private String mTitle;//订单标题
    //    private String mContent;//订单内容
//    private Date mStartTime;//起始时间
//    private String mReleaseUser;//发布订单用户
//    private String mSex;//发布用户性别
//    private String mStartAdrLot; //起始纬度
//    private String mStartAdrLat;//起始经度
//    private String mEndAdrLot;//结束经度
//    private String mEndAdrLat;//结束纬度

    public Order() {
        mUUID=UUID.randomUUID();
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
