package com.example.data.datasource

import com.example.data.dto.TweetDTO
import com.example.data.service.TweetAPI
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.oauth.OAuth10aService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TweetDataSourceImpl(
    private val tweetAPI: TweetAPI,
    private val authService: OAuth10aService
) : TweetDataSource {
    override suspend fun postTweet(tweetDTO: TweetDTO) = tweetAPI.postTweet(tweetDTO)
    override suspend fun getRequestToken(): Flow<OAuth1RequestToken> = flow {
        emit(withContext(Dispatchers.IO) {
            authService.requestToken
        })
    }.catch {
        throw Exception(it)
    }

    override fun getAuthorizationUrl(requestToken: OAuth1RequestToken): String {
        return authService.getAuthorizationUrl(requestToken)
    }

    override suspend fun getAccessToken(
        requestToken: OAuth1RequestToken,
        oauthVerifier: String
    ): Flow<OAuth1AccessToken> =
        flow {
            emit(
                withContext(Dispatchers.IO) {

                    authService.getAccessToken(requestToken, oauthVerifier)
                })
        }.catch {
            throw Exception(it)
        }
}