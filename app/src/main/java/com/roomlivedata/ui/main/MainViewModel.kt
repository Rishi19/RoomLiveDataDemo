package com.roomlivedata.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.roomlivedata.data.local.LocalRepository
import com.roomlivedata.data.model.User
import kotlinx.coroutines.launch

class MainViewModel(aapContext: Application): AndroidViewModel(aapContext) {

    var roomRepository = LocalRepository(aapContext)

    var usersList: LiveData<List<User>>? = null

    init {
        usersList = roomRepository.getAll()
    }

    fun insertUser(user: User) = viewModelScope.launch {
        roomRepository.insert(user)
    }

    fun update(user: User) = viewModelScope.launch {
        roomRepository.update(user)
    }

    fun delete(user: User) = viewModelScope.launch {
        roomRepository.delete(user)
    }

    fun deleteAll() = viewModelScope.launch {
        roomRepository.deleteAll()
    }
}