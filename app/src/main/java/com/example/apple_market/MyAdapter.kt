package com.example.apple_market

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.apple_market.databinding.MarketItemBinding
import java.text.DecimalFormat

class MyAdapter(private val items : MutableList<Item>) : RecyclerView.Adapter<MyAdapter.Holder>() {

    interface ItemClick {
        fun onClick(view: View, position : Int) //데이터 받고 메인액티비티에서 재정의
    }

    interface LongClick {
        fun onLongClick(view:View, position: Int)
    }

    var itemClick : ItemClick?=null
    var longClick : LongClick?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.Holder {
        val binding=MarketItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: MyAdapter.Holder, position: Int) {
        holder.itemView.setOnClickListener { //클릭이벤트추가부분, 여기서 it은 클릭 이벤트가 발생한 뷰를 말함
            itemClick?.onClick(it,position)
        }
        holder.itemView.setOnLongClickListener {
            longClick?.onLongClick(it,position)
            false
        }
        holder.img.setImageResource(items[position].imgData)
        holder.address.text=items[position].addressData
        holder.goodsName.text=items[position].goodsData
        val dec=DecimalFormat("#,###")
        val convert=dec.format(items[position].priceData)
        holder.price.text=convert.toString()+"원"
        holder.like.text=items[position].likeData.toString()
        holder.comment.text=items[position].commentData.toString()

        if(items[position].isLike){
            holder.heart.setImageResource(R.drawable.full_heart)
        }else {
            holder.heart.setImageResource(R.drawable.heart)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    //viewHolder생성
    inner class Holder(private val binding : MarketItemBinding) : RecyclerView.ViewHolder(binding.root){
        val img=binding.imageView
        val goodsName=binding.goodsName
        val address=binding.address
        val price=binding.price
        val like=binding.like
        val comment=binding.comment

        val heart=binding.heart
    }

}