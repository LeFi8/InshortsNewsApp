package com.example.inshortsnewsapp.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inshortsnewsapp.databinding.NewsItemBinding
import com.example.inshortsnewsapp.model.NewsItem
import com.example.inshortsnewsapp.util.HttpRequester
import com.example.inshortsnewsapp.util.NewsArticleParser


class NewsAdapterViewHolder(val binding: NewsItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(newsItem: NewsItem) {
        binding.newsTitle.text = newsItem.title
        binding.newsDate.text = newsItem.date

        Glide.with(binding.root)
            .load(newsItem.imageUrl)
            .into(binding.newsImage)
    }
}

class NewsAdapter : RecyclerView.Adapter<NewsAdapterViewHolder>() {
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

    @SuppressLint("IntentReset")
    override fun onBindViewHolder(holder: NewsAdapterViewHolder, position: Int) {
        holder.bind(newsList[position])
        val binding = holder.binding

        binding.root.setOnClickListener {
            val browserIntent = Intent()
            browserIntent
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setType("text/plain").data = Uri.parse(newsList[position].readMoreUrl)

            binding.root.context.startActivity(browserIntent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    suspend fun loadNewsArticles() {
        val newsArticles = NewsArticleParser.getNewsList(HttpRequester.requestNewsArticles())
        newsList.addAll(newsArticles)
        notifyDataSetChanged()
    }


}