package com.example.labmanager.DataBase.usecase.BloodTests

import com.example.labmanager.Model.BloodTest

interface BloodTestsCallback {
    fun onBloodTestsRetrievalSuccess(bloodTests: ArrayList<BloodTest>)
    fun onBloodTestsRetrievalFailure(message: String?)
}