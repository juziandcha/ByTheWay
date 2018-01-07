package com.example.wmj.bytheway.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.wmj.bytheway.R;

public class OnlyMapActivity extends AppCompatActivity {
    private MapView mapView=null;
    private BaiduMap baiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_only_map);

        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.only_bmapView);
        baiduMap=mapView.getMap();

        Bundle location_bundle=getIntent().getExtras();
        //设定初始位置
        String lat=location_bundle.getString("Longitude");
        String lot=location_bundle.getString("Latitude");
        Toast.makeText(OnlyMapActivity.this, lat+"\n"+lot, Toast.LENGTH_SHORT).show();
        LatLng latLng=new LatLng(Double.parseDouble(lat),Double.parseDouble(lot));
        MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(update);
        update=MapStatusUpdateFactory.zoomTo(16f);
        baiduMap.animateMapStatus(update);

        //添加标记
        BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.drawable.location);
        OverlayOptions options=new MarkerOptions().position(latLng).icon(bitmapDescriptor);
        baiduMap.addOverlay(options);

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
