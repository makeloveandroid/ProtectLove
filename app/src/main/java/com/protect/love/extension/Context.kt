package com.protect.love.extension

import android.content.Context
import android.util.TypedValue
import android.view.View

// 使用扩展函数
fun Context.dp2f(dp: Float): Float {
    // 引用View的context
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
    )
}

// 转换Int
fun Context.dp2i(dp: Float): Int {
    return dp2f(dp).toInt()
}