package cn.edu.nuc.androidlab.yixue.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import cn.edu.nuc.androidlab.yixue.R
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.SignUpCallback
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

    private val context = this // 待修改

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initView()
    }

    private fun initView() {
        bt_register.setOnClickListener {
            val username : String = username.text.toString()
            val email : String = email.text.toString()
            val password : String = password.text.toString()

            val pattern : String = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$"
            if(username == ""){
                username_layout.isErrorEnabled = true
                username_layout.error = "请输入昵称"
            }else if(!Pattern.matches(pattern, email) || email == ""){
                email_layout.isErrorEnabled = true
                email_layout.error = "请输入正确的邮箱！"
            }else if(password == ""){
                password_layout.isErrorEnabled = true
                password_layout.error = "请输入密码"
            }else{
                email_layout.isErrorEnabled = false
                password_layout.isErrorEnabled = false
                username_layout.isErrorEnabled = false
                register(username, email, password)
            }

        }
    }

    private fun register(username : String, email : String, password : String ) {
        Log.i(TAG, "$username $email $password")
        val user : AVUser = AVUser()
        user.username = username
        user.email = email
        user.setPassword(password)

        user.signUpInBackground(object : SignUpCallback(){
            override fun done(p0: AVException?) {
                if(p0 == null){
                    Snackbar.make(email_layout, "注册成功", Snackbar.LENGTH_SHORT).show()
                    startActivity(Intent(context, SelectLiveActivity::class.java))
                }else{
                    Snackbar.make(email_layout, "注册失败", Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }


}