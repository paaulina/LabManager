package com.example.labmanager.Fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.labmanager.DataBase.DataBaseEntry.StorageEntry
import com.example.labmanager.DataBase.usecase.MedicalFiles.MedicalFilesInteractor
import com.example.labmanager.DataBase.usecase.MedicalFiles.MedicalFilesSavingResultPresenter
import com.example.labmanager.Model.MedicalFile
import com.example.labmanager.R

class MedicalFileDetailsFragment(val medicalFile: MedicalFile,
                                 fragmentManager: FragmentManager,
                                 context: Context)
    : Fragment(),
      MedicalFilesSavingResultPresenter{

    val fragmentmanager = fragmentManager
    lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_medical_file_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var textViewName = view.findViewById<TextView>(R.id.text_view_name)
        var imageView = view.findViewById<ImageView>(R.id.imageView)

        textViewName.setText(medicalFile.name)
        imageView.setImageBitmap(medicalFile.imageBitmap)
        progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)

        var deleteButton = view.findViewById<ImageButton>(R.id.buttonDelete)
        deleteButton.setOnClickListener {
            AlertDialog.Builder(context).setMessage("Czy chcesz usunąć plik?")
                .setPositiveButton("Tak") { dialog, which -> deleteFile()}
                .setNegativeButton("Anuluj"){dialog, which -> dialog.cancel() }
                .setCancelable(true)
                .show()

        }
    }

    fun deleteFile(){
        progressBar.visibility = View.VISIBLE
        MedicalFilesInteractor(StorageEntry, null, this).deleteMedicalFile(medicalFile)
    }

    override fun onSavingSuccess() {
        fragmentmanager.popBackStackImmediate()
    }

    override fun onSavingFailure() {}
}