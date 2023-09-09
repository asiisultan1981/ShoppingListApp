package com.sultan.groceryapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sultan.groceryapp.constants.GroceryCategory

@Entity(tableName = "item")
data class Item(
    var name: String,
//    var type : String,
    var type: GroceryCategory?,
    var rate: Double,
    var quantity: Double,
    var isSelected: Boolean = false,
    var isWished: Boolean = false,
    var isCarted: Boolean = false
):java.io.Serializable {
    fun getPrice(): Double {
        return rate * quantity

    }
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}