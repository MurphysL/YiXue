package cn.edu.nuc.androidlab.yixue

import android.support.v7.app.AppCompatActivity
import android.os.Bundle


class MapActivity : AppCompatActivity(){
    private val TAG = "MapActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        initFragment()
    }

    private fun initFragment() {
        supportFragmentManager.beginTransaction()
                .add(R.id.container, MapFragment(), MapFragment().javaClass.name)
                .commit()
    }

}
