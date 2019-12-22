package com.example.labmanager.adapter.selectiveTestResult

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.labmanager.model.TestsGroup
import com.example.labmanager.model.UserTestResult
import com.example.labmanager.R

class SelectiveTestResultAdapter (
    private val resultsArray: ArrayList<UserTestResult>,
    val testsGroup: TestsGroup,
    val context: Context
) : RecyclerView.Adapter<SelectiveTestResultViewHolder>(),
    CheckBoxSelectionCallback{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectiveTestResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_result_list_item_selective, parent, false)

        return SelectiveTestResultViewHolder(view)
    }

    override fun getItemCount(): Int {
        return resultsArray.size
    }

    override fun onBindViewHolder(holder: SelectiveTestResultViewHolder, position: Int) {
        holder.bind(position, resultsArray[position], isInGroup(position), this)
    }

    private fun isInGroup(position: Int) : Boolean{
        if(getPositionAtGroup(position) >= 0)
            return true
        return false
    }

    private fun getPositionAtGroup(positionGeneral: Int) : Int{
        val result = resultsArray[positionGeneral]
        for(i in 0 until testsGroup.resultsList.size){
            if(testsGroup.resultsList[i].id == result.id)
                return i
        }
        return -1
    }

    override fun checkboxAtPositionSelected(position: Int) {
        testsGroup.resultsList.add(resultsArray[position])
    }

    override fun checkboxAtPositionDeselected(position: Int) {
        testsGroup.resultsList.removeAt(getPositionAtGroup(position))

    }
}


