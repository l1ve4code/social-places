package com.example.redmad.data.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.redmad.data.api.AppService
import com.example.redmad.data.model.*
import com.example.redmad.utils.NetworkUtils
import okhttp3.Callback
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response


class AppRepository(
    private val appService: AppService,
    private val applicationContext: Context
) {

    // USER
    private val USER_TOKEN = MutableLiveData<AuthModel>()

    suspend fun register(userData: SignModel): AuthModel? {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = appService.register(userData)
            USER_TOKEN.postValue(result)
            return result
        }
        return null
    }

    suspend fun login(userData: SignModel): AuthModel? {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = appService.login(userData)
            USER_TOKEN.postValue(result)
            println(result)
            return result
        }
        return null
    }

    suspend fun logout() {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            appService.logout("Bearer " + USER_TOKEN.value?.access_token)
        }
    }

    // FEED
    private val feedLiveData = MutableLiveData<List<FeedElementModel>>()
    private val favoriteLiveData = MutableLiveData<List<FeedElementModel>>()

    val feed: LiveData<List<FeedElementModel>>
        get() = feedLiveData

    val favorite: LiveData<List<FeedElementModel>>
        get() = favoriteLiveData

    suspend fun getFeed() {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = appService.getFeed("Bearer " + USER_TOKEN.value?.access_token)
            feedLiveData.postValue(result)
        }
    }

    suspend fun likeFeed(id: String) {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = appService.likeFeed(id, "Bearer " + USER_TOKEN.value?.access_token)
        }
    }

    suspend fun dislikeFeed(id: String) {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = appService.dislikeFeed(id, "Bearer " + USER_TOKEN.value?.access_token)
        }
    }

    suspend fun likedFeed() {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = appService.favorite("Bearer " + USER_TOKEN.value?.access_token)
            favoriteLiveData.postValue(result)
        }
    }

    // ME

    private val meLiveData = MutableLiveData<UserModel>()
    private val friendsLiveData = MutableLiveData<List<UserModel>>()
    private val mePostsLiveData = MutableLiveData<List<FeedElementModel>>()

    val me: LiveData<UserModel>
        get() = meLiveData

    val friends: LiveData<List<UserModel>>
        get() = friendsLiveData

    val mePosts: LiveData<List<FeedElementModel>>
        get() = mePostsLiveData

    suspend fun getMe() {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = appService.getMe("Bearer " + USER_TOKEN.value?.access_token)
            meLiveData.postValue(result)
        }
    }

    suspend fun editMe(profileMap: Map<String, String>): UserModel? {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = appService.patchMe("Bearer " + USER_TOKEN.value?.access_token, profileMap)
            meLiveData.postValue(result)
            return result
        }
        return null
    }

    suspend fun getFriends() {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = appService.getFriends("Bearer " + USER_TOKEN.value?.access_token)
            friendsLiveData.postValue(result)
        }
    }

    suspend fun addFriend(user_id: FriendModel) {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = appService.addFriend("Bearer " + USER_TOKEN.value?.access_token, user_id)
        }
    }

    suspend fun deleteFriend(user_id: String) {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result =
                appService.deleteFriend(user_id, "Bearer " + USER_TOKEN.value?.access_token)
        }
    }

    suspend fun getMePosts() {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = appService.getPosts("Bearer " + USER_TOKEN.value?.access_token)
            mePostsLiveData.postValue(result)
        }
    }

    suspend fun addMePost(text: String, lon: Double, lat: Double, image: MultipartBody.Part) {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result =
                appService.addPost("Bearer " + USER_TOKEN.value?.access_token, text, lon, lat, image)

            getMePosts()

        }
    }

    // SEARCH
    private val searchLiveData = MutableLiveData<List<UserModel>>()

    val search: LiveData<List<UserModel>>
        get() = searchLiveData


    suspend fun search(user: String) {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result =
                appService.search("Bearer " + USER_TOKEN.value?.access_token, user)
            searchLiveData.postValue(result)
        }
    }

}
