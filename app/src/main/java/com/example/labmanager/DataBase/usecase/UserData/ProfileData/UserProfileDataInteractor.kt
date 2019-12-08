package com.example.labmanager.DataBase.usecase.UserData.ProfileData

import com.example.labmanager.DataBase.usecase.UserData.Gateway.UserDataGateway
import com.example.labmanager.DataBase.usecase.UserData.ProfileData.UserDataCallback

class UserProfileDataInteractor(
    private val userDataGatewey: UserDataGateway
) : UserDataCallback {
    override fun onSuccessfulNodeCreation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFailureNodeCreation() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNodeAvailable() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onNodeUnavailable() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionRetrieval(permission: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionRetrievalFailure() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionSwitchSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionSwitchFailure() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}