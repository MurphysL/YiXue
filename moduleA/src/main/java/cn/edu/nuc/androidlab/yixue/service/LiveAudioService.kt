package cn.edu.nuc.androidlab.yixue.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v7.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import cn.edu.nuc.androidlab.common.bean.Live
import cn.edu.nuc.androidlab.yixue.R
import cn.edu.nuc.androidlab.yixue.util.Config
import cn.leancloud.chatkit.cache.LCIMLocalCacheUtils
import cn.leancloud.chatkit.utils.LCIMAudioHelper
import cn.leancloud.chatkit.utils.LCIMPathUtils
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage
import com.bumptech.glide.Glide

/**
 * Live Audio Service
 *
 * 待修改 binder service
 * remoteView 为空
 *
 * Created by MurphySL on 2017/8/19.
 */
class LiveAudioService : Service() {
    private val TAG : String = this.javaClass.simpleName

    private var remoteViews : RemoteViews? = null

    private val audioList = ArrayList<AVIMAudioMessage>()

    private var player_flag = false

    private lateinit var manager : NotificationManager

    private val binder = AudioBinder()

    private lateinit var media : MediaPlayer

    private var currentAudio = 0

    private fun isPlaying() : Boolean = LCIMAudioHelper.getInstance().isPlaying

    private fun playAudio(){
        val msg = audioList[currentAudio]
        Log.i(TAG, audioList[currentAudio].duration.toString())

        val localFilePath = msg.localFilePath
        var playPath = localFilePath
        if(localFilePath == null){
            playPath = LCIMPathUtils.getAudioCachePath(applicationContext, msg.messageId)
            LCIMLocalCacheUtils.downloadFileAsync(msg.fileUrl, playPath)
        }
        Log.i(TAG, "audio path : $playPath")
        LCIMAudioHelper.getInstance().playAudio(playPath)
    }

    fun changeAudio(){
        Log.i(TAG, "change ${audioList.size}")
        if(isPlaying() && audioList.isNotEmpty()){
            LCIMAudioHelper.getInstance().pausePlayer()
        }else{
            playAudio()
        }
    }

    fun nextAudio(){
        Log.i(TAG, "next")
        if(audioList.isNotEmpty()){
            currentAudio ++
            if(currentAudio == audioList.size){
                currentAudio = 0
            }
            playAudio()
        }

    }

    fun previousAudio(){
        Log.i(TAG, "previous")
        if(audioList.isNotEmpty()){
            currentAudio --
            if(currentAudio == -1){
                currentAudio = audioList.size - 1
            }
            playAudio()
        }
    }

    inner class AudioBinder : Binder(){

        fun loadAudio(audioMessage : AVIMAudioMessage){
            Log.i("AudioBinder", "LOAD MESSAGE ${audioList.size}")
            audioList.add(audioMessage)
        }

        fun updateInfo(live : Live?){
            Log.i("AudioBinder", "Live is Null")
            live?.let {
                Log.i("AudioBinder", "Get Live Info Success: ${live.name}")
                updateNotification(live)
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")

        initMediaPlayer()

        initNotification()
    }

    private fun initMediaPlayer() {
        media = MediaPlayer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val action = intent.action
            action?.let {
                when(it){
                    Config.LIVE_SOUNDS_CHANGE -> {
                        changeAudio()
                        player_flag = !player_flag
                        Log.i(TAG, player_flag.toString())
                        updateNotification()
                    }
                    Config.LIVE_SOUNDS_NEXT -> {
                        Log.i(TAG, "next")
                        nextAudio()
                    }
                    Config.LIVE_SOUNDS_PREVIOUS -> {
                        Log.i(TAG, "previous")
                        previousAudio()
                    }
                    else -> Unit
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateNotification(live : Live){
        if(remoteViews == null){
             remoteViews = RemoteViews(packageName, R.layout.item_live_sounds)
        }
        remoteViews?.setTextViewText(R.id.live_name, live.name)
        remoteViews?.setTextViewText(R.id.live_speaker, live.username)

        val notification = NotificationCompat
                .Builder(this)
                .setCustomBigContentView(remoteViews)
                .setContentText(live.name)
                .setContentTitle(live.username)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .build()

        manager.notify(1, notification)
    }

    private fun updateNotification() {
        if(player_flag){
            remoteViews?.setImageViewResource(R.id.change, R.drawable.ic_play_circle_outline_black_24dp)
        }else{
            remoteViews?.setImageViewResource(R.id.change, R.drawable.ic_pause_circle_outline_black_24dp)
        }

        val notification = NotificationCompat
                .Builder(this)
                .setCustomBigContentView(remoteViews)
                .setContentText("Live 主题")
                .setContentTitle("Live 主讲人")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .build()

        manager.notify(1, notification)
    }

    private fun initNotification() {
        remoteViews = RemoteViews(packageName, R.layout.item_live_sounds)

        val intent_change = Intent(this, LiveAudioService::class.java)
        intent_change.action = Config.LIVE_SOUNDS_CHANGE
        val pending_change : PendingIntent = PendingIntent.getService(this, 1, intent_change, PendingIntent.FLAG_CANCEL_CURRENT)
        remoteViews!!.setOnClickPendingIntent(R.id.change, pending_change)

        val intent_previous = Intent(this, LiveAudioService::class.java)
        intent_previous.action = Config.LIVE_SOUNDS_PREVIOUS
        val pending_previous : PendingIntent = PendingIntent.getService(this, 1, intent_previous, PendingIntent.FLAG_CANCEL_CURRENT)
        remoteViews!!.setOnClickPendingIntent(R.id.previous, pending_previous)

        val intent_next = Intent(this, LiveAudioService::class.java)
        intent_next.action = Config.LIVE_SOUNDS_NEXT
        val pending_next : PendingIntent = PendingIntent.getService(this, 1, intent_next , PendingIntent.FLAG_CANCEL_CURRENT)
        remoteViews!!.setOnClickPendingIntent(R.id.next, pending_next)

        manager =  getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat
                .Builder(this)
                .setCustomBigContentView(remoteViews)
                .setContentText("Live 主题")
                .setContentTitle("Live 主讲人")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .build()

        manager.notify(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}