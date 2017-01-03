package com.tony.gaodemap.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.tony.gaodemap.R;
import com.tony.gaodemap.widget.ClusterClickListener;
import com.tony.gaodemap.widget.ClusterItem;
import com.tony.gaodemap.widget.ClusterOverlay;
import com.tony.gaodemap.widget.ClusterRender;
import com.tony.gaodemap.widget.RegionItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tony on 17/1/3.
 */

public class ClusterShowActivity extends BaseActivity implements AMap.OnMapLoadedListener,
        ClusterRender,ClusterClickListener{

    private Map<Integer, Drawable> mBackDrawAbles = new HashMap<>();
    private float clusterRadius = 100;
    private ClusterOverlay mClusterOverlay;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_show_closter;
    }

    @Override
    public int getMapViewId() {
        return R.id.map;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClusterOverlay.onDestroy();
    }

    @Override
    public void init(MapView mapView, AMap aMap) {
        // 初始化地图
        aMap = mapView.getMap();
        aMap.setOnMapLoadedListener(this);
//        //点击可以动态添加点
//        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                double lat = Math.random() + 39.474923;
//                double lon = Math.random() + 116.027116;
//
//                LatLng latLng1 = new LatLng(lat, lon, false);
//                RegionItem regionItem = new RegionItem(latLng1,
//                        "test");
//                mClusterOverlay.addClusterItem(regionItem);
//            }
//        });
    }

    /**
     * 地图加载完成回调
     */
    @Override
    public void onMapLoaded() {
        //添加聚合显示点
        new Thread(){
            @Override
            public void run() {
                List<ClusterItem> items = new ArrayList<>();
                //随机1000个点
                for (int i = 0; i < 10000; i++) {

                    double lat = Math.random() + 39.474923;
                    double lon = Math.random() + 116.027116;

                    LatLng latLng = new LatLng(lat, lon, false);
                    RegionItem regionItem = new RegionItem(latLng,
                            "test" + i);
                    items.add(regionItem);

                }
                mClusterOverlay = new ClusterOverlay(aMap, items,
                        dp2px(getApplicationContext(), clusterRadius),
                        getApplicationContext());
                mClusterOverlay.setClusterRenderer(ClusterShowActivity.this);
                mClusterOverlay.setOnClusterClickListener(ClusterShowActivity.this);
            }
        }.start();
    }

    @Override
    public void onClick(Marker marker, List<ClusterItem> clusterItems) {
        //点击聚合点回调处理
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (ClusterItem clusterItem : clusterItems) {
            builder.include(clusterItem.getPosition());
        }
        LatLngBounds latLngBounds = builder.build();
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 0)
        );
    }

    @Override
    public Drawable getDrawAble(int clusterNum) {
        int radius = dp2px(getApplicationContext(), 80);
        if (clusterNum == 1) {
            Drawable bitmapDrawable = mBackDrawAbles.get(1);
            if (bitmapDrawable == null) {
                bitmapDrawable =
                        getApplication().getResources().getDrawable(
                                R.drawable.icon_openmap_mark);
                mBackDrawAbles.put(1, bitmapDrawable);
            }

            return bitmapDrawable;

        } else if (clusterNum < 5) {

            Drawable bitmapDrawable = mBackDrawAbles.get(2);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(159, 210, 154, 6)));
                mBackDrawAbles.put(2, bitmapDrawable);
            }

            return bitmapDrawable;

        } else if (clusterNum < 10) {
            Drawable bitmapDrawable = mBackDrawAbles.get(3);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(199, 217, 114, 0)));
                mBackDrawAbles.put(3, bitmapDrawable);
            }

            return bitmapDrawable;

        } else {
            Drawable bitmapDrawable = mBackDrawAbles.get(4);
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(null, drawCircle(radius,
                        Color.argb(235, 215, 66, 2)));
                mBackDrawAbles.put(4, bitmapDrawable);
            }

            return bitmapDrawable;
        }
    }

    private Bitmap drawCircle(int radius, int color) {

        Bitmap bitmap = Bitmap.createBitmap(radius * 2, radius * 2,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        RectF rectF = new RectF(0, 0, radius * 2, radius * 2);
        paint.setColor(color);
        canvas.drawArc(rectF, 0, 360, true, paint);
        return bitmap;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
