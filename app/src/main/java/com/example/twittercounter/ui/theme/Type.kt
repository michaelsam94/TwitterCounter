package com.example.twittercounter.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.twittercounter.R


// Declare the font families
object AppFont {
    val DinNext = FontFamily(
        Font(R.font.din_next_regular),
        Font(R.font.din_next_heavy,FontWeight.ExtraBold),
        Font(R.font.din_next_medium, FontWeight.Medium),
        Font(R.font.din_next_light, FontWeight.Light),
        Font(R.font.din_next_bold, FontWeight.Bold),
        Font(R.font.din_next_black, FontWeight.Black),
        Font(R.font.din_next_ultra_light, FontWeight.ExtraLight)
    )
}

private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = AppFont.DinNext),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = AppFont.DinNext),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = AppFont.DinNext),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = AppFont.DinNext),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = AppFont.DinNext),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = AppFont.DinNext),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = AppFont.DinNext),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = AppFont.DinNext),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = AppFont.DinNext),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = AppFont.DinNext),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = AppFont.DinNext),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = AppFont.DinNext),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = AppFont.DinNext),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = AppFont.DinNext),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = AppFont.DinNext)
)


// Set of Material typography styles to start with
/*
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    */
/* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    *//*

)*/
