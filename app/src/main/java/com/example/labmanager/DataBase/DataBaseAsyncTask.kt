package com.example.labmanager.DataBase

import android.os.AsyncTask

class DataBaseAsyncTask() : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {
        DataBaseManager.initBloodTestsArray()
        return " "
    }

}