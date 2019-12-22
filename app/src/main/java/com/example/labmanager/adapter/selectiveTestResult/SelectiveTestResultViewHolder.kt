package com.example.labmanager.adapter.selectiveTestResult


import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labmanager.*
import com.example.labmanager.model.UserTestResult
import com.example.labmanager.service.DateManager
import kotlinx.android.synthetic.main.test_result_list_item_selective.view.*

class SelectiveTestResultViewHolder (itemView: View) :
    RecyclerView.ViewHolder(itemView) {


    private val resultNameTextView: TextView = itemView.testNameTextView
    private val resultDateTextView: TextView = itemView.dateTextView
    private val resultNumericTextView: TextView = itemView.resultValTextView
    private val checkBox : CheckBox = itemView.checkBoxSelect



    fun bind(position: Int, userTestResult: UserTestResult, isSelected: Boolean, callback: CheckBoxSelectionCallback){
        resultNameTextView.text = userTestResult.bloodTestName
        resultDateTextView.text = DateManager.dateMillisToStringDate(userTestResult.dateMillis)

        when(userTestResult.resultType){
            RESULT_TYPE_NUMERIC -> {
                resultNumericTextView.text = userTestResult.result.toString()
            }
            RESULT_TYPE_POSITIVE_NEGATIVE -> {
                when(userTestResult.result){
                    NEGATIVE_RESULT -> {
                        resultNumericTextView.text = "N"
                    }
                    POSITIVE_RESULT -> {
                        resultNumericTextView.text = "P"
                    }
                    else -> {
                        resultNumericTextView.text = ""
                    }
                }
            }
            RESULT_TYPE_DESC -> {
                resultNumericTextView.visibility = View.INVISIBLE
            }
        }

        checkBox.isChecked = isSelected
        checkBox.setOnClickListener {
            if(checkBox.isChecked){
                checkBox.isChecked = true
                callback.checkboxAtPositionSelected(position)
            }else{
                checkBox.isChecked = false
                callback.checkboxAtPositionDeselected(position)
            }
        }

    }


}