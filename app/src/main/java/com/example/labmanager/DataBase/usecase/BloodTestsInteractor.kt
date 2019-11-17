package com.example.labmanager.DataBase.usecase

import com.example.labmanager.Model.BloodTest

class BloodTestsInteractor (
    private val persistance: BloodTestsGateway,
    private val presenter: BloodTestsPresenter

): BloodTestsCallback {

    fun dispatch(){
        persistance.retrieveBloodTests(this)
    }
    override fun onBloodTestsRetrievalSuccess(bloodTests: ArrayList<BloodTest>) {
        presenter.presentBloodTests(bloodTests)
    }

    override fun onBloodTestsRetrievalFailure(message: String?) {
         presenter.presentBloodTestsError(message)
    }

}