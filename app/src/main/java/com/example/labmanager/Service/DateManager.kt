package com.example.labmanager.Service

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
        val sdf = SimpleDateFormat("dd/mm/yyyy")
        val date = sdf.parse(stringDate)
        return date.time
    }
}