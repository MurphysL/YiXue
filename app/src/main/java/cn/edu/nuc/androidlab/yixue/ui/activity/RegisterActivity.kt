package cn.edu.nuc.androidlab.yixue.ui.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.util.Log
import cn.edu.nuc.androidlab.yixue.R
import cn.edu.nuc.androidlab.yixue.util.FileUtil
import com.avos.avoscloud.*
import com.squareup.picasso.Picasso
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.PicassoEngine
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

/**
 * RegisterActivity
 *
 * 用户名唯一 唯一性检查
 *
 * Created by MurphySL on 2017/7/11.
 */
class RegisterActivity : AppCompatActivity(){
    private val TAG  = this.javaClass.simpleName
    private val REQUEST_CODE_CHOOSE = 0x110

    private var select_uri: Uri? = null // 头像

    private val context = this // 待修改

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            select_uri = Matisse.obtainResult(data)[0]
            select_uri?.let {
                Picasso.with(context)
                        .load(it)
                        .into(avatar)
            }
        }
    }

    private fun initView() {
        avatar.setOnClickListener {
            Matisse.from(this)
                    .choose(MimeType.allOf())
                    .maxSelectable(1)
                    .thumbnailScale(0.85f)
                    .imageEngine(PicassoEngine())
                    .forResult(REQUEST_CODE_CHOOSE)
        }

        bt_register.setOnClickListener {
            val username : String = username.text.toString()
            val email : String = email.text.toString()
            val password : String = password.text.toString()

            val pattern : String = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$"
            if(username.isEmpty()){
                username_layout.isErrorEnabled = true
                username_layout.error = "请输入昵称"
            }else if(!Pattern.matches(pattern, email) || email.isEmpty()){
                email_layout.isErrorEnabled = true
                email_layout.error = "请输入正确的邮箱！"
            }else if(password.isEmpty()){
                password_layout.isErrorEnabled = true
                password_layout.error = "请输入密码"
            }else if(select_uri == null){
                Snackbar.make(bt_register, "请选择头像", Snackbar.LENGTH_LONG).show()
            } else {
                email_layout.isErrorEnabled = false
                password_layout.isErrorEnabled = false
                username_layout.isErrorEnabled = false
                register(username, email, password, FileUtil.getFilePahtFromUri(context, select_uri!!)!!)
            }

        }
    }

    private fun register(username : String, email : String, password : String, avatar : String ) {
        Log.i(TAG, "$username $email $password")
        val user : AVUser = AVUser()
        user.username = username
        user.email = email
        user.setPassword(password)

        val file : AVFile = AVFile.withAbsoluteLocalPath(FileUtil.getFileName(avatar), avatar)
        file.saveInBackground(object : SaveCallback(){
            override fun done(p0: AVException?) {
                if(p0 == null){

                    user.signUpInBackground(object : SignUpCallback(){
                        override fun done(p0: AVException?) {
                            if(p0 == null){
                                Snackbar.make(email_layout, "注册成功", Snackbar.LENGTH_SHORT).show()

                                user.put("avatar", file.url)
                                user.saveInBackground(object : SaveCallback(){
                                    override fun done(p0: AVException?) {
                                        startActivity(Intent(context, SelectLiveActivity::class.java))
                                        finish()
                                    }
                                })

                            }else{
                                Snackbar.make(email_layout, "注册失败", Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    })

                }else{
                    Log.i(TAG, "头像上传失败 $p0")
                    Snackbar.make(username_layout, "头像上传失败！", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }


}