package com.example.labmanager.DataBase.usecase.StaticData

import com.example.labmanager.Model.BloodTest

interface StaticDataPresenter {
    fun presentBloodTestsArray(bloodTests : ArrayList<BloodTest>)
    fun presentBloodTestsRetrievalError(error : String)
}