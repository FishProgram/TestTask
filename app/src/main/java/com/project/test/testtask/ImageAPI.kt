package com.project.test.testtask

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
//Интерфейс для работы фрейморка Retrofit, который занимается запросами
interface ImageAPI {
    /*
    Метод поиска картинок по заданным параметрам
    query - тематика картинок
    page - количество страниц
    per_page - количество картинко на странице
    accessKey - API ключ для доступ
     */
    @GET("search/photos")
    fun searchImages(@Query("query") query: String, @Query("page") page: Int, @Query("per_page") perPage: Int, @Query("client_id") accessKey: String): Call<ResultsDTO>
}