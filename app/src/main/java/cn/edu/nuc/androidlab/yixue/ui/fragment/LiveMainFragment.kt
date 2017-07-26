package cn.edu.nuc.androidlab.yixue.ui.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.edu.nuc.androidlab.yixue.util.Config
import cn.edu.nuc.androidlab.yixue.R
import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.fragment_live_main.*

/**
 * Live 主展示页面
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
        val query : AVQuery<AVObject> = AVQuery(Config.TEXT_LIVE_TABLE_NAME)
        query.addDescendingOrder(Config.TEXT_LIVE_STAR) // 按星级排序
        query.addDescendingOrder(Config.TEXT_LIVE_START_TIME) // 按时间排序
        query.findInBackground(object : FindCallback<AVObject>(){
            override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
                p0?.let {
                    Log.i(TAG, "total number : ${p0.size}")
                    live_list.layoutManager = LinearLayoutManager(context)
                    live_list.adapter = object : CommonAdapter<AVObject>(context, R.layout.item_live, it){
                        override fun convert(holder: ViewHolder?, t: AVObject?, position: Int) {
                            t?.let {
                                holder?.setText(R.id.live_name, it.get(Config.TEXT_LIVE_LIVE_NAME) as String)
                                //主讲人

                                // 人数

                                // 星级
                                //holder?.setRating(R.id.live_start, it.get(Config.TEXT_LIVE_STAR) as Float)
                                holder?.setOnClickListener(R.id.live_info) {
                                    // 待进行后台认证

                                    enterLive((t.get(Config.TEXT_LIVE_CONVERSATION_ID) as AVObject).objectId)
                                }
                            }?:Snackbar.make(live_list, "未找到相关Live" , Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }

    private fun  enterLive(conversationId : String) {
        val conversationQuery : AVIMConversationsQuery = AVIMClient.getInstance(AVUser.getCurrentUser().objectId).conversationsQuery
        conversationQuery.let {
            it.whereEqualTo("objectId", conversationId)
            conversationQuery.findInBackground(object : AVIMConversationQueryCallback(){
                override fun done(p0: MutableList<AVIMConversation>?, p1: AVIMException?) {
                    p0?.let {
                        val conversation = p0[0]
                        val member = ArrayList<String>()
                        member.addAll(conversation.members)
                        member += AVUser.getCurrentUser().objectId
                        conversation.addMembers(member, object : AVIMConversationCallback(){
                            override fun done(p0: AVIMException?) {
                                if(p0 != null){
                                    Snackbar.make(live_list, "参与 Live 成功", Snackbar.LENGTH_LONG).show()
                                }else{
                                    Snackbar.make(live_list, "进入 Live 失败 $p0", Snackbar.LENGTH_LONG).show()
                                    Log.i(TAG, p0)
                                }
                            }
                        })
                    }?: Snackbar.make(live_list, "未知的错误 ：$p1", Snackbar.LENGTH_LONG).show()
                }
            })
        }
    }


}