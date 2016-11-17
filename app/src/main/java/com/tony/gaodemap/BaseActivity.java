package com.tony.gaodemap;

import android.app.Activity;
import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;

/**
 * Created by gambler on 16/10/20
 */
public abstract class BaseActivity extends Activity {

    private MapView mapView;
    private AMap aMap;

    public abstract int getLayoutResourceId();
    public abstract int getMapViewId();

    public abstract void init(MapView mapView, AMap aMap);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResourceId());
        mapView = (MapView) findViewById(getMapViewId());
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        init(mapView,aMap);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
