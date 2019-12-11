package com.example.labmanager.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.R
import com.example.labmanager.Service.ItemClickedCallback

class TestResultAdapter (val resultsArray: ArrayList<UserTestResult>, val context: Context, val callback: ItemClickedCallback)
    : RecyclerView.Adapter<TestResultViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_result_list_item, parent, false)
        val viewholder = TestResultViewHolder(view)

        viewholder.itemView.setOnClickListener {
            callback.itemAtPositionSelected(viewholder.adapterPosition)
        }

        viewholder.itemView.setOnLongClickListener(callback, viewholder.adapterPosition)
        return viewholder
    }

    override fun getItemCount(): Int {
        return resultsArray.size
    }

    override fun onBindViewHolder(holder: TestResultViewHolder, position: Int) {
        holder.bind(resultsArray.get(position))
    }

    private fun View.setOnLongClickListener(callback : ItemClickedCallback, position: Int) {
        callback.itemAtPositionLongClicked(position)
    }
}


