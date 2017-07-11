package cn.edu.nuc.androidlab.yixue.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.edu.nuc.androidlab.yixue.R
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * SplashActivity
 *
 * Created by MurphySL on 2017/7/11.
 */
class SplashActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        login.setOnClickListener {
            startActivity(Intent(SplashActivity@this, LoginActivity::class.java))
        }
        register.setOnClickListener {
            startActivity(Intent(SplashActivity@this, RegisterActivity::class.java))
        }
    }
}