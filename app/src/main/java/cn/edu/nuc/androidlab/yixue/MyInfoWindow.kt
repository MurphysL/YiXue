package cn.edu.nuc.androidlab.yixue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.model.Marker

/**
 * 自定义 InfoWindow
 * Created by MurphySL on 2017/7/8.
 */
class MyInfoWindow(private val context : Context) : AMap.InfoWindowAdapter{

    //监听自定义 InfoWindow 内容回调
    override fun getInfoContents(p0: Marker?): View? {
        return null
    }

    override fun getInfoWindow(p0: Marker?): View? {
        val infoWindows : View = LayoutInflater.from(context).inflate(R.layout.item_infowindow, null)
        return infoWindows
    }

}