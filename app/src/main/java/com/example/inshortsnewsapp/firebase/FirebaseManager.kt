package com.example.inshortsnewsapp.firebase

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.inshortsnewsapp.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseManager {

    private val auth: FirebaseAuth = Firebase.auth
    private val database: FirebaseDatabase = Firebase.database
    private val usersRef = database.getReference("users")

    suspend fun login(email: String, password: String, activity: Activity): Boolean {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email.trim(), password).await()
            if (authResult.user == null) throw Exception()
            true
        } catch (e: Exception) {
            val message = activity.getString(R.string.login_failed, e.message)
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun logout(activity: Activity) {
        Toast.makeText(
            activity,
            activity.getString(R.string.logged_out),
            Toast.LENGTH_SHORT
        ).show()
        auth.signOut()
    }
    suspend fun register(email: String, password: String, activity: Activity): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Toast.makeText(
                activity,
                activity.getString(R.string.account_created),
                Toast.LENGTH_SHORT
            ).show()
            true
        } catch (e: Exception) {
            val message = activity.getString(R.string.signup_failed, e.message)
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun googleSignInOptionsBuilder(context: Context): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    private fun getUserUID(): String? {
        return auth.currentUser?.uid
    }

    fun addUserToDb() {
        usersRef.child(getUserUID()!!)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists())
                        usersRef.child(getUserUID()!!).setValue("")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("Error retrieving data from db")
                }
            })
    }

    fun addArticleAsRead(url: String) {
        usersRef
            .child(getUserUID()!!)
            .child(url)
            .setValue(true)
    }

    fun checkIfArticleHasBeenRead(title: String, callback: (Boolean) -> Unit) {
        usersRef.child(getUserUID()!!).child(title)
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists())
                    callback(true)
                else callback(false)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Error retrieving data from db")
            }
        })
    }
}