package com.brook.app.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brook.app.android.dependencylayout.R

class SimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_simple_px)
        window.setBackgroundDrawable(null)
    }
}
