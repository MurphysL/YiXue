package cn.edu.nuc.androidlab.yixue.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import cn.edu.nuc.androidlab.yixue.Config
import cn.edu.nuc.androidlab.yixue.R
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.chatkit.LCChatKitUser
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.SaveCallback
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import kotlinx.android.synthetic.main.activity_create_live.*

/**
 * 创建 Live
 *
 * 待修改：
 * 1. 界面
 * 2. 多人主讲
 * 3. 流程：开通主讲人身份 -> 实名认证 -> 交纳保证金
 *
 * Created by MurphySL on 2017/7/24.
 */
class CreateLiveActivity : AppCompatActivity(){
    private val TAG : String = this.javaClass.simpleName
    private val context : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_live)

        bt_create.setOnClickListener {
            val live_name : String = live_name.text.toString()
            val live_time : String = live_time.text.toString()
            val live_summary : String = live_summary.text.toString()
            val live_price : String = live_price.text.toString()

            createLive(live_name, live_time, live_summary, live_price)
        }
    }

    private fun createLive(live_name : String, live_time : String, live_summary : String, live_pice : String) {
        val userId = AVUser.getCurrentUser().objectId // 用户ID
        val audiences : ArrayList<LCChatKitUser> = ArrayList() // 主讲人及测试用户
        val test_audience = LCChatKitUser("5965e3e70ce463005886ec58", "test2", "http://oh1zr9i3e.bkt.clouddn.com/17-7-24/61140240.jpg") // 官方测试用户
        audiences.add(test_audience)
        //其他主讲人：


        val audiences_clientId : ArrayList<String> = ArrayList() // 所有听众 clientId
        audiences.mapTo(audiences_clientId) { it.userId }

        // 群聊
        LCChatKit.getInstance().client.createConversation(audiences_clientId, live_name, null, false, true, object : AVIMConversationCreatedCallback(){
            override fun done(p0: AVIMConversation?, p1: AVIMException?) {
                p0?.let {
                    val live : AVObject = AVObject(Config.TEXT_LIVE_TABLE_NAME)
                    live.put(Config.TEXT_LIVE_USER_ID, AVObject.createWithoutData(Config.USER_TABLE, userId))
                    live.put(Config.TEXT_LIVE_CONVERSATION_ID, AVObject.createWithoutData(Config.CONVERSATION_TABLE, it.conversationId))
                    live.put(Config.TEXT_LIVE_LIVE_NAME, live_name)
                    live.put(Config.TEXT_LIVE_SUMMARY, live_summary)
                    //开始日期：

                    //价格：

                    live.saveInBackground(object : SaveCallback(){
                        override fun done(p0: AVException?) {
                            if(p0 != null){
                                Snackbar.make(live_name_hint, "创建 Live 信息失败！$p0", Snackbar.LENGTH_SHORT).show()
                            }else{
                                Snackbar.make(live_name_hint, "创建Live成功", Snackbar.LENGTH_SHORT).show()
                                //val intent : Intent = Intent(context, LCIMConversationActivity::class.java)
                                //intent.putExtra(LCIMConstants.CONVERSATION_ID, it.conversationId)
                                //startActivity(intent)

                                startActivity(Intent(context, SelectLiveActivity::class.java))
                            }
                        }
                    })
                }?:Snackbar.make(live_name_hint, "未知的错误发生了", Snackbar.LENGTH_SHORT).show()

            }
        })



    }
}