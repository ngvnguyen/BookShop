package com.ptit.bookshop.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ptit.shared.Alpha
import com.ptit.shared.Black
import com.ptit.shared.Gray
import com.ptit.shared.GrayDarker
import com.ptit.shared.GrayLighter
import com.ptit.shared.Orange
import com.ptit.shared.SurfaceBrand
import com.ptit.shared.SurfaceDarker
import com.ptit.shared.SurfaceLighter

private val LightColorScheme = lightColorScheme(
    primary = Orange,
    onPrimary = Black,
    primaryContainer = Orange.copy(alpha = Alpha.TEN_PERCENT),
    onPrimaryContainer = Black,
    background = GrayLighter,
    onBackground = Black,
    surface = SurfaceLighter,
    surfaceVariant = Orange.copy(alpha = Alpha.TEN_PERCENT)
)

@Composable
fun BookShopTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}