package com.protect.love.hook

import android.app.Activity
import android.os.Bundle
import com.protect.love.core.Core
import com.protect.love.uitl.hookFun
import com.protect.love.xp.MM_PKG
import com.protect.love.xp.log
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object MMCoreHook {
    fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == MM_PKG && lpparam.processName == MM_PKG) {
            hookFun({}, {
                val activity = it.thisObject as Activity
                log("ac:${activity.javaClass.name}")
                if (activity.javaClass.name == "com.tencent.mm.ui.LauncherUI") {
                    Core.init(activity)
                    ViewHook.hook(activity)
                    MsgHook.hook(activity)
                }
            }, Activity::class.java.name, lpparam.classLoader, "onCreate", Bundle::class.java)
        }
    }
}