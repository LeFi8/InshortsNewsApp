package com.example.inshortsnewsapp.authentication

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.inshortsnewsapp.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseManager {

    private var auth: FirebaseAuth = Firebase.auth

    suspend fun login(email: String, password: String, activity: Activity): Boolean {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            if (authResult.user == null) throw Exception()

            val user = auth.currentUser
            Toast.makeText(activity, user?.email, Toast.LENGTH_SHORT).show()
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

    fun getUser(): FirebaseUser? {
        return auth.currentUser;
    }

    fun getUserUID(): String? {
        return auth.currentUser?.uid;
    }

    fun getUserMail(): String {
        return auth.currentUser?.email.toString()
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
}