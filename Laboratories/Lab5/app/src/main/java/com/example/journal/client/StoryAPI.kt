package com.example.journal.client

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object StoryAPI {
    private const val BASE_URL = " http://10.0.2.2:8085"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
    val retrofitService: StoryService by lazy {
        retrofit.create(StoryService::class.java)
    }
}