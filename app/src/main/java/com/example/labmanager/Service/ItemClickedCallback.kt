package com.example.labmanager.Service

interface ItemClickedCallback{
    fun itemAtPositionSelected(position: Int)
    fun itemAtPositionLongClicked(position: Int)
}