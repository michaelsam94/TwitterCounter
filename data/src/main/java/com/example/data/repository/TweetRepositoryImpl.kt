package com.example.data.repository

import com.example.data.datasource.TweetDataSource
import com.example.data.dto.tweetResponseDTOToTweetResponse
import com.example.data.dto.tweetToTweetDTO
import com.example.domain.model.Tweet
import com.example.domain.repository.TweetRepository
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class TweetRepositoryImpl(
    private val tweetDataSource: TweetDataSource,
) : TweetRepository {
    override suspend fun postTweet(tweet: Tweet) = flow {
        val response = tweetDataSource.postTweet(tweet.tweetToTweetDTO())
        if (response.isSuccessful) {
            response.body()?.let {
                emit(it.tweetResponseDTOToTweetResponse())
            } ?: throw Exception("Response body is null")
        } else
            throw Exception("Response is not successful")
    }.catch {
        throw Exception(it)
    }

    override suspend fun getRequestToken(): Flow<OAuth1RequestToken> = tweetDataSource.getRequestToken()

    override fun getAuthorizationUrl(requestToken: OAuth1RequestToken): String =
        tweetDataSource.getAuthorizationUrl(requestToken)

    override suspend fun getAccessToken(
        requestToken: OAuth1RequestToken,
        oauthVerifier: String
    ): Flow<OAuth1AccessToken> = tweetDataSource.getAccessToken(requestToken, oauthVerifier)
}