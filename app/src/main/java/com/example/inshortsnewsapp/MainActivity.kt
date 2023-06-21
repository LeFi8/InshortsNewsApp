package com.example.inshortsnewsapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.inshortsnewsapp.authentication.FirebaseManager

class MainActivity : Activity() {

    private lateinit var firebaseManager: FirebaseManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseManager = FirebaseManager()
        setContentView(R.layout.activity_main)

        val isLoggedIn = firebaseManager.getUser() != null
        if (!isLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
}


