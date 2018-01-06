package com.example.wmj.bytheway.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmj.bytheway.Activities.ByTheWayActivity;
import com.example.wmj.bytheway.InfoClass.AllOrders;
import com.example.wmj.bytheway.Dialogs.Dialog_waitingtask;
import com.example.wmj.bytheway.InfoClass.UserData;
import com.example.wmj.bytheway.Util.EndLessOnScrollListener;
import com.example.wmj.bytheway.InfoClass.Order;
import com.example.wmj.bytheway.R;
import com.pkmmte.view.CircularImageView;

import java.util.List;

/**
 * Created by st4rlight on 2017/12/27.
 */

public class FragmentOrder extends Fragment {
    private RecyclerView mRecyclerView;
    private OrderAdapter mAdapter;
    private TextView mNoDataTV;
    private TextView mNoMoreTV;
    private TextView mLoadTV;
    private SwipeRefreshLayout mSwipeRefreshLayout;//下拉刷新

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_order,container,false);//绑定布局
        initRefresh(view);

        mNoDataTV=view.findViewById(R.id.empty_view);
        mNoMoreTV=view.findViewById(R.id.no_more);
        mLoadTV=view.findViewById(R.id.load_more);

        mRecyclerView=view.findViewById(R.id.recycler_order);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnScrollListener(new EndLessOnScrollListener((LinearLayoutManager) mRecyclerView.getLayoutManager()) {
            //加载更多事件
            @Override
            public void onLoadMore(int currentPage) {
                mLoadTV.setVisibility(View.VISIBLE);
                loadMoreData();
                mLoadTV.setVisibility(View.GONE);
            }

            //到达底部事件
            @Override
            public void onReachEnd(int currentPage){
                LinearLayoutManager layoutManager =(LinearLayoutManager) mRecyclerView.getLayoutManager();
                //屏幕中最后一个可见子项的position
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                //当前屏幕所看到的子项个数
                int visibleItemCount = layoutManager.getChildCount();
                //当前RecyclerView的所有子项个数
                int totalItemCount = layoutManager.getItemCount();
                //RecyclerView的滑动状态
                int state = mRecyclerView.getScrollState();
                if(visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == RecyclerView.SCROLL_STATE_IDLE){
                    mNoMoreTV.setVisibility(View.VISIBLE);
                }else {
                    mNoMoreTV.setVisibility(View.GONE);
                }
            }
        });

        updateUI();
        return view;
    }

    //ViewHolder 内部类
    private class OrderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mNameTV;
        private TextView mTitleTV;
        private TextView mTimeTV;
        private TextView mStartAddrTV;
        private TextView mTargetAddrTV;
        private CircularImageView mLogoImg;//后续拓展。订单上显示用户头像

        private Order mOrder;

        public OrderHolder(View itemView){
            super(itemView);
            mNameTV=(TextView)itemView.findViewById(R.id.text_name);
            mTitleTV=(TextView)itemView.findViewById(R.id.text_content);
            mTimeTV=(TextView)itemView.findViewById(R.id.text_time);
            mStartAddrTV=(TextView)itemView.findViewById(R.id.text_start);
            mTargetAddrTV=(TextView)itemView.findViewById(R.id.text_target);
            mLogoImg=(CircularImageView)itemView.findViewById(R.id.img_logo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            showtaskDialog(mOrder);
        }
        public void bindOrder(Order order){
            mOrder=order;
            if(order.getReleaseUser().getName().equals(""))
                mNameTV.setText(order.getReleaseUser().getID());
            else
                mNameTV.setText(order.getReleaseUser().getName());

            mTitleTV.setText(order.getTitle());
            mTimeTV.setText(order.getReleaseTime().toString());
            mStartAddrTV.setText(order.getStartAddress().getAddress());
            mTargetAddrTV.setText(order.getTargetAddress().getAddress());

            //设置头像，当前只使用默认头像，如果没有性别或者中性，使用App logo
            if(order.getReleaseUser().getGender().equals("male"))
                mLogoImg.setImageResource(R.drawable.boy);
            else if(order.getReleaseUser().getGender().equals("female"))
                mLogoImg.setImageResource(R.drawable.girl);
            else
                mLogoImg.setImageResource(R.mipmap.app_logo);
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

    //创建Adapter，并设置给RecyclerView
    private void updateUI(){
        AllOrders allOrders = AllOrders.get(getActivity());
        List<Order> orders= allOrders.getOrders();

        //如果没有数据的时候,那么只显示没有数据
        if(orders.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            mNoDataTV.setVisibility(View.VISIBLE);
        }else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoDataTV.setVisibility(View.GONE);
        }

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

                //Todo: 重新从服务器获取数据，获取语句卸载Allorders类里
                AllOrders allOrders = AllOrders.get(getActivity());
                List<Order> orders= allOrders.getOrders();

                mAdapter = new OrderAdapter(orders);
                mRecyclerView.setAdapter(mAdapter);
            }
        }, 2000);
    }

    //查看任务界面
    private void showtaskDialog(Order order){
        //Note 可以传递UUID进来，可以直接传order对象进来，选择一个
        Dialog_waitingtask dialog_waitingtask=new Dialog_waitingtask();

        Bundle bundle=new Bundle();
        bundle.putString("release_user","user1");
        bundle.putString("title","task1");
        bundle.putString("content","dsafadfaadfafafadfadfad adacdacaavdcafadfcadfaa");
        bundle.putString("start_address","杭州东");
        bundle.putString("end_address","浙大玉泉校区");
        dialog_waitingtask.setArguments(bundle);

        dialog_waitingtask.setOnDialogClick(new Dialog_waitingtask.DialogClickListener() {
            @Override
            public void onDialogClick(boolean ifreceive) {

                if(ifreceive)
                    Toast.makeText(getActivity(),"yes",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(),"no",Toast.LENGTH_SHORT).show();

            }
        });

        dialog_waitingtask.show(getActivity().getFragmentManager(),"Dialog_waitingtask");
    }
}
