package com.example.labmanager.Model

import com.example.labmanager.USER_GENERATED

class TestsGroup (
    var groupId: String = "",
    var groupName: String = "",
    var resultsList: ArrayList<UserTestResult> = arrayListOf(),
    var groupType: Int = USER_GENERATED
){
    constructor(groupName: String, resultsList: ArrayList<UserTestResult>, groupType: Int)
        : this("", groupName, resultsList, groupType)

    constructor(groupId: String, groupName: String, resultsList: ArrayList<UserTestResult>)
     : this(groupId, groupName, resultsList, USER_GENERATED)

    constructor(groupName: String, resultsList: ArrayList<UserTestResult>)
     : this("", groupName, resultsList, USER_GENERATED)

}