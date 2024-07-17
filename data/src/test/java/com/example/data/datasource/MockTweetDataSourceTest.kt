package com.example.data.datasource

import com.example.data.dto.TweetDTO
import com.example.data.dto.TweetDataDTO
import com.example.data.dto.TweetResponseDTO
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.kotlin.given
import retrofit2.Response


class MockTweetDataSourceTest {

    private val tweetDataSource: TweetDataSource = mock()

    @Test
    fun successfully_posts_tweet() = runBlocking {
        // Arrange
        val tweetDTO = TweetDTO(text = "Hello, world!")
        val expectedResponseDTO = TweetResponseDTO(TweetDataDTO("1", "Hello, world!"))
        val expectedResponse: Response<TweetResponseDTO> = Response.success(expectedResponseDTO)

        given(tweetDataSource.postTweet(tweetDTO)).willReturn(expectedResponse)

        // Act
        val result = tweetDataSource.postTweet(tweetDTO)

        // Assert
        assertEquals(expectedResponse, result)
    }

    @Test
    fun successfully_gets_request_token() = runBlocking {
        // Arrange
        val expectedRequestToken = OAuth1RequestToken("requestToken", "requestTokenSecret")

        given(tweetDataSource.getRequestToken()).willReturn(flowOf(expectedRequestToken))

        // Act
        val result = tweetDataSource.getRequestToken().first()

        // Assert
        assertEquals(expectedRequestToken, result)
    }

    @Test
    fun successfully_gets_authorization_url() {
        // Arrange
        val requestToken = OAuth1RequestToken("requestToken", "requestTokenSecret")
        val expectedUrl = "https://api.twitter.com/oauth/authorize?oauth_token=requestToken"

        given(tweetDataSource.getAuthorizationUrl(requestToken)).willReturn(expectedUrl)

        // Act
        val result = tweetDataSource.getAuthorizationUrl(requestToken)

        // Assert
        assertEquals(expectedUrl, result)
    }

    @Test
    fun successfully_gets_access_token() = runBlocking {
        // Arrange
        val requestToken = OAuth1RequestToken("requestToken", "requestTokenSecret")
        val oauthVerifier = "oauthVerifier"
        val expectedAccessToken = OAuth1AccessToken("accessToken", "accessTokenSecret")

        given(tweetDataSource.getAccessToken(requestToken, oauthVerifier)).willReturn(flowOf(expectedAccessToken))

        // Act
        val result = tweetDataSource.getAccessToken(requestToken, oauthVerifier).first()

        // Assert
        assertEquals(expectedAccessToken, result)
    }
}