package com.example.labmanager.DataBase.usecase

import com.example.labmanager.Model.BloodTest

interface BloodTestsCallback {
    fun onBloodTestsRetrievalSuccess(bloodTests: ArrayList<BloodTest>)
    fun onBloodTestsRetrievalFailure(message: String?)
}