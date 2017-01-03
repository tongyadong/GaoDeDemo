package com.tony.gaodemap.activity;

import android.app.Activity;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;

/**
 * Created by gambler on 16/10/20
 */
public abstract class BaseActivity extends Activity {

    protected MapView mapView;
    protected AMap aMap;

    public abstract int getLayoutResourceId();
    public abstract int getMapViewId();

    public abstract void init(MapView mapView, AMap aMap);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResourceId());
        mapView = (MapView) findViewById(getMapViewId());

        /**必须重写*/
        mapView.onCreate(savedInstanceState);

        aMap = mapView.getMap();
        init(mapView,aMap);
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
    }

    /**
     * 必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
