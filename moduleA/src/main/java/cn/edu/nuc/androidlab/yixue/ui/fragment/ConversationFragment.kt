package cn.edu.nuc.androidlab.yixue.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import cn.edu.nuc.androidlab.yixue.R
import cn.leancloud.chatkit.adapter.LCIMChatAdapter
import cn.leancloud.chatkit.view.LCIMInputBottomBar
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.AVIMMessage
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback

/**
 * Created by MurphySL on 2017/8/20.
 */
class ConversationFragment : Fragment(){
    private val TAG = this.javaClass.simpleName

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2

    private lateinit var recyclerView : RecyclerView
    private lateinit var refreshLayout : SwipeRefreshLayout
    private lateinit var inputBottomBar : LCIMInputBottomBar
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var itemAdapter : LCIMChatAdapter

    private lateinit var conversation : AVIMConversation

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.lcim_conversation_fragment, container, false)

        initView(rootView)

        return rootView
    }

    private fun initView(rootView: View?){
        rootView?.let {
            layoutManager = LinearLayoutManager(context)
            recyclerView = rootView.findViewById(R.id.fragment_chat_rv_chat)
            recyclerView.layoutManager = layoutManager
            itemAdapter = LCIMChatAdapter()
            itemAdapter.resetRecycledViewPoolSize(recyclerView)
            recyclerView.adapter = itemAdapter

            refreshLayout = rootView.findViewById(R.id.fragment_chat_srl_pullrefresh)
            refreshLayout.isEnabled = false
            inputBottomBar = rootView.findViewById(R.id.fragment_chat_inputbottombar)
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        refreshLayout.setOnRefreshListener {
            val message = itemAdapter.firstMessage
            if(message == null){
                refreshLayout.isRefreshing = false
            }else{
                conversation.queryMessages(message.messageId, message.timestamp, 20, object : AVIMMessagesQueryCallback(){
                    override fun done(p0: MutableList<AVIMMessage>?, p1: AVIMException?) {
                        refreshLayout.isRefreshing = true
                        if(p1 != null){
                            Log.i(TAG, "Conversation Query Fail: $p1")
                            return
                        }

                        p0?.let {
                            if(p0.isNotEmpty()){
                                itemAdapter.addMessageList(it)
                                itemAdapter.setDeliveredAndReadMark(conversation.lastDeliveredAt, conversation.lastReadAt)
                                itemAdapter.notifyDataSetChanged()

                            }
                        }


                    }

                })
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }


}