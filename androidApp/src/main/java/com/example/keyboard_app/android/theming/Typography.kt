package com.example.keyboard_app.android.theming

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.keyboard_app.android.R

@Composable
fun AppTypography(): Typography {
    val appfont = FontFamily(
        Font(R.font.inter_bold, FontWeight.Bold),
        Font(R.font.inter_regular, FontWeight.Normal)
    )

    val dynaPuff = FontFamily(
        Font(R.font.inter_bold, FontWeight.Bold),
        Font(R.font.inter_regular, FontWeight.Normal),
        Font(R.font.inter_medium, FontWeight.Medium),
        Font(R.font.inter_semi_bold, FontWeight.SemiBold),
    )

    return Typography(
        headlineLarge = TextStyle(
            fontFamily = dynaPuff,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = appfont,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        displaySmall = TextStyle(
            fontFamily = dynaPuff,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    )

}