package cn.edu.nuc.androidlab.yixue.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cn.edu.nuc.androidlab.common.bean.LU
import cn.edu.nuc.androidlab.common.bean.Live
import cn.edu.nuc.androidlab.yixue.util.Config
import cn.edu.nuc.androidlab.yixue.R
import cn.edu.nuc.androidlab.yixue.ui.activity.im.ConversationActivity
import cn.edu.nuc.androidlab.yixue.ui.adapter.AnimCommonAdapter
import cn.leancloud.chatkit.activity.LCIMConversationActivity
import cn.leancloud.chatkit.utils.LCIMConstants
import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback
import com.squareup.picasso.Picasso
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.fragment_live_main.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Lives order by star and start time
 *
 * unFinished：
 * 1.后台身份认证
 * 2.付费
 *
 * Created by MurphySL on 2017/7/24.
 */
class LiveMainFragment : Fragment(){
    private val TAG : String = this.javaClass.simpleName

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_live_main, container, false)

        initData()

        return rootView
    }

    private fun initData() {
        val query : AVQuery<Live> = AVQuery(Config.LIVE_TABLE)
        query.addDescendingOrder(Config.LIVE_STAR) // 按星级排序
        query.addDescendingOrder(Config.LIVE_START_AT) // 按时间排序
        query.findInBackground(object : FindCallback<Live>(){
            override fun done(p0: MutableList<Live>?, p1: AVException?) {
                if(p1 != null){
                    toast("Query Live Fail").show()
                    Log.i(TAG, "Query Live Fail: $p1")
                    return
                }

                p0?.let {
                    live_list.layoutManager = LinearLayoutManager(context)
                    live_list.adapter = object : AnimCommonAdapter<Live>(context, R.layout.item_live_new, it){
                        override fun convert(holder: ViewHolder?, t: Live?, position: Int) {
                            t?.let {
                                holder?.setText(R.id.live_name, it.name)
                                holder?.setText(R.id.live_type, it.type)
                                holder?.setRating(R.id.live_star, it.star.toFloat())
                                val date = t.startAt
                                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                holder?.setText(R.id.live_time, sdf.format(date))
                                holder?.setText(R.id.live_speaker, it.username)
                                holder?.setImageWithPicasso(R.id.live_pic, it.pic)

                                holder?.setOnClickListener(R.id.live_info) {
                                    // 待进行后台认证
                                    // 付费

                                    AVUser.getCurrentUser()?.let {
                                        uploadInfo(t)
                                    }?: toast("Please Login First").show()
                                }
                            }?: toast("Unknown Error").show()
                        }
                    }
                }?: Log.i(TAG, "Query Live Fail")
            }
        })

    }

    private fun uploadInfo(live : Live){
        val query = AVQuery<LU>("LU")
        query.whereEqualTo(Config.LU_USER_ID, AVObject.createWithoutData(Config.USER_TABLE, AVUser.getCurrentUser().objectId))
        query.whereEqualTo(Config.LU_LIVE_ID, AVObject.createWithoutData(Config.LIVE_TABLE, live.objectId))
        // 查询是否已选
        query.findInBackground(object : FindCallback<LU>(){
            override fun done(p0: MutableList<LU>?, p1: AVException?) {
                if(p1 == null){
                    p0?.let {
                        if(p0.isEmpty()){
                            val lu = LU()
                            lu.liveId = live.objectId
                            lu.userId = AVUser.getCurrentUser().objectId
                            lu.saveInBackground(object : SaveCallback(){
                                override fun done(p0: AVException?) {
                                    p0?.let {
                                        toast("Unknown Error")
                                        Log.i(TAG, "Create LU Fail：$p0")
                                    }?:enterLive(live)
                                }
                            })
                        }else{
                            enterLive(live)
                        }
                    }
                }else{
                    toast("Unknown Error")
                    Log.i(TAG, "Query LU Fail: $p1")
                }
            }
        })

    }

    private fun  enterLive(live : Live) {
        val conversationQuery : AVIMConversationsQuery = AVIMClient.getInstance(AVUser.getCurrentUser().objectId).conversationsQuery
        conversationQuery.let {
            it.whereEqualTo("objectId", live.conversationId)
            conversationQuery.findInBackground(object : AVIMConversationQueryCallback(){
                override fun done(p0: MutableList<AVIMConversation>?, p1: AVIMException?) {
                    if(p1 != null){
                        toast("Query Conversation Fail").show()
                        Log.i(TAG, "Query Conversation Fail: $p1")
                        return
                    }

                    p0?.let {
                        val conversation = p0[0]
                        val member = ArrayList<String>()
                        member.addAll(conversation.members)
                        member += AVUser.getCurrentUser().objectId
                        conversation.addMembers(member, object : AVIMConversationCallback(){
                            override fun done(p0: AVIMException?) {
                                if(p0 == null){
                                    Log.i(TAG, "Get Conversation Success")
                                    val intent = Intent(context, ConversationActivity::class.java)
                                    intent.putExtra(LCIMConstants.CONVERSATION_ID, live.conversationId)
                                    intent.putExtra(Config.LIVE_TABLE, live)
                                    startActivity(intent)
                                }else{
                                    toast("Enter Conversation Fail").show()
                                    Log.i(TAG, "Enter Conversation Fail: $p0")
                                }
                            }
                        })
                    }?: toast("未知的错误").show()
                }
            })
        }
    }

    private fun toast(msg : String) : Snackbar = Snackbar.make(live_list, msg, Snackbar.LENGTH_LONG)

    fun ViewHolder.setImageWithPicasso(viewId : Int, url : String) : ViewHolder{
        val view : ImageView = getView(viewId)
        Picasso.with(context)
                .load(url)
                .into(view)
        return this
    }

}