package cn.edu.nuc.androidlab.yixue.util

import android.content.Context
import android.content.SharedPreferences

/**
 * SharedPreference Util
 * Created by MurphySL on 2017/7/11.
 */
class SharedPreferenceUtil(context: Context) {

    private val sp : SharedPreferences
    private val editor : SharedPreferences.Editor

    private val USER_INFO : String = "user_info"
    private val USER_NAME : String = "username"
    private val USER_EMAIL : String = "email"
    private val USER_PASSWORD : String = "password"


    private var username : String
    get() = sp.getString(USER_NAME, "")
    set(value) {
        editor.putString(USER_NAME, value)
        editor.commit()
    }

    private var email : String
    get() = sp.getString(USER_EMAIL, "")
    set(value) {
        editor.putString(USER_EMAIL, value)
        editor.commit()
    }

    private var password : String
    get() = sp.getString(USER_PASSWORD, "")
    set(value) {
        editor.putString(USER_PASSWORD, value)
        editor.commit()
    }


    init {
        sp = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        editor = sp.edit()
    }


}