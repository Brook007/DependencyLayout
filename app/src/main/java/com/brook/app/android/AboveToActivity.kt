package com.brook.app.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.brook.app.android.dependentlayout.R

class AboveToActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_above_to)
        title = this.javaClass.simpleName
    }
}
