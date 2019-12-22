package com.example.labmanager.dataBase.usecase.staticData

import com.example.labmanager.model.BloodTest

interface StaticDataCallback {
    fun onBloodTestsRetrievalSuccess(bloodTests: ArrayList<BloodTest>)
    fun onBloodTestsRetrievalFailure(message: String?)
}