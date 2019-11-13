package com.roomlivedata.data.local

import android.content.Context
import androidx.lifecycle.LiveData
import com.roomlivedata.data.model.User

class LocalRepository(context: Context) {

    private var dao = LocalDatabase.getDatabase(context)!!.userDao()

    fun getAll(): LiveData<List<User>> {
        return dao.all
    }

    suspend fun insert(user: User) {
        dao.insertAll(user)
    }

    suspend fun update(user: User) {
        dao.update(user)
    }

    suspend fun delete(user: User) {
        dao.delete(user)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

}
