package com.example.labmanager.dataBase.usecase.userData.ProfileData

interface UserGlobalPermissionPresenter {

    fun presentGlobalPersmission(permission: Int)
    fun presentPermissionRetrievalFailure()
}