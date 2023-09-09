package com.sultan.groceryapp.constants

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromCategoryToString(groceryCategory: GroceryCategory): String {
        return groceryCategory.name
    }

    @TypeConverter
    fun fromStringToGroceryCategory(grocery: String): GroceryCategory {
        return enumValueOf(grocery)
    }
}