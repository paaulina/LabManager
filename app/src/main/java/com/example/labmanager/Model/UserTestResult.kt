package com.example.labmanager.Model

import java.sql.Timestamp

data class UserTestResult(
    var dateMillis : Long = 0,
    var result: Float = 0f,
    var note: String = "",
    var unit : String = "",
    var favorite : Int = 0,
    var resultType : Int = 0,
    var bloodTestName: String = ""


)