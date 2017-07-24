package cn.edu.nuc.androidlab.yixue.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import cn.edu.nuc.androidlab.yixue.Config
import cn.edu.nuc.androidlab.yixue.R
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.chatkit.LCChatKitUser
import cn.leancloud.chatkit.activity.LCIMConversationActivity
import cn.leancloud.chatkit.utils.LCIMConstants
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.SaveCallback
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
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

        clean.setOnClickListener {
            AVUser.logOut()
            startActivity(Intent(SelectLiveActivity@this, LoginActivity::class.java))
        }

    }

    /**
     * 开启实时通讯，创建 AVIMClient
     */
    private fun initAVIMClient() {
        val userId = AVUser.getCurrentUser().objectId // 用户ID
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