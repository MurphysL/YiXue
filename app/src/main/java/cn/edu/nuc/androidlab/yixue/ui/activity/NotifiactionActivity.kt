package cn.edu.nuc.androidlab.yixue.ui.activity

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.NotificationCompat
import android.widget.RemoteViews
import cn.edu.nuc.androidlab.yixue.R
import cn.edu.nuc.androidlab.yixue.receiver.LiveSoundsReceiver
import cn.edu.nuc.androidlab.yixue.util.Config

/**
 * Created by MurphySL on 2017/7/31.
 */
class NotifiactionActivity : AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val remoteViews : RemoteViews = RemoteViews(packageName, R.layout.item_live_sounds)

        val intent_start : Intent = Intent(this, LiveSoundsReceiver::class.java)
        intent_start.action = Config.LIVE_SOUNDS_START
        val pending_start : PendingIntent = PendingIntent.getBroadcast(this, 1, intent_start, PendingIntent.FLAG_CANCEL_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.start, pending_start)

        val intent_stop : Intent = Intent(this, LiveSoundsReceiver::class.java)
        intent_stop.action = Config.LIVE_SOUNDS_STOP
        val pending_stop : PendingIntent = PendingIntent.getBroadcast(this, 1, intent_stop, PendingIntent.FLAG_CANCEL_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.stop, pending_stop)

        val intent_previous : Intent = Intent(this, LiveSoundsReceiver::class.java)
        intent_previous.action = Config.LIVE_SOUNDS_PREVIOUS
        val pending_previous : PendingIntent = PendingIntent.getBroadcast(this, 1, intent_previous, PendingIntent.FLAG_CANCEL_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.previous, pending_previous)

        val intent_next : Intent = Intent(this, LiveSoundsReceiver::class.java)
        intent_next.action = Config.LIVE_SOUNDS_NEXT
        val pending_next : PendingIntent = PendingIntent.getBroadcast(this, 1, intent_next , PendingIntent.FLAG_CANCEL_CURRENT)
        remoteViews.setOnClickPendingIntent(R.id.next, pending_next)

        val manager : NotificationManager = getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat
                .Builder(NotifiactionActivity@this)
                .setCustomBigContentView(remoteViews)
                .setContentText("123")
                .setContentTitle("123")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .build()

        manager.notify(1, notification)
    }
}