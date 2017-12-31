package com.example.wmj.bytheway;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by st4rlight on 2017/12/28.
 */

public class OrderFragment extends Fragment {
    private static final String ARG_ORDER_ID="order_id";
    private Order mOrder;
    private TextView mTitleField;

    public static OrderFragment newInstance(UUID orderID){
        Bundle args=new Bundle();
        args.putSerializable(ARG_ORDER_ID,orderID);

        OrderFragment fragment=new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID uuid=(UUID)getArguments().getSerializable(ARG_ORDER_ID);
        mOrder= AllOrders.get(getActivity()).getOrder(uuid);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_orderdetail,container,false);

        mTitleField=v.findViewById(R.id.order_title);
        mTitleField.setText(mOrder.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mOrder.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //
            }
        });

        return v;
    }
}
