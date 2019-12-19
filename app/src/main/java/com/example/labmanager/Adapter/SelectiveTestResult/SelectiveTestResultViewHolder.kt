package com.example.labmanager.Adapter.SelectiveTestResult


import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labmanager.*
import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.Service.DateManager
import kotlinx.android.synthetic.main.test_result_list_item.view.*
import kotlinx.android.synthetic.main.test_result_list_item_selective.view.*

class SelectiveTestResultViewHolder (itemView: View) :
    RecyclerView.ViewHolder(itemView) {


    val resultNameTextView: TextView = itemView.testNameTextView
    val resultDateTextView: TextView = itemView.dateTextView
    val resultNumericTextView: TextView = itemView.resultValTextView
    val checkBox : CheckBox = itemView.checkBoxSelect



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

        if(isSelected){
            checkBox.isChecked = true
        }else{
            checkBox.isChecked = false
        }
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