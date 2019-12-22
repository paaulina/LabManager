package com.example.labmanager.service

import com.example.labmanager.model.UserTestResult

object TestResultsFilter {
    fun sortByDate(input : ArrayList<UserTestResult>) : ArrayList<UserTestResult>{
        input.sortWith(compareBy({it.dateMillis}, {it.bloodTestName}))
        return input
    }

    fun sortByName(input : ArrayList<UserTestResult>) : ArrayList<UserTestResult>{
        input.sortWith(compareBy({it.bloodTestName}, {it.dateMillis}))
        return input
    }


    fun filterByDateFrom(dateFrom : Long, input : ArrayList<UserTestResult>) : ArrayList<UserTestResult>{
        val output = arrayListOf<UserTestResult>()
        for(res in input){
            if(res.dateMillis >= dateFrom)
                output.add(res)
        }
        return output
    }

    fun filterByDateTo(dateTo: Long, input : ArrayList<UserTestResult>) : ArrayList<UserTestResult>{
        val output = arrayListOf<UserTestResult>()
        for(res in input){
            if(res.dateMillis <= dateTo)
                output.add(res)
        }
        return output
    }

    fun filterByName(name : String, input : ArrayList<UserTestResult>) : ArrayList<UserTestResult>{
        val output = arrayListOf<UserTestResult>()
        for(res in input){
            if(res.bloodTestName.toLowerCase().contains(name.toLowerCase()))
                output.add(res)
        }
        return output
    }


}