package com.example.labmanager.service

import java.sql.Date
import java.text.SimpleDateFormat


object DateManager{


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
        stringDate += if(date.month < 10){
            "0${date.month}/${date.year}"
        }else {
            "${date.month}/${date.year}"
        }
        return date.toString()
    }
}