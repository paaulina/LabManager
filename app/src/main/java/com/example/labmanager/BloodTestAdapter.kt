package com.example.labmanager

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.app.Activity
import android.util.Log
import android.widget.TextView


class BloodTestAdapter(context: Context, resource: Int, items: ArrayList<BloodTest>) :
    ArrayAdapter<BloodTest>(context, resource, items) {

    var tempItems = ArrayList<BloodTest>(items)
    var suggestions = ArrayList<BloodTest>()
    var resource = resource
    var items = items

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        if(position == null){
            Log.d("context1 is " , "pnull");
        }else {
            Log.d("context1 is " , "pnot null")
        }
        var view = convertView
        try {
            if (convertView == null) {
                if(parent == null){
                    Log.d("context1 is " , "null");
                }else {
                    Log.d("context1 is " , "not null")
                }
                val inflater = (context as Activity).layoutInflater
                view = inflater.inflate(R.layout.blood_test_row, parent, false)
            }
            val bloodTest = getItem(position)
            val name = view!!.findViewById(R.id.name) as TextView
            if (bloodTest != null) {
                name.text = bloodTest.name
            }
            /*val imageView = view.findViewById(R.id.imageView)
            imageView.setImageResource(fruit!!.getImage())
            name.setText(fruit.getName())*/

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return view
    }


    override fun getItem(position: Int): BloodTest? {
        return items.get(position)
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
                (context as AddTestResultActivity).hideProggressBar()
            }


        }
    }


}