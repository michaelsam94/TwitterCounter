package com.example.twittercounter.util

import android.os.Build
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import android.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object TwitterOAuthUtil {

    private const val HMAC_SHA1_ALGORITHM = "HmacSHA1"


    fun getAuthorizationHeader(
        apiKey: String,
        apiSecretKey: String,
        accessToken: String,
        accessTokenSecret: String,
        httpMethod: String,
        baseURL: String
    ): String {
        val timestamp = System.currentTimeMillis() / 1000
        val nonce = System.nanoTime().toString()

        val oauthParams = sortedMapOf(
            "oauth_consumer_key" to apiKey,
            "oauth_nonce" to nonce,
            "oauth_signature_method" to "HMAC-SHA1",
            "oauth_timestamp" to timestamp.toString(),
            "oauth_token" to accessToken,
            "oauth_version" to "1.0"
        )

        val parameterString = oauthParams.map { "${encode(it.key)}=${encode(it.value)}" }.joinToString("&")
        val baseString = "$httpMethod&${encode(baseURL)}&${encode(parameterString)}"
        val signingKey = "${encode(apiSecretKey)}&${encode(accessTokenSecret)}"
        val signature = generateSignature(baseString, signingKey)

        oauthParams["oauth_signature"] = signature

        return "OAuth " + oauthParams.map { "${it.key}=\"${encode(it.value)}\"" }.joinToString(", ")
    }

    private fun encode(value: String): String {
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString()).replace("+", "%20").replace("*", "%2A").replace("%7E", "~")
    }

    private fun generateSignature(data: String, key: String): String {
        val signingKey = SecretKeySpec(key.toByteArray(), HMAC_SHA1_ALGORITHM)
        val mac = Mac.getInstance(HMAC_SHA1_ALGORITHM)
        mac.init(signingKey)
        val rawHmac = mac.doFinal(data.toByteArray())
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            java.util.Base64.getEncoder().encodeToString(rawHmac)
        } else {
            Base64.encodeToString(rawHmac, Base64.NO_WRAP)
        }
    }
}