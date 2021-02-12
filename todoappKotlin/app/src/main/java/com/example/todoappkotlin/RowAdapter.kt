package com.example.todoappkotlin
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


class RowAdapter(private val context: Activity, private val titles: List<String>, delCallback: (p: Int) -> Int)
    : ArrayAdapter<String>(context, R.layout.row, titles) {

    private var delCallback: (p: Int) -> Int = delCallback

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.row, null, true)

        val titleText = rowView.findViewById(R.id.title) as TextView

        titleText.text = titles[position]

        val button = rowView.findViewById(R.id.delete) as Button

        button.setOnClickListener{
            delCallback(position)
        }

        return rowView
    }
}