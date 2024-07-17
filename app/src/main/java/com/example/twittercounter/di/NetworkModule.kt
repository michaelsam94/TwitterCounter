package com.example.twittercounter.di

import android.util.Log.d
import com.example.data.service.TweetAPI
import com.example.twittercounter.BuildConfig
import com.example.twittercounter.util.AppPreferences
import com.example.twittercounter.util.Constants
import com.example.twittercounter.util.Constants.API_KEY
import com.example.twittercounter.util.Constants.API_SECRET_KEY
import com.example.twittercounter.util.TwitterOAuthUtil
import com.github.scribejava.apis.TwitterApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.oauth.OAuth10aService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun retrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun retrofitService(retrofit: Retrofit): TweetAPI {
        return retrofit.create(TweetAPI::class.java)
    }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: okhttp3.Interceptor,
    ): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(headerInterceptor)
        if (BuildConfig.DEBUG) builder.addInterceptor(loggingInterceptor)
        return builder.build()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message -> d("OkHttp", message) }.apply {
            if (BuildConfig.DEBUG) setLevel(
                HttpLoggingInterceptor.Level.BODY
            )
        }
    }

    @Provides
    fun provideHeaderInterceptor(appPreferences: AppPreferences): okhttp3.Interceptor {
        return okhttp3.Interceptor { chain: okhttp3.Interceptor.Chain ->
            val timeoutSeconds = 10000
            val request: okhttp3.Request = chain.request()
            val requestBuilder: okhttp3.Request.Builder = request.newBuilder()
            requestBuilder.addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Timeout", timeoutSeconds.toString())
            appPreferences.getAccessToken()?.let { accessToken ->
                requestBuilder.addHeader(
                    "Authorization", TwitterOAuthUtil.getAuthorizationHeader(
                        API_KEY,
                        API_SECRET_KEY,
                        accessToken,
                        appPreferences.getAccessTokenSecret()!!,
                        request.method,
                        request.url.toString()
                    )
                )

            }
            chain.proceed(requestBuilder.build())
        }
    }

    @Singleton
    @Provides
    fun authService(): OAuth10aService {
        return ServiceBuilder(API_KEY).apiSecret(API_SECRET_KEY).callback("oob").build(
            TwitterApi.instance()
        )
    }

}