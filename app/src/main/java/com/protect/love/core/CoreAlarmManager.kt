package com.protect.love.core

import android.content.Context
import android.R.string.cancel
import android.annotation.SuppressLint
import android.content.Context.ALARM_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.Message

import com.protect.love.xp.log
import java.util.*
import androidx.core.content.ContextCompat.getSystemService

/**
 * 定时检查任务 15秒一次
 */
object CoreAlarmManager {
    var handler: Handler
    var classLoader: ClassLoader? = null

    init {
        val handlerThread = HandlerThread("TASK_WORK")
        handlerThread.start()
        handler = Handler(handlerThread.looper) {
            if (it.what == 1) {
                classLoader?.let { loader ->
                    TaskManager.invokeTask(loader)
                }

                postMessage()

            }
            true
        }
    }

    private fun postMessage(delayMillis:Long=15*1000L){
        handler.removeMessages(1)

        handler.sendMessageDelayed(Message.obtain().apply {
            what = 1
        },delayMillis)
    }

    /**
     * 判断定时任务
     *
     * 1分钟扫描一次
     */
    @Synchronized
    fun init(context: Context) {
        this.classLoader = context.classLoader

        postMessage(0)
    }
}