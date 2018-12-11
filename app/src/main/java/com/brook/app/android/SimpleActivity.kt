package com.brook.app.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.brook.app.android.dependentlayout.R
import com.brook.app.android.supportlibrary.adapter.TextViewImpl
import com.brook.app.android.supportlibrary.util.DependentLayoutConfig

class SimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DependentLayoutConfig.getInstance(this).setDesignWidth("750px").setDesignHeight("1294px").addViewAdapterHandler(TextView::class.java, TextViewImpl())

        setContentView(R.layout.activity_simple_px)
        window.setBackgroundDrawable(null)
    }
}
