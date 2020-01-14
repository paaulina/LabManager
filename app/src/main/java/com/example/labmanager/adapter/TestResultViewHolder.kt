package com.example.labmanager.adapter

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labmanager.*
import com.example.labmanager.model.UserTestResult
import com.example.labmanager.service.DateManager
import com.example.labmanager.service.UnitsService
import kotlinx.android.synthetic.main.test_result_list_item.view.*

class TestResultViewHolder (itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val resultNameTextView: TextView = itemView.testResultNameTextView
    private val resultDateTextView: TextView = itemView.testDateTextView
    private val resultNumericTextView: TextView = itemView.resultValueTextView
    private val posNegImageEmpty: ImageView = itemView.imageViewEmpty
    private val posNegImageFilled: ImageView = itemView.imageViewFilled
    private var starButton: ImageButton = itemView.starButton
    private var prestarButton: ImageButton = itemView.prestarButton


    fun bind(userTestResult: UserTestResult){
        resultNameTextView.text = userTestResult.bloodTestName
        resultDateTextView.text = DateManager.dateMillisToStringDate(userTestResult.dateMillis)

        when(userTestResult.resultType){
            RESULT_TYPE_NUMERIC -> {
                resultNumericTextView.visibility = View.VISIBLE
                //resultNumericTextView.text = userTestResult.result.toString()
                resultNumericTextView.text = UnitsService().getDisplayableStringFromConvertedResult(userTestResult.result)
                posNegImageEmpty.visibility = View.INVISIBLE
                posNegImageFilled.visibility = View.INVISIBLE
            }
            RESULT_TYPE_POSITIVE_NEGATIVE -> {
                resultNumericTextView.visibility = View.VISIBLE
                posNegImageEmpty.visibility = View.INVISIBLE
                posNegImageFilled.visibility = View.INVISIBLE

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
                posNegImageEmpty.visibility = View.INVISIBLE
                posNegImageFilled.visibility = View.INVISIBLE
                resultNumericTextView.visibility = View.INVISIBLE
            }
        }

        when(userTestResult.favorite){
            IS_FAVORITE -> {
                starButton.visibility = View.VISIBLE
                prestarButton.visibility = View.INVISIBLE
            }
            NOT_FAVORITE -> {
                starButton.visibility = View.INVISIBLE
                prestarButton.visibility = View.VISIBLE
            }
        }
    }
}