package com.brook.app.android

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.brook.app.android.dependentlayout.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "DependentLayoutDemo"
    }

    fun click(view: View) {
        when ((view as Button).text.toString().toLowerCase()) {
            "above".toLowerCase() -> startActivity(Intent(this, AboveToActivity::class.java))
            "align".toLowerCase() -> startActivity(Intent(this, AlignActivity::class.java))
            "align_Parent".toLowerCase() -> startActivity(Intent(this, AlignParentActivity::class.java))
            "margin_padding".toLowerCase() -> startActivity(Intent(this, MarginAndPaddingActivity::class.java))
            "to_Center".toLowerCase() -> startActivity(Intent(this, ToCenterActivity::class.java))
            "to_Parent_Center".toLowerCase() -> startActivity(Intent(this, ToParentCenterActivity::class.java))
        }
    }
}