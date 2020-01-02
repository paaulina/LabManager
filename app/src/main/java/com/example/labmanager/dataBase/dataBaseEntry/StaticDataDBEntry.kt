package com.example.labmanager.dataBase.dataBaseEntry

import com.example.labmanager.BLOOD_TESTS_NODE
import com.example.labmanager.DATA_BASE_ERROR
import com.example.labmanager.dataBase.usecase.staticData.StaticDataCallback
import com.example.labmanager.dataBase.usecase.staticData.StaticDataGateway
import com.example.labmanager.model.BloodTest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object StaticDataDBEntry :  StaticDataGateway{

    private var databaseReference = FirebaseDatabase.getInstance().reference
    private var bloodTestsArray = arrayListOf<BloodTest>()

    override fun getBloodTestsArray(callback: StaticDataCallback) {

        bloodTestsArray = arrayListOf()
        val btEndpoint = databaseReference.child(BLOOD_TESTS_NODE)
        btEndpoint.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bloodTestsArray.clear()
                for(btSnapshot in dataSnapshot.children){
                    bloodTestsArray.add(btSnapshot.getValue(BloodTest::class.java) as BloodTest)
                }

                var newAray = arrayListOf<BloodTest>()
                for(b in bloodTestsArray){
                    newAray.add(b)
                }
                callback.onBloodTestsRetrievalSuccess(newAray)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onBloodTestsRetrievalFailure(DATA_BASE_ERROR)
            }
        })
    }

}