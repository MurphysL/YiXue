package cn.edu.nuc.androidlab.yixue

import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener


/**
 * Location Util
 *
 * Created by MurphySL on 2017/7/5.
 */
object LocationManager {

    val locationClient : AMapLocationClient = AMapLocationClient(MyApplication.instance())
    lateinit var locationClientOption : AMapLocationClientOption

    //建造者模式！！！
    fun setLocationClientOption(){
        locationClientOption = AMapLocationClientOption()

        locationClientOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy // 高精度
        locationClientOption.interval = 2000 // 连续定位时间间隔
        locationClientOption.isNeedAddress = true // 是否需要返回地址描述
        // 强制刷新 WIFI （默认）
        // 不允许模拟 Mock 位置结果 （默认）
        // 定位请求超时时间 30s （默认）
        locationClient.setLocationOption(locationClientOption)
    }


    fun openLocation(){
        locationClient.startLocation()
    }

    fun stopLocation(){
        locationClient.stopLocation()
    }

    public interface LocationListener{
        fun onLocationChanged(aMapLocation : AMapLocation?)
    }

    fun setLocationListener(locationListener : LocationListener){
        val aMapLoctionListener = AMapLocationListener {
            aMapLocation ->
            locationListener.onLocationChanged(aMapLocation)
        }
        locationClient.setLocationListener(aMapLoctionListener)
    }

}
