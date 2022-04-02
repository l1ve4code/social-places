package com.example.redmad.data.api

import com.example.redmad.data.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface AppService {

    // AUTH
    @POST("auth/registration")
    suspend fun register(@Body regData: SignModel): AuthModel

    @POST("auth/login")
    suspend fun login(@Body loginData: SignModel): AuthModel

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") authHeader: String?): Response<Void>

    // FEED
    @GET("feed")
    suspend fun getFeed(@Header("Authorization") authHeader: String?): List<FeedElementModel>

    @POST("feed/{id}/like")
    suspend fun likeFeed(@Path("id") id: String?, @Header("Authorization") authHeader: String): Response<Void>

    @DELETE("feed/{id}/like")
    suspend fun dislikeFeed(@Path("id") id: String?, @Header("Authorization") authHeader: String): Response<Void>

    @GET("feed/favorite")
    suspend fun favorite(@Header("Authorization") authHeader: String): List<FeedElementModel>

    // ME
    @GET("me")
    suspend fun getMe(@Header("Authorization") authHeader: String?): UserModel

    @Multipart
    @PATCH("me")
    suspend fun patchMe(@Header("Authorization") authHeader: String?, @PartMap partMap: Map<String, String>): UserModel

    @GET("me/friends")
    suspend fun getFriends(@Header("Authorization") authHeader: String): List<UserModel>

    @POST("me/friends")
    suspend fun addFriend(@Header("Authorization") authHeader: String, @Body user_id: FriendModel): Response<Void>

    @DELETE("me/friends/{id}")
    suspend fun deleteFriend(@Path("id") user_id: String?, @Header("Authorization") authHeader: String): Response<Void>

    @GET("me/posts")
    suspend fun getPosts(@Header("Authorization") authHeader: String): List<FeedElementModel>

    @Multipart
    @POST("me/posts")
    suspend fun addPost(@Header("Authorization") authHeader: String, @Part("text") text: String?, @Part("lon") lon: Double?, @Part("lat") lat: Double?, @Part image: MultipartBody.Part): FeedElementModel

    @GET("search")
    suspend fun search(@Header("Authorization") authHeader: String, @Query("user") user: String): List<UserModel>
}