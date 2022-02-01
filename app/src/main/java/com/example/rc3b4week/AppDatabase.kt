package com.example.rc3b4week

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ToChange :: class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toChangeDao(): ToChangeDao

    companion object{
        private var instance : AppDatabase? = null

        @Synchronized
        fun getInstance(context : Context) : AppDatabase? {
            if(instance == null){
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "db_change"
                    ).build()
                }
            }
            return instance
        }
    }
}