package com.example.apple_market

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(val imgData:Int, val goodsData:String,
                val addressData:String, val priceData:Int, val introduceData:String, val sellerData:String,
                var likeData:Int, val commentData:Int, var isLike:Boolean):Parcelable
