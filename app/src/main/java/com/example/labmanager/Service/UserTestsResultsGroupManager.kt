package com.example.labmanager.Service

import android.util.Log
import com.example.labmanager.AUTO_GENERATED
import com.example.labmanager.Model.TestsGroup
import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.RESULT_TYPE_NUMERIC

class UserTestsResultsGroupManager (var resultsLists : ArrayList<UserTestResult>){

    var sorted = false;

    fun getPreDifinedGroups() : ArrayList<TestsGroup>{
        var list = resultsLists
        var groups = arrayListOf<TestsGroup>()

        for(i in 0 .. list.size){
            var item = list.get(i)
            var groupList = arrayListOf<UserTestResult>()
            if(item.resultType == RESULT_TYPE_NUMERIC)
                groupList.add(item)

            for(j in i+1 .. list.size ){
                while(j < list.size && item.bloodTestName.equals(list.get(j).bloodTestName) ){
                    if(list.get(j).resultType == RESULT_TYPE_NUMERIC) {
                        groupList.add(list.get(j))
                        list.removeAt(j)
                    }
                }
            }

            if(groupList.size > 1)
                groups.add(TestsGroup(groupList.get(0).bloodTestName, groupList, AUTO_GENERATED))
        }

        return groups
    }


    fun getGroupNamesArray(list: ArrayList<TestsGroup>) : ArrayList<String>{
        var array = arrayListOf<String>()
        for(item in list){
            array.add(item.groupName)
        }
        return array
    }

    fun sort(){
        sorted = true
        for(i in 0 .. resultsLists.size - 2){
            for(j in 0 .. resultsLists.size - i - 2){
                if(resultsLists.get(j).bloodTestName > resultsLists.get(j+1).bloodTestName){
                    var tmp = resultsLists.get(j)
                    resultsLists.set(j, resultsLists.get(j+1))
                    resultsLists.set(j+1, tmp)
                }
            }
        }
    }

    fun sortByDate(resultsLists1: ArrayList<UserTestResult>) : ArrayList<UserTestResult>{
        sorted = true
        for(i in 0 .. resultsLists1.size - 2){
            for(j in 0 .. resultsLists1.size - i - 2){
                if(resultsLists1.get(j).dateMillis > resultsLists1.get(j+1).dateMillis){
                    var tmp = resultsLists1.get(j)
                    resultsLists1.set(j, resultsLists1.get(j+1))
                    resultsLists1.set(j+1, tmp)
                }
            }
        }

        return  resultsLists1
    }

    fun getGroupedByName() : ArrayList<TestsGroup>{
        var groupList = arrayListOf<TestsGroup>()
        var allUserResults = resultsLists

        if(!sorted) sort()

        var currGroup = TestsGroup("", arrayListOf(), 0)
        for(result in resultsLists){
            if(result.resultType == RESULT_TYPE_NUMERIC){
                Log.d("ResultLog: " , result.bloodTestName + " time : ${result.dateMillis}")
                if(result.bloodTestName.equals(currGroup.groupName)){
                    currGroup.resultsList.add(result)
                    Log.d("ResultAddingChart: " , " ${result.bloodTestName} ${result.dateMillis} ${result.result}")

                } else{
                    if (currGroup.resultsList.size > 1){
                        currGroup.resultsList = sortByDate(currGroup.resultsList)
                        groupList.add(currGroup)
                        Log.d("ResultLog group: " , "${currGroup.groupName}  ${currGroup.resultsList.get(0).dateMillis}  ${currGroup.resultsList.get(1).dateMillis}")
                    }
                    currGroup = TestsGroup(result.bloodTestName, arrayListOf<UserTestResult>(result), AUTO_GENERATED)
                }
            }
        }

        Log.d("Groupm" , "$groupList")
        return groupList
    }

}