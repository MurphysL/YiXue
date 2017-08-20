package debug

import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import cn.edu.nuc.androidlab.yixue.receiver.LiveAudioReceiver
import cn.edu.nuc.androidlab.yixue.service.LiveAudioService
import cn.edu.nuc.androidlab.yixue.util.Config

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

        @JvmStatic
        var player_flag = false

    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        startService(Intent(this, LiveAudioService::class.java))
    }

    override fun onTerminate() {
        super.onTerminate()

        stopService(Intent(this, LiveAudioService::class.java))
    }
}