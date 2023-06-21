package com.example.inshortsnewsapp.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.inshortsnewsapp.R

// Set of Material typography styles to start with
val Poppins = FontFamily(
    Font(R.font.poppins_regular),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

val Typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)