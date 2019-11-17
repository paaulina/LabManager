package com.example.labmanager.DataBase.usecase

interface BloodTestsGateway {
    fun retrieveBloodTests(callback: BloodTestsCallback)
}