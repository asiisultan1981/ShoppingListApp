package com.sultan.groceryapp

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.sultan.groceryapp.constants.ListType
import com.sultan.groceryapp.database.Item

class ItemAdapter(
    private val context: Context,
    private val clickListener: ClickListener,
    var list: MutableList<Item>,
    var listType: ListType

) : RecyclerView.Adapter<ItemAdapter.itemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_list_item, parent, false)
        return itemViewHolder(view, clickListener)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {
        val item = list[position]
        val price = item.getPrice()
        holder.apply {
            name.text = item.name
            rate.text = item.rate.toString().toDouble().toString()
            quantity.text = item.quantity.toString().toDouble().toString()
            checkBox.isChecked = item.isSelected
            cbHeart.isChecked = item.isWished
            tvPrice.text = price.toString()


            if (item.isSelected) {
                checkBox.isChecked = true
                rowView.setBackgroundColor(context.getColor(R.color.purple_200))

            } else {
                checkBox.isChecked = false
                rowView.setBackgroundColor(context.getColor(R.color.white))
            }

            if (item.isWished) {
                cbHeart.isChecked = true
                rowView.setBackgroundColor(context.getColor(android.R.color.holo_red_light))

            } else {
                cbHeart.isChecked = false
                rowView.setBackgroundColor(context.getColor(R.color.white))
            }

            if (item.isCarted){
                cbCart.isChecked = true
                rowView.setBackgroundColor(context.getColor(android.R.color.holo_green_light))
            }



        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateItem(position: Int, item: Item) {
        list[position] = item
        notifyItemChanged(position)
    }

    fun setMyList(list: MutableList<Item>, listType: ListType) {
        this.list = list
        this.listType = listType
        notifyDataSetChanged()
    }
    fun setMyList(list: MutableList<Item>) {
        this.list = list

        notifyDataSetChanged()
    }


    fun getMyList(): MutableList<Item> {
        return list
    }


    inner class itemViewHolder(row: View, clickListener: ClickListener) :
        RecyclerView.ViewHolder(row) {
        var name = row.findViewById<TextView>(R.id.name)
        var rate = row.findViewById<TextView>(R.id.tvRate)
        var quantity = row.findViewById<TextView>(R.id.tvQuantity)
        var tvPrice = row.findViewById<TextView>(R.id.tvPrice)
        var checkBox = row.findViewById<CheckBox>(R.id.checkBox)
        var cbHeart:CheckBox = row.findViewById(R.id.checkBoxHeart)
        var cbCart:CheckBox = row.findViewById(R.id.checkboxCart)
        var rowView: View = row.findViewById(R.id.rowView)

        init {
            row.setOnClickListener {
                clickListener.onItemViewClick(bindingAdapterPosition)
            }
            checkBox.setOnClickListener {
                clickListener.onCheckBoxClick(bindingAdapterPosition)
            }
            cbHeart.setOnClickListener {
                clickListener.onCbHeartClick(bindingAdapterPosition)
            }
            cbCart.setOnClickListener {
                clickListener.onCartedClick(bindingAdapterPosition)
            }
        }
    }
}