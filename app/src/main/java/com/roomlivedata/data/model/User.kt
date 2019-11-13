package com.roomlivedata.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class User {

    @PrimaryKey
    @ColumnInfo(name = "name")
    var name = ""

    @ColumnInfo(name = "age")
    var age  = 0

    @ColumnInfo(name = "salary")
    var salary  = 0

    override fun toString(): String {
        return "User(name='$name', age=$age, salary=$salary)"
    }
}