package com.example.chefai.Views

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chefai.R
import com.example.chefai.dto.RecipeData

class MyAdapter(private val recipeData: ArrayList<RecipeData>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = recipeData[position]
        Log.d("curr", currentItem.toString())
        holder.responseTitle.text = currentItem!!.recipeTitle
        holder.responseBody.text = currentItem!!.recipeData


    }

    override fun getItemCount(): Int {
        Log.d("size", recipeData.size.toString())
        return recipeData.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val responseTitle: TextView = itemView.findViewById(R.id.response_title)
        val responseBody: TextView = itemView.findViewById(R.id.response_body)

    }

}