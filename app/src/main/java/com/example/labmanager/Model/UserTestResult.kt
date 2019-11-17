package com.example.labmanager.Model

import java.sql.Timestamp

data class UserTestResult(
    var dateMillis : Long,
    var bloodTestName: String,
    var resultType : Int,
    var result: Float,
    var unit : String,
    var note: String
)