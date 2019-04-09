package com.brook.app.android.testapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.brook.app.android.dependencylayout.R
import com.brook.app.android.supportlibrary.adapter.TextViewImpl
import com.brook.app.android.supportlibrary.util.DependencyLayoutConfig

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DependencyLayoutConfig.getInstance()
                .setDesignWidth("750px")
                .setDesignHeight("1294px")
//                .addViewAdapterHandler(TextView::class.java, TextViewImpl())

        setContentView(R.layout.activity_test)
    }
}
