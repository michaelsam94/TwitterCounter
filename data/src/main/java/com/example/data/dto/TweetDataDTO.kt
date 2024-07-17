package com.example.data.dto

import com.example.domain.model.TweetData

data class TweetDataDTO(
    val id: String = "",
    val text: String = ""
)

fun TweetDataDTO.tweetDataDTOToTweetData(): TweetData {
    return TweetData(
        id = this.id,
        text = this.text
    )
}

fun TweetData.tweetDataToTweetDataDTO(): TweetDataDTO {
    return TweetDataDTO(
        id = this.id,
        text = this.text
    )
}