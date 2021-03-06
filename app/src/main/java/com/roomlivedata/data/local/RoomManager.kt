package com.roomlivedata.data.local

import androidx.lifecycle.LiveData
import com.roomlivedata.data.model.User

interface RoomManager {

    fun getAll(): LiveData<List<User>>
    suspend fun insert(callback: CallbackManager, user: User)
    suspend fun update(callback: CallbackManager, user: User)
    suspend fun delete(callback: CallbackManager, user: User)
    fun deleteAll(callback: CallbackManager)

    interface CallbackManager {
        fun onSetMessage(msg: String)
    }
}