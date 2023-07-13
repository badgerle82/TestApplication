package com.example.testapplication.feature.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R

class BootEventsAdapter(
) : RecyclerView.Adapter<BootEventsAdapter.BootEventsViewHolder>() {

    inner class BootEventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.item_text)
    }

    private val diffCallback = object : DiffUtil.ItemCallback<BootEventUiModel>() {
        override fun areItemsTheSame(oldItem: BootEventUiModel, newItem: BootEventUiModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BootEventUiModel, newItem: BootEventUiModel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun updateList(list: List<BootEventUiModel>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BootEventsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)

        return BootEventsViewHolder(view)
    }

    override fun onBindViewHolder(holder: BootEventsViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.textView.text = item.text
    }

    override fun getItemCount() = differ.currentList.size
}