package com.example.twittercounter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.twittercounter.ui.screen.TweetCounterState
import com.example.twittercounter.ui.screen.TweeterAuthScreen
import com.example.twittercounter.ui.screen.TweeterAuthState
import com.example.twittercounter.ui.screen.TwitterCharacterCountScreen
import com.example.twittercounter.ui.theme.TwitterCounterTheme
import com.example.twittercounter.ui.viewmodel.TwitterCounterViewModel
import com.example.twittercounter.util.AppPreferences
import com.example.twittercounter.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: TwitterCounterViewModel by viewModels()

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val nevController = rememberNavController()
            TwitterCounterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = nevController, startDestination = "post") {
                        composable("post") {
                            TwitterCharacterCountScreen(modifier = Modifier.padding(innerPadding),
                                tweetCounterState = TweetCounterState(
                                    charactersCount = viewModel.charactersCount,
                                    tweetContent = viewModel.tweetContent,
                                    isPostButtonEnabled = viewModel.isPostButtonEnabled,
                                    isClearButtonEnabled = viewModel.isClearButtonEnabled,
                                    isCopyButtonEnabled = viewModel.isCopyButtonEnabled,
                                    characterRemaining = viewModel.characterRemaining,
                                    onValueChange = viewModel::updateCharactersCount,
                                    onCopyClick = ::copyTextToClipboard,
                                    onClearClick = viewModel::clearCharactersCount,
                                    onPostClick = {
                                        lifecycleScope.launch {
                                            appPreferences.getAccessTokenSecret()?.isNotEmpty()
                                                ?.let {
                                                    viewModel.postTweet()
                                                    viewModel.postResult.collect {
                                                        Log.d(
                                                            Constants.MAIN_ACTIVITY_TAG,
                                                            "postTweet: $it"
                                                        )
                                                        it?.let {
                                                            Toast.makeText(
                                                                this@MainActivity,
                                                                this@MainActivity.getString(R.string.your_post_available_on_twitter),
                                                                Toast.LENGTH_SHORT,
                                                            ).show()
                                                        }
                                                    }

                                                }
                                            if (appPreferences.getAccessTokenSecret() == null ||
                                                appPreferences.getAccessTokenSecret()?.isEmpty() == true
                                            ) {
                                                viewModel.authUrl
                                                    .collect { authUrl ->
                                                        authUrl?.let {
                                                            nevController.navigate("auth")
                                                        }
                                                    }
                                            }
                                        }

                                    }
                                ))
                        }

                        composable("auth") {
                            TweeterAuthScreen(
                                tweeterAuthState = TweeterAuthState(
                                    authUrl = viewModel.authUrl.value,
                                    onAuthorize = {
                                        lifecycleScope.launch {
                                            viewModel.accessToken.collect { accessToken ->
                                                accessToken?.let {
                                                    appPreferences.setAccessToken(accessToken.token)
                                                    appPreferences.setAccessTokenSecret(accessToken.tokenSecret)
                                                    viewModel.postTweet()
                                                    viewModel.postResult.collect {
                                                        it?.let {
                                                            Toast.makeText(
                                                                this@MainActivity,
                                                                this@MainActivity.getString(R.string.your_post_available_on_twitter),
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            nevController.navigate("post")
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                    },
                                    onVerifierValueChanged = viewModel::updateVerifier,
                                    verifierValue = viewModel.verifier
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun copyTextToClipboard() {
        val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", viewModel.tweetContent.value)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TwitterCounterTheme {
        Greeting("Android")
    }
}