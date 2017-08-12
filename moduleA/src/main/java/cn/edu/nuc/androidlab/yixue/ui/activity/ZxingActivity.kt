package cn.edu.nuc.androidlab.yixue.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.edu.nuc.androidlab.yixue.R
import kotlinx.android.synthetic.main.activity_zxing.*
import android.graphics.Bitmap
import android.os.Parcelable


/**
 * Created by MurphySL on 2017/8/1.
 */
class ZxingActivity : AppCompatActivity(){

    private val SCANNIN_GREQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zxing)

        zxing.setOnClickListener {
            startActivityForResult(Intent(ZxingActivity@this, MipcaActivityCapture::class.java), SCANNIN_GREQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SCANNIN_GREQUEST_CODE ->
                if (resultCode == RESULT_OK) {
                val bundle = data.extras
                //显示扫描到的内容
                textView.text = bundle.getString("result")
                //显示
                imageView.setImageBitmap(data.getParcelableExtra<Parcelable>("bitmap") as Bitmap)
            }
        }
    }
}