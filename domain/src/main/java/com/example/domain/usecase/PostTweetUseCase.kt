package com.example.domain.usecase

import com.example.domain.model.Tweet
import com.example.domain.repository.TweetRepository

class PostTweetUseCase (private val tweetRepository: TweetRepository) {
    suspend operator fun invoke(tweet: Tweet) = tweetRepository.postTweet(tweet)
}