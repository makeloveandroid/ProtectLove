package com.protect.love.extension

import android.view.View
import com.protect.love.core.Core
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay

/**
 * 定义View 只能点一次的扩展方法
 */
fun View.setOnceClick(block: (view: View) -> Unit) {
    /**
     * 定义 协程 的一个消费者模式
     */
    val eventActor = Core.mainScope.actor<View>(Dispatchers.Main) {
        // 这里注意，协程 channel 若没有数据，会处于 挂起 状态。直到有数过来才会执行
        for (view in channel) {
            block(view)
            // 500 毫秒 才能接受下一次的点击
            delay(500)
        }
    }
    setOnClickListener {
        /**
         * 发送输出,若消费者,没有消费等待数据,发送数据就会失败
         */
        eventActor.offer(it)
    }
}