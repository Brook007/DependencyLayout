package com.brook.app.android.testapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brook.app.android.dependencylayout.R

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        DependencyLayoutConfig.getInstance()
        //                .addViewAdapterHandler(TextView::class.java, TextViewImpl())

        setContentView(R.layout.activity_test)
    }
}
