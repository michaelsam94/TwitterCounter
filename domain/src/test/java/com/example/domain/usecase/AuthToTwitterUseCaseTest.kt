package com.example.domain.usecase

import com.example.domain.repository.TweetRepository
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.given
import org.mockito.kotlin.willSuspendableAnswer

class AuthToTwitterUseCaseTest {

    @Test
    fun successfully_gets_access_token() = runBlocking {
        // Arrange
        val requestToken = OAuth1RequestToken("requestToken", "requestTokenSecret")
        val oauthVerifier = "oauthVerifier"
        val expectedAccessToken = OAuth1AccessToken("accessToken", "accessTokenSecret")
        val tweetRepository: TweetRepository = mock()

        given(tweetRepository.getAccessToken(requestToken, oauthVerifier)).willSuspendableAnswer {
            withContext(Dispatchers.Default) { flowOf(expectedAccessToken)  }
        }

        val authToTwitterUseCase = AuthToTwitterUseCase(tweetRepository)

        // Act
        val result = authToTwitterUseCase.getAccessToken(requestToken, oauthVerifier)

        // Assert
        assertEquals(expectedAccessToken, result.first())
    }

    @Test
    fun successfully_gets_request_token() = runBlocking {
        // Arrange
        val expectedRequestToken = OAuth1RequestToken("requestToken", "requestTokenSecret")
        val tweetRepository: TweetRepository = mock()

        given(tweetRepository.getRequestToken()).willSuspendableAnswer {
            withContext(Dispatchers.Default) { flowOf(expectedRequestToken ) }
        }

        val authToTwitterUseCase = AuthToTwitterUseCase(tweetRepository)

        // Act
        val result = authToTwitterUseCase.getRequestToken()

        // Assert
        assertEquals(expectedRequestToken, result.first())
    }

    @Test
    fun successfully_gets_authorization_url() {
        // Arrange
        val requestToken = OAuth1RequestToken("requestToken", "requestTokenSecret")
        val expectedUrl = "https://api.twitter.com/oauth/authorize?oauth_token=requestToken"
        val tweetRepository: TweetRepository = mock()

        given(tweetRepository.getAuthorizationUrl(requestToken)).willReturn(expectedUrl)

        val authToTwitterUseCase = AuthToTwitterUseCase(tweetRepository)

        // Act
        val result = authToTwitterUseCase.getAuthorizationUrl(requestToken)

        // Assert
        assertEquals(expectedUrl, result)
    }
}