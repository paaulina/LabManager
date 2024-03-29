package com.example.labmanager.model

import com.example.labmanager.service.Hash

class AnonymousTestResult(
    var hashedId : String,
    var bloodTest : String,
    var result: Float,
    var unit: String,
    var dateMillis: Long
){
    constructor(userId: String, userTestResult: UserTestResult): this(
        Hash.hashString(userId),
        userTestResult.bloodTestName,
        userTestResult.result,
        userTestResult.unit,
        userTestResult.dateMillis
    )
}