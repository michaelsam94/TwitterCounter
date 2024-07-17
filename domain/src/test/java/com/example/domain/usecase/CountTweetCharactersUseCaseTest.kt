package com.example.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Test

class CountTweetCharactersUseCaseTest {

    private val countTweetCharactersUseCase = CountTweetCharactersUseCase()

    @Test
    fun counts_characters_in_empty_string() {
        // Arrange
        val content = ""

        // Act
        val result = countTweetCharactersUseCase(content)

        // Assert
        assertEquals(0, result)
    }

    @Test
    fun counts_characters_in_standard_string() {
        // Arrange
        val content = "Hello, world!"

        // Act
        val result = countTweetCharactersUseCase(content)

        // Assert
        assertEquals(13, result)
    }

    @Test
    fun counts_characters_in_string_with_emojis() {
        // Arrange
        val content = "Hello, world! 😊"

        // Act
        val result = countTweetCharactersUseCase(content)

        // Assert
        assertEquals(15, result) // 13 + 1 emoji (2 code points)
    }

    @Test
    fun counts_characters_in_string_with_surrogate_pairs() {
        // Arrange
        val content = "Hello, world! \uD83D\uDE00" // Smiley face emoji

        // Act
        val result = countTweetCharactersUseCase(content)

        // Assert
        assertEquals(15, result) // 13 + 1 emoji (2 code points)
    }

    @Test
    fun counts_characters_in_mixed_string() {
        // Arrange
        val content = "Hello, 😊 world! \uD83D\uDE00"

        // Act
        val result = countTweetCharactersUseCase(content)

        // Assert
        assertEquals(17, result) // 13 + 2 emojis (4 code points)
    }
}
