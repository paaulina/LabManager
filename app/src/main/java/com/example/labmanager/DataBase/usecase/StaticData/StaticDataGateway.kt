package com.example.labmanager.DataBase.usecase.StaticData

import com.example.labmanager.Model.BloodTest

interface StaticDataGateway {
    fun getBloodTestsArray (callback: StaticDataCallback)
}