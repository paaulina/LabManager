package com.example.labmanager.Adapter.SelectiveTestResult

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.labmanager.Adapter.TestResultViewHolder
import com.example.labmanager.Model.TestsGroup
import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.R
import com.example.labmanager.Service.ItemClickedCallback

class SelectiveTestResultAdapter (
    val resultsArray: ArrayList<UserTestResult>,
    val testsGroup: TestsGroup,
    val context: Context
) : RecyclerView.Adapter<SelectiveTestResultViewHolder>(),
    CheckBoxSelectionCallback{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectiveTestResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.test_result_list_item_selective, parent, false)
        val viewholder = SelectiveTestResultViewHolder(view)

        return viewholder
    }

    override fun getItemCount(): Int {
        return resultsArray.size
    }

    override fun onBindViewHolder(holder: SelectiveTestResultViewHolder, position: Int) {
        holder.bind(position, resultsArray.get(position), isInGroup(position), this)
    }

    fun isInGroup(position: Int) : Boolean{
        if(getPositionAtGroup(position) >= 0)
            return true
        return false
    }

    fun getPositionAtGroup(positionGeneral: Int) : Int{
        var result = resultsArray.get(positionGeneral)
        for(i in 0 .. testsGroup.resultsList.size-1){
            if(testsGroup.resultsList.get(i).id.equals(result.id))
                return i
        }
        return -1
    }

    override fun checkboxAtPositionSelected(position: Int) {
        testsGroup.resultsList.add(resultsArray.get(position))
    }

    override fun checkboxAtPositionDeselected(position: Int) {
        testsGroup.resultsList.removeAt(getPositionAtGroup(position))

    }

    fun getFinalTestResultsArray(): TestsGroup{
        return testsGroup
    }

}


