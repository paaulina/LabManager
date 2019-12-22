package com.example.labmanager.adapter.selectiveTestResult

interface CheckBoxSelectionCallback {
    fun checkboxAtPositionSelected(position : Int)
    fun checkboxAtPositionDeselected(position : Int)
}