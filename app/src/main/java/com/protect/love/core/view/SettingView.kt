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
import com.protect.love.bean.AlarmTask
import com.protect.love.core.Core
import com.protect.love.core.CoreAlarmManager
import com.protect.love.core.TodayWeatherRetrofit
import com.protect.love.dao.AlarmTaskDao
import com.protect.love.extension.showInputDialog
import com.protect.love.extension.showTimePickerDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
            add(ClickItem("请配置图灵机器人 apikey 与 密钥", "图灵机器人账号申请，请搜索图灵机器人") { view ->
                com.protect.love.xp.log("设置图key")

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
    SwitchItem("每日一句", "每天只为你准备一句正能量", oneDayMSG?.IsOpen ?: false) {
    init {
        onClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                com.protect.love.xp.log("每日一句:$v")
                val switchButton = v as SwitchButton
                click(switchButton)
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
    SwitchItem("今日天气", "每天只为你关注天气", weather?.IsOpen ?: false) {
    init {
        onClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val switchButton = v as SwitchButton
                click(switchButton)
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
    SwitchItem("土味情话", "每天只为你准备一句情话", loveMsg?.IsOpen ?: false) {
    init {
        onClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val switchButton = v as SwitchButton
                click(switchButton)
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
    SwitchItem("自定义情话", "每天一句自定义情话", costom?.IsOpen ?: false) {
    init {
        onClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val switchButton = v as SwitchButton
                click(switchButton)
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