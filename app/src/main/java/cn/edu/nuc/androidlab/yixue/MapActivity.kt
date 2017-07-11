package cn.edu.nuc.androidlab.yixue

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVOSCloud
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.SaveCallback


class MapActivity : AppCompatActivity(){
    private val TAG = "MapActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initFragment()

        initLeanCloud()
    }

    private fun initLeanCloud(){
        val testObject : AVObject = AVObject("TestObject")
        testObject.put("test", "Hello World")
        testObject.saveInBackground(object : SaveCallback(){
            override fun done(p0: AVException?) {
                if(p0 != null)
                    Log.i(TAG, "success!")
            }
        })

    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
                .add(R.id.container, MapFragment(), MapFragment().javaClass.name)
                .commit()
    }

}
