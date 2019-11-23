package com.example.labmanager.DataBase.usecase.StaticData

import com.example.labmanager.Model.BloodTest

interface StaticDataCallback {
    fun onBloodTestsRetrievalSuccess(bloodTests: ArrayList<BloodTest>)
    fun onBloodTestsRetrievalFailure(message: String?)
}