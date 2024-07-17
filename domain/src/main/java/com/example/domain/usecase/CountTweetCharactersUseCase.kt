package com.example.domain.usecase

class CountTweetCharactersUseCase() {
    operator fun invoke(content: String): Int {
        /**     returns the number of Unicode code points in the string.
        This method counts characters correctly,
        including those represented by surrogate pairs,
        such as certain emojis.*/
        return content.codePointCount(0, content.length)
    }

}