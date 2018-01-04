package com.example.wmj.bytheway.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.example.wmj.bytheway.R;

public class MainActivity extends AppCompatActivity {
    private TextView location;
    private MapView mapView=null;
    private BaiduMap baiduMap;
    private LatLng latLng;
    private RoutePlanSearch mSearch;
    private RouteLine mRoute;
    public LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap=mapView.getMap();

        latLng=new LatLng(30.270258,120.126694);
        MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(update);
        update=MapStatusUpdateFactory.zoomTo(16f);
        baiduMap.animateMapStatus(update);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
}
