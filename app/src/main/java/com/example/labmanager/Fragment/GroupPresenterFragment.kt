package com.example.labmanager.Fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.labmanager.*
import com.example.labmanager.Adapter.TestResultAdapter
import com.example.labmanager.DataBase.DataBaseEntry.UserDataDBEntry
import com.example.labmanager.DataBase.usecase.UserData.TestResults.UserTestResultsInteractor
import com.example.labmanager.Model.TestsGroup
import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.Service.DateManager
import com.example.labmanager.Service.ItemClickedCallback
import com.example.labmanager.Service.UserTestsResultsGroupManager
import kotlinx.android.synthetic.main.fragment_grouped_results_overview.*
import kotlinx.android.synthetic.main.fragment_tests_results_overview.*

class GroupPresenterFragment (
    context: Context,
    fragmentManager: FragmentManager,
    var testsGroup: TestsGroup,
    val allResults: ArrayList<UserTestResult>
) : ItemClickedCallback, Fragment(){


    var parentContext = context
    var fragmentmanager = fragmentManager
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group_presenter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.resltsRecycler)

        var buttonEdit = view.findViewById<Button>(R.id.button_edit)
        buttonEdit.setOnClickListener {
            var groupEditionFragment = GroupEditionFragment(parentContext, testsGroup,  allResults)
            fragmentmanager.beginTransaction().replace(R.id.fragments_container, groupEditionFragment).addToBackStack("frag").commit()
        }
    }

    fun setUpResults(){
        if(recyclerView != null){
            val recyclerAdapter = TestResultAdapter(testsGroup.resultsList, parentContext, this)
            resltsRecyclerOverview.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            resltsRecyclerOverview.setHasFixedSize(true)
            resltsRecyclerOverview.adapter = recyclerAdapter
        }
    }

    override fun itemAtPositionSelected(position: Int) {
        //showDetailsDialog(displayableResultList.get(position))
    }

//    fun showDetailsDialog(userTestResult: UserTestResult){
//
//        selectedResult = userTestResult
//        detailsDialog = Dialog(parentContext)
//        detailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        detailsDialog.setContentView(R.layout.dialog_test_result_details)
//
//        detailsDialog.findViewById<TextView>(R.id.testDateTextView).text = DateManager.dateMillisToStringDate(userTestResult.dateMillis)
//        detailsDialog.findViewById<TextView>(R.id.testResultNameTextView).text = userTestResult.bloodTestName
//
//        var text = ""
//        when(userTestResult.resultType){
//            RESULT_TYPE_NUMERIC -> text = userTestResult.result.toString()
//            RESULT_TYPE_POSITIVE_NEGATIVE -> {
//                when(userTestResult.result){
//                    POSITIVE_RESULT -> text = resources.getString(R.string.positive)
//                    NEGATIVE_RESULT -> text = resources.getString(R.string.negative)
//                }
//            }
//            RESULT_TYPE_DESC -> text = ""
//        }
//        detailsDialog.findViewById<TextView>(R.id.resultValueTextView).text = text
//        detailsDialog.findViewById<TextView>(R.id.notesTextView).text = userTestResult.note
//
//        var favouriteIcon = R.drawable.prestar
//        when(userTestResult.favorite){
//            IS_FAVORITE -> {
//                favouriteIcon = R.drawable.star
//            }
//        }
//        favouriteButton = detailsDialog.findViewById<ImageButton>(R.id.imageButtonFavourite)
//        favouriteButton.setImageResource(favouriteIcon)
//        favouriteButton.setOnClickListener{
//            if (selectedResult.favorite == IS_FAVORITE) addToFavourites() else removeFromFavourites()
//        }
//        detailsDialog.show()
//    }
//
//    fun addToFavourites(){
//        selectedResult.favorite = NOT_FAVORITE
//        favouriteButton.setImageResource(R.drawable.prestar)
//        UserDataDBEntry.updateUserTestResult(selectedResult, this)
//        //todo save
//    }
//
//    fun removeFromFavourites(){
//        selectedResult.favorite = IS_FAVORITE
//        favouriteButton.setImageResource(R.drawable.star)
//        UserDataDBEntry.updateUserTestResult(selectedResult, this)
//        //todo save
//    }

    override fun itemAtPositionLongClicked(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}