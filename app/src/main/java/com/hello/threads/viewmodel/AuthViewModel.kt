package com.hello.threads.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.animation.core.snap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.hello.threads.model.UserModel
import com.hello.threads.utils.SharedPref
import java.util.UUID

class AuthViewModel : ViewModel() {
    //    Creating firebase reference
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance()
    private val userRef = db.getReference("users")

    //    Creating firebase datastore for saving images of the users
    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: LiveData<FirebaseUser?> = _firebaseUser

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)

                    getData(auth.currentUser!!.uid, context)
                } else {
                    _error.postValue(it.exception!!.message)
                }
            }
    }

    private fun getData(uid: String, context: Context) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                SharedPref.storeData(
                    name = userData!!.name,
                    email = userData.email,
                    bio = userData.bio,
                    userName = userData.userName,
                    imageUrl = userData.imageUrl,
                    context = context
                )
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun register(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        context: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    saveImage(
                        email = email,
                        password = password,
                        name = name,
                        bio = bio,
                        userName = userName,
                        imageUri = imageUri,
                        uid = auth.currentUser?.uid,
                        context = context
                    )
                } else {
                    _error.postValue("Something went wrong")
                }
            }
    }

    private fun saveImage(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUri: Uri,
        uid: String?,
        context: Context
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(
                    email = email, password = password, name = name, bio = bio, userName = userName,
                    imageUrl = it.toString(), uid = uid, context = context
                )
            }
        }
    }

    private fun saveData(
        email: String,
        password: String,
        name: String,
        bio: String,
        userName: String,
        imageUrl: String,
        uid: String?,
        context: Context
    ) {

        val firestoreDb = Firebase.firestore
        val followersRef = firestoreDb.collection("followers").document(uid!!)
        val followingRef = firestoreDb.collection("following").document(uid)

        followingRef.set(mapOf("followingIds" to listOf<String>()))
        followersRef.set(mapOf("followersIds" to listOf<String>()))





        val userData = UserModel(
            email = email, password = password, name = name, bio = bio,
            userName = userName, imageUrl = imageUrl, uid = uid!!
        )
        userRef.child(uid).setValue(userData).addOnSuccessListener {
            SharedPref.storeData(
                name = name, email = email, bio = bio,
                userName = userName, imageUrl = imageUrl, context = context
            )
        }.addOnFailureListener {

        }
    }

    //    Logout Function
    fun logout() {
        auth.signOut()
        _firebaseUser.postValue(null)
    }
}