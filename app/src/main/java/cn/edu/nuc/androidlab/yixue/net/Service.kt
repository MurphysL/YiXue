package cn.edu.nuc.androidlab.yixue.net

import cn.edu.nuc.androidlab.yixue.bean.Book
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by MurphySL on 2017/8/1.
 */
object Service{
    val api_douban : DoubanService by lazy {
        Retrofit.Builder()
                .baseUrl("https://api.douban.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(DoubanService::class.java)
    }

}

interface DoubanService{
    @GET("book/isbn/:{name}")
    fun getBookInfo(@Path("name") name : String) : Observable<Book>
}