package com.example.labmanager.Activity

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.labmanager.Adapter.ImagesRecyclerAdapter
import com.example.labmanager.DataBase.DataBaseEntry.StorageEntry
import com.example.labmanager.DataBase.usecase.MedicalFiles.MedicalFilesInteractor
import com.example.labmanager.DataBase.usecase.MedicalFiles.MedicalFilesPresenter
import com.example.labmanager.Model.MedicalFile
import com.example.labmanager.R
import kotlinx.android.synthetic.main.activity_add_medical_files.*
import kotlinx.android.synthetic.main.activity_medical_files.*

class MedicalFilesActivity : AppCompatActivity(), MedicalFilesPresenter {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_files)

        MedicalFilesInteractor(StorageEntry, this, null).retrieveMedicalFiles()
    }

    fun exitActivity(view: View){
        onBackPressed()
    }

    override fun onBackPressed(){
        val intent = Intent(this, MainPageActivity::class.java)
        startActivity(intent)
    }

    override fun presentMedicalFiles(medicalFilesArrayList: ArrayList<MedicalFile>) {
        Log.d("sendindcallback", "getting ${medicalFilesArrayList.size}")
        Log.d("MedicalFilesRetrieval" , "medicalfiles ${medicalFilesArrayList.size}")
        setUpRecycler(medicalFilesArrayList)

    }

    override fun presentMedicalFilesRetrievalError() {
        Log.d("MedicalFilesRetrieval" , "medicalfiles fail")
    }

    fun setUpRecycler(medicalFiles: ArrayList<MedicalFile>){

        progress_bar_med.visibility = View.GONE
        gridRecycler.layoutManager = GridLayoutManager(this, 3)
        gridRecycler.adapter = ImagesRecyclerAdapter(medicalFiles)

        gridRecycler.addOnItemTouchListener()

    }

}
