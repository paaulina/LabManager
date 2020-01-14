package com.example.labmanager.model

import android.graphics.Bitmap

data class MedicalFile(
    var name: String = "",
    var path: String ="",
    var imageBitmap: Bitmap,
    var dateString: String = "",
    var smallBitmap : Bitmap? = null
)