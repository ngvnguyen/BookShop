package com.ptit.feature.domain

import androidx.annotation.DrawableRes
import com.ptit.shared.Resources

enum class BannerItem(
    @DrawableRes val image: Int
) {
    First(Resources.Image.Banner1),
    Second(Resources.Image.Banner2),
    Third(Resources.Image.Banner3),
    Fourth(Resources.Image.Banner4)
}