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
import com.protect.love.core.robot.PDbootManager.msgTask


/**
 *图灵自动回复机器人
 */
object TuLingManager  {
    var classLoader: ClassLoader? = null





    fun sendMsg(msg: String, talker: String) {
        log("图灵机器人发送  $msg  $talker")

    }

    fun initConfig(context: Activity) {


    }


}

