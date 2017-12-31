package com.example.wmj.bytheway;

import java.util.UUID;

/**
 * Created by st4rlight on 2017/12/28.
 */

public class Order {
    private UUID mUUID;
    private String mTitle;

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
