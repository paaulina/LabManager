package com.example.labmanager.dataBase.usecase.globalData

import com.example.labmanager.dataBase.dataBaseEntry.GlobalDataDBEntry
import com.example.labmanager.model.AnonymousTestResult
import com.example.labmanager.model.UserTestResult
import com.example.labmanager.RESULT_TYPE_DESC
import com.google.firebase.auth.FirebaseAuth

object GlobalDataInteractor {

    fun save(userTestResult: UserTestResult){
        if(userPermission()){
            if(userTestResult.resultType != RESULT_TYPE_DESC){

                GlobalDataDBEntry.saveUserTestResult(AnonymousTestResult(
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    userTestResult
                ))
            }
        }
    }

    fun userPermission() : Boolean{
        return true
    }
}