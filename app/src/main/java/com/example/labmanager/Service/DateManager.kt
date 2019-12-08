package com.example.labmanager.Service

import android.util.Log
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter




object DateManager{

    fun toTimestamp(stringDate : String) : Timestamp {
        try {
            val dateFormat = SimpleDateFormat("DD-MM-YYYY")
            val parsedDate = dateFormat.parse(stringDate)
            return java.sql.Timestamp(parsedDate.getTime())
        } catch (e: Exception) {  }

        return Timestamp(System.currentTimeMillis())
    }

    fun toMillis(stringDate: String) : Long {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val date = sdf.parse(stringDate)
        return date.time
    }

    fun dateMillisToStringDate(milis: Long) : String {


        val date = Date(milis)
        var stringDate = "${date.day}/"
        if(date.day < 10){
            stringDate = "0${date.day}/"
        }
        if(date.month < 10){
            stringDate = stringDate + "0${date.month}/${date.year}"
        }else {
            stringDate = stringDate + "${date.month}/${date.year}"
        }
        Log.d("dateMillis " , " millis: $milis time: $stringDate , ${date.toString()}"  )
        return date.toString()
    }
}