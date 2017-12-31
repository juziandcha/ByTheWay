package com.example.wmj.bytheway;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class OrderActivity extends SingleFragmentActivity {
    public static final String EXTRA_ORDER_ID="com.bignerdrance.android.orderintent.order_id";

    public static Intent newIntent(Context packageContext, UUID orderID){
        Intent intent = new Intent(packageContext,OrderActivity.class);
        intent.putExtra(EXTRA_ORDER_ID , orderID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID orderID=(UUID) getIntent().getSerializableExtra(EXTRA_ORDER_ID);
        return OrderFragment.newInstance(orderID);
    }
}
