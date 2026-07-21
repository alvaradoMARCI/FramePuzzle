package com.jhoel.framepuzzle.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

val FramePuzzleTypography = Typography(
    headlineMedium = Typography().headlineMedium.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
    ),
    titleLarge = Typography().titleLarge.copy(
        fontWeight = FontWeight.SemiBold,
    ),
    bodyLarge = Typography().bodyLarge.copy(
        fontWeight = FontWeight.Normal,
    ),
)
