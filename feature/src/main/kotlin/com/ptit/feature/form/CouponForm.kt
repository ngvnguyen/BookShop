package com.ptit.feature.form

import com.ptit.data.model.coupon.CouponData
import com.ptit.data.model.coupon.CouponRequestForm
import kotlinx.serialization.SerialName
import java.time.LocalDateTime

data class CouponForm(
    val id:String = "0",
    val code: String = "",
    val description: String="",
    val discountType: CouponData.CouponType= CouponData.CouponType.PERCENTAGE,
    val discountValue: Int = 0,
    val expiresAt: LocalDateTime = LocalDateTime.now(),
    val maximumDiscountAmount: Int = 0,
    val minimumOrderAmount: Int = 0,
    val name: String = "",
    val startsAt: LocalDateTime = LocalDateTime.now(),
    val usageLimit: Int = 0,
    val usageLimitPerCustomer: Int = 0,
    val status: CouponData.Status = CouponData.Status.ACTIVE
)

fun CouponData.toCouponForm() = CouponForm(
    id = id,
    code = code,
    description = description,
    discountType = discountType,
    discountValue = discountValue,
    expiresAt = LocalDateTime.parse(expiresAt),
    maximumDiscountAmount = maximumDiscountAmount,
    minimumOrderAmount = minimumOrderAmount,
    name = name,
    startsAt = LocalDateTime.parse(startsAt),
    usageLimit = usageLimit,
    usageLimitPerCustomer = usageLimitPerCustomer,
    status = status
)

fun CouponForm.toCouponRequestForm() = CouponRequestForm(
    code = code,
    description = description,
    discountType = discountType.toString(),
    discountValue = discountValue,
    expiresAt = expiresAt.toString(),
    maximumDiscountAmount = maximumDiscountAmount,
    minimumOrderAmount = minimumOrderAmount,
    name = name,
    startsAt = startsAt.toString(),
    usageLimit = usageLimit,
    usageLimitPerCustomer = usageLimitPerCustomer,
    status = status
)