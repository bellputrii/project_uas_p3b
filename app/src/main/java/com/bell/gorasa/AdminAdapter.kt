package com.bell.gorasa

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bell.gorasa.admin.AdminEditActivity
import com.bell.gorasa.database.Data
import com.bell.gorasa.network.APIClient
import com.bell.gorasa.network.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminAdapter(
    private val context: Context,
    private val itemList: MutableList<Data>  // Make itemList mutable to modify it
) : RecyclerView.Adapter<AdminAdapter.AdminViewHolder>() {

    private val apiService: APIService = APIClient.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_admin_list_item, parent, false)
        return AdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val foodItem = itemList[position]

        // Display data in the respective views
        holder.txtId.text = (position + 1).toString()  // Item ID (order number)
        holder.txtNama.text = foodItem.foodname ?: ""  // Item name (foodname)
        holder.icEdit.setImageResource(R.drawable.ic_edit)  // Edit icon
        holder.icDelete.setImageResource(R.drawable.ic_delete)  // Delete icon
        holder.icView.setImageResource(R.drawable.ic_eyes)  // View icon

        // Set onClickListener for the item in RecyclerView
        holder.itemView.setOnClickListener {
            // Open DetailActivity and send data
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("food_name", foodItem.foodname)
                putExtra("food_price", foodItem.price)  // Assuming price exists in foodItem
                putExtra("food_description", foodItem.description)  // Assuming description exists in foodItem
            }
            context.startActivity(intent)
        }

        // Set onClickListener for Edit icon
        holder.icEdit.setOnClickListener {
            val editIntent = Intent(context, AdminEditActivity::class.java).apply {
                putExtra("_id", foodItem.id)  // Send the item ID
                putExtra("foodname", foodItem.foodname)  // Send item name
                putExtra("price", foodItem.price)  // Send item price
                putExtra("description", foodItem.description)  // Send item description
            }
            context.startActivity(editIntent)
        }

        // Set onClickListener for Delete icon
        holder.icDelete.setOnClickListener {
            deleteItem(foodItem.id, position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun deleteItem(itemId: Int, position: Int) {
        // Call API to delete the item from the database
        apiService.deleteMenu(itemId.toString()).enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    // Remove the item from the list
                    itemList.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // ViewHolder for each item in the RecyclerView
    class AdminViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtId: TextView = view.findViewById(R.id.txtId)
        val txtNama: TextView = view.findViewById(R.id.txtNama)
        val icEdit: ImageView = view.findViewById(R.id.iconEdit)
        val icDelete: ImageView = view.findViewById(R.id.iconDelete)
        val icView: ImageView = view.findViewById(R.id.iconView)
    }
}
