package com.example.labmanager.dataBase.usecase.userData.ProfileData

interface UserDataCallback {

    fun onSuccessfulNodeCreation()
    fun onFailureNodeCreation()
    fun onPermissionRetrieval(permission: Int)
    fun onPermissionRetrievalFailure()
    fun onPermissionSwitchSuccess()
    fun onPermissionSwitchFailure()
}