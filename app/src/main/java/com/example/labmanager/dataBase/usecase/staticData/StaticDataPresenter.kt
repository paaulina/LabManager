package com.example.labmanager.dataBase.usecase.staticData

import com.example.labmanager.model.BloodTest

interface StaticDataPresenter {
    fun presentBloodTestsArray(bloodTests : ArrayList<BloodTest>)
    fun presentBloodTestsRetrievalError(error : String)
}