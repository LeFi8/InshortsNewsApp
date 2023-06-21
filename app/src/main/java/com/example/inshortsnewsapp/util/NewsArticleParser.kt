package com.example.inshortsnewsapp.util

import com.example.inshortsnewsapp.model.NewsItem
import org.json.JSONArray

class NewsArticleParser {
    companion object {
        fun getNewsList(newsArticles: JSONArray): List<NewsItem> {
            val newsList = mutableListOf<NewsItem>()
            for (i in 0 until newsArticles.length()) {
                val newsObject = newsArticles.getJSONObject(i)
                val date = newsObject.getString("date")
                val id = newsObject.getString("id")
                val imageUrl = newsObject.getString("imageUrl")
                val readMoreUrl = newsObject.optString("readMoreUrl")
                val title = newsObject.getString("title")

                val newsItem = NewsItem(
                     date, id, imageUrl, readMoreUrl, title
                )
                newsList.add(newsItem)
            }
            return newsList
        }
    }
}