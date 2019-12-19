package com.example.labmanager.DataBase.usecase.UserData.ProfileData

import com.example.labmanager.DataBase.usecase.UserData.Gateway.UserDataGateway
import com.example.labmanager.DataBase.usecase.UserData.ProfileData.UserDataCallback
import com.example.labmanager.GLOBAL_ALLOWED
import com.example.labmanager.GLOBAL_DECLINED

class UserProfileDataInteractor(
    private val userDataGatewey: UserDataGateway
) : UserDataCallback {

    lateinit var nodeCreationPresenter : UserNodeCreationPresenter
    fun createUserNode(userID: String, name: String, gender: String, presenter: UserNodeCreationPresenter){
        nodeCreationPresenter = presenter
        userDataGatewey.createUserNode(userID, name, gender, GLOBAL_ALLOWED, this)

    }
    override fun onSuccessfulNodeCreation() {
        nodeCreationPresenter.onNodeCreationSuccess()
    }

    override fun onFailureNodeCreation() {
        nodeCreationPresenter.onNodeCreationFailure()
    }


    lateinit var permissionPresenter : UserGlobalPermissionPresenter
    fun getGlobalPermission(userGlobalPermissionPresenter: UserGlobalPermissionPresenter){
        permissionPresenter = userGlobalPermissionPresenter
        userDataGatewey.getGlobalPermission(this)
    }
    override fun onPermissionRetrieval(permission: Int) {
        permissionPresenter.presentGlobalPersmission(permission)
    }

    override fun onPermissionRetrievalFailure() {
        permissionPresenter.presentPermissionRetrievalFailure()
    }

    fun switchGlobal(currentGlobal: Int) {
        when(currentGlobal){
            GLOBAL_ALLOWED -> userDataGatewey.disableGlobal(this)
            GLOBAL_DECLINED -> userDataGatewey.allowGlobal(this)
        }

    }

    override fun onPermissionSwitchSuccess() {}

    override fun onPermissionSwitchFailure() {}

}