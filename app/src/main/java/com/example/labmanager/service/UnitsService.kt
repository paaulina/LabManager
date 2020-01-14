package com.example.labmanager.service

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
        val resultList = arrayListOf<UserTestResult>()

        for(result in group.resultsList){
            if(result.unit.split("/").size < 2) return arrayListOf()
            val firstUnit = result.unit.split("/")[0]
            val secondUnit = result.unit.split("/")[1]

            val firstMultiply = getFirstUnitMultiplayer(targetFirstUnit, firstUnit)
            val secondMultiply = getSecondUnitMultiplayer(targetSecondUnit, secondUnit)

            if(firstMultiply < 0 || secondMultiply < 0){
                return arrayListOf()
            }

            val newResult = UserTestResult(result)
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

        val valuesHashMap = when {
            FIRST_UNIT_MOL.containsKey(targetUnit) -> FIRST_UNIT_MOL
            FIRST_UNIT_IU.containsKey(targetUnit) -> FIRST_UNIT_IU
            FIRST_UNIT_GRAM.containsKey(targetUnit) -> FIRST_UNIT_GRAM
            else -> return -1.0
        }

        if(valuesHashMap.containsKey(unit)){
            val x1 = (valuesHashMap.getValue(unit)).toDouble()
            val x2 = valuesHashMap.getValue(targetUnit).toDouble()
            return (1/x1)*x2
        }
        return -1.0
    }

    private fun getSecondUnitMultiplayer(targetUnit: String, unit: String) : Double{

        if (targetUnit.equals(unit))
            return 1.0

        val valuesHashMap = if(SECOND_UNIT_ML.containsKey(targetUnit)){
            SECOND_UNIT_ML
        } else{
            return -1.0
        }

        if(valuesHashMap.containsKey(unit)){
            val x1 = valuesHashMap.getValue(unit)
            val x2 = valuesHashMap.getValue(targetUnit)
            return (1/x1)*x2
        }
        return -1.0
    }


    fun getDisplayableStringFromConvertedResult(resultValue: Float) : String{
        if(resultValue.toString().contains('E')){
            return (resultValue.toBigDecimal()).toPlainString()
        }
        return resultValue.toString()
    }
}

