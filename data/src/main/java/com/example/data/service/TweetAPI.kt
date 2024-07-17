package com.example.data.service

import com.example.data.dto.TweetDTO
import com.example.data.dto.TweetResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TweetAPI {
    @POST("/2/tweets")
    suspend fun postTweet(
        @Body tweetDTO: TweetDTO
    ): Response<TweetResponseDTO>
}