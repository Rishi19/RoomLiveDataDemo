package com.roomlivedata.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.roomlivedata.data.model.User


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        internal var roomDatabase: LocalDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): LocalDatabase? {
            if (roomDatabase == null) {
                roomDatabase = Room.databaseBuilder(context, LocalDatabase::class.java, "user_db")
//                    .addMigrations(MIGRATION_1_2)
//                    .allowMainThreadQueries()
                    .build()
            }
            return roomDatabase
        }

//        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//
//                database.execSQL("ALTER TABLE user ADD COLUMN salary TEXT DEFAULT 0 NOT NULL")
//
//                // Change the table name to the correct one
//                database.execSQL(
//                    "ALTER TABLE user RENAME TO user_old")
//
//                database.execSQL(
//                    "CREATE TABLE user (name TEXT NOT NULL, age TEXT NOT NULL, salary TEXT NOT NULL, PRIMARY KEY(name))")
//
//                // Copy the data
//                database.execSQL(
//                    "INSERT INTO user (name, age, salary) SELECT first_name, age, salary FROM user_old")
//
//                // Remove the old table
//                database.execSQL("DROP TABLE user_old")
//            }
//        }
    }

}
