package com.example.labmanager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FirebaseStorage
import android.net.Uri
import android.provider.MediaStore
import android.content.Intent
import android.app.Activity
import android.graphics.drawable.BitmapDrawable
import android.view.View.*
import com.example.labmanager.EMAIL_ERROR_TEXT
import kotlinx.android.synthetic.main.activity_add_medical_files.*
import java.io.IOException
import com.example.labmanager.dataBase.dataBaseEntry.StorageEntry
import com.example.labmanager.dataBase.usecase.medicalFiles.MedicalFilesInteractor
import com.example.labmanager.dataBase.usecase.medicalFiles.MedicalFilesSavingResultPresenter
import com.example.labmanager.MED_FILE_ADDITION
import com.example.labmanager.model.MedicalFile
import com.example.labmanager.PICK_IMAGE_REQUEST
import com.example.labmanager.SUCCESS_ACTIVITY_ENTRY_POINT
import com.example.labmanager.service.InternetConnectionChecker

class AddMedicalFilesActivity : AppCompatActivity(), MedicalFilesSavingResultPresenter {

    private var mStorageRef: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.labmanager.R.layout.activity_add_medical_files)
        mStorageRef = FirebaseStorage.getInstance().reference

        buttonUploadImage.setOnClickListener {
            showFileChooser()
        }

        buttonSave.setOnClickListener {
            if(correctNameValue()
                && InternetConnectionChecker.checkConnection(
                    this,
                    this,
                    resources.getString(com.example.labmanager.R.string.set_up_connection_to_continue)
                )){
                setUpLoadingView()
                uploadFileFromImageView()
            }
        }

        buttonAddOther.setOnClickListener {
            showFileChooser()
        }

        InternetConnectionChecker.checkConnection(
            this,
            this,
            resources.getString(com.example.labmanager.R.string.app_cant_work_incorrectly_alert)
        )
    }

    fun exitActivity(view: View){
        onBackPressed()
    }

    override fun onBackPressed(){
        val intent = Intent(this, MainPageActivity::class.java)
        startActivity(intent)
    }

    private fun correctNameValue(): Boolean {
        val nameText = editTextName.text.toString()
        if(nameText.isEmpty()){
            linearLayout6.isErrorEnabled = true
            linearLayout6.error = EMAIL_ERROR_TEXT
            return false
        }
        linearLayout6.isErrorEnabled = false
        return true
    }

    private fun setUpLoadingView(){
        linearLayout6.visibility = INVISIBLE
        linearLayout7.visibility = INVISIBLE
        fileImageView.visibility = INVISIBLE
        layoutbuttons.visibility = INVISIBLE
        progress_bar.visibility = VISIBLE
    }

    private lateinit var filePath: Uri
    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null
            && data.data != null) {

            buttonUploadImage.visibility = GONE
            buttonAddOther.visibility = VISIBLE
            fileImageView.visibility = VISIBLE

            filePath = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                fileImageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun uploadFileFromImageView(){

        MedicalFilesInteractor(StorageEntry, null, this)
            .saveMedicalFile(MedicalFile(editTextName.text.toString(),
                                    "",
                                        (fileImageView.drawable as BitmapDrawable).bitmap ))
    }

    override fun onSavingSuccess() {
        val intent = Intent(this, SuccessActivity::class.java)
        intent.putExtra(SUCCESS_ACTIVITY_ENTRY_POINT, MED_FILE_ADDITION)
        startActivity(intent)
    }

    override fun onSavingFailure() {}

}
