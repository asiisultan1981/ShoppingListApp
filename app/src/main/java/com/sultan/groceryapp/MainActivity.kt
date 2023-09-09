package com.sultan.groceryapp

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sultan.groceryapp.constants.GroceryCategory
import com.sultan.groceryapp.constants.ListType
import com.sultan.groceryapp.databinding.ActivityMainBinding
import com.sultan.groceryapp.database.ItemDatabase
import com.sultan.groceryapp.database.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ItemAdapter
    private var mainList = mutableListOf<Item>()
    private var cartList = mutableListOf<Item>()
    private var wishList = mutableListOf<Item>()
    private lateinit var clickListenerMain: ClickListener
    private lateinit var toggle: ActionBarDrawerToggle
    private var selectedType: GroceryCategory? = null


    //private var selectedType: String = "pulses" // default
    lateinit var database: ItemDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = ItemDatabase.getInstance(this)

        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                mainList = database.itemDao.showALlItems() as MutableList<Item>
            }
            binding.content.textViewTotal.text = "Total Items: ${mainList.size}"
            mainRecyclerView()


            initUI()

            filterList("")
        }





        binding.buttonAddItems.setOnClickListener {
            addNewItem()
        }

        binding.customToolbar.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.customToolbar.navHamburger.setOnClickListener {
            openOrCloseDrawer()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            mainRecyclerView()
            when (it.itemId) {
                R.id.nav_pulses -> {
                    selectedType = GroceryCategory.PULSES
//                    selectedType = "pulses"
                    Log.e("s", "onCreate: ${selectedType?.name} ")
                    filterList("")
                    openOrCloseDrawer()

                }

                R.id.nav_baking -> {
                    selectedType = GroceryCategory.BAKING
//                    selectedType = "baking"
                    filterList("")
                    openOrCloseDrawer()
                }

                R.id.nav_grains -> {
                    selectedType = GroceryCategory.GRAINS
//                    selectedType = "grains"
                    filterList("")
                    openOrCloseDrawer()

                }

                R.id.nav_produce -> {
                    selectedType = GroceryCategory.PRODUCE
//                    selectedType = "produce"
                    filterList("")
                    openOrCloseDrawer()

                }

                R.id.nav_bakery -> {
                    selectedType = GroceryCategory.BAKERY
//                    selectedType = "bakery"
                    filterList("")
                    openOrCloseDrawer()
                }

                R.id.nav_saucesOils -> {
                    selectedType = GroceryCategory.OILS
//                    selectedType = "saucesoils"
                    filterList("")
                    openOrCloseDrawer()
                }

                R.id.nav_snacks -> {
                    selectedType = GroceryCategory.SNACKS
//                    selectedType = "snacks"
                    filterList("")
                    openOrCloseDrawer()
                }

                R.id.nav_condiments -> {
                    selectedType = GroceryCategory.CONDIMNETS
//                    selectedType = "condiments"
                    filterList("")
                    openOrCloseDrawer()
                }

                R.id.nav_spices -> {
                    selectedType = GroceryCategory.SPICES
//                    selectedType = "spices"
                    filterList("")
                    openOrCloseDrawer()
                }

                R.id.nav_baby -> {
                    selectedType = GroceryCategory.BABY_CARE
//                    selectedType = "baby"
                    filterList("")
                    openOrCloseDrawer()

                }

                R.id.nav_personal -> {
                    selectedType = GroceryCategory.PERSONAL_CARE
//                    selectedType = "personal"
                    filterList("")
                    openOrCloseDrawer()
                }

                R.id.nav_household -> {
                    selectedType = GroceryCategory.HOUSEHOLDS
//                    selectedType = "household"
                    filterList("")
                    openOrCloseDrawer()
                }
            }
            true
        }


        binding.tvTotalCheckout.setOnClickListener {
            var totalPrice: Double = 0.0
            var totalProducts = 0
            cartList.forEach {
                if (it.isSelected) {
                    val price = it.getPrice()
                    totalPrice += price
                    totalProducts++
                }
            }
            binding.content.textViewTotalPrice.text = "Total Rs. $totalPrice"
            binding.tvTotalCheckout.text = "checkout ($totalProducts)"
        }

    }


    private fun filterList(text: String?) {
        val filteredList = mutableListOf<Item>()
        if (selectedType == null) {
            binding.buttonAddItems.visibility = View.GONE
        } else {
            binding.buttonAddItems.visibility = View.VISIBLE
        }

        when (adapter.listType) {

            ListType.MainList -> {
                if (text == null || text == "") {

                    mainList.forEach {
                        if (it.type == selectedType || selectedType == null) {
                            filteredList.add(it)
                            Log.e("click", "filterList when text null: ")
                        }
                    }
                } else {
                    mainList.forEach {
                        if ((it.type == selectedType || selectedType == null) && it.name.lowercase()
                                .contains(text)
                        ) {
                            filteredList.add(it)
                        }
                    }
                }
                adapter.setMyList(filteredList, ListType.MainList)

                if (filteredList.isEmpty()) {

                    Log.e("click", "filterList size: ${filteredList.size}")
                    Log.e("click", "filterList: No Data Found")
                    Toast.makeText(this@MainActivity, "No items added yet", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    for (i in filteredList) {
                        Log.e("click", "MainList data:\n $i \n")
                    }
                }
            }

            ListType.WishList -> {
                if (text == null || text == "") {
                    wishList.forEach {

                        filteredList.add(it)

                    }
                } else {
                    wishList.forEach {
                        if (it.name.lowercase().contains(text)) {
                            filteredList.add(it)
                        }
                    }
                }
                adapter.setMyList(filteredList, ListType.WishList)
                if (filteredList.isEmpty()) {
                    Log.e("click", "filterList: No Data Found")
                    Toast.makeText(this@MainActivity, "No items added yet", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    for (i in filteredList) {
                        Log.e("click", "WishList data: \n $i \n")
                    }
                }
            }

            ListType.CartList -> {
                if (text == null || text == "") {
                    cartList.forEach {
                        filteredList.add(it)
                    }
                } else {
                    cartList.forEach {
                        if (it.name.lowercase().contains(text)) {
                            filteredList.add(it)
                        }
                    }
                }
                adapter.setMyList(filteredList, ListType.CartList)
                if (filteredList.isEmpty()) {
                    Log.e("click", "filterList: No Data Found")
                    Toast.makeText(this@MainActivity, "No items added yet", Toast.LENGTH_SHORT)
                        .show()
                } else {

                    for (i in filteredList) {
                        Log.e("click", "CartList data: \n $i \n")
                    }
                }
            }
        }

    }

    private fun openOrCloseDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun addNewItem() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val dialogView: View = layoutInflater.inflate(R.layout.add_item_dialog, null)
        builder.setView(dialogView)
        val dialog: AlertDialog = builder.create()
        val itemName = dialogView.findViewById<EditText>(R.id.etName)
        val itemRate = dialogView.findViewById<EditText>(R.id.etRate)
        val itemQuantity = dialogView.findViewById<EditText>(R.id.etQuantity)
        val btnSaveItem = dialogView.findViewById<Button>(R.id.BtnSaveItem)
        val btnCancel = dialogView.findViewById<Button>(R.id.BtnCancel)
        btnSaveItem?.setOnClickListener {
            Log.e("d", "addNewItem: clicked")
            val name = itemName?.text.toString()
            val rateText = itemRate?.text.toString()
            val quantityText = itemQuantity?.text.toString()

            if (rateText.isNotEmpty() && quantityText.isNotEmpty()) {
                val rate = rateText.toDouble()
                val quantity = quantityText.toDouble()
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        val item = saveData(name, selectedType!!, rate, quantity)
                        mainList.add(item)

                    }
                    adapter.setMyList(mainList, ListType.MainList)
                    binding.content.textViewTotal.text = "Total Items: ${mainList.size}"
                    selectedType = null
                    adapter.setMyList(mainList, ListType.MainList)
                    filterList("")
                    Toast.makeText(this@MainActivity, "Saved", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
        }
        btnCancel?.setOnClickListener {
            Log.e("d", "addNewItem: clicked")
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

    }

    private suspend fun saveData(
        name: String, selectedType: GroceryCategory, rate: Double, quantity: Double
    ): Item {
        val newItem = Item(name, selectedType, rate, quantity)
        database.itemDao.insertItem(newItem)
        return newItem
    }

    private fun editOrDeleteDialog(position: Int) {
        showCustomDialog(
            R.style.RoundedCornersDialog,
            R.layout.edit_delete_item_dialog,
            R.id.btnEdit,
            R.id.btnDelete,
            { openEditDialog(position) },
            {
                deleteItemAtPosition(position)
//------------------------ adapter.setMyList(mainList, ListType.MainList)
                adapter.setMyList(mainList, ListType.MainList)
            }
        )
    }

    private fun openEditDialog(position: Int) {
        val alertDialog = Dialog(this)
        alertDialog.setCancelable(false)
        alertDialog.setContentView(R.layout.edit_item_dialog)
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        alertDialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        // Initialize and set values to the EditText fields based on the item at the specified position
        // Set up save and cancel buttons similar to your addNewItem function
        val item = adapter.getMyList().get(position)

        val itemName = alertDialog.findViewById<EditText>(R.id.editName)
        val itemRate = alertDialog.findViewById<EditText>(R.id.editRate)
        val itemQuantity = alertDialog.findViewById<EditText>(R.id.editQuantity)
        val btnSaveItem = alertDialog.findViewById<Button>(R.id.editBtnSaveItem)
        val btnCancel = alertDialog.findViewById<Button>(R.id.editBtnCancel)
        itemName.setText(item.name)
        itemRate.setText(item.rate.toString())
        itemQuantity.setText(item.quantity.toString())

        btnSaveItem.setOnClickListener {
            val newName = itemName.text.toString()
            val newRate = itemRate.text.toString()
            val newQuantity = itemQuantity.text.toString()

            if (newRate.isNotEmpty() && newQuantity.isNotEmpty()) {
                val rate = newRate.toDouble()
                val quantity = newQuantity.toDouble()
                item.name = newName
                item.rate = rate
                item.quantity = quantity
                CoroutineScope(Dispatchers.IO).launch {
                    database.itemDao.updateItem(item)
                    withContext(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()
                    }
                }
//                updateItemData(position, newName, rate, quantity)
                filterList("")
                Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_LONG).show()
            }
        }

        btnCancel.setOnClickListener {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            alertDialog.dismiss()
        }
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

    private fun deleteItemAtPosition(position: Int) {
        val item = adapter.getMyList().get(position)
        CoroutineScope(Dispatchers.IO).launch {

            ItemDatabase.getInstance(this@MainActivity).itemDao.deleteItem(item)
            withContext(Dispatchers.Main) {

                mainList.remove(item)
                adapter.notifyItemRemoved(position)
                binding.content.textViewTotal.text = "Total Items: ${mainList.size}"

                toast("deleted")

            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initUI() {
        binding.customToolbar.btnCart.setOnClickListener {
            toast("Now showing CART LIST")
            mainList.forEach {
                if (it.isCarted) {
                    if (!cartList.contains(it)) {
                        cartList.add(it)
                        CoroutineScope(Dispatchers.Main).launch {
                            withContext(Dispatchers.IO) {
                                database.itemDao.updateItem(it)
                            }
                            adapter.notifyDataSetChanged()

                        }
                    } else {
                        toast("Already added")
                    }
                    adapter.setMyList(cartList, ListType.CartList)

                }

            }
            if (cartList.size > 0) {
                binding.customToolbar.btnCart.setColorFilter(Color.GREEN)
            }
            if (cartList.size == 0) {
                toast("CART LIST EMPTY")
            }


        }

        binding.customToolbar.ivWish.setOnClickListener {
            toast("Showing WISH LIST")
            mainList.forEach {
                if (it.isWished) {
                    if (!wishList.contains(it)) {
                        wishList.add(it)
                    } else {
                        toast("Already added")
                    }
                } else {
                    wishList.remove(it)
                }
                CoroutineScope(Dispatchers.IO).launch {
                    database.itemDao.updateItem(it)
                    withContext(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()
                    }
                }
//----------------------------  adapter.setMyList(wishList, ListType.WishList)
                adapter.setMyList(wishList, ListType.WishList)
            }
            if (wishList.size > 0) {
                binding.customToolbar.ivWish.setColorFilter(Color.RED)
            }
            if (wishList.size == 0) {
                toast("WISH LIST EMPTY")
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                selectedType = null
                mainRecyclerView()
                filterList("")

            }
        })

    }

    private fun mainRecyclerView() {
        toast("Now showing MAIN LIST")
        clickListenerMain = object : ClickListener {
            override fun onCheckBoxClick(position: Int) {
                checkBoxClick(position)
            }

            override fun onCbHeartClick(position: Int) {
                cbHeartClick(position)
            }

            override fun onItemViewClick(position: Int) {
                checkBoxClick(position)
            }

            override fun onCartedClick(position: Int) {
                cbCartClick(position)
            }
        }



        adapter = ItemAdapter(this@MainActivity, clickListenerMain, mainList, ListType.MainList)
        binding.content.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.content.recyclerView.adapter = adapter
        filterList("")


    }

    private fun cbCartClick(position: Int) {
        val item = adapter.getMyList().get(position)
        when (adapter.listType) {

            ListType.MainList -> {
                if (item.isCarted) {
                    showAlertDialog(
                        "Yes",
                        "No",
                        "ALERT",
                        "you want to remove this?",
                        {
                            item.isCarted = false
                            adapter.updateItem(position, item)
                            cartList.remove(item)
                            CoroutineScope(Dispatchers.IO).launch {
                                database.itemDao.updateItem(item)
                            }

                            adapter.setMyList(mainList, ListType.MainList)
                            if (cartList.isEmpty()) {
                                toast("Cart list is empty now")
                            }
                        },
                        { toast("cancelled") }
                    )
                } else {
                    item.isCarted = true
                    adapter.updateItem(position, item)
                    if (!cartList.contains(item)) {
                        cartList.add(item)

                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        database.itemDao.updateItem(item)
                    }

                }

            }

            ListType.CartList -> {
                if (item.isCarted) {
                    showAlertDialog(
                        "Yes",
                        "No",
                        "ALERT",
                        "you want to remove this?",
                        {
                            item.isCarted = false
                            adapter.updateItem(position, item)
                            cartList.remove(item)
                            CoroutineScope(Dispatchers.Main).launch {

                                withContext(Dispatchers.IO) {
                                    database.itemDao.updateItem(item)
                                }
                                adapter.setMyList(cartList, ListType.CartList)
                            }


                            if (cartList.isEmpty()) {
                                toast("Cart list is empty now")
                            }
                        },
                        { toast("cancelled") }
                    )
                } else {
                    item.isCarted = true

                    CoroutineScope(Dispatchers.Main).launch {
                        adapter.updateItem(position, item)
                        withContext(Dispatchers.Main){
                            database.itemDao.updateItem(item)
                        }
                        adapter.setMyList(cartList, ListType.CartList)
                    }
                }
            }

            ListType.WishList -> {
                if (item.isCarted) {
                    showAlertDialog(
                        "Yes",
                        "No",
                        "ALERT",
                        "you want to remove this?",
                        {
                            item.isCarted = false
                            adapter.updateItem(position, item)
                            cartList.remove(item)
                            CoroutineScope(Dispatchers.IO).launch {
                                database.itemDao.updateItem(item)
                            }
                            binding.content.textViewTotal.text = wishList.size.toString()
                            adapter.setMyList(wishList, ListType.WishList)
                            if (cartList.isEmpty()) {
                                toast("Cart list is empty now")
                            }
                        },
                        { toast("cancelled") }
                    )

                } else {
                    item.isCarted = true
                    adapter.updateItem(position, item)

                }
                adapter.setMyList(wishList, ListType.WishList)

            }


        }

    }

    private fun cbHeartClick(position: Int) {
        val item = adapter.getMyList().get(position)
        when (adapter.listType) {
            ListType.MainList -> {
                if (item.isWished) {
                    showAlertDialog(
                        "Yes",
                        "No",
                        "ALERT",
                        "you want to remove this?",
                        {
                            item.isWished = false
                            adapter.updateItem(position, item)
                            wishList.remove(item)
                            CoroutineScope(Dispatchers.IO).launch {
                                database.itemDao.updateItem(item)
                            }
                            adapter.setMyList(mainList, ListType.MainList)
                            if (cartList.isEmpty()) {
                                toast("Cart list is empty now")
                            }
                        },
                        { toast("cancelled") }
                    )


                    //-------------------------------------
//                    item.isWished = false
//                    adapter.updateItem(position, item)
//                    wishList.remove(item)
//                    CoroutineScope(Dispatchers.IO).launch {
//                        database.itemDao.updateItem(item)
//                    }
//                    for (i in wishList) {
//                        Log.e("wish", "cbHeartClickMain:  ${i.name} \n")
//                    }
//                    Log.e("wish", "........................................ ")
                } else {
                    item.isWished = true
                    adapter.updateItem(position, item)
                    if (!wishList.contains(item)) {
                        wishList.add(item)
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        database.itemDao.updateItem(item)
                    }
                    for (i in wishList) {

                        Log.e("wish", "cbHeartClickMain:  ${i.name} \n")

                        // Add a dotted line after each iteration

                    }
                    Log.e("wish", "........................................ ")


                }

                adapter.setMyList(mainList, ListType.MainList)
            }

            ListType.WishList -> {
                if (item.isWished) {
                    showAlertDialog(
                        "Yes",
                        "No",
                        "ALERT",
                        "you want to remove this?",
                        {
                            item.isWished = false
                            adapter.updateItem(position, item)
                            wishList.remove(item)
                            CoroutineScope(Dispatchers.IO).launch {
                                database.itemDao.updateItem(item)
                            }
                            adapter.setMyList(wishList, ListType.WishList)
                            if (cartList.isEmpty()) {
                                toast("Cart list is empty now")
                            }
                        },
                        { toast("cancelled") }
                    )
                } else {
                    item.isWished = true
                    adapter.updateItem(position, item)
                    CoroutineScope(Dispatchers.IO).launch {
                        database.itemDao.updateItem(item)
                    }
                }
                adapter.setMyList(wishList, ListType.WishList)
            }

            ListType.CartList -> {
                if (item.isWished) {
                    item.isWished = false
                    adapter.updateItem(position, item)
                    toast("heart is Unchecked in Cart list")
                    CoroutineScope(Dispatchers.IO).launch {
                        database.itemDao.updateItem(item)
                    }
                    adapter.setMyList(cartList, ListType.CartList)
                    if (cartList.isEmpty()) {
                        toast("Cart list is empty now")
                    }
                } else {
                    item.isWished = true
                    adapter.updateItem(position, item)
                    toast("heart is checked in Cart list")
//                    if(!cartList.contains(item)){
//                        cartList.add(item)
//                    }

                }
                adapter.setMyList(cartList, ListType.CartList)
            }
        }

    }

    private fun checkBoxClick(position: Int) {
        val item = adapter.getMyList()[position]

        when (adapter.listType) {
            ListType.MainList -> {
                if (item.isSelected) {
                    item.isSelected = false
                    adapter.updateItem(position, item)


                } else {
                    //item ko mainlist me update b to kro dono cases me.
                    item.isSelected = true
                    adapter.updateItem(position, item)
                    editOrDeleteDialog(position)// main list me checkBox pe click se dialog a gaya , tk hay
                }
                adapter.setMyList(mainList, ListType.MainList)
            }

            ListType.WishList -> {
                if (item.isSelected) {
                    item.isSelected = false
                    adapter.updateItem(position, item)
                    toast("Unchecked in wish list")
                } else {
                    item.isSelected = true
                    adapter.updateItem(position, item)
                    toast("CB checked in WISH LIST")
                }
                adapter.setMyList(wishList, ListType.WishList)
            }

            ListType.CartList -> {
                if (item.isSelected) {
                    item.isSelected = false
                    adapter.updateItem(position, item)
                    toast("CB UN-CHECKED in CART LIST")

                } else {
                    //item ko mainlist me update b to kro dono cases me.
                    item.isSelected = true
//                    showCheckOutDialog(position)
                    adapter.updateItem(position, item)
                    toast("CB CHECKED in CART LIST")

                }
                adapter.setMyList(cartList, ListType.CartList)
            }
        }

    }

    private fun showCheckOutDialog(position: Int) {
        val item = adapter.getMyList().get(position)
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.checkout_dialog, null)
        val dialog = builder.setView(dialogView).create()

        val btnYes = dialogView.findViewById<Button>(R.id.btnCheckOutYes)
        val btnNo = dialogView.findViewById<Button>(R.id.btnCheckOutNo)

        btnYes.setOnClickListener {

            val viewHolder = binding.content.recyclerView.findViewHolderForAdapterPosition(position)
            val checkBox = viewHolder?.itemView?.findViewById<CheckBox>(R.id.checkBox)
            checkBox?.isEnabled = false
            CoroutineScope(Dispatchers.IO).launch {
                database.itemDao.updateItem(item)
                withContext(Dispatchers.Main) {
                    adapter.updateItem(position, item)
                }
            }
            dialog.dismiss()
        }
        btnNo.setOnClickListener {
            toast("Cancelled!!")
            dialog.dismiss()

        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()


    }


}


