package com.example.labmanager.DataBase.DataBaseEntry

import android.util.Log
import com.example.labmanager.*
import com.example.labmanager.DataBase.usecase.UserData.TestResults.UserTestResultSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.Gateway.UserDataGateway
import com.example.labmanager.DataBase.usecase.UserData.ProfileData.UserDataCallback
import com.example.labmanager.DataBase.usecase.UserData.TestGruops.TestGroupsGetterCallback
import com.example.labmanager.DataBase.usecase.UserData.TestGruops.TestGroupsSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.TestResults.UserTestResultsGetterCallback
import com.example.labmanager.Model.TestsGroup
import com.example.labmanager.Model.UserTestResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

object UserDataDBEntry : UserDataGateway {

    private var databaseReference = FirebaseDatabase.getInstance().reference
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var userId = ""
    private var userTestsResultsArray = arrayListOf<UserTestResult>()
    private var userResultsUploaded = false
    private var userTestsGroupsArray = arrayListOf<TestsGroup>()
    private var userGroupsUploaded = false


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


    override fun getUserTestsResults(callback: UserTestResultsGetterCallback) {

        Log.d("USerTestsResultDB 0 " , "${userTestsResultsArray.size} $userResultsUploaded")
        if(userTestsResultsArray.size > 0 && userResultsUploaded){
            callback.onTestResultRetrievalSuccess(userTestsResultsArray)
            return
        }else{
            userResultsUploaded = true
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
                    userResultsUploaded = true
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



    override fun createUserNode(
        name: String,
        gender: String,
        globalPermission: Int,
        callback: UserDataCallback
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkUserNode(callback: UserDataCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGlobalPermission(callback: UserDataCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun allowGlobal(callback: UserDataCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun disableGlobal(callback: UserDataCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun getUserTestsGroup(callback: TestGroupsGetterCallback) {

        if(userTestsGroupsArray.size > 0 && userGroupsUploaded){
            callback.oGroupsRetrievalSuccess(userTestsGroupsArray)
            return
        }else{
            userGroupsUploaded= true
            var groups = arrayListOf<TestsGroup>()

            userId = firebaseAuth.currentUser!!.uid
            var userEndpoint = databaseReference.child(USERS_NODE).child(
                userId
            ).child(TESTS_GROUPS_NODE)


            userEndpoint.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for(snapshot in dataSnapshot.children){
                        groups.add(groupFromDataSnapshot(snapshot))
                    }
                    userGroupsUploaded = true
                    userTestsGroupsArray = groups
                    callback.oGroupsRetrievalSuccess(userTestsGroupsArray)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    callback.onGroupRetrievalFailure()
                }
            })
        }
    }

    fun groupFromDataSnapshot(dataSnapshot: DataSnapshot) : TestsGroup{
        var group = TestsGroup()
        for(snap in dataSnapshot.children){
            var key = snap.key

            when(snap.key){
                TEST_GROUP_NAME -> group.groupName = snap.getValue(String::class.java)!!
                TEST_IDs -> {
                    var resultsIDs = arrayListOf<String>()
                    for(id in snap.children){
                        resultsIDs.add(id.getValue(String::class.java)!!)
                    }

                    group.resultsList = getResultsFromIDs(resultsIDs)
                }
            }

        }
        group.groupId = dataSnapshot.key!!
        return group
    }


    fun getResultsFromIDs(ids: ArrayList<String>) : ArrayList<UserTestResult>{
        var resultsList = arrayListOf<UserTestResult>()
        for(id in ids){
            resultFromId(id)?.let { resultsList.add(it) }
        }
        return resultsList
    }

    fun resultFromId(id:String): UserTestResult?{
        for( result in userTestsResultsArray){
            if(result.id.equals(id))
                return result
        }
        return null
    }

    override fun saveUserTestGroup(testsGroup: TestsGroup, callback: TestGroupsSavingCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateUserTestGroup(testsGroup: TestsGroup, callback: TestGroupsSavingCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteUserTestGroup(testsGroup: TestsGroup, callback: TestGroupsSavingCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }




}