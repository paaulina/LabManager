package com.example.labmanager.Model

import android.graphics.Bitmap
import android.graphics.BitmapFactory

data class MedicalFile(
    var name: String = "",
    var path: String ="",
    var imageBitmap: Bitmap
)