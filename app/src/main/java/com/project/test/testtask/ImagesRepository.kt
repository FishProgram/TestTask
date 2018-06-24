package com.project.test.testtask

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.net.HttpURLConnection
import java.net.URL


class ImagesRepository {
    private val ACCESS_KEY = Application.appContext.getString(R.string.images_api_key)
    private val BASE_URL = Application.appContext.getString(R.string.base_url_images)
    private var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    private val imageAPI = retrofit.create(com.project.test.testtask.ImageAPI::class.java)

    fun searchImages(query: String = "space", page: Int = 1, perPage: Int = 20): Array<Bitmap> {
        val resultsDTO = imageAPI.searchImages(query, page, perPage, ACCESS_KEY).execute()
        val bitmaps = Array(page * perPage) { i -> getBitmapFromURL(resultsDTO.body()!!.results[i].urls.regular) }
        return bitmaps
    }

    private fun getBitmapFromURL(url: URL): Bitmap {
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val input = connection.inputStream
        return BitmapFactory.decodeStream(input)

    }
}