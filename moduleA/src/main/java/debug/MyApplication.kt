package debug

import android.content.Intent
import android.util.Log
import cn.edu.nuc.androidlab.yixue.service.LiveAudioService

/**
 * MyApplication
 *
 * Created by MurphySL on 2017/7/5.
 */
class MyApplication : cn.edu.nuc.androidlab.common.BaseApplication(){

    companion object {

        private var instance : MyApplication? = null
        @JvmStatic
        fun instance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        startService(Intent(this, LiveAudioService::class.java))
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.i("APP", "onTer")
        stopService(Intent(this, LiveAudioService::class.java))
    }
}