package com.protect.love

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import com.facebook.stetho.Stetho

import kotlinx.android.synthetic.main.activity_main.*
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.protect.love.bean.CityIdBean
import com.protect.love.core.LoveMsgRetrofit
import com.protect.love.core.TodayWeatherRetrofit
import com.protect.love.xp.log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Stetho.initializeWithDefaults(this)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//            try {
//                val string = String(assets.open("city.json").readBytes())
//
//                val type = object : TypeToken<List<CityIdBean>>() {
//
//                }.type
//                val reslut: List<CityIdBean> = Gson().fromJson(string, type)
//                log("得到结果:$reslut")
//
//                log("re:${Gson().toJson(reslut)}")
//                val s = "$externalCacheDir/city.json"
//                log("路径:$s")
//                File(s).writeBytes(Gson().toJson(reslut).toByteArray())
//
//            } catch (e: Exception) {
//            }

            GlobalScope.launch {
                val loveMsg = LoveMsgRetrofit.api.getLoveMsg()
                log("得到结果:${loveMsg.string()}")


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
