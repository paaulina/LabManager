package com.example.labmanager.Service

import com.example.labmanager.IS_FAVORITE
import com.example.labmanager.Model.UserTestResult

object TestResultsFilter {
    fun sortByDate(input : ArrayList<UserTestResult>) : ArrayList<UserTestResult>{
        input.sortWith(compareBy({it.dateMillis}, {it.bloodTestName}))
//        var sorted = arrayListOf<UserTestResult>()
//        for(obj in input){
//            sorted.add(obj)
//        }
        return input
    }

    fun sortByName(input : ArrayList<UserTestResult>) : ArrayList<UserTestResult>{
        input.sortWith(compareBy({it.bloodTestName}, {it.dateMillis}))
        return input
    }

    fun sortByFavourites(input : ArrayList<UserTestResult>) : ArrayList<UserTestResult>{
        var sortedByName = sortByName(input)
        var output = arrayListOf<UserTestResult>()
        for(res in sortedByName){
            when(res.favorite){
                IS_FAVORITE -> {
                    output.add(res)
                    sortedByName.remove(res)
                }
            }
        }
        for(res in sortedByName){
            output.add(res)
        }
        return output
    }

    fun filterByDateFrom(dateFrom : Long, input : ArrayList<UserTestResult>) : ArrayList<UserTestResult>{
        var output = arrayListOf<UserTestResult>()
        for(res in input){
            if(res.dateMillis >= dateFrom)
                output.add(res)
        }
        return output
    }

    fun filterByDateTo(dateTo: Long, input : ArrayList<UserTestResult>) : ArrayList<UserTestResult>{
        var output = arrayListOf<UserTestResult>()
        for(res in input){
            if(res.dateMillis <= dateTo)
                output.add(res)
        }
        return output
    }

    fun filterByName(name : String, input : ArrayList<UserTestResult>) : ArrayList<UserTestResult>{
        var output = arrayListOf<UserTestResult>()
        for(res in input){
            if(res.bloodTestName.toLowerCase().contains(name.toLowerCase()))
                output.add(res)
        }
        return output
    }


}