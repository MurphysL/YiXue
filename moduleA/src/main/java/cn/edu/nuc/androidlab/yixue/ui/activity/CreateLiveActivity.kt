package cn.edu.nuc.androidlab.yixue.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.util.Log
import android.widget.ProgressBar
import cn.edu.nuc.androidlab.yixue.util.Config
import cn.edu.nuc.androidlab.yixue.R
import cn.edu.nuc.androidlab.yixue.util.FileUtil
import cn.leancloud.chatkit.LCChatKit
import cn.leancloud.chatkit.LCChatKitUser
import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import com.bigkoo.pickerview.OptionsPickerView
import com.bigkoo.pickerview.TimePickerView
import com.squareup.picasso.Picasso
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.PicassoEngine
import kotlinx.android.synthetic.main.activity_create_live.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * 创建 Live
 *
 * 待修改：
 * 2. 多人主讲
 * 3. 流程：开通主讲人身份 -> 实名认证 -> 交纳保证金
 * 4. context
 * 5.创建live完成后流程
 *
 * Created by MurphySL on 2017/7/24.
 */
class CreateLiveActivity : AppCompatActivity(){
    private val TAG : String = this.javaClass.simpleName

    private val context : Context = this

    private var select_time: Calendar = Calendar.getInstance() // Live 开始时间
    private var select_uri: Uri? = null // Live 封面
    private var select_type : String = ""

    private val types = ArrayList<String>() // Live 类型

    private val REQUEST_CODE_CHOOSE = 0x110

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_live)

        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            select_uri = Matisse.obtainResult(data)[0]
            select_uri?.let {
                Picasso.with(context)
                        .load(it)
                        .into(live_img)

                val bitmap = BitmapFactory.decodeFile(FileUtil.getFilePahtFromUri(context, it))

                Palette.from(bitmap).generate {
                    it.darkVibrantSwatch?.let {
                        nestedScrollView.setBackgroundColor(it.rgb)
                    }?:it.darkMutedSwatch?.let {
                        nestedScrollView.setBackgroundColor(it.rgb)
                    }?:it.vibrantSwatch?.let {
                        nestedScrollView.setBackgroundColor(it.rgb)
                    }
                }
            }

        }
    }

    private fun initType() {
        types += "计算机"
        types += "经济管理"
        types += "心理学"
        types += "外语"
        types += "文学历史"
        types += "艺术设计"
        types += "工学"
        types += "理学"
        types += "哲学"
        types += "法学"
        types += "生命科学"
    }

    private fun initView() {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        bt_create.setOnClickListener {
            confirmInfo()
        }

        card_live_time.setOnClickListener {
            val pvTime = TimePickerView.Builder(this, TimePickerView.OnTimeSelectListener {
                date, _ ->
                val sdf : SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                select_time.time = date
                live_time.text = sdf.format(date)
            })
                    .isCenterLabel(false)
                    .setRangDate(Calendar.getInstance(), null)
                    .setSubmitColor(resources.getColor(R.color.colorTheme))
                    .setCancelColor(resources.getColor(R.color.colorTheme))
                    .setContentSize(14)
                    .build()
            pvTime.setDate(select_time)
            pvTime.show()
        }

        card_live_type.setOnClickListener {
            initType()
            val pvOptions = OptionsPickerView.Builder(this, OptionsPickerView.OnOptionsSelectListener {
                options1, _, _, _ ->
                live_type.text = types[options1]
                select_type = types[options1]
            })
                    .setSubmitColor(resources.getColor(R.color.colorTheme))
                    .setCancelColor(resources.getColor(R.color.colorTheme))
                    .build()
            pvOptions.setPicker(types)
            pvOptions.show()
        }

        live_img.setOnClickListener {
            Matisse.from(this)
                    .choose(MimeType.allOf())
                    .maxSelectable(1)
                    .thumbnailScale(0.85f)
                    .imageEngine(PicassoEngine())
                    .forResult(REQUEST_CODE_CHOOSE)
        }
    }

    private fun confirmInfo(){
        val live_name : String = live_name.text.toString()
        val live_price : String = live_price.text.toString()
        val live_time : String = live_time.text.toString()
        val live_summary : String = live_summary.text.toString()
        val live_type : String = live_type.text.toString()
        if(live_name.isEmpty()){
            Snackbar.make(img_live_name, "请输入 Live 主题！", Snackbar.LENGTH_LONG).show()
        }else if(live_price.isEmpty()){
            Snackbar.make(img_live_name, "请输入 Live 价格！", Snackbar.LENGTH_LONG).show()
        }else if(live_time.isEmpty()){
            Snackbar.make(img_live_name, "请选择开始时间！", Snackbar.LENGTH_LONG).show()
        }else if(live_summary.isEmpty()){
            Snackbar.make(img_live_name, "请输入 Live 简介！", Snackbar.LENGTH_LONG).show()
        }else if(live_type.isEmpty()){
            Snackbar.make(img_live_name, "请选择 Live 种类！", Snackbar.LENGTH_LONG).show()
        }else if(select_uri == null){
            Snackbar.make(img_live_name, "请选择 Live 封面！", Snackbar.LENGTH_LONG).show()
        } else{
            mProgress.visibility = ProgressBar.VISIBLE

            val path = FileUtil.getFilePahtFromUri(context, select_uri!!)
            path?.let {
                Log.i(TAG, "live_name: $live_name \n live_price: $live_price \n live_time: $live_time \n live_summary: $live_summary \n live_type: $live_type \n live_pic: $path")
                createLiveWithPic(live_name, select_time.time, live_summary, live_price.toFloat(), select_type, it)
            }?:Log.i(TAG, "解析图片出错")

        }
    }

    private fun createLiveWithPic(live_name : String, live_time : Date, live_summary : String, live_price : Float, live_type : String, path : String){
        val fileName = FileUtil.getFileName(path)
        val file : AVFile = AVFile.withAbsoluteLocalPath(fileName, path)

        file.saveInBackground(object : SaveCallback(){
            override fun done(p0: AVException?) {
                if(p0 == null){
                    //图片上传成功

                    val userId = AVUser.getCurrentUser().objectId // 用户ID
                    val username = AVUser.getCurrentUser().username
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
                                val live : AVObject = AVObject(Config.LIVE_TABLE)
                                live.put(Config.LIVE_USER_ID, AVObject.createWithoutData(Config.USER_TABLE, userId))
                                live.put(Config.LIVE_USER_NAME, username)
                                live.put(Config.LIVE_CONVERSATION_ID, AVObject.createWithoutData(Config.CONVERSATION_TABLE, it.conversationId))
                                live.put(Config.LIVE_NAME, live_name)
                                live.put(Config.LIVE_SUMMARY, live_summary)
                                live.put(Config.LIVE_START_AT, live_time)
                                live.put(Config.LIVE_PRICE, live_price)
                                live.put(Config.LIVE_TYPE, live_type)
                                live.put(Config.LIVE_PIC, file.url)

                                live.saveInBackground(object : SaveCallback(){
                                    override fun done(p0: AVException?) {
                                        if(p0 != null){
                                            mProgress.visibility = ProgressBar.GONE
                                            Snackbar.make(img_live_name, "创建 Live 信息失败！$p0", Snackbar.LENGTH_SHORT).show()
                                            Log.i(TAG, "创建 Live 信息失败！$p0")
                                        }else{
                                            mProgress.visibility = ProgressBar.GONE
                                            Snackbar.make(img_live_name, "创建Live成功", Snackbar.LENGTH_SHORT).show()
                                            //val intent : Intent = Intent(context, LCIMConversationActivity::class.java)
                                            //intent.putExtra(LCIMConstants.CONVERSATION_ID, it.conversationId)
                                            //startActivity(intent)

                                            startActivity(Intent(context, SelectLiveActivity::class.java))
                                        }
                                    }
                                })

                            }?:Snackbar.make(img_live_name, "创建 Live 失败！$p0", Snackbar.LENGTH_SHORT).show()

                            Log.i(TAG, p1.toString())
                        }
                    })


                }else{
                    mProgress.visibility = ProgressBar.GONE
                    Snackbar.make(live_img, "图片上传失败", Snackbar.LENGTH_LONG).show()
                }
            }
        })

    }

}