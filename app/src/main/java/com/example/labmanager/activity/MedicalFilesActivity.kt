package com.example.labmanager.activity

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.labmanager.adapter.ImagesRecyclerAdapter
import com.example.labmanager.dataBase.dataBaseEntry.StorageEntry
import com.example.labmanager.dataBase.usecase.medicalFiles.MedicalFilesInteractor
import com.example.labmanager.dataBase.usecase.medicalFiles.MedicalFilesPresenter
import com.example.labmanager.fragment.MedicalFileDetailsFragment
import com.example.labmanager.model.MedicalFile
import com.example.labmanager.R
import com.example.labmanager.service.InternetConnectionChecker
import com.example.labmanager.service.ItemClickedCallback
import com.example.labmanager.service.MedicalFilesManager
import kotlinx.android.synthetic.main.activity_medical_files.*




class MedicalFilesActivity : AppCompatActivity(), MedicalFilesPresenter, ItemClickedCallback {

    var filesList = arrayListOf<MedicalFile>()
    var detailsDisplayed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_files)

        InternetConnectionChecker.checkConnection(
            this,
            this,
            resources.getString(R.string.app_cant_work_incorrectly_alert)
        )
        MedicalFilesInteractor(StorageEntry, this, null).retrieveMedicalFiles()
    }

    fun exitActivity(view: View){
        onBackPressed()
    }

    override fun onBackPressed(){
        if(detailsDisplayed){
            detailsDisplayed = false
            fragmetContainer1.visibility = View.GONE
            text_view_title.visibility = View.VISIBLE
            gridRecycler.visibility = View.VISIBLE
        }else{
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
        }

    }


    override fun presentMedicalFiles(medicalFilesArrayList: ArrayList<MedicalFile>) {
        filesList = MedicalFilesManager().sortFilesByName(medicalFilesArrayList)
        for(file in filesList){
            file.smallBitmap = getSmallBitmap(file.imageBitmap)
        }
        this.setUpRecycler(filesList)

    }

    private fun getSmallBitmap(image: Bitmap) : Bitmap{
        var maxSize = 200
        var width = image.getWidth()
        var height = image.getHeight()

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun presentMedicalFilesRetrievalError() {
        progress_bar_med.visibility = View.GONE
        textViewNoItems.visibility = View.VISIBLE
    }

    private fun setUpRecycler(medicalFiles: ArrayList<MedicalFile>){

        progress_bar_med.visibility = View.GONE
        gridRecycler.layoutManager = GridLayoutManager(this, 2)
        gridRecycler.setHasFixedSize(true)
        gridRecycler.setItemViewCacheSize(20)
        gridRecycler.setDrawingCacheEnabled(true);
        gridRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        var adapter = ImagesRecyclerAdapter(medicalFiles, this)
        gridRecycler.adapter = adapter
        adapter.notifyDataSetChanged()


        if(medicalFiles.isEmpty()){
            textViewNoItems.visibility = View.VISIBLE
        } else {
            textViewNoItems.visibility = View.GONE
        }
    }

    private lateinit var detailsFragment: MedicalFileDetailsFragment
    private var detailsFragmentInitialized = false
    override fun itemAtPositionSelected(position: Int) {
        detailsDisplayed = true
        if(detailsFragmentInitialized){
            supportFragmentManager.beginTransaction()
                .hide(detailsFragment)
                .commit()
        }
        detailsFragmentInitialized = true
        fragmetContainer1.visibility = View.VISIBLE
        gridRecycler.visibility = View.GONE
        text_view_title.visibility = View.INVISIBLE
        detailsFragment = MedicalFileDetailsFragment(filesList[position])
        supportFragmentManager.beginTransaction()
                              .add(R.id.fragmetContainer1, detailsFragment)
                              .commit()
    }

    override fun itemAtPositionLongClicked(position: Int) {}
}
