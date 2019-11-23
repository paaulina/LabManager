package com.example.labmanager.DataBase.usecase.StaticData

import com.example.labmanager.Model.BloodTest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StaticDataInteractor(private val gateway: StaticDataGateway) : StaticDataCallback{

    private var databaseReference = FirebaseDatabase.getInstance().reference
    private var bloodTestsArray = ArrayList<BloodTest>()
    lateinit var successfulBloodTestRetrieval : (ArrayList<BloodTest>) -> Unit
    lateinit var failureBloodTestRetrieval : (String) -> Unit

    fun getBloodTestsArray(
        success: (ArrayList<BloodTest>) -> Unit,
        failure: (String) -> Unit
    ) {
        successfulBloodTestRetrieval = success
        failureBloodTestRetrieval = failure
        gateway.getBloodTestsArray(this)
    }

    override fun onBloodTestsRetrievalSuccess(bloodTests: ArrayList<BloodTest>) {
        successfulBloodTestRetrieval(bloodTests)
    }

    override fun onBloodTestsRetrievalFailure(message: String?) {
        if (message == null) {
            failureBloodTestRetrieval("Error")
            return
        }
        failureBloodTestRetrieval(message)
    }

}