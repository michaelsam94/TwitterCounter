package com.example.twittercounter.di

import android.content.Context
import com.example.domain.repository.TweetRepository
import com.example.domain.usecase.AuthToTwitterUseCase
import com.example.domain.usecase.CountTweetCharactersUseCase
import com.example.domain.usecase.PostTweetUseCase
import com.example.twittercounter.util.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun countTweetCharactersUseCase() =
        CountTweetCharactersUseCase()

    @Provides
    fun postTweetUseCase(tweetRepository: TweetRepository) =
        PostTweetUseCase(tweetRepository)

    @Suppress
    @Provides
    fun appPreferences(@ApplicationContext context: Context) = AppPreferences(context)

    @Provides
    fun authToTwitterUseCase(tweetRepository: TweetRepository) = AuthToTwitterUseCase(tweetRepository)



}