package com.protect.love.core

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.protect.love.bean.OneDayMsgBean
import com.protect.love.bean.WeaterBean
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private val appInterceptor = mutableListOf<Interceptor>()

private val netInterceptor = mutableListOf<Interceptor>().apply {
    add(StethoInterceptor())
}
private val http: OkHttpClient = OkHttpClient
    .Builder()
    .apply {
        appInterceptor.forEach {
            addInterceptor(it)
        }

        netInterceptor.forEach {
            addNetworkInterceptor(it)
        }
    }
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()


open class BaseRtrofit(val baseUrl: String) {
    val retrofit by lazy {
        Retrofit.Builder().client(http)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}

/**
 * 每日一句API
 */
object OneDayMsgRetrofit : BaseRtrofit("http://open.iciba.com") {
    val api = retrofit.create(OneDayMsgApi::class.java)
}

interface OneDayMsgApi {
    @GET("/dsapi")
    suspend fun getOneDayMsg(): OneDayMsgBean
}

/**
 * 今日天气
 */
object TodayWeatherRetrofit : BaseRtrofit("http://wthrcdn.etouch.cn") {
    val api = retrofit.create(TodayWeatherApi::class.java)
}


interface TodayWeatherApi {
    @GET("/weather_mini")
    suspend fun getWeather(@Query("city") city_code: String): WeaterBean
}

/**
 * 土味情话
 */
object LoveMsgRetrofit : BaseRtrofit("https://api.lovelive.tools") {
    val api = retrofit.create(LoveMsgApi::class.java)
}

interface LoveMsgApi {
    @GET("/api/SweetNothings")
    suspend fun getLoveMsg(): ResponseBody
}