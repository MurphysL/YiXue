package cn.edu.nuc.androidlab.yixue.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import cn.edu.nuc.androidlab.yixue.service.LiveAudioService
import cn.edu.nuc.androidlab.yixue.util.Config
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.AVIMMessage
import com.avos.avoscloud.im.v2.Conversation
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage

/**
 * Live Audio Receiver
 *
 * Created by MurphySL on 2017/7/31.
 */
class LiveAudioReceiver : BroadcastReceiver(){
    private val TAG : String = this.javaClass.simpleName

    private val service = LiveAudioService()

    override fun onReceive(p0: Context?, p1: Intent?) {
        if(p0 == null)
            return

        val action = p1?.action
        action?.let {
            when(it){
                Config.LIVE_SOUNDS_CHANGE -> {
                    service.startAudio()
                }
                Config.LIVE_SOUNDS_NEXT -> {
                    Log.i(TAG, "next")
                    service.nextAudio()
                }
                Config.LIVE_SOUNDS_PREVIOUS -> {
                    Log.i(TAG, "previous")
                    service.previousAudio()
                }
                else -> Unit
            }
        }
    }

    class AudioPlayTask(val context: Context, val audioList : List<AVIMAudioMessage>) : Runnable{
        override fun run() {
            audioList.forEach {
                val uri = Uri.parse(it.fileUrl)
                val player = MediaPlayer.create(context, uri)
                player.start()
            }
        }

    }


}