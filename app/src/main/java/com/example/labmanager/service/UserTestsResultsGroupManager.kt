package com.example.labmanager.service

import android.util.Log
import com.example.labmanager.AUTO_GENERATED
import com.example.labmanager.AUTO_GENERATED_NO_CHART
import com.example.labmanager.model.TestsGroup
import com.example.labmanager.model.UserTestResult
import com.example.labmanager.RESULT_TYPE_NUMERIC

class UserTestsResultsGroupManager (private var resultsLists : ArrayList<UserTestResult>){

    private var sorted = false

    fun getGroupNamesArray(list: ArrayList<TestsGroup>) : ArrayList<String>{
        val array = arrayListOf<String>()
        for(item in list){
            array.add(item.groupName)
        }
        return array
    }

    private fun sort(){
        sorted = true
        for(i in 0 .. resultsLists.size - 2){
            for(j in 0 .. resultsLists.size - i - 2){
                if(resultsLists[j].bloodTestName > resultsLists[j+1].bloodTestName){
                    val tmp = resultsLists[j]
                    resultsLists[j] = resultsLists[j+1]
                    resultsLists[j+1] = tmp
                }
            }
        }
    }

    fun sortByDate(resultsLists1: ArrayList<UserTestResult>) : ArrayList<UserTestResult>{
        sorted = true
        for(i in 0 .. resultsLists1.size - 2){
            for(j in 0 .. resultsLists1.size - i - 2){
                if(resultsLists1[j].dateMillis > resultsLists1[j+1].dateMillis){
                    val tmp = resultsLists1[j]
                    resultsLists1[j] = resultsLists1[j+1]
                    resultsLists1[j+1] = tmp
                }
            }
        }

        return  resultsLists1
    }

    private fun sortedDatesAreUnique(resultsLists1: ArrayList<UserTestResult>) : Boolean{
        var lastDate : Long = 0
        for(result in resultsLists1){
            if(lastDate > 0){
                if(result.dateMillis == lastDate){
                    return false
                }
            }
            lastDate = result.dateMillis
        }
        return true
    }

    fun getGroupedByName() : ArrayList<TestsGroup>{
        val groupList = arrayListOf<TestsGroup>()

        if(!sorted) sort()

        var currGroup = TestsGroup("", arrayListOf(), 0)
        for(result in resultsLists){

            if(result.bloodTestName == currGroup.groupName){
                currGroup.resultsList.add(result)
                if(result.resultType != RESULT_TYPE_NUMERIC){
                    currGroup.groupType = AUTO_GENERATED_NO_CHART
                }
            } else{
                if (currGroup.resultsList.size > 1){
                    currGroup.resultsList = sortByDate(currGroup.resultsList)
                    if(!sortedDatesAreUnique(currGroup.resultsList)){
                        currGroup.groupType = AUTO_GENERATED_NO_CHART
                    }
                    groupList.add(currGroup)
                }
                currGroup = TestsGroup(result.bloodTestName, arrayListOf(result), AUTO_GENERATED)
                if(result.resultType != RESULT_TYPE_NUMERIC){
                    currGroup.groupType = AUTO_GENERATED_NO_CHART
                }
            }

            if(currGroup.groupType == AUTO_GENERATED){
                var unified = UnitsService().unifyNumGroupUnits(currGroup)
                if(unified.isEmpty()){
                    currGroup.groupType = AUTO_GENERATED_NO_CHART
                } else {
                    currGroup.resultsList = unified
                }
            }
        }



        if (currGroup.resultsList.size > 1){
            currGroup.resultsList = sortByDate(currGroup.resultsList)
            if(!sortedDatesAreUnique(currGroup.resultsList)){
                currGroup.groupType = AUTO_GENERATED_NO_CHART
            }
            groupList.add(currGroup)
         }


        return groupList
    }

}