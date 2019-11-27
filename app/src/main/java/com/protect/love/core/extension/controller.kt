package com.protect.love.core.extension

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import com.protect.love.core.view.SettingBuild
import com.protect.love.uitl.SharedPreferencesUtils

/**
 * 当前类为控制器类
 */

fun Activity.showSettingDialog(){
    val settingView = SettingBuild().create(this)
    AlertDialog
        .Builder(this)
        .setView(settingView)
        .setPositiveButton("确定", null)
        .setNegativeButton("取消", null)
        .setCancelable(false)
        .show()
}