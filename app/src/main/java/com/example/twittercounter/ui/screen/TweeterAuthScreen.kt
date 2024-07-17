package com.example.twittercounter.ui.screen

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TweeterAuthScreen(
    tweeterAuthState: TweeterAuthState,
) {
    val verifier by tweeterAuthState.verifierValue.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,

        ) {
        tweeterAuthState.authUrl?.let {
            WebViewComponent(it)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = verifier,
                onValueChange = tweeterAuthState.onVerifierValueChanged,
                label = { Text("Enter PIN") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            FilledTonalButton(onClick = tweeterAuthState.onAuthorize) {
                Text("Authorize")
            }
        }
    }

}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewComponent(url: String) {
    AndroidView(factory = {
        WebView(it).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl(url)
        }
    }, update = {
        it.loadUrl(url)
    })
}

data class TweeterAuthState(
    val authUrl: String? = null,
    val onVerifierValueChanged: (String) -> Unit,
    val verifierValue: StateFlow<String>,
    val onAuthorize: () -> Unit
)