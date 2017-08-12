package cn.edu.nuc.androidlab.yixue.ui.activity

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import cn.edu.nuc.androidlab.yixue.R
import com.avos.avoscloud.AVUser
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * SplashActivity
 *
 * Created by MurphySL on 2017/7/11.
 */
class SplashActivity : AppCompatActivity(){
    private val TAG : String = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val currentUser = AVUser.getCurrentUser()

        if(currentUser != null){
            Log.i(TAG, "{ user : ${currentUser.username},\n email : ${currentUser.email}\n}")
            startActivity(Intent(SplashActivity@this, SelectLiveActivity::class.java))
            finish()
        }


        login.setOnClickListener {
            startActivity(Intent(SplashActivity@this, LoginActivity::class.java))
            finish()
        }
        register.setOnClickListener {
            startActivity(Intent(SplashActivity@this, RegisterActivity::class.java))
            finish()
        }


    }
}