package com.example.inshortsnewsapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inshortsnewsapp.adapter.NewsAdapter
import com.example.inshortsnewsapp.authentication.FirebaseManager
import com.example.inshortsnewsapp.databinding.ActivityMainBinding
import com.example.inshortsnewsapp.util.HttpRequester
import com.example.inshortsnewsapp.util.NewsArticleParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat

class MainActivity : Activity() {

    private lateinit var firebaseManager: FirebaseManager
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseManager = FirebaseManager()

        val isLoggedIn = firebaseManager.getUser() != null
        if (!isLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date = SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis())
        binding.todaysDate.text = getString(R.string.todays_date, date)

        val adapter = NewsAdapter()
        CoroutineScope(Dispatchers.Main).launch {
            adapter.loadNewsArticles()
        }

        binding.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(applicationContext)
        }

    }
}


