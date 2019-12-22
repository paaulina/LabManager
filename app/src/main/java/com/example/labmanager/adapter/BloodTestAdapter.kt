package com.example.labmanager.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.app.Activity
import android.util.Log
import android.widget.TextView
import com.example.labmanager.model.BloodTest
import com.example.labmanager.R
import com.example.labmanager.activity.AddTestResultActivity


class BloodTestAdapter(context: Context, resource: Int, private var items: ArrayList<BloodTest>) :
    ArrayAdapter<BloodTest>(context, resource, items) {

    var tempItems = ArrayList<BloodTest>(items)
    var suggestions = ArrayList<BloodTest>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var view = convertView
        try {
            if (convertView == null) {
                val inflater = (context as Activity).layoutInflater
                view = inflater.inflate(R.layout.blood_test_row, parent, false)
            }
            val bloodTest = getItem(position)
            val name = view!!.findViewById(R.id.name) as TextView
            if (bloodTest != null) {
                name.text = bloodTest.name
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return view
    }


    override fun getItem(position: Int): BloodTest? {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getFilter(): Filter {
        return filter
    }


    private val filter = object : Filter() {
        override fun convertResultToString(resultValue: Any): String? {
            val bloodTest = resultValue as BloodTest
            return bloodTest.name
        }

        override fun performFiltering(charSequence: CharSequence?): Filter.FilterResults {
            Log.d("AutoComPleTeLog", "filtering")
            if (charSequence != null) {
                suggestions.clear()
                for (bloodTest in tempItems) {
                    if (bloodTest.name!!.toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(bloodTest)
                    }
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                return filterResults
            } else {
                return Filter.FilterResults()
            }
        }

        override fun publishResults(charSequence: CharSequence?, filterResults: Filter.FilterResults) {

            Log.d("AutoComPleTeLog", "publish results")
            if(filterResults.values != null){
                val tempValues = filterResults.values as ArrayList<BloodTest>
                if (filterResults != null && filterResults.count > 0) {
                    clear()
                    for (btObj in tempValues) {
                        add(btObj)
                        notifyDataSetChanged()
                    }
                } else {
                    clear()
                    notifyDataSetChanged()
                }
            }
            (context as AddTestResultActivity).resultsPublished()
        }
    }


}