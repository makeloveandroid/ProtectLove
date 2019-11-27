package com.protect.love.extension

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.view.ViewGroup
import android.widget.EditText
import com.protect.love.uitl.SharedPreferencesUtils
import org.jetbrains.anko.UI
import org.jetbrains.anko.editText
import org.jetbrains.anko.toast
import org.jetbrains.anko.verticalLayout
import android.app.TimePickerDialog
import android.widget.TimePicker


fun Activity.showHintDialog(tile: String, msg: String, block: () -> Unit) {

    AlertDialog
        .Builder(this)
        .setTitle(tile)
        .setMessage(msg)
        .setPositiveButton("确定") { dialogInterface: DialogInterface, i: Int ->
            block()
        }
        .setNegativeButton("取消", null)
        .setCancelable(false)
        .show()

}


fun Activity.showInputDialog(title: String, hint: String, defaultStr: String, block: (String) -> Boolean) {
    var editText: EditText? = null


    val view = UI {
        verticalLayout {
            editText = editText(defaultStr) {
                this.hint = hint
                maxLines = 2
                minLines = 2
            }.lparams {
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                width = ViewGroup.LayoutParams.MATCH_PARENT
                topMargin = dp2i(10f)
                leftMargin = dp2i(10f)
                rightMargin = dp2i(10f)
                bottomMargin = dp2i(20f)
            }
        }

    }.view


    val dialog = AlertDialog.Builder(this)
        .setTitle(title)
        .setView(view)
        .setPositiveButton("确定", null)
        .setNegativeButton("取消", null)
        .setCancelable(false)
        .show()


    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        .setOnClickListener {
            val str = editText!!.text.toString()
            if (str.isBlank()) {
                toast(hint)
                return@setOnClickListener
            }
            if (block(str)) {
                dialog.dismiss()
            }
        }
}

fun Activity.showTimePickerDialog(h:Int,m:Int,block: (Boolean, Int, Int) -> Unit) {

    val timePickerDialog =
        TimePickerDialog(this, 5, { timePicker: TimePicker, h: Int, m: Int ->
            block(false, h, m)
        }, h, m, true)
    timePickerDialog.setOnCancelListener {
        block(true, 0, 0)
    }
    timePickerDialog.show()
}