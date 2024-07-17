package com.example.data.dto

import com.example.domain.model.Tweet

data class TweetDTO (
    val text: String
)
fun TweetDTO.tweetDTOToTweet(): Tweet {
    return Tweet(
        text = this.text
    )
}

fun Tweet.tweetToTweetDTO(): TweetDTO {
    return TweetDTO(
        text = this.text
    )
}