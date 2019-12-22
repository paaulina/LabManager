package com.example.labmanager.dataBase.usecase.bloodTests

import com.example.labmanager.model.BloodTest

interface BloodTestsCallback {
    fun onBloodTestsRetrievalSuccess(bloodTests: ArrayList<BloodTest>)
    fun onBloodTestsRetrievalFailure(message: String?)
}