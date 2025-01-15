package com.pmdk.gymapp.data.network

import NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("from") fromDate: String,
        @Query("sortBy") sortBy: String,
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
        @Query("language") language: String
    ): NewsResponse
}