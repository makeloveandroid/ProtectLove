package com.protect.love.hook

import android.app.Activity
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.protect.love.core.Core
import com.protect.love.uitl.hookFun
import com.protect.love.xp.log
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.util.*

object TestHook {
    fun hook(classLoader: ClassLoader) {
        hookFun({}, {
            val activity = it.thisObject as Activity
            log("hookac:${activity.javaClass.name}")
            printIntent(activity)

        }, Activity::class.java.name, classLoader, "onCreate", Bundle::class.java)
    }


    private fun printIntent(activity: Activity) {
        try {
            val intent = activity.intent
            val bundle = intent.extras
            val keySet = bundle?.keySet() ?: return  //获取所有的Key,

            for (key in keySet) {  //bundle.get(key);来获取对应的value
                //自己的业务需要
                if (bundle.get(key) is Bundle) {
                    val b = bundle.get(key) as Bundle?
                    val keys = b!!.keySet()
                    for (keyStr in keys) {  //bundle.get(key);来获取对应的value
                        log("Activity:" + key + "   Build:" + keyStr + b.get(keyStr))
                    }
                } else {
                    log("Activity:" + key + "   " + bundle.get(key))
                }
            }
        } catch (e: Exception) {
        }
    }

}

