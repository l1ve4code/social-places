package com.example.redmad.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.redmad.data.model.*
import com.example.redmad.data.repository.AppRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AppViewModel(private val repository: AppRepository): ViewModel() {

    // AUTH
    suspend fun register(userData: SignModel): AuthModel? {
        return repository.register(userData)
    }
    suspend fun login(userData: SignModel): AuthModel? {
        return repository.login(userData)
    }
    suspend fun logout(){
        repository.logout()
    }

    // FEED
    suspend fun getFeed(): LiveData<List<FeedElementModel>> {
        repository.getFeed()
        return repository.feed
    }

    suspend fun likeFeed(id: String){
        repository.likeFeed(id)
    }

    suspend fun dislikeFeed(id: String){
        repository.dislikeFeed(id)
    }

    suspend fun getLikedFeed(): LiveData<List<FeedElementModel>> {
        repository.likedFeed()
        return repository.favorite
    }

    // ME
    suspend fun getMe(): LiveData<UserModel> {
        repository.getMe()
        return repository.me
    }
    suspend fun editMe(profileMap: Map<String, String>): UserModel? {
        return repository.editMe(profileMap)
    }

    suspend fun getFriends(): LiveData<List<UserModel>> {
        repository.getFriends()
        return repository.friends
    }

    suspend fun addFriend(user_id: FriendModel){
        repository.addFriend(user_id)
    }

    suspend fun deleteFriend(user_id: String){
        repository.deleteFriend(user_id)
    }

    suspend fun getMePosts(): LiveData<List<FeedElementModel>> {
        repository.getMePosts()
        return repository.mePosts
    }

    suspend fun addMePost(text: String, lon: Double, lat: Double, image: MultipartBody.Part){
        repository.addMePost(text, lon, lat, image)
    }

    // SEARCH
    suspend fun search(user: String): LiveData<List<UserModel>> {
        repository.search(user)
        return repository.search
    }

}