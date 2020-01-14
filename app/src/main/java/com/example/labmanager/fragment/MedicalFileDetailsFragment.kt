package com.example.labmanager.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.labmanager.activity.MedicalFilesActivity
import com.example.labmanager.dataBase.dataBaseEntry.StorageEntry
import com.example.labmanager.dataBase.usecase.medicalFiles.MedicalFilesInteractor
import com.example.labmanager.dataBase.usecase.medicalFiles.MedicalFilesSavingResultPresenter
import com.example.labmanager.model.MedicalFile
import com.example.labmanager.R
import kotlinx.android.synthetic.main.fragment_medical_file_details.*

class MedicalFileDetailsFragment(
    private val medicalFile: MedicalFile
) : Fragment(),
    MedicalFilesSavingResultPresenter{

    private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medical_file_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewName = view.findViewById<TextView>(R.id.textViewFileTitme)
        val imageView = view.findViewById<ImageView>(R.id.imageView)

        textViewName.text = medicalFile.name
        imageView.setImageBitmap(medicalFile.imageBitmap)
        dateTextView.text = medicalFile.dateString
        progressBar = view.findViewById(R.id.progress_bar)

        val deleteButton = view.findViewById<ImageButton>(R.id.buttonDelete)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(context).setMessage(getString(R.string.do_you_want_to_delete_file))
                .setPositiveButton(getString(R.string.yes)) { _, _ -> deleteFile()}
                .setNegativeButton(getString(R.string.cancel)){ dialog, _ -> dialog.cancel() }
                .setCancelable(true)
                .show()

        }

        imageView.setOnClickListener {
            if(dateTextView.visibility == View.INVISIBLE){
                dateTextView.visibility = View.VISIBLE
            } else{
                dateTextView.visibility = View.INVISIBLE
            }
        }
    }

    private fun deleteFile(){
        progressBar.visibility = View.VISIBLE
        MedicalFilesInteractor(StorageEntry, null, this).deleteMedicalFile(medicalFile)
        (context as MedicalFilesActivity).onBackPressed()
    }

    override fun onSavingSuccess() {}

    override fun onSavingFailure() {}
}