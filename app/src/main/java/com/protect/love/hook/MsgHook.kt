package com.protect.love.hook

import android.app.Activity
import android.content.ContentValues
import android.text.TextUtils
import com.protect.love.core.Core
import com.protect.love.uitl.hookFun

object MsgHook {
    var isHook = false

    fun hook(actvity: Activity) {
        if (!isHook){
            isHook = true
            hookFun(
                {
                    if (TextUtils.equals("message", it.args[0].toString())) {
                        try {
                            val contentValues = it.args[2] as ContentValues

                            contentValues.apply {

                                val msgSvrId = getAsString("msgSvrId")
                                if (TextUtils.isEmpty(msgSvrId)) {
                                    return@hookFun
                                }
                                val talker = getAsString("talker")
                                val type = getAsString("type")
                                val content = contentValues.getAsString("content")
                                Core.msgGo(talker, type, content, actvity)

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Core.toast("消息拦截失败!!")
                        }
                    }
                },
                {},
                "com.tencent.wcdb.database.SQLiteDatabase",
                actvity.classLoader,
                "insert",
                String::class.java,
                String::class.java,
                ContentValues::class.java
            )
        }

    }
}

