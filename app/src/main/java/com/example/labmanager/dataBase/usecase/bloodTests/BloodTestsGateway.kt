package com.example.labmanager.dataBase.usecase.bloodTests

interface BloodTestsGateway {
    fun retrieveBloodTests(callback: BloodTestsCallback)
}