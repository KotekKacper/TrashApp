package com.example.trashapp

import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerApiService {
    @GET("/{data}")
    suspend fun getJson(@Path("data") sql: String, @Query("function") sqlFun: String): Response<ResponseBody>
}
