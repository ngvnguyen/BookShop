package com.ptit.feature.domain

enum class FilterPriceItem(
    val minPrice:String,
    val maxPrice:String?
) {
    First("0","50.000"),
    Second("50.000","100.000"),
    Third("100.000","200.000"),
    Fourth("200.000","500.000"),
    Final("500.000",null)
}