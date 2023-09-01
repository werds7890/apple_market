package com.example.apple_market

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.example.apple_market.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private var likeCounting=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val itemList= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {  //api 33이상 안드로이드 13이상
            intent.getParcelableExtra("item",Item::class.java)
        } else {
            intent.getParcelableExtra("item")
        }
        var like=itemList?.isLike
        heartView(like,binding)
        binding.like.setOnClickListener {
            likeCounting=!likeCounting  //좋아요 카운트(좋아요 눌러져있는상태라면 좋아요수더이상X)
            like=!like!!
            if(like==LikeResponse.YES){
                binding.like.setImageResource(R.drawable.full_heart)
            }else {
                binding.like.setImageResource(R.drawable.heart)
            }
        }
        if(itemList!=null){
            binding.goodsData.text=itemList.goodsData
            binding.detailImg.setImageResource(itemList.imgData)
            binding.sellerData.text=itemList.sellerData
            binding.addressData.text=itemList.addressData
            binding.introduceData.text=itemList.introduceData
            val dec= DecimalFormat("#,###")
            val convert=dec.format(itemList.priceData)
            binding.priceData.text=convert+"원"
        }
        binding.previous.setOnClickListener {
            var positionData=intent.getIntExtra("itemPosition",0)
            intent.putExtra("likeResult",like)
            intent.putExtra("position",positionData)
            intent.putExtra("likeCount",likeCounting)
            setResult(RESULT_OK,intent)
            finish()
        }
    }

    fun heartView(like:Boolean?,binding:ActivityDetailBinding){
        if(like==LikeResponse.YES){
            binding.like.setImageResource(R.drawable.full_heart)
        }else {
            binding.like.setImageResource(R.drawable.heart)
        }
    }
}