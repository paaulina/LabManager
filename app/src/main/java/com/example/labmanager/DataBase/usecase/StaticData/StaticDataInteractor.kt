package com.example.labmanager.DataBase.usecase.StaticData

import com.example.labmanager.Model.BloodTest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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