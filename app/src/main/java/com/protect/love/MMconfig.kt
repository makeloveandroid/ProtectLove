package com.protect.love

import android.content.Context
import android.R.attr.versionCode
import android.content.pm.PackageInfo


object MMconfig {
    var netCore = "com.tencent.mm.mode.az"
    var netMethod = "aap"
    var msgClzz = "com.tencent.mm.modelmulti.h"
    var netCoreMethod = "b"

    fun init(context: Context) {
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        val versionName = info.versionName
        when (versionName) {
            "7.0.8" -> {
                netCore = "com.tencent.mm.model.az"
                netMethod = "ZS"
                msgClzz = "com.tencent.mm.modelmulti.h"
                netCoreMethod = "b"
            }

        }

    }
}