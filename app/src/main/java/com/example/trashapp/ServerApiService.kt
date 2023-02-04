package com.example.trashapp

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ServerApiService {
    @GET("/raw/{data}")
    suspend fun getJson(@Path("data") sql: String, @Query("function") sqlFun: String): Response<ResponseBody>
}

interface ImageUploadApi {
    @Multipart
    @POST("/api/image-upload")
    fun uploadImage(
        @Part("trash-id") uri: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<ResponseBody>
}

interface ImageDownloadApi {
    @GET("/api/image-download")
    fun getImages(@Query("trash-id") trashId: String, @Query("img-number") imgNumber: String): Call<ResponseBody>
}

interface ImageDownloadByIdApi {
    @GET("/api/image-download-by-id")
    fun getImages(@Query("image-id") trashId: String): Call<ResponseBody>
}