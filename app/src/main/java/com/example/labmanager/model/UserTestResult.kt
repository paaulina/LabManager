package com.example.labmanager.model



data class UserTestResult(
    var id: String = "",
    var dateMillis : Long = 0,
    var result: Float = 0f,
    var note: String = "",
    var unit : String = "",
    var favorite : Int = 0,
    var resultType : Int = 0,
    var bloodTestName: String = ""
) {
    constructor(
        dateMillis : Long = 0,
        result: Float = 0f,
        note: String = "",
        unit : String = "",
        favorite : Int = 0,
        resultType : Int = 0,
        bloodTestName: String = ""
    ) : this ("", dateMillis, result, note, unit, favorite, resultType, bloodTestName)

}