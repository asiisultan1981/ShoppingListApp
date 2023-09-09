package com.sultan.groceryapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters
@Database(entities = [Item::class], version = 1)
abstract class ItemDatabase:RoomDatabase() {

    abstract val itemDao: ItemDao

    companion object {
        @Volatile
        private var INSTANCE: ItemDatabase?= null

        fun getInstance(context: Context): ItemDatabase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ItemDatabase::class.java,
                        "itemDB"
                    ).build()
                }
                return instance
            }
        }
    }
}