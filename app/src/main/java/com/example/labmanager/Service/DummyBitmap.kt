package com.example.labmanager.Service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.labmanager.R

object DummyBitmap {
    private lateinit var bitmap: Bitmap
    fun initialaze(context: Context){
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.prestar)
    }


    fun getEmptyBitmap(): Bitmap{
        return bitmap
    }
}