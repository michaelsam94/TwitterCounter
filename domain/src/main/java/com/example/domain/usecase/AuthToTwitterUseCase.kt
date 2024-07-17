package com.example.domain.usecase

import com.example.domain.repository.TweetRepository
import com.github.scribejava.core.model.OAuth1RequestToken

class AuthToTwitterUseCase(private val tweetRepository: TweetRepository) {
    suspend fun getAccessToken(requestToken: OAuth1RequestToken, oauthVerifier: String) =
        tweetRepository.getAccessToken(requestToken, oauthVerifier)

    suspend fun getRequestToken() = tweetRepository.getRequestToken()

    fun getAuthorizationUrl(requestToken: OAuth1RequestToken) =
        tweetRepository.getAuthorizationUrl(requestToken)
}