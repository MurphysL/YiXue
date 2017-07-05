package cn.edu.nuc.androidlab.yixue

import android.app.Application

/**
 * MyApplication
 *
 * Created by MurphySL on 2017/7/5.
 */
class MyApplication : Application(){

    companion object {
        private var instance : Application? = null
        fun instance() = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}