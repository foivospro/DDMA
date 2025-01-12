package com.example.gymappdemo.data.network

import NewsResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://newsapi.org/v2/"
val json = Json {
    ignoreUnknownKeys = true
}
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(json.asConverterFactory(
        contentType = "application/json".toMediaType())
    ).build()

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

object NewsApi {
    val retrofitService : NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java) }
}