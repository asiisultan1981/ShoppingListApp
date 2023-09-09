package com.sultan.groceryapp

interface ClickListener {
    fun onCheckBoxClick(position:Int)
    fun onCbHeartClick(position: Int)
    fun onItemViewClick(position:Int)
    fun onCartedClick(position: Int)
}