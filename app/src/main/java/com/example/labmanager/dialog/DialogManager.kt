package com.example.labmanager.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.ImageButton
import android.widget.TextView
import com.example.labmanager.*
import com.example.labmanager.dataBase.dataBaseEntry.UserDataDBEntry
import com.example.labmanager.dataBase.usecase.userData.TestResults.TestResultsInteractor
import com.example.labmanager.model.UserTestResult
import com.example.labmanager.service.DateManager

class DialogManager() {

    private lateinit var selectedResult : UserTestResult
    private lateinit var detailsDialog : Dialog
    private lateinit var favouriteButton : ImageButton
    private lateinit var deleteButton : ImageButton
    private var displayer : DetailsDialogDisplayer? = null


    fun showDetailsDialog(editableMode: Int, userTestResult: UserTestResult, parentContext : Context, dialogDisplayer : DetailsDialogDisplayer?){

        displayer = dialogDisplayer
        selectedResult = userTestResult
        detailsDialog = Dialog(parentContext)
        detailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        detailsDialog.setContentView(R.layout.dialog_test_result_details)
        detailsDialog.findViewById<TextView>(R.id.testDateTextView).text = DateManager.dateMillisToStringDate(userTestResult.dateMillis)
        detailsDialog.findViewById<TextView>(R.id.testResultNameTextView).text = userTestResult.bloodTestName

        var text = ""
        val resources = parentContext.resources
        when(userTestResult.resultType){
            RESULT_TYPE_NUMERIC -> text = userTestResult.result.toString() + " " + userTestResult.unit
            RESULT_TYPE_POSITIVE_NEGATIVE -> {
                when(userTestResult.result){
                    POSITIVE_RESULT -> text = resources.getString(R.string.positive)
                    NEGATIVE_RESULT -> text = resources.getString(R.string.negative)
                }
            }
            RESULT_TYPE_DESC -> text = ""
        }
        detailsDialog.findViewById<TextView>(R.id.resultValueTextView).text = text
        detailsDialog.findViewById<TextView>(R.id.notesTextView).text = userTestResult.note
        var favouriteIcon = R.drawable.prestar
        when(userTestResult.favorite){
            IS_FAVORITE -> {
                favouriteIcon = R.drawable.star
            }
        }
        favouriteButton = detailsDialog.findViewById(R.id.imageButtonFavourite)
        favouriteButton.setImageResource(favouriteIcon)

        if(editableMode == DIALOG_EDITABLE){
            favouriteButton.setOnClickListener{
                if (selectedResult.favorite == IS_FAVORITE) removeFromFavourites() else  addToFavourites()
            }

            deleteButton = detailsDialog.findViewById(R.id.imageButtonDelete)
            deleteButton.setOnClickListener {
                detailsDialog.hide()
                displayer?.deleteSelectedResult()
            }
        } else if(editableMode == DIALOG_NOT_EDITABLE) {
            deleteButton = detailsDialog.findViewById(R.id.imageButtonDelete)
            deleteButton.visibility = View.INVISIBLE

            if(userTestResult.favorite == NOT_FAVORITE){
                favouriteButton.visibility = View.INVISIBLE
            }
        }

        detailsDialog.show()
    }

    private fun addToFavourites(){
        favouriteButton.setImageResource(R.drawable.star)
        displayer?.addResultToFavourites()
    }

    private fun removeFromFavourites(){
        favouriteButton.setImageResource(R.drawable.prestar)
        displayer?.removeResultFromFavourites()
    }

}