package com.sultan.groceryapp.database

import androidx.room.*

@Dao
interface ItemDao {
    @Insert
    suspend fun insertItem(item: Item)

    @Delete
    suspend fun deleteItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM item ORDER BY id DESC")
    suspend fun showALlItems(): List<Item>

    @Query("SELECT * FROM item WHERE name LIKE :query")
    suspend fun searchItemsList(query:String) : List<Item>

}