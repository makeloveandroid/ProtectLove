package com.protect.love.xp


import android.util.Log
import com.protect.love.hook.MMCoreHook
import com.protect.love.hook.ViewHook
import de.robv.android.xposed.callbacks.XC_LoadPackage
import de.robv.android.xposed.IXposedHookLoadPackage

const val MM_PKG = "com.tencent.mm"


fun log(msg: String) = Log.d("wyz", msg)

class XpCore : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        MMCoreHook.hook(lpparam)
    }
}