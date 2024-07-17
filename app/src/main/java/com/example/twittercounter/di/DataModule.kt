package com.example.twittercounter.di

import com.example.data.datasource.TweetDataSource
import com.example.data.datasource.TweetDataSourceImpl
import com.example.data.repository.TweetRepositoryImpl
import com.example.data.service.TweetAPI
import com.example.domain.repository.TweetRepository
import com.github.scribejava.core.oauth.OAuth10aService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    fun provideTweetDataSource(tweetAPI: TweetAPI, authService: OAuth10aService): TweetDataSource =
        TweetDataSourceImpl(tweetAPI, authService)

    @Provides
    fun provideTweetRepository(tweetDataSource: TweetDataSource): TweetRepository =
        TweetRepositoryImpl(tweetDataSource)
}