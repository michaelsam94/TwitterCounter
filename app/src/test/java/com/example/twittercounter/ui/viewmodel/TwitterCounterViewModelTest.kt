package com.example.twittercounter.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.model.Tweet
import com.example.domain.model.TweetData
import com.example.domain.model.TweetResponse
import com.example.domain.usecase.AuthToTwitterUseCase
import com.example.domain.usecase.CountTweetCharactersUseCase
import com.example.domain.usecase.PostTweetUseCase
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.given


@ExperimentalCoroutinesApi
class TwitterCounterViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var countTweetCharactersUseCase: CountTweetCharactersUseCase

    @Mock
    private lateinit var postTweetUseCase: PostTweetUseCase

    @Mock
    private lateinit var authToTwitterUseCase: AuthToTwitterUseCase



    private lateinit var viewModel: TwitterCounterViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        viewModel = TwitterCounterViewModel(
            countTweetCharactersUseCase,
            postTweetUseCase,
            authToTwitterUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testUpdateCharactersCount() = runTest {
        val content = "Hello, Twitter!"
        val expectedCount = content.length

        given(countTweetCharactersUseCase.invoke(content)).willReturn(expectedCount)

        viewModel.updateCharactersCount(content)

        val actualCount = viewModel.charactersCount.first()
        val actualContent = viewModel.tweetContent.first()

        Assert.assertEquals(expectedCount, actualCount)
        Assert.assertEquals(content, actualContent)
    }

    @Test
    fun testClearCharactersCount() = runTest {
        viewModel.clearCharactersCount()

        val actualCount = viewModel.charactersCount.first()
        val actualContent = viewModel.tweetContent.first()

        Assert.assertEquals(0, actualCount)
        Assert.assertEquals("", actualContent)
    }

    @Test
    fun testPostTweet() = runTest {
        val tweetContent = "Hello, Twitter!"
        val tweet = Tweet(tweetContent)
        val tweetResponse = TweetResponse(TweetData())
        val flowTweetResponse = flowOf(tweetResponse)

        given(postTweetUseCase.invoke(tweet)).willReturn(flowTweetResponse)

        viewModel.updateCharactersCount(tweetContent)
        viewModel.postTweet()

        val actualResult = viewModel.postResult.first()
        val actualCount = viewModel.charactersCount.first()
        val actualContent = viewModel.tweetContent.first()

        Assert.assertEquals(tweetResponse, actualResult)
        Assert.assertEquals(0, actualCount)
        Assert.assertEquals("", actualContent)
    }

    @Test
    fun testGetRequestToken() = runTest {
        val testScope = TestScope()
        val requestToken = OAuth1RequestToken("requestToken", "requestTokenSecret")
        val flowRequestToken = flowOf(requestToken)



        testScope.launch {
            given(authToTwitterUseCase.getRequestToken()).willReturn(flowRequestToken)
        }


        testScope.advanceUntilIdle()

        val result = viewModel.requestToken.first()

        Assert.assertNotNull(result)
    }


    @Test
    fun testRequestTokenFlow() = runTest {
        // Given
        val expectedRequestToken = OAuth1RequestToken("requestToken", "requestTokenSecret")
        val flowRequestToken = flow {
            emit(expectedRequestToken)
        }
        given(authToTwitterUseCase.getRequestToken()).willReturn(flowRequestToken)

        // When
        advanceUntilIdle() // Ensure all coroutines are processed

        // Then
        val actualRequestToken = flowRequestToken.first()
        Assert.assertEquals(expectedRequestToken, actualRequestToken)
    }

    @Test
    fun testGetAuthorizationUrl() = runTest {
        val requestToken = OAuth1RequestToken("requestToken", "requestTokenSecret")
        val expectedUrl = "https://api.twitter.com/oauth/authorize?oauth_token=requestToken"
        val flowRequestToken = flowOf(requestToken)

        given(authToTwitterUseCase.getRequestToken()).willReturn(flowRequestToken)
        given(authToTwitterUseCase.getAuthorizationUrl(requestToken)).willReturn(expectedUrl)

        val actualUrl = viewModel.authUrl.first()

        Assert.assertEquals(expectedUrl, actualUrl)
    }

    @Test
    fun testGetAccessToken() = runTest {
        val testScope = TestScope(testDispatcher)
        val requestToken = OAuth1RequestToken("requestToken", "requestTokenSecret")
        val oauthVerifier = "verifier"
        val accessToken = OAuth1AccessToken("accessToken", "accessTokenSecret")
        val flowAccessToken = flowOf(accessToken)


        viewModel.updateVerifier(oauthVerifier)
        given(authToTwitterUseCase.getRequestToken()).willReturn(flowOf(requestToken))
        given(authToTwitterUseCase.getAccessToken(requestToken, oauthVerifier)).willReturn(flowAccessToken)

        testScope.launch {
            viewModel.requestToken.collect()
            viewModel.accessToken.collect()
            viewModel.authUrl.collect()
        }

        testScope.advanceUntilIdle()

        val actualAccessToken = viewModel.accessToken.first()

        Assert.assertEquals(accessToken, actualAccessToken)
    }



}