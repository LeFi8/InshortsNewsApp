package com.example.inshortsnewsapp.util

import com.example.inshortsnewsapp.model.NewsItem
import org.json.JSONArray

class NewsArticleParser {
    companion object {
        fun getNewsList(newsArticles: JSONArray): List<NewsItem> {
            val newsList = mutableListOf<NewsItem>()
            for (i in 0 until newsArticles.length()) {
                val newsObject = newsArticles.getJSONObject(i)
                val author = newsObject.getString("author")
                val title = newsObject.getString("title")
                val readMoreUrl = newsObject.optString("url")
                val imageUrl = newsObject.getString("urlToImage")
                val date = newsObject.getString("publishedAt")

                val newsItem = NewsItem(
                     author, title, readMoreUrl, imageUrl, date
                )
                newsList.add(newsItem)
            }
            return newsList
        }
    }
}