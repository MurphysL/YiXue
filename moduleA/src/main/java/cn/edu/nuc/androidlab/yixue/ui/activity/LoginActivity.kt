package cn.edu.nuc.androidlab.yixue.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import cn.edu.nuc.androidlab.yixue.R
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.LogInCallback
import kotlinx.android.synthetic.main.activity_login.*

/**
 * LoginActivity
 *
 * Created by MurphySL on 2017/7/11.
 */
class LoginActivity : AppCompatActivity(){
    private val TAG : String = this.javaClass.simpleName

    private val context = this // 待修改

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()
    }

    private fun initView() {
        bt_login.setOnClickListener {
            val username : String = username.text.toString()
            val password : String = password.text.toString()

            if(username == ""){
                username_layout.isErrorEnabled = true
                username_layout.error = "请输入电子邮箱"
            }else if(password == ""){
                password_layout.isErrorEnabled = true
                password_layout.error = "请输入密码"
            }else{
                login(username, password)
            }
        }

        register.setOnClickListener {
            startActivity(Intent(LoginActivity@this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun login(username : String , password : String) {
        AVUser.logInInBackground(username, password, object  : LogInCallback<AVUser>(){
            override fun done(p0: AVUser?, p1: AVException?) {
                if(p1 == null){
                    p0?.let {
                        startActivity(Intent(context, SelectLiveActivity::class.java))
                    }?:Snackbar.make(password_layout, "未知的错误！", Snackbar.LENGTH_LONG).show()
                }else{
                    Snackbar.make(password_layout, "登录错误：$p1" ,Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }
}