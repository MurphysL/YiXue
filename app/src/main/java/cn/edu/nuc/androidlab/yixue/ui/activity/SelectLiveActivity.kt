package cn.edu.nuc.androidlab.yixue.ui.activity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import cn.edu.nuc.androidlab.yixue.R
import cn.leancloud.chatkit.LCChatKit
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import kotlinx.android.synthetic.main.activity_select_live.*

/**
 * Live 测试入口
 *
 * Created by MurphySL on 2017/7/11.
 */
class SelectLiveActivity : AppCompatActivity(){
    private val TAG : String = this.javaClass.simpleName

    private val context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_live)

        initAVIMClient()

        create.setOnClickListener {
            startActivity(Intent(SelectLiveActivity@this, CreateLiveActivity::class.java))
        }

        clean.setOnClickListener {
            AVUser.logOut()
            startActivity(Intent(SelectLiveActivity@this, LoginActivity::class.java))
        }

        all.setOnClickListener {
            startActivity(Intent(SelectLiveActivity@this, MyLiveActivity::class.java))
        }

        my.setOnClickListener {
            startActivity(Intent(SelectLiveActivity@this, JoinLiveActivity::class.java))
        }

        ui.setOnClickListener {
            startActivity(Intent(SelectLiveActivity@this, UITestActivity::class.java))
        }

        notificationTest()

    }

    private fun notificationTest() {
        val intent : Intent = Intent(this, MyLiveActivity::class.java)
        val pending : PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val manager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat
                .Builder(context)
                .setContentTitle("Test")
                .setContentText("Test")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
                .setContentIntent(pending)
                .setLights(Color.GREEN, 1000, 1000)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))) // 大图片
                .setPriority(NotificationCompat.PRIORITY_MAX) // 通知重要程度
                .build()

        manager.notify(1, notification)
        //manager.cancel(1)

    }

    /**
     * 开启实时通讯，创建 AVIMClient
     */
    private fun initAVIMClient() {
        val currentUser : AVUser? = AVUser.getCurrentUser()

        currentUser?.let {
            val userId = it.objectId

            /**
             * AVIMClient.open(AVIMClientCallback cb) 这个方法表示开始连接 LeanCloud 云端服务器（即启动实时通信服务），它在整个使用周期内只需要调用一次。
             */
            LCChatKit.getInstance().open(userId, object : AVIMClientCallback(){
                override fun done(p0: AVIMClient?, p1: AVIMException?) {
                    p0?.let {
                        go.setOnClickListener {
                            Log.i(TAG, p0.clientId)
                            startActivity(Intent(context, LiveActivity::class.java))
                        }

                        create.setOnClickListener {
                            startActivity(Intent(context, CreateLiveActivity::class.java))
                        }
                    } ?: Snackbar.make(clean, p1.toString(), Snackbar.LENGTH_SHORT).show()
                }
            })

        }


    }

}