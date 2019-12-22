package com.example.labmanager.service

interface ItemClickedCallback{
    fun itemAtPositionSelected(position: Int)
    fun itemAtPositionLongClicked(position: Int)
}