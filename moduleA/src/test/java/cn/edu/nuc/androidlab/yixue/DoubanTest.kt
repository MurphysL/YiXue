package cn.edu.nuc.androidlab.yixue


import cn.edu.nuc.androidlab.yixue.net.Service
import org.junit.Test

/**
 * Created by MurphySL on 2017/8/1.
 */
class DoubanTest{

    @Test
    fun getBookInfo(){
        Service.api_douban.getBookInfo("9787111407010").subscribe { print(it) }
    }
}