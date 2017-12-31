package com.example.wmj.bytheway;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by st4rlight on 2017/12/27.
 */

public class FragmentOrder extends Fragment {
    private RecyclerView mRecyclerView;
    private OrderAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order,container,false);
        initRefresh(view);
        mRecyclerView=view.findViewById(R.id.recycler_order);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnScrollListener(new EndLessOnScrollListener((LinearLayoutManager) mRecyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreData();
            }
        });

        updateUI();
        return view;
    }

    private void updateUI(){
        AllOrders allOrders = AllOrders.get(getActivity());
        List<Order> orders= allOrders.getOrders();

        mAdapter = new OrderAdapter(orders);
        mRecyclerView.setAdapter(mAdapter);
    }

    //每次上拉加载的时候，给RecyclerView的后面添加了10条数据数据
    private void loadMoreData(){
        for (int i =0; i < 10; i++){
            Order order=new Order();
            order.setTitle("上拉加载 #"+i);
            mAdapter.addData(order);
            mAdapter.notifyDataSetChanged();
        }
    }

    //下拉刷新
    private void initRefresh(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
    }
    private void refreshContent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    //ViewHolder 内部类
    private class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private Order mOrder;

        public OrderHolder(View itemView){
            super(itemView);
            mTitleTextView=(TextView)itemView.findViewById(R.id.text_content);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = OrderActivity.newIntent(getActivity(), mOrder.getUUID());
            startActivity(intent);
        }
        public void bindOrder(Order order){
            mOrder=order;
            mTitleTextView.setText(order.getTitle());
        }
    }


    //Adapter 内部类
    private class OrderAdapter extends RecyclerView.Adapter<OrderHolder>{
        private List<Order> mOrders;

        public OrderAdapter(List<Order> orders ){
            mOrders=orders;
        }

        public void addData(Order order){
            mOrders.add(order);
        }

        @Override
        public int getItemCount() {
            return mOrders.size();
        }

        @Override
        public void onBindViewHolder(OrderHolder holder, int position) {
            Order order=mOrders.get(position);
            holder.bindOrder(order);
        }

        @Override
        public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
            View view=layoutInflater.inflate(R.layout.cardview, parent,false);

            return new OrderHolder(view);
        }
    }
}
