package com.example.labmanager.DataBase.usecase.UserData.ProfileData

interface UserDataCallback {

    fun onSuccessfulNodeCreation()
    fun onFailureNodeCreation()
    fun onNodeAvailable()
    fun onNodeUnavailable()
    fun onPermissionRetrieval(permission: Int)
    fun onPermissionRetrievalFailure()
    fun onPermissionSwitchSuccess()
    fun onPermissionSwitchFailure()
}