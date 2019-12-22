package com.example.labmanager.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_group_with_chart.*
import com.example.labmanager.adapter.TestResultAdapter
import com.example.labmanager.model.TestsGroup
import com.example.labmanager.R
import com.example.labmanager.service.DateManager
import com.example.labmanager.service.ItemClickedCallback
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter


class GroupWithChartFragment (
    private var testsGroup: TestsGroup,
    fragmentManager: FragmentManager,
    private var parentContext: Context
) : Fragment(),
    ItemClickedCallback{


    private var fragmentmanager = fragmentManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group_with_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backImageButton.setOnClickListener { fragmentmanager.popBackStackImmediate() }
        progress_bar.visibility = View.INVISIBLE
        textViewGroupName.text = testsGroup.groupName
        showChart()
        setUpRecycler()
    }


    lateinit var valuseHashMap: HashMap<Float, String>
    private fun showChart(){

        val entries = arrayListOf<Entry>()
        val testResults = testsGroup.resultsList

        valuseHashMap = hashMapOf<Float, String>()
        for(data in testResults){
            entries.add(BarEntry(data.dateMillis.toFloat(), data.result))
            valuseHashMap.put(data.dateMillis.toFloat(), DateManager.dateMillisToStringDate(data.dateMillis))
        }

        val dataSet = LineDataSet(entries, getString(R.string.creation_date))
        dataSet.color = resources.getColor(R.color.chartColor1)
        dataSet.valueTextColor = Color.BLACK
        dataSet.isHighlightEnabled = false

        val lineData = LineData(dataSet)
        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.labelCount = 5
        xAxis.valueFormatter = object: ValueFormatter() {

            override fun getFormattedValue(value: Float): String {
                if(valuseHashMap.containsKey(value)){
                    return valuseHashMap[value]!!
                }
                return DateManager.dateMillisToStringDate(value.toLong())
            }
        }

        LineDataSet(entries, getString(R.string.results))
        chart.data = lineData

        chart.invalidate()
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
    }

    override fun itemAtPositionSelected(position: Int) {}

    override fun itemAtPositionLongClicked(position: Int) {}

}