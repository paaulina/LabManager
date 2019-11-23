package com.example.labmanager.DataBase.usecase.BloodTests

import com.example.labmanager.Model.BloodTest

interface BloodTestsPresenter {
    fun presentBloodTests(bloodTests : ArrayList<BloodTest>)
    fun presentBloodTestsError(message: String?)
}