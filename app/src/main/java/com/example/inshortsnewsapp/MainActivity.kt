package com.example.inshortsnewsapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inshortsnewsapp.adapter.NewsAdapter
import com.example.inshortsnewsapp.firebase.FirebaseManager
import com.example.inshortsnewsapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class MainActivity : Activity() {

    private lateinit var firebaseManager: FirebaseManager
    private lateinit var binding: ActivityMainBinding
    private val apiKey = BuildConfig.NEWS_API_KEY
    private lateinit var category: String

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseManager = FirebaseManager()

        val isLoggedIn = firebaseManager.getUser() != null
        if (!isLoggedIn) {
            startActivity(
                Intent(
                    this@MainActivity,
                    LoginActivity::class.java
                )
            )
            finish()
        }
        firebaseManager.addUserToDb()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date = SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis())
        binding.todaysDate.text = getString(R.string.todays_date, date)

        val adapter = NewsAdapter(firebaseManager)
        category = getString(R.string.general)
        loadArticles(adapter)

        binding.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(applicationContext)
        }

        val tabLayout = binding.tabLayout
        addNewTab(tabLayout, getString(R.string.general))
        addNewTab(tabLayout, getString(R.string.tech))
        addNewTab(tabLayout, getString(R.string.sports))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                category = tab.text.toString()
                binding.progressBar.visibility = View.VISIBLE
                loadArticles(adapter)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.settings.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(getString(R.string.log_out))
            alertDialog.setMessage(getString(R.string.log_out_message))
            alertDialog.setCancelable(true)
            alertDialog.setPositiveButton(getString(R.string.yes)) { _, _ ->
                firebaseManager.logout(this)
                startActivity(
                    Intent(
                        this@MainActivity,
                        LoginActivity::class.java
                    )
                )
                finish()
            }
            alertDialog.setNegativeButton(getString(R.string.no)) { _, _ -> }
            alertDialog.show()
        }
    }

    private fun addNewTab(tabLayout: TabLayout, name: String) {
        val tab = tabLayout.newTab()
        tab.text = name
        tabLayout.addTab(tab)
    }

    private fun loadArticles(adapter: NewsAdapter) = CoroutineScope(Dispatchers.Main)
        .launch {
            adapter.loadNewsArticles(apiKey, category)
            binding.progressBar.visibility = View.GONE
        }
}


