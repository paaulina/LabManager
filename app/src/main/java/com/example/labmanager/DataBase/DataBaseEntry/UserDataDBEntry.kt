package com.example.labmanager.DataBase.DataBaseEntry

import android.graphics.Bitmap
import android.util.Log
import com.example.labmanager.*
import com.example.labmanager.DataBase.usecase.UserData.TestResults.TestResultsSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.Gateway.UserDataGateway
import com.example.labmanager.DataBase.usecase.UserData.MedicalFiles.MedicalFilesIDsRetrievalCallback
import com.example.labmanager.DataBase.usecase.UserData.MedicalFiles.MedicalFilesIDsSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.ProfileData.UserDataCallback
import com.example.labmanager.DataBase.usecase.UserData.TestGruops.TestGroupsRetrievalCallback
import com.example.labmanager.DataBase.usecase.UserData.TestGruops.TestGroupsSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.TestResults.TestResultsRetrievalCallback
import com.example.labmanager.Model.MedicalFile
import com.example.labmanager.Model.TestsGroup
import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.Service.DummyBitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

object UserDataDBEntry : UserDataGateway {

    private var databaseReference = FirebaseDatabase.getInstance().reference
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var userId = ""
    private var userTestsResultsArray = arrayListOf<UserTestResult>()
    private var userTestsGroupsArray = arrayListOf<TestsGroup>()
    private var globalPermission = -1



    override fun saveUserTestResult(userTestResult: UserTestResult, callback: TestResultsSavingCallback) {
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

    override fun updateUserTestResult(userTestResult: UserTestResult, callback: TestResultsSavingCallback) {

        if(removeUserTestResult(userTestResult)){
            saveUserTestResult(userTestResult, callback)
        } else{
            callback.onSaveFailure()
        }
    }

    override fun setFavourite(testID: String, newFavValue: Int, callback: TestResultsSavingCallback) {
        if(firebaseAuth.currentUser != null){
            userId = firebaseAuth.currentUser!!.uid
            databaseReference.child(USERS_NODE)
                             .child(userId)
                             .child(TESTS_RESULTS_NODE)
                             .child(testID)
                             .child(FAVORITE)
                             .setValue(newFavValue)
            callback.onSaveSuccess()
        }
        callback.onSaveFailure()

    }

    private fun removeUserTestResult(userTestResult: UserTestResult) : Boolean{
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

    override fun deleteUserTestResult(userTestResult: UserTestResult, callback: TestResultsSavingCallback) {
        if(removeUserTestResult(userTestResult)){
            callback.onSaveSuccess()
        }else{
            callback.onSaveFailure()
        }
    }


    override fun getUserTestsResults(callback: TestResultsRetrievalCallback) {

        if(userTestsResultsArray.size > 0){
            callback.onTestResultRetrievalSuccess(userTestsResultsArray)
            return
        }

        userId = firebaseAuth.currentUser!!.uid
        var userEndpoint = databaseReference.child(USERS_NODE)
                                                             .child(userId)
                                                             .child(TESTS_RESULTS_NODE)


        userEndpoint.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userTestsResultsArray.clear()
                for(snapshot in dataSnapshot.children){
                    userTestsResultsArray.add(userTestResultFromDataSnapshot(snapshot))
                }
                callback.onTestResultRetrievalSuccess(userTestsResultsArray)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onTestResultRetrievalFailure()
            }
        })

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
        userId: String,
        name: String,
        gender: String,
        globalPermission: Int,
        callback: UserDataCallback
    ) {
        databaseReference.child(USERS_NODE).child(userId).child(USER_NAME).setValue(name)
        databaseReference.child(USERS_NODE).child(userId).child(USER_GENDER).setValue(gender)
        databaseReference.child(USERS_NODE).child(userId).child(USER_GLOBAL_PERMISSION).setValue(globalPermission)
        callback.onSuccessfulNodeCreation()
    }


    override fun getGlobalPermission(callback: UserDataCallback) {
        if(globalPermission >= 0){
            callback.onPermissionRetrieval(globalPermission)
            return
        }

        if(firebaseAuth.currentUser == null){
            callback.onPermissionRetrievalFailure()
            return
        }
        userId = firebaseAuth.currentUser!!.uid
        var userEndpoint = databaseReference.child(USERS_NODE)
            .child(userId).child(USER_GLOBAL_PERMISSION)


        userEndpoint.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                globalPermission = dataSnapshot.getValue(Int::class.java)!!
                callback.onPermissionRetrieval(globalPermission)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onPermissionRetrievalFailure()
            }
        })
    }

    override fun allowGlobal(callback: UserDataCallback) {
        userId = firebaseAuth.currentUser!!.uid
        databaseReference.child(USERS_NODE).child(userId).child(USER_GLOBAL_PERMISSION).setValue(GLOBAL_ALLOWED)

    }

    override fun disableGlobal(callback: UserDataCallback) {
        userId = firebaseAuth.currentUser!!.uid
        databaseReference.child(USERS_NODE).child(userId).child(USER_GLOBAL_PERMISSION).setValue(
            GLOBAL_DECLINED)
    }



    override fun getUserTestsGroup(callback: TestGroupsRetrievalCallback) {

        if(userTestsGroupsArray.size > 0){
            callback.oGroupsRetrievalSuccess(userTestsGroupsArray)
            return
        }
        userId = firebaseAuth.currentUser!!.uid
        var userEndpoint = databaseReference.child(USERS_NODE).child(
            userId
        ).child(TESTS_GROUPS_NODE)


        userEndpoint.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userTestsGroupsArray.clear()
                for(snapshot in dataSnapshot.children){
                    userTestsGroupsArray.add(groupFromDataSnapshot(snapshot))
                }
                callback.oGroupsRetrievalSuccess(userTestsGroupsArray)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onGroupRetrievalFailure()
            }
        })
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
        if(firebaseAuth.currentUser != null){
            userId = firebaseAuth.currentUser!!.uid
            var userEndpoint = databaseReference.child(USERS_NODE).child(
                userId
            )
            var key = userEndpoint.child(TESTS_GROUPS_NODE).push().key
            if (key != null) {
                writeTestGroup(userEndpoint.child(TESTS_GROUPS_NODE).child(key), testsGroup)
                testsGroup.groupId = key
                userTestsGroupsArray.add(testsGroup)
                callback.onSaveSuccess()
                return
            }
        }
        callback.onSaveFailure()
    }

    private fun writeTestGroup(databaseReference: DatabaseReference, testsGroup: TestsGroup){
        databaseReference.child(TEST_GROUP_NAME).setValue(testsGroup.groupName)
        for(i in 0 .. testsGroup.resultsList.size - 1){
            databaseReference.child(TEST_IDs).child(i.toString()).setValue(testsGroup.resultsList.get(i).id)
        }
    }

    override fun updateUserTestGroup(testsGroup: TestsGroup, callback: TestGroupsSavingCallback) {
        userId = firebaseAuth.currentUser!!.uid
        var ref = databaseReference.child(USERS_NODE).child(userId).child(TESTS_GROUPS_NODE).child(testsGroup.groupId)
        ref.removeValue()
        saveUserTestGroup(testsGroup, callback)
    }

    override fun deleteUserTestGroup(testsGroup: TestsGroup, callback: TestGroupsSavingCallback) {
        userId = firebaseAuth.currentUser!!.uid
        var ref = databaseReference.child(USERS_NODE).child(userId).child(TESTS_GROUPS_NODE).child(testsGroup.groupId)
        ref.removeValue()
        callback.onSaveSuccess()
    }



    override fun addMedicalFileID(name: String, callback: MedicalFilesIDsSavingCallback) {
        if(firebaseAuth.currentUser != null){
            userId = firebaseAuth.currentUser!!.uid
            var userEndpoint = databaseReference.child(USERS_NODE).child(
                userId
            )
            var key = userEndpoint.child(MEDICAL_FILES_NODE).push().key
            if (key != null) {
                userEndpoint.child(MEDICAL_FILES_NODE).child(key).setValue(name)
                callback.onSaveSuccess(key)
                return
            }
        }
        callback.onFailure()
    }


    override fun removeMedicalFileID(id: String, callback: MedicalFilesIDsSavingCallback) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllMadicalFilesIDs(callback: MedicalFilesIDsRetrievalCallback) {
        var medicalFiles = arrayListOf<MedicalFile>()

        userId = firebaseAuth.currentUser!!.uid
        var filesEndpoint = databaseReference.child(USERS_NODE)
            .child(userId)
            .child(MEDICAL_FILES_NODE)


        filesEndpoint.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    medicalFiles.add(MedicalFile( snapshot.getValue(String::class.java)!!, snapshot.key!!,DummyBitmap.getEmptyBitmap()))
                }
                callback.onRetrievalSuccess(medicalFiles)

            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback.onRetrievalFailure()
            }
        })

    }




}