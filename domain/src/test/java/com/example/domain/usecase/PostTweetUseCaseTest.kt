package com.example.domain.usecase

import com.example.domain.model.Tweet
import com.example.domain.model.TweetData
import com.example.domain.model.TweetResponse
import com.example.domain.repository.TweetRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.given
import org.mockito.kotlin.willSuspendableAnswer


class PostTweetUseCaseTest {

    @Test
    fun successfully_posts_valid_tweet() = runBlocking {
        // Arrange
        val tweet = Tweet(text = "Hello, world!")
        val tweetResponse = TweetResponse(TweetData("1","Hello, world!"))
        val tweetRepository: TweetRepository = mock()

        given(tweetRepository.postTweet(tweet)).willSuspendableAnswer {
            withContext(Dispatchers.Default) { flowOf(tweetResponse) }
        }

        val postTweetUseCase = PostTweetUseCase(tweetRepository)

        // Act
        val result = postTweetUseCase(tweet).first()

        // Assert
        assertEquals(tweetResponse, result)
    }

    @Test
    fun posts_tweet_with_empty_text() = runBlocking {
        // Arrange
        val tweet = Tweet(text = "")
        val tweetResponse = TweetResponse(TweetData("2",""))
        val tweetRepository: TweetRepository = mock()

        given(tweetRepository.postTweet(tweet)).willSuspendableAnswer {
            withContext(Dispatchers.Default) { flowOf(tweetResponse) }
        }

        val postTweetUseCase = PostTweetUseCase(tweetRepository)

        // Act
        val result = postTweetUseCase(tweet).first()

        // Assert
        assertEquals(tweetResponse, result)
    }

    @Test
    fun posts_tweet_with_special_characters() = runBlocking {
        // Arrange
        val tweet = Tweet(text = "@Hello, #world!")
        val tweetResponse = TweetResponse(TweetData("3","@Hello, #world!"))
        val tweetRepository: TweetRepository = mock()

        given(tweetRepository.postTweet(tweet)).willSuspendableAnswer {
            withContext(Dispatchers.Default) { flowOf(tweetResponse) }
        }

        val postTweetUseCase = PostTweetUseCase(tweetRepository)

        // Act
        val result = postTweetUseCase(tweet).first()

        // Assert
        assertEquals(tweetResponse, result)
    }

    @Test
    fun handles_network_error() = runBlocking {
        // Arrange
        val tweet = Tweet(text = "Hello, world!")
        val tweetRepository: TweetRepository = mock()

        given(tweetRepository.postTweet(tweet)).willSuspendableAnswer {
            withContext(Dispatchers.Default) { throw Exception("Network error") }
        }

        val postTweetUseCase = PostTweetUseCase(tweetRepository)

        // Act
        val result = runCatching { postTweetUseCase(tweet).first() }

        // Assert
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun posts_tweet_with_long_text() = runBlocking {
        // Arrange
        val longText = "a".repeat(280)
        val tweet = Tweet(text = longText)
        val tweetResponse = TweetResponse(TweetData("4", longText))
        val tweetRepository: TweetRepository = mock()

        given(tweetRepository.postTweet(tweet)).willSuspendableAnswer {
            withContext(Dispatchers.Default) { flowOf(tweetResponse) }
        }

        val postTweetUseCase = PostTweetUseCase(tweetRepository)

        // Act
        val result = postTweetUseCase(tweet).first()

        // Assert
        assertEquals(tweetResponse, result)
    }
}


