package com.example.uas_happycal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [AteFood::class],
    version = 1,
    exportSchema = false)
abstract class FoodRoomDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao?

    companion object {
        @Volatile
        private var INSTANCE: FoodRoomDatabase? = null
        @OptIn(InternalCoroutinesApi::class)
        fun getDatabase(context: Context) : FoodRoomDatabase? {
            synchronized(FoodRoomDatabase::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FoodRoomDatabase::class.java, "custom_food_database"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }

}