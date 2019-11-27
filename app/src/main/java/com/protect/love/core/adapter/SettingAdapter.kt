package com.protect.love.core.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import androidx.appcompat.view.menu.MenuView
import com.protect.love.core.view.CLICK_TYPE
import com.protect.love.core.view.SWITCHITEM_TYPE
import com.protect.love.core.view.TITLE_TYPE
import com.protect.love.core.view.ViewItem

class SettingAdapter(val items: List<ViewItem>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return convertView ?: getItemObj(position).createView(parent.context)
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    fun getItemObj(position: Int): ViewItem {
        return getItem(position) as ViewItem
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return (getItem(position) as ViewItem).type
    }

    override fun getViewTypeCount(): Int {
        return 3
    }

    override fun getCount(): Int {
        return items.size
    }

}