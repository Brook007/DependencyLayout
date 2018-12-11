package com.brook.app.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brook.app.android.dependentlayout.R

class MarginAndPaddingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_margin)
        title = this.javaClass.simpleName
    }
}
