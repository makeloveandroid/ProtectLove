package com.protect.love.core.view

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.protect.love.core.adapter.SettingAdapter
import com.protect.love.uitl.SharedPreferencesUtils
import org.jetbrains.anko.*
import android.app.TimePickerDialog
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import com.protect.love.bean.AlarmTask
import com.protect.love.core.Core
import com.protect.love.core.CoreAlarmManager
import com.protect.love.core.TodayWeatherRetrofit
import com.protect.love.core.robot.PDbootManager
import com.protect.love.core.robot.TuLingManager
import com.protect.love.dao.AlarmTaskDao
import com.protect.love.extension.showInputDialog
import com.protect.love.extension.showTimePickerDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.core.content.ContextCompat.startActivity


class SettingBuild {


    /**
     * 创建 settingView
     * 由于 Xposed 的特殊性,通过 anko 编写布局
     */
    fun create(context: Activity): View {
        val settingAdapter = SettingAdapter(buildSettingItem(context))
        return context.UI {
            listView {
                adapter = settingAdapter
                dividerHeight = 0
                selector = ColorDrawable(Color.TRANSPARENT)
            }
        }.view

    }

    private fun buildSettingItem(context: Activity): List<ViewItem> {
        return mutableListOf<ViewItem>().apply {
            add(TitleItem("图灵机器人开发者 Key 配置"))
            add(SwitchItem("是否启用机器人回复", "关闭后就不使用机器人自动回复消息啦", SharedPreferencesUtils.IS_AUTO_MSG).apply {
                onClickListener = View.OnClickListener {
                    val switchButton = it as SwitchButton

                    switchButton.toggle(true)


                }
                checkedChangeListener = { switchButton: SwitchButton, b: Boolean ->
                    SharedPreferencesUtils.IS_AUTO_MSG = b
                }

            })
            add(
                SwitchItem(
                    "请配置小式器人 apikey",
                    "点击我修改 apikey，长按我进入小式机器人主页",
                    SharedPreferencesUtils.IS_USE_XIAO_SHI
                ).apply {

                    onClickListener = View.OnClickListener {
                        val switchButton = it as SwitchButton
                        if (!switchButton.isChecked) {
                            context.showInputDialog(
                                "请输入",
                                "请输入 Access Key ",
                                SharedPreferencesUtils.XIAO_SHI_ACCESS_KEY ?: ""
                            ) {

                                SharedPreferencesUtils.IS_USE_XIAO_SHI = true

                                SharedPreferencesUtils.XIAO_SHI_ACCESS_KEY = it
                                PDbootManager.initConfig(context)
                                switchButton.toggle(true)
                                true
                            }
                        } else {
                            switchButton.toggle(true)
                            SharedPreferencesUtils.IS_USE_XIAO_SHI = false
                        }

                    }


                    itemClickListener = View.OnClickListener {
                        context.showInputDialog(
                            "请输入",
                            "请输入 Access Key ",
                            SharedPreferencesUtils.XIAO_SHI_ACCESS_KEY ?: ""
                        ) {
                            SharedPreferencesUtils.XIAO_SHI_ACCESS_KEY = it
                            PDbootManager.initConfig(context)
                            true
                        }
                    }


                    itemLongClickListener = View.OnLongClickListener {
                        com.protect.love.xp.log("进入小式机器人")
//                        Intent().apply {
//                            putExtra("rawUrl", "https://bot.4paradigm.com/admin/home/index")
//                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                            component = ComponentName(
//                                "com.tencent.mm",
//                                "com.tencent.mm.plugin.webview.ui.tools.WebViewUI"
//                            )
//
//                        }.apply {
//                            context.startActivity(this)
//                        }


                        val intent = Intent()
                        intent.action = "android.intent.action.VIEW"
                        val content_url = Uri.parse("https://bot.4paradigm.com/admin/home/index")
                        intent.data = content_url
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        context.startActivity(intent)
                        true
                    }

                })


            add(SwitchItem("请配置图灵机器人 apikey 与 密钥", "图灵机器人账号申请，请搜索图灵机器人", SharedPreferencesUtils.IS_USE_TU_LING).apply {


                onClickListener = View.OnClickListener {
                    val switchButton = it as SwitchButton
                    if (!switchButton.isChecked) {
                        context.showInputDialog(
                            "请输入",
                            "请输入 Api Key ",
                            SharedPreferencesUtils.TU_LING_ACCESS_KEY ?: ""
                        ) {

                            SharedPreferencesUtils.IS_USE_TU_LING = true

                            SharedPreferencesUtils.TU_LING_ACCESS_KEY = it
                            TuLingManager.initConfig(context)
                            switchButton.toggle(true)
                            true
                        }
                    } else {
                        switchButton.toggle(true)
                        SharedPreferencesUtils.IS_USE_TU_LING = false
                    }

                }


                itemClickListener = View.OnClickListener {
                    context.showInputDialog(
                        "请输入",
                        "请输入 Api Key ",
                        SharedPreferencesUtils.TU_LING_ACCESS_KEY ?: ""
                    ) {
                        SharedPreferencesUtils.TU_LING_ACCESS_KEY = it
                        TuLingManager.initConfig(context)
                        true
                    }
                }


                itemLongClickListener = View.OnLongClickListener {
                    com.protect.love.xp.log("进入图灵机器人主页")
//                    Intent().apply {
//                        putExtra("rawUrl", "http://www.tuling123.com/sso-web/index.html")
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        component = ComponentName(
//                            "com.tencent.mm",
//                            "com.tencent.mm.plugin.webview.ui.tools.WebViewUI"
//                        )
//
//                    }.apply {
//                        context.startActivity(this)
//                    }

                    val intent = Intent()
                    intent.action = "android.intent.action.VIEW"
                    val content_url = Uri.parse("http://www.tuling123.com/sso-web/index.html")
                    intent.data = content_url
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)


                    true
                }


            })

            add(TitleItem("定时提醒任务"))

            // 每日一句
            val oneDayMSG = Core.getDaoSession()
                .alarmTaskDao
                .queryBuilder()
                .where(AlarmTaskDao.Properties.Name.eq(AlarmTask.ONE_DATA_MSG_TASK_NAME))
                .unique()
            add(OneDayMsgItem(context, oneDayMSG))


            // 今日天气
            val weatherMsg = Core.getDaoSession()
                .alarmTaskDao
                .queryBuilder()
                .where(AlarmTaskDao.Properties.Name.eq(AlarmTask.TODAY_WEATHER_TASK_NAME))
                .unique()
            add(TodayWeatherItem(context, weatherMsg))

            // 土味情话
            val loveMsg = Core.getDaoSession()
                .alarmTaskDao
                .queryBuilder()
                .where(AlarmTaskDao.Properties.Name.eq(AlarmTask.LOVE_MSG_TASK_NAME))
                .unique()
            add(LoveMsgItem(context, loveMsg))


            // 自定义情话
            val customMsg = Core.getDaoSession()
                .alarmTaskDao
                .queryBuilder()
                .where(AlarmTaskDao.Properties.Name.eq(AlarmTask.TODAY_CUSTOM_TASK_NAME))
                .unique()

            add(CustomMsgItem(context, customMsg))
        }

    }


}

/**
 * 每日一句
 */
class OneDayMsgItem(val context: Activity, var oneDayMSG: AlarmTask?) :
    SwitchItem("每日一句", "每天只为你准备一句正能量（点我修改时间）", oneDayMSG?.IsOpen ?: false) {
    init {
        onClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                com.protect.love.xp.log("每日一句:$v")
                val switchButton = v as SwitchButton
                click(switchButton)
            }
        }


        itemClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                context.showTimePickerDialog(
                    oneDayMSG?.hour ?: 0,
                    oneDayMSG?.minute ?: 0
                ) { isCancel: Boolean, hourOfDay, minute ->
                    if (!isCancel) {
                        oneDayMSG = oneDayMSG?.apply {
                            hour = hourOfDay
                            this.minute = minute
                        } ?: AlarmTask(AlarmTask.ONE_DATA_MSG_TASK_NAME, false, hourOfDay, minute)

                        Core.getDaoSession()
                            .alarmTaskDao
                            .insertOrReplace(oneDayMSG)
                        // 重新初始化下定时任务
                        CoreAlarmManager.init(context)
                    }
                }
            }
        }

    }

    /**
     * 点击事件
     */
    private fun click(switchButton: SwitchButton) {
        //http://open.iciba.com/dsapi/
        if (!switchButton.isChecked) {
            //展示输入昵称dialog
            showTimePickerDialog(switchButton)
        } else {
            // 关闭
            switchButton.toggle(true)
            oneDayMSG?.let {
                it.IsOpen = false

                Core.getDaoSession()
                    .alarmTaskDao
                    .insertOrReplace(it)
                // 重新初始化下定时任务
                CoreAlarmManager.init(context)
            }
        }
    }

    private fun showTimePickerDialog(switchButton: SwitchButton) {
        context.showTimePickerDialog(
            oneDayMSG?.hour ?: 0,
            oneDayMSG?.minute ?: 0
        ) { isCancel: Boolean, hourOfDay, minute ->
            if (!isCancel) {
                switchButton.toggle(true)

                oneDayMSG = oneDayMSG?.apply {
                    isOpen = true
                    hour = hourOfDay
                    this.minute = minute
                } ?: AlarmTask(AlarmTask.ONE_DATA_MSG_TASK_NAME, true, hourOfDay, minute)

                Core.getDaoSession()
                    .alarmTaskDao
                    .insertOrReplace(oneDayMSG)
                // 重新初始化下定时任务
                CoreAlarmManager.init(context)
            }
        }
    }
}


/**
 * 今日天气
 */
class TodayWeatherItem(val context: Activity, var weather: AlarmTask?) :
    SwitchItem("今日天气", "每天只为你关注天气（点我修改时间丶城市）", weather?.IsOpen ?: false) {
    init {
        onClickListener = View.OnClickListener { v ->
            val switchButton = v as SwitchButton
            click(switchButton)
        }

        itemClickListener = View.OnClickListener {
            context.showTimePickerDialog(
                weather?.hour ?: 0,
                weather?.minute ?: 0
            ) { isCancel: Boolean, hourOfDay, minute ->
                if (!isCancel) {
                    context.showInputDialog("提示", "请输入关注天气的城市", SharedPreferencesUtils.CITY_CODE ?: "") { msg ->
                        SharedPreferencesUtils.CITY_CODE = msg

                        weather = weather?.apply {
                            hour = hourOfDay
                            this.minute = minute
                        } ?: AlarmTask(AlarmTask.TODAY_WEATHER_TASK_NAME, false, hourOfDay, minute)

                        Core.getDaoSession()
                            .alarmTaskDao
                            .insertOrReplace(weather)
                        // 重新初始化下定时任务
                        CoreAlarmManager.init(context)
                        true
                    }
                }
            }
        }

    }

    /**
     * 点击事件
     */
    private fun click(switchButton: SwitchButton) {
        //http://open.iciba.com/dsapi/
        if (!switchButton.isChecked) {
            //展示输入昵称dialog
            showTimePickerDialog(switchButton)
        } else {
            // 关闭
            switchButton.toggle(true)
            weather?.let {
                it.IsOpen = false

                Core.getDaoSession()
                    .alarmTaskDao
                    .insertOrReplace(it)
                // 重新初始化下定时任务
                CoreAlarmManager.init(context)
            }
        }
    }

    private fun showTimePickerDialog(switchButton: SwitchButton) {
        context.showTimePickerDialog(weather?.hour ?: 0, weather?.minute ?: 0) { isCancel: Boolean, hourOfDay, minute ->
            if (!isCancel) {
                context.showInputDialog("提示", "请输入关注天气的城市", SharedPreferencesUtils.CITY_CODE ?: "") { msg ->
                    SharedPreferencesUtils.CITY_CODE = msg

                    switchButton.toggle(true)

                    weather = weather?.apply {
                        isOpen = true
                        hour = hourOfDay
                        this.minute = minute
                    } ?: AlarmTask(AlarmTask.TODAY_WEATHER_TASK_NAME, true, hourOfDay, minute)

                    Core.getDaoSession()
                        .alarmTaskDao
                        .insertOrReplace(weather)
                    // 重新初始化下定时任务
                    CoreAlarmManager.init(context)
                    true
                }
            }
        }
    }
}


/**
 * 爱情一句
 */
class LoveMsgItem(val context: Activity, var loveMsg: AlarmTask?) :
    SwitchItem("土味情话", "每天只为你准备一句情话（点我修改时间）", loveMsg?.IsOpen ?: false) {
    init {
        onClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val switchButton = v as SwitchButton
                click(switchButton)
            }
        }

        itemClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                context.showTimePickerDialog(
                    loveMsg?.hour ?: 0,
                    loveMsg?.minute ?: 0
                ) { isCancel: Boolean, hourOfDay, minute ->
                    if (!isCancel) {

                        loveMsg = loveMsg?.apply {
                            hour = hourOfDay
                            this.minute = minute
                        } ?: AlarmTask(AlarmTask.LOVE_MSG_TASK_NAME, false, hourOfDay, minute)

                        Core.getDaoSession()
                            .alarmTaskDao
                            .insertOrReplace(loveMsg)
                        // 重新初始化下定时任务
                        CoreAlarmManager.init(context)
                    }
                }
            }
        }

    }

    /**
     * 点击事件
     */
    private fun click(switchButton: SwitchButton) {
        //http://open.iciba.com/dsapi/
        if (!switchButton.isChecked) {
            //展示输入昵称dialog
            showTimePickerDialog(switchButton)
        } else {
            // 关闭
            switchButton.toggle(true)
            loveMsg?.let {
                it.IsOpen = false

                Core.getDaoSession()
                    .alarmTaskDao
                    .insertOrReplace(it)
                // 重新初始化下定时任务
                CoreAlarmManager.init(context)
            }
        }
    }

    private fun showTimePickerDialog(switchButton: SwitchButton) {
        context.showTimePickerDialog(loveMsg?.hour ?: 0, loveMsg?.minute ?: 0) { isCancel: Boolean, hourOfDay, minute ->
            if (!isCancel) {
                switchButton.toggle(true)

                loveMsg = loveMsg?.apply {
                    isOpen = true
                    hour = hourOfDay
                    this.minute = minute
                } ?: AlarmTask(AlarmTask.LOVE_MSG_TASK_NAME, true, hourOfDay, minute)

                Core.getDaoSession()
                    .alarmTaskDao
                    .insertOrReplace(loveMsg)
                // 重新初始化下定时任务
                CoreAlarmManager.init(context)
            }
        }
    }
}


/**
 * 自定义情话
 */
class CustomMsgItem(val context: Activity, var costom: AlarmTask?) :
    SwitchItem("自定义情话", "每天一句自定义情话（点我修改时间丶内容）", costom?.IsOpen ?: false) {
    init {
        onClickListener = View.OnClickListener { v ->
            val switchButton = v as SwitchButton
            click(switchButton)
        }

        itemClickListener = View.OnClickListener { v ->

            context.showTimePickerDialog(
                costom?.hour ?: 0,
                costom?.minute ?: 0
            ) { isCancel: Boolean, hourOfDay, minute ->
                if (!isCancel) {

                    context.showInputDialog("提示", "请输入发送内容", SharedPreferencesUtils.CUSTOM_LOVE_MSG ?: "") { msg ->
                        SharedPreferencesUtils.CUSTOM_LOVE_MSG = msg

                        costom = costom?.apply {
                            hour = hourOfDay
                            this.minute = minute
                        } ?: AlarmTask(AlarmTask.TODAY_CUSTOM_TASK_NAME, false, hourOfDay, minute)

                        Core.getDaoSession()
                            .alarmTaskDao
                            .insertOrReplace(costom)
                        // 重新初始化下定时任务
                        CoreAlarmManager.init(context)

                        true
                    }

                }
            }
        }


    }

    /**
     * 点击事件
     */
    private fun click(switchButton: SwitchButton) {
        //http://open.iciba.com/dsapi/
        if (!switchButton.isChecked) {
            //展示输入昵称dialog
            showTimePickerDialog(switchButton)
        } else {
            // 关闭
            switchButton.toggle(true)
            costom?.let {
                it.IsOpen = false

                Core.getDaoSession()
                    .alarmTaskDao
                    .insertOrReplace(it)
                // 重新初始化下定时任务
                CoreAlarmManager.init(context)
            }
        }
    }

    private fun showTimePickerDialog(switchButton: SwitchButton) {
        context.showTimePickerDialog(costom?.hour ?: 0, costom?.minute ?: 0) { isCancel: Boolean, hourOfDay, minute ->
            if (!isCancel) {

                context.showInputDialog("提示", "请输入发送内容", SharedPreferencesUtils.CUSTOM_LOVE_MSG ?: "") { msg ->
                    SharedPreferencesUtils.CUSTOM_LOVE_MSG = msg

                    switchButton.toggle(true)

                    costom = costom?.apply {
                        isOpen = true
                        hour = hourOfDay
                        this.minute = minute
                    } ?: AlarmTask(AlarmTask.TODAY_CUSTOM_TASK_NAME, true, hourOfDay, minute)

                    Core.getDaoSession()
                        .alarmTaskDao
                        .insertOrReplace(costom)
                    // 重新初始化下定时任务
                    CoreAlarmManager.init(context)

                    true
                }

            }
        }
    }
}