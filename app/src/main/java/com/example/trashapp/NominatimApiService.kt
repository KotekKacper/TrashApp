package com.example.trashapp

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NominatimApiService {
    @GET("reverse")
    suspend fun getReverseJson(@Query("lat") lat: String,
                               @Query("lon") lon: String,
                               @Query("format") format: String): Response<ResponseBody>
}