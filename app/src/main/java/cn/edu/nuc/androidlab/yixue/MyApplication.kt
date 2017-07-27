package cn.edu.nuc.androidlab.yixue

import android.app.Application
import cn.edu.nuc.androidlab.yixue.bean.LU
import cn.edu.nuc.androidlab.yixue.bean.Live
import com.avos.avoscloud.AVOSCloud
import com.avos.avoscloud.AVObject

/**
 * MyApplication
 *
 * Created by MurphySL on 2017/7/5.
 */
class MyApplication : Application(){

    companion object {
        private var instance : Application? = null
        fun instance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        AVObject.registerSubclass(Live::class.java)
        AVObject.registerSubclass(LU::class.java)

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this, "O5aEuqARNjtbvT2tGTW23bB5-gzGzoHsz", "XMaxhc0a9L5cDOIAXrBeqoS8")
        // LeanCloud 调试日志
        AVOSCloud.setDebugLogEnabled(true)


    }
}