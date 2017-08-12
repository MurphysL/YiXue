package debug

import android.app.Application
import android.content.IntentFilter
import cn.edu.nuc.androidlab.yixue.receiver.LiveSoundsReceiver
import cn.edu.nuc.androidlab.yixue.util.Config
import com.avos.avoscloud.AVOSCloud
import com.avos.avoscloud.AVObject

/**
 * MyApplication
 *
 * Created by MurphySL on 2017/7/5.
 */
class MyApplication : cn.edu.nuc.androidlab.common.BaseApplication(){

    val liveTypes = arrayOf("计算机", "经济管理", "心理学", "外语", "文学历史", "艺术设计", "工学", "理学", "哲学", "法学", "生命科学")

    private val liveSoundsReceiver = LiveSoundsReceiver()

    companion object {
        private var instance : Application? = null
        fun instance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        //AVObject.registerSubclass(Live::class.java)
        //AVObject.registerSubclass(LU::class.java)

        // 初始化参数依次为 this, AppId, AppKey
        //AVOSCloud.initialize(this, "O5aEuqARNjtbvT2tGTW23bB5-gzGzoHsz", "XMaxhc0a9L5cDOIAXrBeqoS8")
        // LeanCloud 调试日志
        /*AVOSCloud.setDebugLogEnabled(true)*/

        registerLiveSoundsReceiver()


    }

    private fun registerLiveSoundsReceiver() {
        val filter : IntentFilter = IntentFilter()
        filter.addAction(Config.LIVE_SOUNDS_START)
        filter.addAction(Config.LIVE_SOUNDS_STOP)
        filter.addAction(Config.LIVE_SOUNDS_NEXT)
        filter.addAction(Config.LIVE_SOUNDS_PREVIOUS)

        registerReceiver(liveSoundsReceiver, filter)
    }

    override fun onTerminate() {
        super.onTerminate()

        unregisterReceiver(liveSoundsReceiver)
    }
}