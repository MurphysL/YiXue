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
import cn.edu.nuc.androidlab.common.bean.Live
import cn.edu.nuc.androidlab.yixue.R
import cn.edu.nuc.androidlab.yixue.ui.adapter.AnimCommonAdapter
import cn.edu.nuc.androidlab.yixue.util.Config
import com.avos.avoscloud.*
import com.squareup.picasso.Picasso
import com.zhy.adapter.recyclerview.base.ViewHolder
import kotlinx.android.synthetic.main.fragment_live_main.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * LiveMyFragment
 *
 * 当前用户获取失败
 *
 * Created by MurphySL on 2017/7/24.
 */
class LiveMyFragment : Fragment(){
    private val TAG = this.javaClass.simpleName

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_live_my, container, false)
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

    private fun initData() {
        val userId = AVUser.getCurrentUser().objectId

        val query : AVQuery<Live> = AVQuery(Config.LIVE_TABLE)
        query.addDescendingOrder(Config.LIVE_STAR) // 按星级排序
        query.addDescendingOrder(Config.LIVE_START_AT) // 按时间排序
        query.whereEqualTo(Config.LIVE_USER_ID, AVObject.createWithoutData(Config.USER_TABLE, userId)) // 主讲人为当前用户
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
                                    //点击事件
                                }
                            }?:Snackbar.make(live_list, "未找到相关Live" , Snackbar.LENGTH_LONG).show()
                        }
                    }
                }?: Snackbar.make(live_list, "您还没有创建 Live ", Snackbar.LENGTH_LONG).show()
            }
        })


        /*val conversationQuery : AVIMConversationsQuery = AVIMClient.getInstance(currentUserId).conversationsQuery
        val list  = ArrayList<String>()
        list.add(currentUserId)
        conversationQuery.whereEqualTo("c", currentUserId) // 对话创建者
        //conversationQuery.whereContains()
        //conversationQuery.containsMembers(list)
        conversationQuery.findInBackground(object : AVIMConversationQueryCallback(){
            override fun done(p0: MutableList<AVIMConversation>?, p1: AVIMException?) {
                p0?.let {


                }
            }
        })*/
    }






















}