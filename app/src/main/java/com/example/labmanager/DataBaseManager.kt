package com.example.labmanager

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/*
    Database use cases:
    1. Reading data:
        - get all blood tests names: for adding new blood test result
        - display user's blood test results
        - display group of blood test results
        - display chart for blood tests
 */



object DataBaseManager {

    private var databaseReference = FirebaseDatabase.getInstance().reference
    private var bloodTestsArray = ArrayList<BloodTest>()
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var userId = ""

    fun initArray(){
        if(bloodTestsArray.size == 0){
            var btEndpoint = databaseReference.child("BloodTests")
            btEndpoint.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for(btSnapshot in dataSnapshot.children){
                        bloodTestsArray.add(btSnapshot.getValue(BloodTest::class.java) as BloodTest)
                        //Log.d("Debugging ", (btSnapshot.getValue(BloodTest::class.java) as BloodTest).name)
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("Debugging ", databaseError.message)
                }
            })
        }
    }
    fun getAllBloodTests() : ArrayList<BloodTest>{
        initArray()
        return bloodTestsArray
    }

    fun getUserNode(){
        if(userId.isEmpty()){
            if(firebaseAuth.currentUser != null){
                userId = firebaseAuth.currentUser!!.uid

                var btEndpoint = databaseReference.child("Users").child(userId)
                btEndpoint.addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(dataSnapshot.value == null){
                            databaseReference.child("Users").setValue(userId)
                        }

                    }
                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }


                })
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                rootRef.child("childName")
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.getValue() == null) {
                            // The child doesn't exist
                        }
                    }
                });
            }
        }

        var userId = firebaseAuth.currentUser.uid
        firebase.auth().onAuthStateChanged((user) => {
            if (user) {
                // User logged in already or has just logged in.
                console.log(user.uid);
            } else {
                // User not logged in or has just logged out.
            }
        });
    }

}