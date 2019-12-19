package com.example.labmanager.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.labmanager.Model.MedicalFile
import com.example.labmanager.R

class ImagesRecyclerAdapter (val medicalFiles: ArrayList<MedicalFile>): RecyclerView.Adapter<ImagesRecyclerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_recycler_item, parent, false)
        val viewholder = ImagesRecyclerViewHolder(view)

//        viewholder.itemView.setOnClickListener {
//            callback.itemAtPositionSelected(viewholder.adapterPosition)
//        }
//
//        viewholder.itemView.setOnLongClickListener(callback, viewholder.adapterPosition)
        return viewholder
    }

    override fun getItemCount(): Int {
        return medicalFiles.size
    }

    override fun onBindViewHolder(holder: ImagesRecyclerViewHolder, position: Int) {
        holder.bind(medicalFiles.get(position))
    }

}