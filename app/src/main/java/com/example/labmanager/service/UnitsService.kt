package com.example.labmanager.service

import android.util.Log
import com.example.labmanager.*
import com.example.labmanager.model.TestsGroup
import com.example.labmanager.model.UserTestResult

class UnitsService{

    fun unifyNumGroupUnits(group: TestsGroup) : ArrayList<UserTestResult>{

        if(!allNumeric(group) ||
            group.resultsList.isEmpty() ||
            group.resultsList[0].unit.isEmpty() ||
            group.resultsList[0].unit.split("/").size < 2
        ) return arrayListOf()


        val targetFirstUnit = group.resultsList[0].unit.split("/")[0]
        val targetSecondUnit = group.resultsList[0].unit.split("/")[1]
        var resultList = arrayListOf<UserTestResult>()

        for(result in group.resultsList){
            if(result.unit.split("/").size < 2) return arrayListOf()
            val firstUnit = result.unit.split("/")[0]
            val secondUnit = result.unit.split("/")[1]

            var firstMultiply = getFirstUnitMultiplayer(targetFirstUnit, firstUnit)
            var secondMultiply = getSecondUnitMultiplayer(targetSecondUnit, secondUnit)

            if(firstMultiply < 0 || secondMultiply < 0){
                return arrayListOf()
            }

            var newResult = UserTestResult(result)
            newResult.result = ((result.result*firstMultiply)/secondMultiply).toFloat()
            newResult.unit = "$targetFirstUnit/$targetSecondUnit"

            resultList.add(newResult)
        }
        return resultList
    }

    private fun allNumeric(group: TestsGroup): Boolean{
        for(result in group.resultsList){
            if(result.resultType != RESULT_TYPE_NUMERIC)
                return false
        }
        return true
    }

    private fun getFirstUnitMultiplayer(targetUnit: String, unit: String) : Double{

        if (targetUnit.equals(unit))
            return 1.0

        Log.d("Changing123", "changing $unit to $targetUnit ");

        var valuesHashMap = if(FIRST_UNIT_MOL.containsKey(targetUnit)){
            FIRST_UNIT_MOL
        } else if(FIRST_UNIT_IU.containsKey(targetUnit)){
            FIRST_UNIT_IU
        } else if(FIRST_UNIT_GRAM.containsKey(targetUnit)){
            FIRST_UNIT_GRAM
        } else{
            return -1.0
        }

        Log.d("Changing123", "changing $unit to $targetUnit  finish ")
        if(valuesHashMap.containsKey(unit)){
            var x1 = (valuesHashMap.getValue(unit)).toDouble()
            var x2 = valuesHashMap.getValue(targetUnit).toDouble()
            return (1/x1)*x2
        }
        return -1.0
    }

    private fun getSecondUnitMultiplayer(targetUnit: String, unit: String) : Double{

        if (targetUnit.equals(unit))
            return 1.0

        var valuesHashMap = if(SECOND_UNIT_ML.containsKey(targetUnit)){
            SECOND_UNIT_ML
        } else{
            return -1.0
        }

        if(valuesHashMap.containsKey(unit)){
            var x1 = valuesHashMap.getValue(unit) as Double
            var x2 = valuesHashMap.getValue(targetUnit) as Double
            return (1/x1)*x2
        }
        return -1.0
    }
}

