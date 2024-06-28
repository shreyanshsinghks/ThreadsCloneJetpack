package com.hello.threads.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import com.hello.threads.model.ThreadModel
import com.hello.threads.model.UserModel
import com.hello.threads.utils.SharedPref
import java.util.UUID

class AddThreadViewModel  : ViewModel(){
    private val db = FirebaseDatabase.getInstance()
    private val userRef = db.getReference("threads")

    //    Creating firebase datastore for saving images of the users
    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("threads/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    fun saveImage(
        thread: String,
        userId: String,
        imageUri: Uri?
    ) {
        val uploadTask = imageRef.putFile(imageUri!!)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(thread, userId, it.toString())
            }
        }
    }

    private fun saveData(
        thread: String,
        userId: String,
        image: String
    ) {
        val threadData = ThreadModel(thread = thread, image = image, userId = userId, System.currentTimeMillis().toString())
        userRef.child(userRef.push().key!!).setValue(threadData).addOnSuccessListener {
            _isPosted.postValue(true)
        }.addOnFailureListener {
            _isPosted.postValue(false)
        }
    }
}