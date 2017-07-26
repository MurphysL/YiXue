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
import cn.edu.nuc.androidlab.yixue.R
import cn.leancloud.chatkit.activity.LCIMConversationActivity
import cn.leancloud.chatkit.utils.LCIMConstants
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMConversationsQuery
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.fragment_live_main.*

/**
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

    private fun initData() {
        val currentUserId = AVUser.getCurrentUser().objectId
        val conversationQuery : AVIMConversationsQuery = AVIMClient.getInstance(currentUserId).conversationsQuery
        val list  = ArrayList<String>()
        list.add(currentUserId)
        //conversationQuery.whereEqualTo("c", currentUserId) // 对话创建者
        //conversationQuery.whereContains()
        conversationQuery.containsMembers(list)
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
}