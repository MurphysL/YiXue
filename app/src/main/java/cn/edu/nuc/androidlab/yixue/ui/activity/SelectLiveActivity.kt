package cn.edu.nuc.androidlab.yixue.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import cn.edu.nuc.androidlab.yixue.R
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.chatkit.LCChatKitUser
import cn.leancloud.chatkit.activity.LCIMConversationActivity
import cn.leancloud.chatkit.utils.LCIMConstants
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import kotlinx.android.synthetic.main.activity_select_live.*

/**
 * Live 测试入口
 * Created by MurphySL on 2017/7/11.
 */
class SelectLiveActivity : AppCompatActivity(){
    private val TAG : String = this.javaClass.simpleName

    private val context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_live)

        go.setOnClickListener {
            Log.i(TAG, "123")
            openLive()
        }

        clean.setOnClickListener {
            AVUser.logOut()
            startActivity(Intent(SelectLiveActivity@this, LoginActivity::class.java))
        }
    }

    private fun openLive() {
        val clientId  = AVUser.getCurrentUser().username // 用户昵称作为clientId,待修改
        val live_name : String = "TEST" // Live 名称
        val audiences : ArrayList<LCChatKitUser> = ArrayList() // 听众
        val audiences_clientId : ArrayList<String> = ArrayList() // 所有听众 clientId

        audiences.mapTo(audiences_clientId) { it.userId }


        LCChatKit.getInstance().open(clientId, object : AVIMClientCallback(){
            override fun done(p0: AVIMClient?, p1: AVIMException?) {
                if(p1 == null){

                    LCChatKit.getInstance().client.createConversation(audiences_clientId, live_name, null, true,
                            object : AVIMConversationCreatedCallback(){
                                override fun done(p0: AVIMConversation?, p1: AVIMException?) {
                                    if(p0 != null){
                                        val intent : Intent = Intent(context, LCIMConversationActivity::class.java)
                                        intent.putExtra(LCIMConstants.CONVERSATION_ID, p0.conversationId)
                                        startActivity(intent)
                                    }else{
                                        Snackbar.make(clean, "未知的错误发生了", Snackbar.LENGTH_SHORT).show()
                                        Log.i(TAG, "获取会话失败")
                                    }
                                }
                            })

                }else{
                    Snackbar.make(clean, p1.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }
        })

    }
}