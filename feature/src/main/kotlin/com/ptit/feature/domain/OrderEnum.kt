package com.ptit.feature.domain

enum class OrderEnum(val title:String, val index:Int) {
    PENDING("Pending",0),
    PROCESSING("Processing",1),
    SHIPPED("Shipped",2),
    DELIVERED("Delivered",3),
    CANCELLED("Cancelled",4);
    companion object{
        fun getOrderTabByIndex(index:Int):OrderEnum{
            return OrderEnum.entries.firstOrNull { it.index == index }?:PENDING
        }
    }
}