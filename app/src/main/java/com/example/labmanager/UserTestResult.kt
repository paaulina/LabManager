package com.example.labmanager

data class UserTestResult(
    var dateInMillis : Int,
    var bloodTestId : Int,
    var bloodTestName: String,
    var result: Float,
    var unit : String,
    var note: String
)