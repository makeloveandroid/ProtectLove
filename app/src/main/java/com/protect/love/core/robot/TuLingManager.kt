package com.protect.love.core.robot

import android.content.Context
import com.google.gson.Gson
import com.paradigm.botkit.BotKitClient
import com.paradigm.botlib.BotLibClient
import com.paradigm.botlib.MenuItem
import com.paradigm.botlib.Message
import com.protect.love.core.Core
import com.protect.love.uitl.SharedPreferencesUtils
import com.protect.love.xp.log
import org.jetbrains.anko.AnkoAsyncContext
import java.util.ArrayList
import java.util.concurrent.ConcurrentLinkedQueue
import android.R.id.message
import android.app.Activity
import com.paradigm.botlib.MessageContentText
import com.protect.love.bean.OneDayMsgBean
import com.protect.love.bean.TuLingResult
import com.protect.love.core.BaseRtrofit
import com.protect.love.core.OneDayMsgApi
import com.protect.love.core.OneDayMsgRetrofit
import com.protect.love.core.robot.PDbootManager.msgTask
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


/**
 *图灵自动回复机器人
 */
object TuLingManager {
    var classLoader: ClassLoader? = null


    fun sendMsg(msg: String, talker: String, block: (Boolean) -> Unit) {
        log("图灵机器人发送  $msg  $talker")
        GlobalScope.launch {
            if (SharedPreferencesUtils.TU_LING_ACCESS_KEY.isNullOrBlank()) {
                return@launch
            }
            try {
                val msgResult =
                    TuLingMsgRetrofit.api.getMsg(TuLingMsg(SharedPreferencesUtils.TU_LING_ACCESS_KEY!!, msg))
                log("图灵机器人反馈结果 $msg  $msgResult")

                if (msgResult.code == 100000 && msgResult.text.isNotBlank() && classLoader != null) {
                    Core.receiverMsg(classLoader!!, msgResult.text, talker)
                    block(true)
                } else {
                    Core.toast(msgResult.text)
                    block(false)
                }
            } catch (e: Exception) {
                log("图灵出错:${e.message}")
                Core.toast("图灵出错:${e.message}")
                block(false)
            }
        }

    }

    fun initConfig(context: Activity) {
        this.classLoader = context.classLoader

    }


}


/**
 * 图灵机器人API
 */
object TuLingMsgRetrofit : BaseRtrofit("http://www.tuling123.com") {
    val api = retrofit.create(TuLingMsgApi::class.java)

}

data class TuLingMsg(val key: String, val info: String)


interface TuLingMsgApi {
    @POST("/openapi/api")
    suspend fun getMsg(@Body msg: TuLingMsg): TuLingResult
}


