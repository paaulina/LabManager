package com.example.labmanager.DataBase.DataBaseEntry

import android.util.Log
import com.example.labmanager.*
import com.example.labmanager.Model.AnonymousTestResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object GlobalDataDBEntry {

    private var databaseReference = FirebaseDatabase.getInstance().reference
    private var firebaseAuth = FirebaseAuth.getInstance()

    fun saveUserTestResult(anonymousTestResult: AnonymousTestResult) {

        Log.d("globalkey-0","o")
        var dbEndpoint = databaseReference.child(GLOBAL_NODE)

        var key = databaseReference.child(GLOBAL_NODE).push().key
        Log.d("globalkey",key)
        if (key != null) {

            Log.d("globalkey", "key not null")
            dbEndpoint.child(key).child(HASHED_ID).setValue(anonymousTestResult.hashedId)
            dbEndpoint.child(key).child(BLOOD_TEST_REF).setValue(anonymousTestResult.bloodTest)
            dbEndpoint.child(key).child(RESULT).setValue(anonymousTestResult.result)
            dbEndpoint.child(key).child(UNIT).setValue(anonymousTestResult.unit)
            dbEndpoint.child(key).child(DATE).setValue(anonymousTestResult.dateMillis)
            return
        }
    }
}