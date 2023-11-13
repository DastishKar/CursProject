package com.example.cursproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cursproject.R
import com.example.cursproject.data.list.UserData
import com.example.cursproject.databinding.LayoutListViewBinding

class MainAdapter: ListAdapter<UserData, MainAdapter.Holder>(Comparator()) {
    class Holder(view: View): RecyclerView.ViewHolder(view){
        private val binding = LayoutListViewBinding.bind(view)

        fun bind(userData: UserData) = with(binding){
            cityTextView.text = userData.city
            dateTextView.text = userData.date
            quantityTextView.text = userData.quantity.toString()
        }
    }
    class Comparator: DiffUtil.ItemCallback<UserData>(){
        override fun areItemsTheSame(oldItem: UserData, newItem: UserData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserData, newItem: UserData): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_list_view, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
        fun addItem(userData: UserData) {
            val currentList = currentList.toMutableList()
            currentList.add(userData)
            submitList(currentList)
        }




}