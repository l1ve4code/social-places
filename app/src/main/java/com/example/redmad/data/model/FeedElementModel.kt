package com.example.redmad.data.model

data class FeedElementModel (
    val id: String,
    val text: String,
    val image_url: String,
    val lon: Double,
    val lat: Double,
    val author: UserModel,
    val likes: Number,
    val liked: Boolean
        )