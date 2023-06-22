package com.example.inshortsnewsapp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inshortsnewsapp.R
import com.example.inshortsnewsapp.databinding.NewsItemBinding
import com.example.inshortsnewsapp.firebase.FirebaseManager
import com.example.inshortsnewsapp.model.NewsItem
import com.example.inshortsnewsapp.util.HttpRequester
import com.example.inshortsnewsapp.util.NewsArticleParser


class NewsAdapterViewHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(newsItem: NewsItem, firebaseManager: FirebaseManager) {
        binding.newsDescription.text = newsItem.description
        binding.newsDate.text = newsItem.date
        binding.seen.setImageResource(R.drawable.seen)
        binding.seen.visibility = View.INVISIBLE

        firebaseManager.checkIfArticleHasBeenRead(newsItem.title) { hasRead ->
            if (hasRead) binding.seen.visibility = View.VISIBLE
        }

        Glide.with(binding.root)
            .load(newsItem.urlToImage)
            .into(binding.newsImage)
    }
}

class NewsAdapter(private val firebaseManager: FirebaseManager) : RecyclerView.Adapter<NewsAdapterViewHolder>() {
    private val newsList = mutableListOf<NewsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapterViewHolder {
        val binding = NewsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int = newsList.size

    @SuppressLint("IntentReset", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: NewsAdapterViewHolder, position: Int) {
        holder.bind(newsList[position], firebaseManager)
        val binding = holder.binding

        binding.root.setOnClickListener {
            binding.seen.visibility = View.VISIBLE
            firebaseManager.addArticleAsRead(newsList[position].title)
            val browserIntent = Intent()
            browserIntent
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setType("text/plain").data = Uri.parse(newsList[position].url)
            binding.root.context.startActivity(browserIntent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    suspend fun loadNewsArticles(apiKey: String) {
        val newsArticles = NewsArticleParser.getNewsList(HttpRequester.requestNewsArticles(apiKey))
        newsList.addAll(newsArticles)
        notifyDataSetChanged()
    }
}