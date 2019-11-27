package com.protect.love.hook

import android.app.Activity
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
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
//        val ale = XposedHelpers.findClass(AlarmManager::class.java.name, classLoader)
//        log("ale:$ale")
//
//        XposedBridge.hookAllMethods(ale,"setImpl",object :XC_MethodHook(){
//            override fun afterHookedMethod(param: MethodHookParam) {
//                super.afterHookedMethod(param)
//                log("注册:${Arrays.toString(param.args)}")
//
//            }
//        })
    }
}

