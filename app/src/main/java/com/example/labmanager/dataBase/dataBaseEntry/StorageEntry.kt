package com.example.labmanager.dataBase.dataBaseEntry

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.labmanager.dataBase.usecase.medicalFiles.MedicalFilesCallback
import com.example.labmanager.dataBase.usecase.medicalFiles.MedicalFilesGateway
import com.example.labmanager.dataBase.usecase.medicalFiles.MedicalFilesSavingCallback
import com.example.labmanager.dataBase.usecase.userData.MedicalFiles.MedicalFilesIDsRetrievalCallback
import com.example.labmanager.dataBase.usecase.userData.MedicalFiles.MedicalFilesIDsSavingCallback
import com.example.labmanager.model.MedicalFile
import com.example.labmanager.NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.ArrayList

object StorageEntry : MedicalFilesGateway,
                      MedicalFilesIDsRetrievalCallback,
                      MedicalFilesIDsSavingCallback{

    private var storageRef: StorageReference? = FirebaseStorage.getInstance().reference
    private var firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentlySavedMedicalFile: MedicalFile
    private lateinit var currentlySavingCallback: MedicalFilesSavingCallback
    private lateinit var currentlyRetrievalCallback: MedicalFilesCallback
    private var uploaded = false

    private var allMedicalFiles = arrayListOf<MedicalFile>()

// ############################ GET ################################################

    override fun getAllMedicalFiles(medicalFilesCallback: MedicalFilesCallback) {
        currentlyRetrievalCallback = medicalFilesCallback
        if(allMedicalFiles.size > 0 && uploaded){
            medicalFilesCallback.onRetrievalSuccess(allMedicalFiles)
        } else{
            UserDataDBEntry.getAllMadicalFilesIDs(this)
            uploaded = true
        }
    }

    override fun onRetrievalSuccess(emptyFilesList: ArrayList<MedicalFile>) {

        val medicalFiles = arrayListOf<MedicalFile>()
        for(file in emptyFilesList){
            var counter = 0
            val userId = firebaseAuth.currentUser!!.uid
            val path = userId +"/"+ file.path
            val pathReference = storageRef!!.child(path)
            val localFile = File.createTempFile("Images", "bmp")
            pathReference.getFile(localFile).addOnSuccessListener {
                file.imageBitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                medicalFiles.add(file)
                counter++
                if(medicalFiles.size == emptyFilesList.size){
                    allMedicalFiles = medicalFiles
                    currentlyRetrievalCallback.onRetrievalSuccess(medicalFiles)
                }

            }.addOnFailureListener {
                Log.d("RetrievalMed", "bitmapfail")
            }

        }
        if(emptyFilesList.isEmpty())
            currentlyRetrievalCallback.onRetrievalFailure()
    }

    override fun onRetrievalFailure() {
        currentlyRetrievalCallback.onRetrievalFailure()
    }

// ##################################### SAVE ###########################################################
    override fun saveMedicalFile(medicalFile: MedicalFile, medicalFilesSavingCallback: MedicalFilesSavingCallback) {
        currentlySavedMedicalFile = medicalFile
        currentlySavingCallback = medicalFilesSavingCallback
        UserDataDBEntry.addMedicalFileID(medicalFile.name, this)
    }


    override fun onSaveSuccess(fileID: String) {

        if(fileID.isNotEmpty()){
            currentlySavedMedicalFile.path = fileID
            val userId = firebaseAuth.currentUser!!.uid
            val path = "$userId/$fileID"
            val mountainsRef = storageRef?.child(path)

            val bitmap = currentlySavedMedicalFile.imageBitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val metadata = StorageMetadata.Builder()
                .setCustomMetadata(NAME, currentlySavedMedicalFile.name)
                .build()

            val uploadTask = mountainsRef?.putBytes(data, metadata)
            uploadTask?.addOnFailureListener {
                currentlySavingCallback.onFailure()
            }?.addOnSuccessListener {
                allMedicalFiles.add(currentlySavedMedicalFile)
                currentlySavingCallback.onSuccess()
            }
        }

    }

    override fun onFailure() { }

// ############################################ DELETE ###############################################################

    override fun deleteMedicalFile(
        medicalFile: MedicalFile,
        medicalFilesSavingCallback: MedicalFilesSavingCallback
    ) {
        UserDataDBEntry.removeMedicalFileID(medicalFile.path, this)


        val userId = firebaseAuth.currentUser!!.uid
        val path = userId +"/"+ medicalFile.path
        storageRef?.child(path)?.delete()
         allMedicalFiles.remove(medicalFile)
    }


// #################################################################################################################
// ############################################  DESTROY ###########################################################


    fun destroy(){
        allMedicalFiles.clear()
    }
}