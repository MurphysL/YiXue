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
import cn.edu.nuc.androidlab.yixue.R
import cn.edu.nuc.androidlab.yixue.bean.LU
import cn.edu.nuc.androidlab.yixue.bean.Live
import cn.edu.nuc.androidlab.yixue.ui.adapter.AnimCommonAdapter
import cn.edu.nuc.androidlab.yixue.util.Config
import cn.leancloud.chatkit.activity.LCIMConversationActivity
import cn.leancloud.chatkit.utils.LCIMConstants
import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMConversationsQuery
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback
import com.squareup.picasso.Picasso
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.fragment_live_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * LiveJoinFragment
 *
 * Created by MurphySL on 2017/7/24.
 */
class LiveJoinFragment : Fragment(){
    private val TAG : String = this.javaClass.simpleName

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_live_join, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    //扩展函数
    fun ViewHolder.setImageWithPicasso(viewId : Int, url : String) : ViewHolder{
        val view : ImageView = getView(viewId)
        Picasso.with(context)
                .load(url)
                .into(view)
        return this
    }

    private fun liveInfo(liveId_list : List<LU>){
        Log.i(TAG, liveId_list.size.toString())
        val query_list = ArrayList<AVQuery<Live>>()

        liveId_list.forEach {
            val query = AVQuery<Live>(Config.LIVE_TABLE)
            query.whereEqualTo(Config.LIVE_ID, it.liveId)
            query_list += query
        }

        val query : AVQuery<Live> = AVQuery.or(query_list)
        query.findInBackground(object : FindCallback<Live>(){
            override fun done(p0: MutableList<Live>?, p1: AVException?) {
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
                                // 人数

                                holder?.setOnClickListener(R.id.live_info) {
                                    enterConversation(t.conversationId)
                                }
                            }?:Snackbar.make(live_list, "未找到相关Live" , Snackbar.LENGTH_LONG).show()
                        }
                    }
                }?: Snackbar.make(live_list, "您还没有创建 Live ", Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun enterConversation(conversationId: String) {
        Log.i(TAG,"conversationId $conversationId")
        //val intent: Intent = Intent(context, LCIMConversationActivity::class.java)
        //intent.putExtra(LCIMConstants.CONVERSATION_ID, conversationId)
        //startActivity(intent)
    }

    @Deprecated("deprecated")
    private fun  enterLive(conversationId: String) {
        val currentUserId = AVUser.getCurrentUser().objectId
        val conversationQuery : AVIMConversationsQuery = AVIMClient.getInstance(currentUserId).conversationsQuery
        val list  = ArrayList<String>()
        list.add(currentUserId)
        //conversationQuery.whereEqualTo("c", currentUserId) // 对话创建者
        //conversationQuery.whereContains()
        //conversationQuery.containsMembers(list)

        conversationQuery.whereEqualTo("objectId", conversationId)
        conversationQuery.findInBackground(object : AVIMConversationQueryCallback(){
            override fun done(p0: MutableList<AVIMConversation>?, p1: AVIMException?) {
                p0?.let {
                    live_list.layoutManager = LinearLayoutManager(context)
                    live_list.adapter = object : CommonAdapter<AVIMConversation>(context, R.layout.item_live, it){
                        override fun convert(holder: ViewHolder?, t: AVIMConversation?, position: Int) {
                            Log.i(TAG, t?.name)
                            t?.let {
                                holder?.setText(R.id.live_name, it.name as String)
                                // 相关信息

                                holder?.setOnClickListener(R.id.live_info, {
                                    val intent: Intent = Intent(context, LCIMConversationActivity::class.java)
                                    intent.putExtra(LCIMConstants.CONVERSATION_ID, t.conversationId)
                                    startActivity(intent)
                                })

                            }?: Snackbar.make(live_list, "未找到相关Live" , Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }

    private fun initData() {
        val query : AVQuery<LU> = AVQuery(Config.LU_TABLE)
        query.addDescendingOrder(Config.LIVE_START_AT) // 按时间排序
        query.whereEqualTo(Config.LU_USER_ID, AVObject.createWithoutData(Config.USER_TABLE, AVUser.getCurrentUser().objectId))
        query.findInBackground(object : FindCallback<LU>(){
            override fun done(p0: MutableList<LU>?, p1: AVException?) {
                p0?.let {
                    Log.i(TAG, p0.size.toString())
                    if(p0.size == 0)
                        Snackbar.make(live_list, "您还没有购买 Live ", Snackbar.LENGTH_LONG).show()
                    else
                        liveInfo(it)
                }?: Snackbar.make(live_list, "未知的错误 $p1 ", Snackbar.LENGTH_LONG).show()
            }
        })
    }
}