package com.example.labmanager.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labmanager.model.MedicalFile
import kotlinx.android.synthetic.main.image_recycler_item.view.*

class ImagesRecyclerViewHolder (itemView: View) :
    RecyclerView.ViewHolder(itemView){

    private val imageView : ImageView = itemView.imageView
    private val imageTitle: TextView = itemView.textViewImageTitle


    fun bind(medicalFile: MedicalFile){
        imageView.setImageBitmap(medicalFile.imageBitmap)
        imageTitle.text = medicalFile.name
    }
}