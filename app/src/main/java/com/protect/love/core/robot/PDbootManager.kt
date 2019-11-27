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
import com.paradigm.botlib.MessageContentText



/**
 * 第四范式自动回复机器人
 */
object PDbootManager : BotLibClient.MessageListener, BotLibClient.ConnectionListener {
    var classLoader: ClassLoader? = null
    /**
     * 存放消息的队列
     */
    val msgTask = ConcurrentLinkedQueue<String>()

    override fun onConnectionStateChanged(state: Int) {
        when (state) {
            BotKitClient.ConnectionIdel -> log("连接断开")
            BotKitClient.ConnectionConnecting -> log("正在连接...")
            BotKitClient.ConnectionConnectedRobot, BotKitClient.ConnectionConnectedRobot -> log("连接成功  ${BotLibClient.getInstance().robotName}")  // 显示机器人名字
            else -> log("连接失败")
        }
    }

    override fun onAppendMessage(message: Message) {

        if (message.direction == 1) {
            // 接收到消息
            try {
                val wxid = msgTask.poll()
                // 判断逻辑
                if (wxid?.isNotBlank() == true && classLoader != null && message.contentType == Message.ContentTypeText) {
                    val contentText = message.content as MessageContentText
                    Core.receiverMsg(classLoader!!, contentText.text, wxid)
                }
            } catch (e: Exception) {
            }


        }
    }

    override fun onReceivedSuggestion(suggestions: ArrayList<MenuItem>?) {

    }

    fun initConfig(context: Context) {
        this.classLoader = context.classLoader

        if (SharedPreferencesUtils.XIAO_SHI_ACCESS_KEY?.isNotBlank() == true) {
            log("初始化${SharedPreferencesUtils.XIAO_SHI_ACCESS_KEY}")
            BotKitClient.getInstance().init(context, SharedPreferencesUtils.XIAO_SHI_ACCESS_KEY);

            BotKitClient.getInstance().connect()
        }
    }


    init {
        BotKitClient.getInstance().setMessageListener(this)
        BotKitClient.getInstance().setConnectionListener(this)
    }


    fun sendMsg(msg: String, talker: String) {
        log("像机器人发送  $msg  $talker")
        msgTask.add(talker)
        BotKitClient.getInstance().askQuestion(msg)
    }


}

