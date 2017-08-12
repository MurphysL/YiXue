package cn.edu.nuc.androidlab.yixue.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cn.edu.nuc.androidlab.yixue.util.Config

/**
 * Created by MurphySL on 2017/7/31.
 */
class LiveSoundsReceiver : BroadcastReceiver(){
    private val TAG : String = this.javaClass.simpleName

    override fun onReceive(p0: Context?, p1: Intent?) {
        val action = p1?.action
        action?.let {
            when(it){
                Config.LIVE_SOUNDS_START -> Log.i(TAG, "start")
                Config.LIVE_SOUNDS_STOP -> Log.i(TAG, "stop")
                Config.LIVE_SOUNDS_NEXT -> Log.i(TAG, "next")
                Config.LIVE_SOUNDS_PREVIOUS -> Log.i(TAG, "previous")
                else -> Unit
            }
        }
    }


}