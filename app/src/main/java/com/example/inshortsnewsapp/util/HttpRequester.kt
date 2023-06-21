package com.example.inshortsnewsapp.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpRequester {
    companion object {
        private val url = URL("http://knopers.com.pl:5000/news?category=science")

        suspend fun requestNewsArticles(): JSONArray {
            return withContext(Dispatchers.IO) {
                try {
                    val connection = url.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"

                    val responseCode = connection.responseCode
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        val inputStream = connection.inputStream
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        val responseString = reader.use(BufferedReader::readText)

                        val jsonObj = JSONObject(responseString)
                        val jsonArray = jsonObj.getJSONArray("data")

                        jsonArray
                    } else {
                        throw Exception("$responseCode")
                    }
                } catch (e: Exception) {
                    throw Exception("Error making HTTP request: ${e.message}")
                }
            }
        }
    }
}
