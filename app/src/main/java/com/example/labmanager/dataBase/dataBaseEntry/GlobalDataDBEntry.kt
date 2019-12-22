package com.example.labmanager.dataBase.dataBaseEntry

import com.example.labmanager.*
import com.example.labmanager.model.AnonymousTestResult
import com.google.firebase.database.FirebaseDatabase

object GlobalDataDBEntry {

    private var databaseReference = FirebaseDatabase.getInstance().reference

    fun saveUserTestResult(anonymousTestResult: AnonymousTestResult) {

        val dbEndpoint = databaseReference.child(GLOBAL_NODE)
        val key = databaseReference.child(GLOBAL_NODE).push().key
        if (key != null) {
            dbEndpoint.child(key).child(HASHED_ID).setValue(anonymousTestResult.hashedId)
            dbEndpoint.child(key).child(BLOOD_TEST_REF).setValue(anonymousTestResult.bloodTest)
            dbEndpoint.child(key).child(RESULT).setValue(anonymousTestResult.result)
            dbEndpoint.child(key).child(UNIT).setValue(anonymousTestResult.unit)
            dbEndpoint.child(key).child(DATE).setValue(anonymousTestResult.dateMillis)
            return
        }
    }
}