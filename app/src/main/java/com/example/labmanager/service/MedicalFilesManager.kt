package com.example.labmanager.service

import com.example.labmanager.model.MedicalFile

class MedicalFilesManager {

    fun sortFilesByName(input: ArrayList<MedicalFile>) :ArrayList<MedicalFile>{
        input.sortWith(compareBy {it.name.toUpperCase()})
        return input
    }
}