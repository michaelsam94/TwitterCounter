package com.example.domain.repository

import com.example.domain.model.Tweet
import com.example.domain.model.TweetData
import com.example.domain.model.TweetResponse
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.given

class MockTweetRepositoryTest {

    private val tweetRepository: TweetRepository = mock()

    @Test
    fun successfully_posts_tweet() = runBlocking {
        // Arrange
        val tweet = Tweet(text = "Hello, world!")
        val expectedResponse = TweetResponse(TweetData("1", "Hello, world!"))

        given(tweetRepository.postTweet(tweet)).willReturn(flowOf(expectedResponse))

        // Act
        val result = tweetRepository.postTweet(tweet).first()

        // Assert
        assertEquals(expectedResponse, result)
    }

    @Test
    fun successfully_gets_request_token() = runBlocking {
        // Arrange
        val expectedRequestToken = OAuth1RequestToken("requestToken", "requestTokenSecret")

        given(tweetRepository.getRequestToken()).willReturn(flowOf(expectedRequestToken))

        // Act
        val result = tweetRepository.getRequestToken().first()

        // Assert
        assertEquals(expectedRequestToken, result)
    }

    @Test
    fun successfully_gets_authorization_url() {
        // Arrange
        val requestToken = OAuth1RequestToken("requestToken", "requestTokenSecret")
        val expectedUrl = "https://api.twitter.com/oauth/authorize?oauth_token=requestToken"

        given(tweetRepository.getAuthorizationUrl(requestToken)).willReturn(expectedUrl)

        // Act
        val result = tweetRepository.getAuthorizationUrl(requestToken)

        // Assert
        assertEquals(expectedUrl, result)
    }

    @Test
    fun successfully_gets_access_token() = runBlocking {
        // Arrange
        val requestToken = OAuth1RequestToken("requestToken", "requestTokenSecret")
        val oauthVerifier = "oauthVerifier"
        val expectedAccessToken = OAuth1AccessToken("accessToken", "accessTokenSecret")

        given(tweetRepository.getAccessToken(requestToken, oauthVerifier)).willReturn(flowOf(expectedAccessToken))

        // Act
        val result = tweetRepository.getAccessToken(requestToken, oauthVerifier).first()

        // Assert
        assertEquals(expectedAccessToken, result)
    }
}