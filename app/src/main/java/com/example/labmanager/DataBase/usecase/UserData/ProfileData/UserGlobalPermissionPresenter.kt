package com.example.labmanager.DataBase.usecase.UserData.ProfileData

interface UserGlobalPermissionPresenter {

    fun presentGlobalPersmission(permission: Int)
    fun presentPermissionRetrievalFailure()
}