package com.roomlivedata.ui.main

interface AdapterClickListener {
    fun onViewClick(position: Int)
    fun onEditClick(position: Int)
    fun onDeleteClick(position: Int)
}
