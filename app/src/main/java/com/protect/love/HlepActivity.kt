package com.protect.love

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.protect.love.xp.log
import kotlinx.android.synthetic.main.activity_hlep.*

class HlepActivity : AppCompatActivity() {

    private var lastIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hlep)
        val items = mutableListOf<HelpItem>().apply {
            add(HelpItem(R.mipmap.help_1, "点击即可守护此为娇妻!"))
            add(HelpItem(R.mipmap.help_2, "点击即可关闭守护此为娇妻!"))
            add(HelpItem(R.mipmap.hlep_3, "长按击即可关闭守护此为娇妻!"))
            add(HelpItem(R.mipmap.hlep_4, "自动回复功能，需要配置开发者 Key，请长按设置去测试账号吧!"))
            add(HelpItem(R.mipmap.hlep_5, "每日一句话，需要设置每日一句话的发送时间，发送事件存在 15 秒内的延迟!"))
        }
        viewpager.adapter = HelpAdapter(items)
        initView(items)

    }

    private fun initView(items: MutableList<HelpItem>) {
        indicator.removeAllViews()
        repeat(items.size) {
            val dotImage = ImageView(this)
            dotImage.setPadding(7, 0, 7, 0)
            dotImage.setImageResource(R.drawable.banner_indicator)
            indicator.addView(dotImage)
            dotImage.isSelected = it == 0

            lastIndex = 0
        }

        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                onIndicatorChanged(position)
            }

        })


    }


    private fun onIndicatorChanged(position: Int) {
        val lastDotImage = indicator.getChildAt(lastIndex) as ImageView
        val dotImage = indicator.getChildAt(position) as ImageView
        if (lastDotImage != null && dotImage != null) {
            // 之前选中的圆点显示正常状态
            lastDotImage.isSelected = false
            // 当前的原点显示选中状态
            dotImage.isSelected = true
        }
        lastIndex = position

    }

}

class HelpItem(val resourceId: Int, val msg: String)

class HelpAdapter(val items: List<HelpItem>) : PagerAdapter() {


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_help, container, false)
        fillView(view, position)

        container.addView(view)

        return view
    }

    private fun fillView(view: View, position: Int) {
        val helpItem = items[position]

        view.findViewById<ImageView>(R.id.alarm_help_pic).setImageResource(helpItem.resourceId)
        view.findViewById<TextView>(R.id.tv_content).text = helpItem.msg

    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }

    override fun getCount(): Int {
        return items.size
    }

}