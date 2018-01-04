package com.example.wmj.bytheway.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.baidu.location.LocationClient;
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
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.wmj.bytheway.R;

public class MapActivity extends AppCompatActivity {
    private MapView mapView=null;
    private BaiduMap baiduMap;
    private GeoCoder mSearch = null; // 搜索模块，用于正向或反向编码
    private LatLng latLng;
    public LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        //获取地图控件引用
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap=mapView.getMap();

        latLng=new LatLng(30.270258,120.126694);
        MapStatusUpdate update= MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(update);
        update=MapStatusUpdateFactory.zoomTo(16f);
        baiduMap.animateMapStatus(update);

        //添加标记
        BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.drawable.location);
        OverlayOptions options=new MarkerOptions().position(latLng).icon(bitmapDescriptor);
        baiduMap.addOverlay(options);

        //反向获取地址编码
        mSearch = GeoCoder.newInstance();
        OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
            public void onGetGeoCodeResult(GeoCodeResult result) {}

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果'
                    return;
                }
                //获取反向地理编码结果
                //addresstext.setText(result.getAddress());
            }
        };
        mSearch.setOnGetGeoCodeResultListener(listener);

        //双击地图选择位置
        BaiduMap.OnMapDoubleClickListener Clicklistener = new BaiduMap.OnMapDoubleClickListener() {
            public void onMapDoubleClick(LatLng point){
                baiduMap.clear();
                BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromResource(R.drawable.location);
                OverlayOptions options=new MarkerOptions().position(point).icon(bitmapDescriptor);
                baiduMap.addOverlay(options);

                //调用反向编码接口，触发listener，结果在listener函数中体现
                ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
                reverseGeoCodeOption.location(point);
                mSearch.reverseGeoCode(reverseGeoCodeOption);
            }
        };
        //百度双击监听事件
        baiduMap.setOnMapDoubleClickListener(Clicklistener);

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
