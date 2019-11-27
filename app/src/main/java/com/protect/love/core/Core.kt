package com.protect.love.core

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.protect.love.bean.OpenProtectUser
import com.protect.love.dao.DaoMaster
import com.protect.love.dao.DaoSession
import com.protect.love.dao.OpenProtectUserDao
import com.protect.love.extension.dp2i
import com.protect.love.extension.setOnceClick
import com.protect.love.uitl.SharedPreferencesUtils
import com.protect.love.xp.log
import de.robv.android.xposed.XposedHelpers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.toast
import kotlin.coroutines.CoroutineContext
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import android.R
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.widget.*
import com.protect.love.core.extension.showSettingDialog
import java.io.StringReader


@SuppressLint("StaticFieldLeak")
object Core {
    val icon_open_shouhu by lazy {
        val data =
            "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"
        val decode = Base64.decode(data, Base64.NO_WRAP)
        BitmapFactory.decodeByteArray(decode, 0, decode.size)
    }
    val icon_stop_shouhu by lazy {
        val data =
            "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"
        val decode = Base64.decode(data, Base64.NO_WRAP)
        BitmapFactory.decodeByteArray(decode, 0, decode.size)
    }


    class MyContextScope(context: CoroutineContext) : CoroutineScope {
        override val coroutineContext: CoroutineContext = context
    }

    private lateinit var daoSession: DaoSession
    /**
     * 反射创建 mainScope
     * 为啥要反射创建呢?因为算了不想说
     */
    val mainScope by lazy {
        val dispatcherFactoryClzz = Class.forName("kotlinx.coroutines.android.AndroidDispatcherFactory")
        val factoryObj = dispatcherFactoryClzz.newInstance()
        val method = dispatcherFactoryClzz.getMethod("createDispatcher", List::class.java)
        val invoke = method.invoke(factoryObj, mutableListOf<Any>()) as CoroutineContext
        val myContextScope = MyContextScope(SupervisorJob() + invoke)
        myContextScope
    }


    private var launcherAc: Activity? = null


    fun init(activity: Activity) {
        this.launcherAc = activity

        if (!this::daoSession.isInitialized) {
            SharedPreferencesUtils.init(activity.applicationContext)
            val dbHelper = DaoMaster.DevOpenHelper(activity.applicationContext, "core", null)
            var db = dbHelper.writableDatabase
            // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
            daoSession = DaoMaster(db).newSession()

            // 重新初始化下定时
            CoreAlarmManager.init(activity)
        }
    }

    fun getDaoSession(): DaoSession {
        return daoSession
    }


    fun msgGo(talker: String, type: String, content: String, activity: Activity) {
        try {
            if (talker.endsWith("@chatroom")) {
                // 不守护群
                return
            }
            log("消息来了:$talker $type $content")
//            val sendWxId = content.split(":")[0]
//            val msg = content.split(":")[1]
            // 判断是否启用自动回复
            if (SharedPreferencesUtils.IS_AUTO_MSG){
                // 判断是否守护
                Core
                    .getDaoSession()
                    .openProtectUserDao
                    .queryBuilder()
                    .where(OpenProtectUserDao.Properties.WxId.eq(talker))
                    .unique()
                    ?.takeIf {
                        it.isOpen
                    }
                    ?.apply {
                        if (type == "1") {
                            // TEXT 消息
                            log("收到文本消息:$talker  $content")
                            receiverMsg(activity.classLoader, "你好可爱哦", talker)
                        } else {
                            log("收到其他消息:$talker  $content")
                        }
                    }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * hook 发消息方法
     */
    fun receiverMsg(classLoader: ClassLoader, msg: String, talker: String) {
        log("发送消息 $msg  $talker")
        val azClzz = XposedHelpers.findClass("com.tencent.mm.model.az", classLoader)
        val obj = XposedHelpers.callStaticMethod(azClzz, "ZS")
        val msgClzz = XposedHelpers.findClass("com.tencent.mm.modelmulti.h", classLoader)
        XposedHelpers.callMethod(obj, "b", XposedHelpers.newInstance(msgClzz, talker, msg, 1))
    }


    /**
     * 增加开关入口view
     */
    fun addClickView(layout: ViewGroup, cWxid: String) {
        val tag = layout.tag
        if (tag == null) {
            try {
                layout
                    .getChildAt(0)
                    ?.takeIf {
                        it is ViewGroup
                    }
                    ?.let {
                        (it as ViewGroup).getChildAt(0)
                    }
                    ?.takeIf {
                        it is ViewGroup
                    }
                    ?.let {
                        (it as ViewGroup).getChildAt(1)
                    }
                    ?.takeIf {
                        it is ViewGroup
                    }
                    ?.apply {
                        val messangerLayout = this as LinearLayout
                        // 取一个按钮

                        val context = messangerLayout.context
                        val imageView = ImageView(layout.context)
                        val layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        layoutParams.setMargins(0, 0, context.dp2i(2f), 0)
                        layoutParams.gravity = Gravity.CENTER
                        imageView.layoutParams = layoutParams
                        layout.tag = imageView
                        messangerLayout.addView(imageView)
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                toast("注入按钮失败,请尝试重启应用!")
            }
        }

        val icon = layout.tag as ImageView
        val childAt = (icon.parent as LinearLayout).getChildAt(0)

        if (cWxid.endsWith("@chatroom")) {
            // 是群 咋就不守护了
            icon.visibility = View.GONE
            return
        } else {
            // 不是群 咋就守护了
            icon.visibility = View.VISIBLE
        }

        icon.initProtectIcon(cWxid)

        childAt.post {
            //            log("得到高度:${childAt.width}  ${childAt.height}  $childAt  ${childAt.measuredWidth}  ${childAt.measuredHeight}")
            icon.layoutParams.apply {
                if (childAt.width == 0) {
                    width = 86
                    height = 86
                } else {
                    width = childAt.width
                    height = childAt.height
                }

            }

            icon.parent.requestLayout()
        }



        icon.setOnceClick {
            val protectThe = Core.getDaoSession().openProtectUserDao.queryBuilder()
                .where(OpenProtectUserDao.Properties.WxId.eq(cWxid)).unique()

            if (protectThe != null && protectThe.isOpen) {
                protectThe.isOpen = false
                Core.getDaoSession().openProtectUserDao.insertOrReplace(protectThe)
                (it as ImageView).imageBitmap = icon_stop_shouhu
                toast("关闭守护此娇妻!")
            } else {
                val bean = if (protectThe != null) {
                    protectThe.isOpen = true
                    protectThe
                } else {
                    OpenProtectUser().apply {
                        this.wxId = cWxid
                        this.isOpen = true
                    }
                }
                Core.getDaoSession().openProtectUserDao.insertOrReplace(bean)
                (it as ImageView).imageBitmap = icon_open_shouhu
                toast("开启守护此娇妻!")
            }
        }

        /**
         * 长按进入设置
         */
        icon.setOnLongClickListener {
            XposedHelpers.getObjectField(layout, "activity")?.let {
                it as Activity
            }?.apply {
                showSettingDialog()
            }
            true
        }
    }


    fun toast(msg: String) {
        launcherAc?.toast(msg)
        log("消息:$msg")
    }
}

fun ImageView.initProtectIcon(wxId: String) {
    val bean = Core.getDaoSession().openProtectUserDao.queryBuilder()
        .where(OpenProtectUserDao.Properties.WxId.eq(wxId)).unique()
    if (bean != null && bean.isOpen) {
        // 打开
        imageBitmap = Core.icon_open_shouhu

    } else {
        // 关闭
        imageBitmap = Core.icon_stop_shouhu
    }
}