package com.example.labmanager.dataBase.usecase.bloodTests

import com.example.labmanager.model.BloodTest

class BloodTestsInteractor (
    private val persistence: BloodTestsGateway,
    private val presenter: BloodTestsPresenter

): BloodTestsCallback {

    fun dispatch(){
        persistence.retrieveBloodTests(this)
    }
    override fun onBloodTestsRetrievalSuccess(bloodTests: ArrayList<BloodTest>) {
        presenter.presentBloodTests(bloodTests)
    }

    override fun onBloodTestsRetrievalFailure(message: String?) {
         presenter.presentBloodTestsError(message)
    }

}