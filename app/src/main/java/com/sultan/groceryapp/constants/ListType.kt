package com.sultan.groceryapp.constants

sealed class ListType{
    object MainList : ListType()
    object WishList : ListType()
    object CartList : ListType()
}
