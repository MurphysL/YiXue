package cn.edu.nuc.androidlab.yixue.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import cn.edu.nuc.androidlab.yixue.R
import cn.edu.nuc.androidlab.yixue.service.LiveAudioService


/**
 * Created by MurphySL on 2017/7/31.
 */
class NotifiactionActivity : AppCompatActivity(){
    private val TAG : String = this.javaClass.simpleName

    private val conn = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

        }

        override fun onServiceDisconnected(p0: ComponentName?) {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)


        val intent = Intent(this, LiveAudioService::class.java)
        startService(intent)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)

    }

    override fun onDestroy() {
        super.onDestroy()

        unbindService(conn)
    }
}