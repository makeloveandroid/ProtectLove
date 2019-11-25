package com.protect.love.hook

import android.app.Activity
import android.view.ViewGroup
import com.protect.love.core.Core
import com.protect.love.uitl.hookFun
import com.protect.love.xp.log

object ViewHook {
    fun hook(actvity: Activity) {
        log("view Hook开始")
        hookFun({}, {
            val viewGroup = it.thisObject as ViewGroup
            val wxId = it.args[0] as String
            Core.addClickView(viewGroup, wxId)

        }, "com.tencent.mm.pluginsdk.ui.chat.ChatFooter", actvity.classLoader,
            "setUserName",
            String::class.java
        )
    }
}

