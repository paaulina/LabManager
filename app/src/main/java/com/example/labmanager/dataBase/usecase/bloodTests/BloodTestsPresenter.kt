package com.example.labmanager.dataBase.usecase.bloodTests

import com.example.labmanager.model.BloodTest

interface BloodTestsPresenter {
    fun presentBloodTests(bloodTests : ArrayList<BloodTest>)
    fun presentBloodTestsError(message: String?)
}