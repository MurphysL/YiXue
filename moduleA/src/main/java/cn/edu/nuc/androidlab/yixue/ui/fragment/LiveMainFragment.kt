package cn.edu.nuc.androidlab.yixue.ui.fragment

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
import cn.edu.nuc.androidlab.yixue.ui.adapter.AnimCommonAdapter
import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback
import com.squareup.picasso.Picasso
import com.zhy.adapter.recyclerview.CommonAdapter
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.fragment_live_main.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Live 主展示页面 无限制
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

    //扩展函数
    fun ViewHolder.setImageWithPicasso(viewId : Int, url : String) : ViewHolder{
        val view : ImageView = getView(viewId)
        Picasso.with(context)
                .load(url)
                .into(view)
        return this
    }

    private fun initData() {
        val query : AVQuery<Live> = AVQuery(Config.LIVE_TABLE)
        query.addDescendingOrder(Config.LIVE_STAR) // 按星级排序
        query.addDescendingOrder(Config.LIVE_START_AT) // 按时间排序
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
                                // 相关信息

                                holder?.setOnClickListener(R.id.live_info) {
                                    // 待进行后台认证
                                    // 付费

                                    uploadInfo(t.objectId, t.conversationId)
                                }
                            }?:Snackbar.make(live_list, "未找到相关Live" , Snackbar.LENGTH_LONG).show()
                        }
                    }
                }?: Snackbar.make(live_list, "您还没有创建 Live ", Snackbar.LENGTH_LONG).show()
            }
        })

    }

    private fun uploadInfo(liveId : String, conversationId: String){
        val lu : LU = LU()
        lu.liveId = liveId
        lu.userId = AVUser.getCurrentUser().objectId
        lu.saveInBackground(object : SaveCallback(){
            override fun done(p0: AVException?) {
                p0?.let {
                    Snackbar.make(live_list, "创建信息失败：$p0", Snackbar.LENGTH_LONG).show()
                    Log.i(TAG, "创建信息失败：$p0")
                }?:enterLive(conversationId)
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
                                    Snackbar.make(live_list, "参与 Live 失败 $p0", Snackbar.LENGTH_LONG).show()
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