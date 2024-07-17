package com.example.twittercounter.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.twittercounter.R
import com.example.twittercounter.ui.theme.AppFont
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TwitterCharacterCountScreen(modifier: Modifier, tweetCounterState: TweetCounterState) {
    val charactersCount by tweetCounterState.charactersCount.collectAsStateWithLifecycle()
    val tweetContent by tweetCounterState.tweetContent.collectAsStateWithLifecycle()
    val remainingCharacters by tweetCounterState.characterRemaining.collectAsStateWithLifecycle()
    val isPostButtonEnabled by tweetCounterState.isPostButtonEnabled.collectAsStateWithLifecycle()
    val isClearButtonEnabled by tweetCounterState.isClearButtonEnabled.collectAsStateWithLifecycle()
    val isCopyButtonEnabled by tweetCounterState.isCopyButtonEnabled.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Twitter character count", style = MaterialTheme.typography.headlineLarge)

            Spacer(modifier = Modifier.height(16.dp))

            Icon(
                painter = painterResource(id = R.drawable.ic_twitter),
                contentDescription = "twitter",
                tint = Color(0xff1da1f2)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Box(
                    Modifier
                        .weight(1.0f)
                        .background(color = Color(0xffE6F6FE), RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(3.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            stringResource(R.string.characters_typed), textAlign = TextAlign.Center,
                            fontFamily = AppFont.DinNext,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            Modifier
                                .background(Color.White, RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp))
                                .height(80.dp),

                            ) {
                            Text(
                                text = "${charactersCount}/280",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(alignment = Alignment.Center),
                                fontFamily = AppFont.DinNext,
                                fontWeight = FontWeight.Medium,
                                fontSize = 26.sp,
                                color = Color.Black
                            )
                        }

                    }
                }
                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    Modifier
                        .weight(1.0f)
                        .background(color = Color(0xffE6F6FE), RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(3.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.characters_remaining),
                            textAlign = TextAlign.Center,
                            fontFamily = AppFont.DinNext,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(

                            Modifier
                                .background(Color.White, RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp))
                                .height(80.dp),

                            ) {
                            Text(
                                text = "$remainingCharacters",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(alignment = Alignment.Center),
                                fontFamily = AppFont.DinNext,
                                fontWeight = FontWeight.Medium,
                                fontSize = 26.sp,
                                color = Color.Black
                            )
                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            OutlinedTextField(
                value = tweetContent,
                onValueChange = tweetCounterState.onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                placeholder = {
                    Text(
                        "Start typing! You can enter up to 280 characters",
                        fontFamily = AppFont.DinNext,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                },
                minLines = 8,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color(240, 240, 241, 255),
                    focusedBorderColor = Color(33, 150, 243, 255),
                    unfocusedLabelColor = Color(94, 97, 96, 255)
                ),
                shape = RoundedCornerShape(12.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    enabled = isCopyButtonEnabled,
                    onClick = tweetCounterState.onCopyClick,
                    colors = ButtonDefaults.buttonColors(Color(0xff00A970)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Copy Text", color = Color.White)
                }
                Button(
                    enabled = isClearButtonEnabled,
                    onClick = tweetCounterState.onClearClick,
                    colors = ButtonDefaults.buttonColors(Color(0xffDC0125)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Clear Text", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = tweetCounterState.onPostClick,
                enabled = isPostButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff1da1f2))
            ) {
                Text(
                    "Post tweet",
                    fontFamily = AppFont.DinNext,
                    fontWeight = FontWeight.W700,
                    fontSize = 18.sp,
                    color = Color.White,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

data class TweetCounterState(
    val charactersCount: StateFlow<Int>,
    val tweetContent: StateFlow<String>,
    val isPostButtonEnabled: StateFlow<Boolean>,
    val isClearButtonEnabled: StateFlow<Boolean>,
    val isCopyButtonEnabled: StateFlow<Boolean>,
    val characterRemaining: StateFlow<Int>,
    val onValueChange: (String) -> Unit,
    val onCopyClick: () -> Unit,
    val onClearClick: () -> Unit,
    val onPostClick: () -> Unit
)
