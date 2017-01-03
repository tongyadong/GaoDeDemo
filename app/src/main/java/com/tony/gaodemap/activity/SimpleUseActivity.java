package com.tony.gaodemap.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.CancelableCallback;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.tony.gaodemap.R;


/**
 * AMapV2地图中介绍如何显示一个基本地图
 */
public class SimpleUseActivity extends BaseActivity implements OnClickListener, OnMapClickListener {

	private Button basicMap;
	private Button rsMap;
	private Button nightMap;
	private Boolean firstTouch = true;

	@Override
	public int getLayoutResourceId() {
		return R.layout.basicmap_activity;
	}

	@Override
	public int getMapViewId() {
		return R.id.map;
	}

	@Override
	public void init(MapView mapView, AMap aMap) {
		if (aMap == null) {
			aMap = mapView.getMap();
		}
		aMap.setOnMapClickListener(this);
		basicMap = (Button)findViewById(R.id.basicmap);
		basicMap.setOnClickListener(this);
		rsMap = (Button)findViewById(R.id.rsmap);
		rsMap.setOnClickListener(this);
		nightMap = (Button)findViewById(R.id.nightmap);
		nightMap.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.basicmap:
			aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
			break;
		case R.id.rsmap:
			aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
			break;
		case R.id.nightmap:
			aMap.setMapType(AMap.MAP_TYPE_NIGHT);//夜景地图模式
			break;
		}
		
	}
	@Override
	public void onMapClick(LatLng arg0) {
		if (firstTouch) {
			firstTouch = false;
			aMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(39.92463, 116.389139), 18, 0, 0)), 1500, new CancelableCallback() {

			@Override
			public void onFinish() {
				aMap.moveCamera(CameraUpdateFactory.changeTilt(60));
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				aMap.animateCamera(CameraUpdateFactory.changeBearing(90), 2000, null);
			}
			@Override
			public void onCancel() {

			}
		});

		}
	}

}
