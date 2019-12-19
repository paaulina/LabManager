package com.example.labmanager.DataBase.DataBaseEntry

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.RemoteController
import android.util.Log
import com.example.labmanager.Activity.SuccessActivity
import com.example.labmanager.DataBase.usecase.MedicalFiles.MedicalFilesCallback
import com.example.labmanager.DataBase.usecase.MedicalFiles.MedicalFilesGateway
import com.example.labmanager.DataBase.usecase.MedicalFiles.MedicalFilesSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.MedicalFiles.MedicalFilesIDsRetrievalCallback
import com.example.labmanager.DataBase.usecase.UserData.MedicalFiles.MedicalFilesIDsSavingCallback
import com.example.labmanager.Model.MedicalFile
import com.example.labmanager.NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_medical_files.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.ArrayList

object StorageEntry : MedicalFilesGateway,
                      MedicalFilesIDsRetrievalCallback,
                      MedicalFilesIDsSavingCallback{

    private var storageRef: StorageReference? = FirebaseStorage.getInstance().getReference()
    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentlySavedMedicalFile: MedicalFile
    private lateinit var currentlySavingCallback: MedicalFilesSavingCallback
    private lateinit var currentlyRetrievalCallback: MedicalFilesCallback

    var allMedicalFiles = arrayListOf<MedicalFile>()

    override fun getAllMedicalFiles(callback: MedicalFilesCallback) {
        currentlyRetrievalCallback = callback
        if(allMedicalFiles.size > 0){
            callback.onRetrievalSuccess(allMedicalFiles)
        } else{
            UserDataDBEntry.getAllMadicalFilesIDs(this)
        }
    }

    override fun saveMedicalFile(medicalFile: MedicalFile, callback: MedicalFilesSavingCallback) {
        currentlySavedMedicalFile = medicalFile
        currentlySavingCallback = callback
        UserDataDBEntry.addMedicalFileID(medicalFile.name, this)
    }


    override fun deleteMedicalFile(
        medicalFile: MedicalFile,
        medicalFilesSavingCallback: MedicalFilesSavingCallback
    ) {
        //TODO
    }

    override fun onRetrievalSuccess(emptyFilesList: ArrayList<MedicalFile>) {
        var medicalFiles = arrayListOf<MedicalFile>()

        for(file in emptyFilesList){
            var counter = 0;
            var userId = firebaseAuth.currentUser!!.uid
            var path = userId +"/"+ file.path
            Log.d("RetrievalMed", path)
            val pathReference = storageRef!!.child(path)
            Log.d("RetrievalMed path", pathReference.path)


            val localFile = File.createTempFile("Images", "bmp")
            pathReference.getFile(localFile).addOnSuccessListener {
                file.imageBitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                medicalFiles.add(file)
                Log.d("RetrievalMed", "bitmapsucc")
                counter++
                if(medicalFiles.size == emptyFilesList.size){
                    Log.d("sendindcallback", "bitmapsucc ${medicalFiles.size}")
                    allMedicalFiles = medicalFiles
                    currentlyRetrievalCallback.onRetrievalSuccess(medicalFiles)
                }

            }.addOnFailureListener {
                Log.d("RetrievalMed", "bitmapfail")
            }

        }
    }

    override fun onRetrievalFailure() {
        currentlyRetrievalCallback.onRetrievalFailure()
    }

    override fun onSaveSuccess(fileID: String) {
        if(currentlySavedMedicalFile != null && currentlySavingCallback != null){
            var userId = firebaseAuth.currentUser!!.uid
            var path = userId +"/"+ fileID
            val mountainsRef = storageRef?.child(path)

            var bitmap = currentlySavedMedicalFile.imageBitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            var metadata = StorageMetadata.Builder()
                .setCustomMetadata(NAME, currentlySavedMedicalFile.name)
                .build()

            var uploadTask = mountainsRef?.putBytes(data, metadata)
            uploadTask?.addOnFailureListener {
                currentlySavingCallback.onFailure()
            }?.addOnSuccessListener {
                currentlySavingCallback.onSuccess()
            }
        }

    }

    override fun onFailure() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}