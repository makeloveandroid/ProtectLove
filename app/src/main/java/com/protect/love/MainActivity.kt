package com.protect.love

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.facebook.stetho.Stetho

import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Stetho.initializeWithDefaults(this)
        fab.setOnClickListener { view ->
            openWebView("https://github.com/makeloveandroid/ProtectLove")
        }

        initView()

    }

    private fun openWebView(url:String) {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val content_url = Uri.parse(url)
        intent.data = content_url
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun initView() {
        Glide.with(this).load(R.drawable.gif_icon).into(image)
        btn_360.setOnClickListener {

            openWebView("https://a.app.qq.com/o/simple.jsp?pkgname=com.qihoo.magic.xposed")
        }

        btn_taiji.setOnClickListener {
            openWebView("https://taichi.cool/README_CN.html")
        }

        help.setOnClickListener {
            Intent(applicationContext,HlepActivity::class.java).apply {
                startActivity(this)
            }
        }

    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
