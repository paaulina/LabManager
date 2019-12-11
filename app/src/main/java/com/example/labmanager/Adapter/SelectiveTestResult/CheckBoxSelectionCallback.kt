package com.example.labmanager.Adapter.SelectiveTestResult

interface CheckBoxSelectionCallback {
    fun checkboxAtPositionSelected(position : Int)
    fun checkboxAtPositionDeselected(position : Int)
}