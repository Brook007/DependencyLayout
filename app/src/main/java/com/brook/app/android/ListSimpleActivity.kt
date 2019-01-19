package com.brook.app.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.brook.app.android.dependencylayout.R


class ListSimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_simple)
        title = this.javaClass.simpleName

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)

        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return if (viewType == 0) {
                    DependencyLayoutHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_simple_dependent, parent, false))
                } else {
                    RelativeHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_simple_relative, parent, false))
                }
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val viewType = getItemViewType(position)
                if (viewType == 0) {
                    val viewHolder = holder as DependencyLayoutHolder

                } else {
                    val relativeHolder = holder as RelativeHolder
                    relativeHolder.position.text = "使用RelativeLayout$position"
                }
            }

            override fun getItemCount(): Int {
                return 50
            }

            override fun getItemViewType(position: Int): Int {
                val value = position % 2
                return if (0 == value) {
                    1
                } else {
                    0
                }
            }

            inner class RelativeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                var position = itemView.findViewById<TextView>(R.id.position)
            }

            inner class DependencyLayoutHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        }
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
}
