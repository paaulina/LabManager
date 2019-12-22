package com.example.labmanager.dataBase.usecase.staticData

interface StaticDataGateway {
    fun getBloodTestsArray (callback: StaticDataCallback)
}