package com.example.inshortsnewsapp.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

class HttpRequester {
    companion object {
        private const val newsURL = "https://newsapi.org/v2/everything?q=technology&sortBy=popularity&pageSize=10&apiKey="
        suspend fun requestNewsArticles(apiKey: String): JSONArray {
            return withContext(Dispatchers.IO) {
                try {
                    val client = OkHttpClient()
                    val request = Request.Builder()
                        .url(newsURL + apiKey)
                        .build()

                    val response: Response = client.newCall(request).execute()
                    val responseString: String? = response.body?.string()

                    if (response.isSuccessful && responseString != null) {
                        val jsonObj = JSONObject(responseString)
                        val jsonArray = jsonObj.getJSONArray("articles")
                        jsonArray
                    } else {
                        throw Exception(response.code.toString())
                    }
                } catch (e: Exception) {
                    throw Exception("Error making HTTP request: ${e.message}")
                }
            }
        }

    }
}
