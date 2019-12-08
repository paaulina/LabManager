package com.example.labmanager.DataBase.DataBaseEntry

import android.util.Log
import com.example.labmanager.*
import com.example.labmanager.DataBase.usecase.UserData.TestResults.UserTestResultSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.Gateway.UserDataGateway
import com.example.labmanager.DataBase.usecase.UserData.TestResults.UserTestResultsGetterCallback
import com.example.labmanager.Model.UserTestResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

object UserDataDBEntry : UserDataGateway {

    private var databaseReference = FirebaseDatabase.getInstance().reference
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var userId = ""
    private var testIds = arrayListOf<String>()
    private var userTestsResultsArray = arrayListOf<UserTestResult>()
    private var uploaded = false

    override fun saveUserTestResult(userTestResult: UserTestResult, callback: UserTestResultSavingCallback) {

        if(firebaseAuth.currentUser != null){
            userId = firebaseAuth.currentUser!!.uid
            var userEndpoint = databaseReference.child(USERS_NODE).child(
                userId
            )

            var key = userEndpoint.child(TESTS_RESULTS_NODE).push().key
            if (key != null) {
                writeTestResult(userEndpoint.child(TESTS_RESULTS_NODE).child(key), userTestResult)
                userTestResult.id = key
                userTestsResultsArray.add(userTestResult)


                callback.onSaveSuccess()
                return
            }
        }
        callback.onSaveFailure()
    }

    override fun updateUserTestResult(userTestResult: UserTestResult, callback: UserTestResultSavingCallback) {

        if(removeUserTestResult(userTestResult)){
            saveUserTestResult(userTestResult, callback)
        } else{
            callback.onSaveFailure()
        }
    }

    private fun removeUserTestResult(userTestResult: UserTestResult) : Boolean{

        Log.d("RemovingUserTestResult " , "${userTestResult.id} ${userTestResult.bloodTestName}")
        var counter = 0;
        var toRemove = -1;
        for( res in userTestsResultsArray){
            if(res.id.equals(userTestResult.id)){
                toRemove = counter
                break
            }
            counter++
        }

        if(toRemove >= 0){
            userTestsResultsArray.removeAt(toRemove)
            userId = firebaseAuth.currentUser!!.uid
            var userEndpoint = databaseReference.child(USERS_NODE).child(
                userId
            )
            userEndpoint.child(TESTS_RESULTS_NODE).child(userTestResult.id).removeValue()
            return true

        }
        return false
    }

    private fun writeTestResult(databaseReference: DatabaseReference, userTestResult: UserTestResult){
        databaseReference.child(DATE).setValue(userTestResult.dateMillis)
        databaseReference.child(FAVORITE).setValue(userTestResult.favorite)
        databaseReference.child(BLOODTEST_NAME).setValue(userTestResult.bloodTestName)
        databaseReference.child(RESULT_TYPE).setValue(userTestResult.resultType)
        databaseReference.child(RESULT).setValue(userTestResult.result)
        databaseReference.child(UNIT).setValue(userTestResult.unit)
        databaseReference.child(NOTES).setValue(userTestResult.note)
    }

    override fun deleteUserTestResult(userTestResult: UserTestResult, callback: UserTestResultSavingCallback) {
        if(removeUserTestResult(userTestResult)){
            callback.onSaveSuccess()
        }else{
            callback.onSaveFailure()
        }
    }
//
//    fun areEqual(u1: UserTestResult, u2: UserTestResult) : Boolean{
//        if( u1.bloodTestName.equals(u2.bloodTestName) &&
//            u1.dateMillis.equals(u2.dateMillis) &&
//            u1.result.equals(u2.result) &&
//            u1.unit.equals(u2.unit) &&
//            u1.resultType.equals(u2.resultType) &&
//            u1.note.equals(u2.note) &&
//            u1.favorite.equals(u2.favorite))
//            return true
//
//        return false
//    }
//
//    fun removeResult(testID : String) : Boolean{
//        if(firebaseAuth.currentUser != null){
//            userId = firebaseAuth.currentUser!!.uid
//            var userEndpoint = databaseReference.child(USERS_NODE).child(
//                userId
//            )
//
//            var key = userEndpoint.child(TESTS_RESULTS_NODE).child(testID)
//            if (key != null) {
//                key.ref.removeValue()
//                return true
//            }
//        }
//        return false
//    }
//
//

    override fun getUserTestsResults(callback: UserTestResultsGetterCallback) {

        Log.d("USerTestsResultDB 0 " , "${userTestsResultsArray.size} $uploaded")
        if(userTestsResultsArray.size > 0 && uploaded){
            callback.onTestResultRetrievalSuccess(userTestsResultsArray)
            return
        }else{
            uploaded = true
            var userTestResults1 = arrayListOf<UserTestResult>()

            userId = firebaseAuth.currentUser!!.uid
            var userEndpoint = databaseReference.child(USERS_NODE).child(
                userId
            ).child(TESTS_RESULTS_NODE)


            userEndpoint.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for(snapshot in dataSnapshot.children){
                        userTestResults1.add(userTestResultFromDataSnapshot(snapshot))
                    }
                    uploaded = true
                    userTestsResultsArray = userTestResults1
                    Log.d("USerTestsResultDB 1 " , "${userTestsResultsArray.size}")
                    callback.onTestResultRetrievalSuccess(userTestsResultsArray)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback.onTestResultRetrievalFailure()
                }
            })
        }


    }


    fun userTestResultFromDataSnapshot(dataSnapshot: DataSnapshot) : UserTestResult{
        var userTestResult = UserTestResult()
        for(snap in dataSnapshot.children){
            var key = snap.key

            when(snap.key){
                DATE -> userTestResult.dateMillis = snap.getValue(Long::class.java)!!
                FAVORITE -> userTestResult.favorite = snap.getValue(Int::class.java)!!
                BLOODTEST_NAME -> userTestResult.bloodTestName = snap.getValue(String::class.java)!!
                NOTES -> userTestResult.note = snap.getValue(String::class.java)!!
                RESULT_TYPE -> userTestResult.resultType = snap.getValue(Int::class.java)!!
                UNIT -> userTestResult.unit = snap.getValue(String::class.java)!!
                RESULT -> userTestResult.result = snap.getValue(Float::class.java)!!
            }
            Log.d("child1", "child1 key  " + key + "  val " + snap.getValue(Any::class.java))
        }
        userTestResult.id = dataSnapshot.key!!
        return userTestResult
    }



    fun getGlobalPermission(){

    }



}