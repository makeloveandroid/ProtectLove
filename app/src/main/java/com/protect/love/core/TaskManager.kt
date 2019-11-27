package com.protect.love.core

import android.content.Context
import com.protect.love.bean.AlarmTask
import com.protect.love.bean.AlarmTaskLog
import com.protect.love.bean.OpenProtectUser
import com.protect.love.dao.AlarmTaskLogDao
import com.protect.love.uitl.SharedPreferencesUtils
import com.protect.love.xp.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

object TaskManager {
    fun invokeTask(classLoader: ClassLoader) {
        GlobalScope.launch {
            // 获取所有娇妻
            log("执行定时消息!")

            val wifes = Core.getDaoSession().openProtectUserDao.queryBuilder().list().filter {
                it.isOpen
            }

            if (wifes.isNotEmpty()) {
                // 获取所有任务
                val tasks = Core.getDaoSession()
                    .alarmTaskDao
                    .queryBuilder()
                    .list()
                    .asSequence()
                    .filter {
                        it.isOpen
                    }
                    .filter {
                        // 判断时间是否到了
                        val cd = Calendar.getInstance()
                        // 获取现在的时间 当前的系统时间 和 我设定的时间一直
                        log("${it.name}  ${cd.get(Calendar.HOUR_OF_DAY)} ${cd.get(Calendar.MINUTE)}  ${it.hour}  ${it.minute}")

                        it.hour == cd.get(Calendar.HOUR_OF_DAY) && it.minute == cd.get(Calendar.MINUTE)
                    }
                    .filter {
                        val cd = Calendar.getInstance()
                        // 判断这个 今日是否执行过
                        Core.getDaoSession().alarmTaskLogDao
                            .queryBuilder()
                            .where(AlarmTaskLogDao.Properties.Name.eq(it.name))
                            .where(AlarmTaskLogDao.Properties.Year.eq(cd.get(Calendar.YEAR)))
                            .where(AlarmTaskLogDao.Properties.Month.eq(cd.get(Calendar.MARCH)))
                            .where(AlarmTaskLogDao.Properties.Day.eq(cd.get(Calendar.DATE)))
                            .where(AlarmTaskLogDao.Properties.Hour.eq(it.hour))
                            .where(AlarmTaskLogDao.Properties.Minute.eq(it.minute))
                            .unique() == null
                    }.toList()

                if (tasks.isNotEmpty()) {
                    execute(classLoader, tasks, wifes)
                }
            }
        }
    }


    /**
     * 正式执行任务
     */
    private fun execute(classLoader: ClassLoader, tasks: List<AlarmTask>, wifes: List<OpenProtectUser>) {
        GlobalScope.launch {
            tasks.forEach {
                // 执行了 记录日志
                val cd = Calendar.getInstance()
                AlarmTaskLog(
                    it.name,
                    cd.get(Calendar.YEAR),
                    cd.get(Calendar.MONTH),
                    cd.get(Calendar.DATE),
                    System.currentTimeMillis(),
                    it.hour,
                    it.minute
                ).apply {
                    Core.getDaoSession().alarmTaskLogDao.insertOrReplace(this)
                }

                GlobalScope.launch(Dispatchers.IO) {
                    // 每日一句
                    if (it.name == AlarmTask.ONE_DATA_MSG_TASK_NAME) {
                        excuteOneDayMsg(classLoader, wifes)
                    }
                    // 今日天气
                    if (it.name == AlarmTask.TODAY_WEATHER_TASK_NAME) {
                        excuteTodayWeather(classLoader, wifes)
                    }

                    // 情话
                    if (it.name == AlarmTask.LOVE_MSG_TASK_NAME) {
                        excuteLoveMsg(classLoader, wifes)
                    }

                    // 自定义
                    if (it.name == AlarmTask.TODAY_CUSTOM_TASK_NAME) {
                        excuteCustomMsg(classLoader, wifes)
                    }
                }


            }


        }
    }

    /**
     * 执行每日一句任务
     */
    suspend fun excuteOneDayMsg(classLoader: ClassLoader, wifes: List<OpenProtectUser>) {
        // 尝试3次
        run breaking@{
            repeat(3) {
                try {
                    val msg = OneDayMsgRetrofit.api.getOneDayMsg()
                    log("获取到每日一句 ${msg.note}")
                    // 循环发送
                    if (msg.note.isNotBlank()) {
                        wifes.forEach {
                            Core.receiverMsg(classLoader, msg.note, it.getWxId())
                            // 别发送太快
                            delay(2000)
                        }
                    }
                    return@breaking
                } catch (e: Exception) {
                }
            }
        }
    }


    /**
     * 执行今日提起那
     */
    suspend fun excuteTodayWeather(classLoader: ClassLoader, wifes: List<OpenProtectUser>) {
        // 尝试3次
        run breaking@{
            repeat(3) {
                try {
                    // 获取 城市 code
                    val code = SharedPreferencesUtils.CITY_CODE
                    if (code?.isNotBlank() == true) {
                        val msg = TodayWeatherRetrofit.api.getWeather(code)
                        log("获取今日天气 $msg")
                        // 循环发送
                        if (msg?.data?.forecast?.isNotEmpty() ?: false) {
                            val forecast = msg.data.forecast[0]

                            val msgStr =
                                "城市:${msg.data.city}\n日期:${forecast.date}\n风力:${forecast.fengli}\n风向:${forecast.fengxiang}\n最高温度:${forecast.high}\n最低温度:${forecast.low}\n天气:${forecast.type}\n甜蜜提醒:${msg.data.ganmao}"
                            wifes.forEach {
                                Core.receiverMsg(classLoader, msgStr, it.getWxId())
                                // 别发送太快
                                delay(2000)
                            }

                        }


                    }
                    return@breaking
                } catch (e: Exception) {
                }
            }
        }
    }


    /**
     * 执行每日情话
     */
    suspend fun excuteLoveMsg(classLoader: ClassLoader, wifes: List<OpenProtectUser>) {
        // 尝试3次
        run breaking@{
            repeat(3) {
                try {
                    val msg = LoveMsgRetrofit.api.getLoveMsg()
                    val msgStr = msg.string()
                    log("今日情话 $msgStr")
                    // 循环发送
                    if (msgStr.isNotEmpty()) {
                        wifes.forEach {
                            Core.receiverMsg(classLoader, msgStr, it.getWxId())
                            // 别发送太快
                            delay(2000)
                        }

                    }
                    return@breaking
                } catch (e: Exception) {
                }
            }
        }
    }


    /**
     * 执行之定义情话
     */
    suspend fun excuteCustomMsg(classLoader: ClassLoader, wifes: List<OpenProtectUser>) {
        val msgStr = SharedPreferencesUtils.CUSTOM_LOVE_MSG

        // 循环发送
        if (msgStr?.isNotBlank() == true) {
            wifes.forEach {
                Core.receiverMsg(classLoader, msgStr, it.getWxId())
                // 别发送太快
                delay(2000)
            }
        }

    }


}