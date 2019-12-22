package com.example.labmanager.dataBase.usecase.staticData

import com.example.labmanager.model.BloodTest

class StaticDataInteractor(
    private val gateway: StaticDataGateway,
    private val presenter: StaticDataPresenter
) : StaticDataCallback{


    fun getBloodTestsArray() {
        gateway.getBloodTestsArray(this)
    }

    override fun onBloodTestsRetrievalSuccess(bloodTests: ArrayList<BloodTest>) {
        presenter.presentBloodTestsArray(bloodTests)
    }

    override fun onBloodTestsRetrievalFailure(message: String?) {
        presenter.presentBloodTestsRetrievalError(message + "")
    }

}