package com.example.labmanager.Fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labmanager.Model.UserTestResult
import kotlinx.android.synthetic.main.fragment_group_with_chart.*
import com.example.labmanager.Adapter.TestResultAdapter
import com.example.labmanager.Model.TestsGroup
import com.example.labmanager.R
import com.example.labmanager.Service.DateManager
import com.example.labmanager.Service.ItemClickedCallback
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter


class GroupWithChartFragment (var testsGroup: TestsGroup, fragmentManager: FragmentManager, var parentContext: Context) : Fragment(), ItemClickedCallback{


    var fragmentmanager = fragmentManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.example.labmanager.R.layout.fragment_group_with_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backImageButton.setOnClickListener { fragmentmanager.popBackStackImmediate() }
        //showChart()
        progress_bar.visibility = View.INVISIBLE
        textViewGroupName.text = testsGroup.groupName
        showChart()
        setUpRecycler()
    }


    lateinit var valuseHashMap: HashMap<Float, String>
    fun showChart(){

        //val entries: MutableList<Entry> = mutableListOf()
        val entries = arrayListOf<Entry>()
        var testResults = testsGroup.resultsList

        valuseHashMap = hashMapOf<Float, String>()
        for(data in testResults){
            Log.d("AddingChartData", "${data.result} : ${data.dateMillis}" )
            entries.add(BarEntry(data.dateMillis.toFloat(), data.result))
            valuseHashMap.put(data.dateMillis.toFloat(), DateManager.dateMillisToStringDate(data.dateMillis))
        }

        var dataSet = LineDataSet(entries, "Data wykonania.")
        dataSet.color = resources.getColor(R.color.chartColor1)
        dataSet.valueTextColor = Color.BLACK
        dataSet.isHighlightEnabled = false


        val lineData = LineData(dataSet)

        var xAxis = chart.xAxis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setDrawGridLines(false)
        xAxis.setLabelCount(5)
        xAxis.valueFormatter = object: ValueFormatter() {

            override fun getFormattedValue(value: Float): String {
                if(valuseHashMap.containsKey(value)){
                    return valuseHashMap[value]!!
                }
                Log.d("Valuereturn", "$value : ${DateManager.dateMillisToStringDate(value.toLong())}")
                return DateManager.dateMillisToStringDate(value.toLong())
            }
        }

        var lineDataSet = LineDataSet(entries, "Wyniki")
        chart.data = lineData

        chart.invalidate()
        //chart.description.text = resources.getString(R.string.number_of_places)

        chart.lineData.setValueTextColor(parentContext.resources.getColor(R.color.colorChartMain))
        chart.description.textColor = parentContext.resources.getColor(R.color.black)
        chart.description.isEnabled = false

    }

    fun setUpRecycler(){
        val recyclerAdapter = TestResultAdapter(testsGroup.resultsList, parentContext, this)
        resltsRecyclerOverview.layoutManager =
            LinearLayoutManager(parentContext, LinearLayoutManager.VERTICAL, false)
        resltsRecyclerOverview.setHasFixedSize(true)
        resltsRecyclerOverview.adapter = recyclerAdapter
        //resltsRecyclerOverview.setNestedScrollingEnabled(false)
    }

    override fun itemAtPositionSelected(position: Int) {

    }

    override fun itemAtPositionLongClicked(position: Int) {

    }

}