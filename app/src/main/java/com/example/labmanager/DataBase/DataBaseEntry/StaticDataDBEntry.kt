package com.example.labmanager.DataBase.DataBaseEntry

import android.util.Log
import com.example.labmanager.BLOOD_TESTS_NODE
import com.example.labmanager.BLOOD_TESTS_NODE_VERSION
import com.example.labmanager.DATA_BASE_ERROR
import com.example.labmanager.DataBase.usecase.StaticData.StaticDataCallback
import com.example.labmanager.DataBase.usecase.StaticData.StaticDataGateway
import com.example.labmanager.Model.BloodTest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object StaticDataDBEntry :  StaticDataGateway{

    private var databaseReference = FirebaseDatabase.getInstance().reference
    private var bloodTestsArray = arrayListOf<BloodTest>()

    override fun getBloodTestsArray(callback: StaticDataCallback) {

        if(bloodTestsArray.size > 0){
            callback.onBloodTestsRetrievalSuccess(bloodTestsArray)
            return
        }
        callback.onBloodTestsRetrievalFailure(DATA_BASE_ERROR)

        bloodTestsArray = arrayListOf<BloodTest>()
        var btEndpoint = databaseReference.child(BLOOD_TESTS_NODE)
        btEndpoint.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bloodTestsArray.clear()
                for(btSnapshot in dataSnapshot.children){
                    bloodTestsArray.add(btSnapshot.getValue(BloodTest::class.java) as BloodTest)
                    Log.d("StatickDataRetrieval", "Fuuuuuck")

                }
                callback.onBloodTestsRetrievalSuccess(bloodTestsArray)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onBloodTestsRetrievalFailure(DATA_BASE_ERROR)
            }
        })
    }


//    private fun isUpToDate() : Boolean{
//        var returnValue = false;
//        var btEndpoint = databaseReference.child(BLOOD_TESTS_NODE_VERSION)
//        btEndpoint.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                returnValue = dataVersion <= dataSnapshot.getValue() as Long
//                dataVersion = dataSnapshot.getValue() as Long
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                returnValue = true
//            }
//        })
//        return returnValue
//    }

}