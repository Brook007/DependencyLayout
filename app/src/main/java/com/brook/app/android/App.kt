package com.brook.app.android

import android.app.Application
import android.widget.TextView
import com.brook.app.android.supportlibrary.adapter.TextViewImpl
import com.brook.app.android.supportlibrary.util.DependencyLayoutConfig

/**
 *
 *
 * @time 2019/1/24 14:08
 * @auther Brook
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DependencyLayoutConfig.getInstance()
                .setDesignWidth("750px")
                .setDesignHeight("1294px")
                .addViewAdapterHandler(TextView::class.java, TextViewImpl())
    }
}