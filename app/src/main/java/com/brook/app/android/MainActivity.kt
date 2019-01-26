package com.brook.app.android

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.brook.app.android.dependencylayout.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        title = "DependencyLayoutDemo"

    }

    fun click(view: View) {
        when ((view as Button).text.toString().toLowerCase()) {
            "above".toLowerCase() -> startActivity(Intent(this, AboveToActivity::class.java))
            "align".toLowerCase() -> startActivity(Intent(this, AlignActivity::class.java))
            "align_Parent".toLowerCase() -> startActivity(Intent(this, AlignParentActivity::class.java))
            "margin_padding".toLowerCase() -> startActivity(Intent(this, MarginAndPaddingActivity::class.java))
            "to_Center".toLowerCase() -> startActivity(Intent(this, ToCenterActivity::class.java))
            "to_Parent_Center".toLowerCase() -> startActivity(Intent(this, ToParentCenterActivity::class.java))
            "simple".toLowerCase() -> startActivity(Intent(this, SimpleActivity::class.java))
            "list_simple".toLowerCase() -> startActivity(Intent(this, ListSimpleActivity::class.java))
        }
    }
}
