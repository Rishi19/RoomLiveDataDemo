package com.roomlivedata.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.roomlivedata.R
import com.roomlivedata.databinding.ListRoomUsersBinding
import com.roomlivedata.data.model.User

open class UserAdapter(
    private val context: Context,
    private val list: List<User>,
    private val listener: AdapterClickListener
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ListRoomUsersBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {

            binding.ivEdit.setOnClickListener(this)
            binding.ivDelete.setOnClickListener(this)
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            when {
                v === binding.ivEdit -> listener.onEditClick(adapterPosition)
                v === binding.ivDelete -> listener.onDeleteClick(adapterPosition)
                v === binding.root -> listener.onViewClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = DataBindingUtil.inflate<ListRoomUsersBinding>(
            inflater,
            R.layout.list_room_users,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.model = item
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getItem(id: Int): User {
        return list[id]
    }
}


