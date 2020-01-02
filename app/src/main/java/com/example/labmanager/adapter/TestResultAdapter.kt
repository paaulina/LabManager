package com.example.labmanager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.labmanager.model.UserTestResult
import com.example.labmanager.R
import com.example.labmanager.service.ItemClickedCallback
import androidx.annotation.UiThread




class TestResultAdapter (private val resultsArray: ArrayList<UserTestResult>,
                         val context: Context,
                         private val callback: ItemClickedCallback
) : RecyclerView.Adapter<TestResultViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_result_list_item, parent, false)
        val viewHolder = TestResultViewHolder(view)

        viewHolder.itemView.setOnClickListener {
            callback.itemAtPositionSelected(viewHolder.adapterPosition)
        }

        setOnLongClickListener(callback, viewHolder.adapterPosition)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return resultsArray.size
    }

    override fun onBindViewHolder(holder: TestResultViewHolder, position: Int) {
        holder.bind(resultsArray[position])
    }

    private fun setOnLongClickListener(
        callback: ItemClickedCallback,
        position: Int
    ) {
        callback.itemAtPositionLongClicked(position)
    }

}


