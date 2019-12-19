package com.example.labmanager.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.FirebaseStorage
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnSuccessListener
import android.net.Uri
import android.provider.MediaStore
import android.graphics.Bitmap
import android.content.Intent
import android.R
import android.app.Activity
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import kotlinx.android.synthetic.main.activity_add_medical_files.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import com.example.labmanager.DataBase.DataBaseEntry.StorageEntry
import com.example.labmanager.DataBase.DataBaseEntry.UserDataDBEntry
import com.example.labmanager.DataBase.usecase.MedicalFiles.MedicalFilesInteractor
import com.example.labmanager.DataBase.usecase.MedicalFiles.MedicalFilesSavingResultPresenter
import com.example.labmanager.Model.MedicalFile
import com.example.labmanager.Service.TextValidation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageMetadata
import kotlinx.android.synthetic.main.activity_signup.*


class AddMedicalFilesActivity : AppCompatActivity(), MedicalFilesSavingResultPresenter {

    private var mStorageRef: StorageReference? = null
    private var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.labmanager.R.layout.activity_add_medical_files)
        mStorageRef = FirebaseStorage.getInstance().getReference();

        buttonUploadImage.setOnClickListener {
            showFileChooser()
        }

        buttonSave.setOnClickListener {
            if(correctNameValue()){
                setUpLoadingView()
                uploadFileFromImageView()
            }
        }

        buttonAddOther.setOnClickListener {
            showFileChooser()
        }
    }

    fun exitActivity(view: View){
        onBackPressed()
    }

    override fun onBackPressed(){
        val intent = Intent(this, MainPageActivity::class.java)
        startActivity(intent)
    }

    fun correctNameValue(): Boolean {
        var nameText = editTextName.text.toString()
        if(!nameText.isNotEmpty()){
            linearLayout6.isErrorEnabled = true
            linearLayout6.error =
                TextValidation.EMAIL_ERROR_TEXT
            return false
        }
        linearLayout6.isErrorEnabled = false
        return true
    }

    fun setUpLoadingView(){
        linearLayout6.visibility = View.INVISIBLE
        linearLayout7.visibility = View.INVISIBLE
        fileImageView.visibility = View.INVISIBLE
        layoutbuttons.visibility = View.INVISIBLE
        progress_bar.visibility = View.VISIBLE
    }

    private val PICK_IMAGE_REQUEST = 234
    lateinit var filePath: Uri
    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    //handling the image chooser activity result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            buttonUploadImage.visibility = View.GONE
            buttonAddOther.visibility = View.VISIBLE
            fileImageView.visibility = View.VISIBLE

            filePath = data.data

            Log.d("FilePAth " , filePath.toString() + " " + filePath.pathSegments.get(1))
            try {
                var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                if(bitmap.width > bitmap.height){
                    val matrix = Matrix()
                    matrix.postRotate(90f)
                    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)

                    bitmap = Bitmap.createBitmap(
                        scaledBitmap,
                        0,
                        0,
                        scaledBitmap.width,
                        scaledBitmap.height,
                        matrix,
                        true
                    )
                }

                fileImageView.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }


    }


    fun uploadFileFromImageView(){

        MedicalFilesInteractor(StorageEntry, null, this)
            .saveMedicalFile(MedicalFile(editTextName.text.toString(), "",(fileImageView.drawable as BitmapDrawable).bitmap ))

//        val storage = FirebaseStorage.getInstance()
//        val storageRef = storage.reference
//        var userId = firebaseAuth.currentUser!!.uid
//        var path = userId +"/"+ filePath.pathSegments.get(1)
//        val mountainsRef = storageRef.child(path)
//
//
//        fileImageView.isDrawingCacheEnabled = true
//        fileImageView.buildDrawingCache()
//        val bitmap = (fileImageView.drawable as BitmapDrawable).bitmap
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val data = baos.toByteArray()
//
//
//        if(editTextName.text.length > 0){
//
//        }
//        var metadata = StorageMetadata.Builder()
//            .setCustomMetadata("Name", editTextName.text.toString())
//            .build()
//
//        var uploadTask = mountainsRef.putBytes(data, metadata)
//        uploadTask.addOnFailureListener {
//            // Handle unsuccessful uploads
//            Log.d("upload", "success")
//            val intent = Intent(this, SuccessActivity::class.java)
//            startActivity(intent)
//        }.addOnSuccessListener {
//            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
//            // ...
//            val intent = Intent(this, SuccessActivity::class.java)
//            startActivity(intent)
//        }

    }

    override fun onSavingSuccess() {

        val intent = Intent(this, SuccessActivity::class.java)
        startActivity(intent)
    }

    override fun onSavingFailure() {

    }




}
