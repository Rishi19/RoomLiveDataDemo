package com.roomlivedata.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.roomlivedata.data.model.User

@Dao
interface UserDao {

    @get:Query("SELECT * FROM user")
    val all: LiveData<List<User>>

    @Insert
    suspend fun insertAll(users: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM user")
    suspend fun deleteAll()
}
