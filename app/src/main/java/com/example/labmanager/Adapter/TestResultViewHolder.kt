package com.example.labmanager.Adapter

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labmanager.*
import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.Service.DateManager
import kotlinx.android.synthetic.main.test_result_list_item.view.*

class TestResultViewHolder (itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    val resultNameTextView: TextView = itemView.testResultNameTextView
    val resultDateTextView: TextView = itemView.testDateTextView
    val resultNumericTextView: TextView = itemView.resultValueTextView
    val posNegImageEmpty: ImageView = itemView.imageViewEmpty
    val posNegImageFilled: ImageView = itemView.imageViewFilled
    var starButton: ImageButton = itemView.starButton


    fun bind(userTestResult: UserTestResult){
        resultNameTextView.text = userTestResult.bloodTestName
        resultDateTextView.text = DateManager.dateMillisToStringDate(userTestResult.dateMillis)

        when(userTestResult.resultType){
            RESULT_TYPE_NUMERIC -> {
                resultNumericTextView.visibility = View.VISIBLE
                resultNumericTextView.text = userTestResult.result.toString()
                posNegImageEmpty.visibility = View.INVISIBLE
                posNegImageFilled.visibility = View.INVISIBLE
            }
            RESULT_TYPE_POSITIVE_NEGATIVE -> {
//                resultNumericTextView.visibility = View.INVISIBLE
//                when(userTestResult.result){
//                    NEGATIVE_RESULT -> {
//                        posNegImageEmpty.visibility = View.VISIBLE
//                        posNegImageFilled.visibility = View.INVISIBLE
//                    }
//                    POSITIVE_RESULT -> {
//                        posNegImageEmpty.visibility = View.INVISIBLE
//                        posNegImageFilled.visibility = View.VISIBLE
//                    }
//                    else -> {
//                        posNegImageEmpty.visibility = View.INVISIBLE
//                        posNegImageFilled.visibility = View.INVISIBLE
//                    }
//                }
//                resultNumericTextView.visibility = View.INVISIBLE

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
            IS_FAVORITE ->  starButton = itemView.starButton
            NOT_FAVORITE -> starButton = itemView.prestarButton
        }

        starButton.visibility = View.VISIBLE
    }


}