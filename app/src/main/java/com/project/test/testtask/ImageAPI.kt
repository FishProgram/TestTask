package com.project.test.testtask

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageAPI {
    @GET("search/photos")
    fun searchImages(@Query("query") query: String, @Query("page") page: Int, @Query("per_page") perPage: Int, @Query("client_id") accessKey: String): Call<ResultsDTO>
}