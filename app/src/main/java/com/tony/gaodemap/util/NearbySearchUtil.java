package com.tony.gaodemap.util;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.nearby.NearbySearch;
import com.amap.api.services.nearby.NearbySearchFunctionType;
import com.amap.api.services.nearby.UploadInfo;

/**
 * Created by gambler on 16/10/14
 */
public class NearbySearchUtil {

    /*** 本类处理搜索相关操作*/

    /**
     * 单次上传用户信息
     * @param ctx context
     * @param aMapLocation 高德位置
     * @param userId  用户ID
     */
    public static void uploadUserInfoOnce(Context ctx, AMapLocation aMapLocation, String userId){
        /**构造上传位置信息*/
        UploadInfo loadInfo = new UploadInfo();
        /**设置上传位置的坐标系支持AMap坐标数据与GPS数据*/
        loadInfo.setCoordType(NearbySearch.AMAP);
        /**设置上传数据位置,位置的获取推荐使用高德定位sdk进行获取*/
        loadInfo.setPoint(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
        /**设置上传用户id*/
        loadInfo.setUserID(userId);
        /**调用异步上传接口*/
        NearbySearch.getInstance(ctx).uploadNearbyInfoAsyn(loadInfo);
    }



    /**
     * 搜索附近
     * @param ctx context
     * @param aMapLocation 高德位置
     */
    public static void startSearchNearbyAsyn(Context ctx, AMapLocation aMapLocation, int radius, int time){
        /**设置搜索条件*/
        NearbySearch.NearbyQuery query = new NearbySearch.NearbyQuery();
        /**设置搜索的中心点*/
        query.setCenterPoint(new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude()));
        /**设置搜索的坐标体系*/
        query.setCoordType(NearbySearch.AMAP);
        /**设置搜索半径*/
        query.setRadius(radius);//默认为3000米，上限为10000米,下限0米
        /**设置查询的时间*/
        query.setTimeRange(time);//上限为24小时，下限为5秒，默认为1800秒。
        /**设置查询的方式驾车还是距离*/
        query.setType(NearbySearchFunctionType.DISTANCE_SEARCH);
        /**开启异步查询*/
        NearbySearch.getInstance(ctx).searchNearbyInfoAsyn(query);
    }

    /**
     * 清除用户信息
     * @param ctx context
     */
    public static void clearUserInfo(Context ctx ,String userId){
        NearbySearch.getInstance(ctx).setUserID(userId);
        NearbySearch.getInstance(ctx).clearUserInfoAsyn();
    }

    /**
     * 销毁派单(搜索)对象
     */
    public static void destroyNearbySearch(){
        NearbySearch.destroy();
    }



}
