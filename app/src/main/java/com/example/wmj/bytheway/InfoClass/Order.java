package com.example.wmj.bytheway.InfoClass;

import java.util.Date;
import java.util.UUID;

/**
 * Created by st4rlight on 2017/12/28.
 */

//Note：String类型若没有设为""，其他为NULL
public class Order {
    private UUID mUUID;//uuid

    private String mOrderID;//订单id
    private String mTitle;//订单标题
    private String mContent;//订单内容

    private Date mReleaseTime;//发布时间
    private Date mFinishTime;//完成时间
    private Date mReceiveTime;//接收时间

    private UserData mReleaseUser;//发布用户
    private UserData mReceiveUser;//接收用户

    private Address mStartAddress;//起始地址
    private Address mTargetAddress;//目标地址

    private String mStatus;//订单状态

    public Order() {
        mUUID=UUID.randomUUID();
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getOrderID() {
        return mOrderID;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public Date getReleaseTime() {
        return mReleaseTime;
    }

    public Date getFinishTime() {
        return mFinishTime;
    }

    public Date getReceiveTime() {
        return mReceiveTime;
    }

    public UserData getReleaseUser() {
        return mReleaseUser;
    }

    public UserData getReceiveUser() {
        return mReceiveUser;
    }

    public Address getStartAddress() {
        return mStartAddress;
    }

    public Address getTargetAddress() {
        return mTargetAddress;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public void setOrderID(String orderID) {
        mOrderID = orderID;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public void setReleaseTime(Date releaseTime) {
        mReleaseTime = releaseTime;
    }

    public void setFinishTime(Date finishTime) {
        mFinishTime = finishTime;
    }

    public void setReceiveTime(Date receiveTime) {
        mReceiveTime = receiveTime;
    }

    public void setReleaseUser(UserData releaseUser) {
        mReleaseUser = releaseUser;
    }

    public void setReceiveUser(UserData receiveUser) {
        mReceiveUser = receiveUser;
    }

    public void setStartAddress(Address startAddress) {
        mStartAddress = startAddress;
    }

    public void setTargetAddress(Address targetAddress) {
        mTargetAddress = targetAddress;
    }

    public void setStatus(String status) {
        mStatus = status;
    }
}
