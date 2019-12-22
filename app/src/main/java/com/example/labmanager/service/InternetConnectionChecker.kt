package com.example.labmanager.service

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import com.example.labmanager.R
import com.tapadoo.alerter.Alerter


class InternetConnectionChecker {
    companion object{
        fun isOnline(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

        fun checkConnection(activity: Activity, context: Context, descriptionText : String) : Boolean{
            if(!isOnline(context)){
                Alerter.create(activity)
                    .setTitle("Brak połączenia z internetem")
                    .setText(descriptionText)
                    .setBackgroundColorRes(R.color.alertColor)
                    .setIcon(context.getDrawable(R.drawable.wifi))
                    .setDuration(5000)
                    .show()
                return false
            }
            return true
        }


    }
}