package com.example.labmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.labmanager.model.MedicalFile
import com.example.labmanager.R
import com.example.labmanager.service.ItemClickedCallback

class ImagesRecyclerAdapter (
    private val medicalFiles: ArrayList<MedicalFile>,
    private val callback: ItemClickedCallback
): RecyclerView.Adapter<ImagesRecyclerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_recycler_item, parent, false)
        val viewHolder = ImagesRecyclerViewHolder(view)

        viewHolder.itemView.setOnClickListener {
            callback.itemAtPositionSelected(viewHolder.adapterPosition)
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return medicalFiles.size
    }

    override fun onBindViewHolder(holder: ImagesRecyclerViewHolder, position: Int) {
        holder.bind(medicalFiles[position])
    }

}