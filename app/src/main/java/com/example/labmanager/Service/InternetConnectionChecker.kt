package com.example.labmanager.Service

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import com.example.labmanager.R
import com.tapadoo.alerter.Alerter


class InternetConnectionChecker {
    companion object{
        fun isOnline(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm!!.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }

        fun checkConnection(activity: Activity, context: Context) : Boolean{
            if(!isOnline(context)){
                Alerter.create(activity)
                    .setTitle("Brak połączenia z internetem")
                    .setText("Aplikacja może nie działać poprawnie.")
                    .setBackgroundColorRes(R.color.alertColor) // or setBackgroundColorInt(Color.CYAN)
                    .setIcon(context.getDrawable(R.drawable.wifi))
                    .setDuration(3000)
                    .show()
                return false
            }
            return true
        }
    }
}