package com.brook.app.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.brook.app.android.supportlibrary.dependencylayout.R

class AlignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_align)
        title = this.javaClass.simpleName
    }
}
