package com.tony.gaodemap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.nearby.NearbyInfo;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchResult;
import com.tony.gaodemap.util.GDLocationUtil;


/**
 * Created by gambler on 16/10/20
 */
public class LocationActivity extends Activity implements AMap.OnMapClickListener,
        AMapLocationListener, LocationSource, NearbySearch.NearbyListener, AMap.OnMarkerClickListener {

    /**
     * (company location)GDMap 坐标系:39.954136N , 116.372925E
     */

    //private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    //private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private static final String USER_ID = "user_id";

    private AMap aMap;
    private MapView mapView;

    private Context ctx;
    private AMapLocationClient mLocationClient;
    private OnLocationChangedListener mListener;
    private boolean firstLocation;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);/*** 必须重写*/
        aMap = mapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);//矢量地图模式
        aMap.setTrafficEnabled(true);//实时交通显示
        initData();
        addListener();
        configMap();
    }

    /**
     * 必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        GDLocationUtil.clearUserInfo(ctx, userId);
        GDLocationUtil.destroyNearbySearch();
    }

    /**
     * 必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            userId = TextUtils.isEmpty(intent.getStringExtra(USER_ID)) ? "unKnown" : intent
                    .getStringExtra(USER_ID);
        }
    }

    private void addListener() {
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
    }

    public void configMap() {
        ctx = this;
        //配置地图SDk控件
        configUiSettings();
        //配置定位
        configLocation();
        //配置搜索
        GDLocationUtil.addNearbyListener(ctx, this);
    }

    private void configUiSettings() {

        UiSettings mUiSettings = aMap.getUiSettings();

        mUiSettings.setCompassEnabled(true);//显示指南针

        mUiSettings.setScaleControlsEnabled(false);//不显示比例尺控件

        mUiSettings.setZoomControlsEnabled(false);//不显示缩放按钮
        mUiSettings.setZoomPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);//缩放按钮位置

        mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);//设置logo位置

        mUiSettings.setAllGesturesEnabled(true);//开启所有手势(不设置默认开启)

    }

    private void configLocation() {
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        //设置定位图标样式
        GDLocationUtil.configLocationPointStyle(aMap);
    }

    private void configLocationClient() {
        //初始化定位参数对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置返回地址信息，默认为true
        mLocationOption.setNeedAddress(true);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms(省电和流量)
        mLocationOption.setInterval(2000);//

        //初始化定位客户端对象
        mLocationClient = new AMapLocationClient(ctx);
        //设置定位监听
        mLocationClient.setLocationListener(this);
        //配置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 地图点击事件响应回调
     *
     * @param latLng 点击位置的经纬度
     */
    @Override
    public void onMapClick(LatLng latLng) {

    }


    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {//定位成功
                //显示系统定位点
                mListener.onLocationChanged(aMapLocation);
                //打印定位信息
                GDLocationUtil.logAMapLocationInfo(aMapLocation);
                if (!firstLocation) {
                    moveToLocation(aMapLocation);
                    //上传用户信息
                    GDLocationUtil.uploadUserInfoOnce(ctx, aMapLocation, userId);
                    firstLocation = true;
                }
                //以当前定位点进行附近搜索(10000米,10000秒)
                GDLocationUtil.startSearchNearbyAsync(ctx, aMapLocation, 10000, 10000);

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("aMapError", +aMapLocation.getErrorCode() + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 移动地图照相机至当前位置
     *
     * @param aMapLocation 地图对象
     */
    private void moveToLocation(AMapLocation aMapLocation) {
        //移动到定位位置
        LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 17, 30, 0)));
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            configLocationClient();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void onNearbyInfoUploaded(int i) {

        GDLocationUtil.showToast(ctx, i == 1000 ? "用户信息上传成功" : "用户信息上传失败");

    }

    @Override
    public void onUserInfoCleared(int i) {

        GDLocationUtil.showToast(ctx, i == 1000 ? "用户信息清除成功" : "用户信息清除失败");

    }

    @Override
    public void onNearbyInfoSearched(NearbySearchResult nearbySearchResult, int resultCode) {
        if (resultCode == 1000) {
            if (nearbySearchResult != null
                    && nearbySearchResult.getNearbyInfoList() != null
                    && nearbySearchResult.getNearbyInfoList().size() > 0) {

                for (NearbyInfo nearbyInfo : nearbySearchResult.getNearbyInfoList()) {
                    //添加marker
                    LatLng latLng = new LatLng(nearbyInfo.getPoint().getLatitude(), nearbyInfo
                            .getPoint().getLongitude());
                    GDLocationUtil.addMarkerToMap(aMap, latLng, "USER:" + nearbyInfo.getUserID(),
                            "hello");
                }

            } else {
                GDLocationUtil.showToast(ctx, "周边搜索结果为空");
            }
        } else {
            GDLocationUtil.showToast(ctx, "\"周边搜索出现异常，异常码为：\"+resultCode");
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
            GDLocationUtil.jumpPoint(aMap, marker);
        }
        GDLocationUtil.showToast(ctx, marker.getId() == null ? "Humax" : marker.getId());
        return true;
    }
}
