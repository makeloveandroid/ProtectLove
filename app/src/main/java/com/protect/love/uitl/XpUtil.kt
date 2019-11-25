package com.protect.love.uitl

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers

// 封装下 xp框架
fun hookFun(
    beforeHookedMethod: (param: XC_MethodHook.MethodHookParam) -> Unit = {},
    afterHookedMethod: (param: XC_MethodHook.MethodHookParam) -> Unit = {},
    clazz: Class<*>,
    methodName: String,
    vararg parameterTypes: Any
) {

    val parameterTypesAndCallback = arrayOfNulls<Any>(parameterTypes.size + 1)
    parameterTypes.forEachIndexed { index, any ->
        parameterTypesAndCallback[index] = any
    }
    parameterTypesAndCallback[parameterTypes.size] = object :
        XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam?) {
            super.beforeHookedMethod(param)
            param?.let(beforeHookedMethod)
        }

        override fun afterHookedMethod(param: MethodHookParam?) {
            super.afterHookedMethod(param)
            param?.let(afterHookedMethod)
        }
    }
    XposedHelpers.findAndHookMethod(clazz, methodName, *parameterTypesAndCallback)
}

fun hookFun(
    beforeHookedMethod: (param: XC_MethodHook.MethodHookParam) -> Unit = {},
    afterHookedMethod: (param: XC_MethodHook.MethodHookParam) -> Unit = {},
    className: String, classLoader: ClassLoader, methodName: String, vararg parameterTypes: Any
) {

    val parameterTypesAndCallback = arrayOfNulls<Any>(parameterTypes.size + 1)
    parameterTypes.forEachIndexed { index, any ->
        parameterTypesAndCallback[index] = any
    }
    parameterTypesAndCallback[parameterTypes.size] = object :
        XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam?) {
            super.beforeHookedMethod(param)
            param?.let(beforeHookedMethod)
        }

        override fun afterHookedMethod(param: MethodHookParam?) {
            super.afterHookedMethod(param)
            param?.let(afterHookedMethod)
        }
    }
    XposedHelpers.findAndHookMethod(className, classLoader, methodName, *parameterTypesAndCallback)
}
