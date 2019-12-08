package com.example.labmanager.DataBase.usecase.GlobalData

import com.example.labmanager.DataBase.DataBaseEntry.GlobalDataDBEntry
import com.example.labmanager.Model.AnonymousTestResult
import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.RESULT_TYPE_DESC
import com.example.labmanager.RESULT_TYPE_NUMERIC
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