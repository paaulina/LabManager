package com.example.labmanager.DataBase.usecase.BloodTests

interface BloodTestsGateway {
    fun retrieveBloodTests(callback: BloodTestsCallback)
}