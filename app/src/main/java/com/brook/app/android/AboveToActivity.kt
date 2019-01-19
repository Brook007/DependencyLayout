package com.brook.app.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brook.app.android.dependencylayout.R

class AboveToActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_above_to)
        title = this.javaClass.simpleName
    }
}
