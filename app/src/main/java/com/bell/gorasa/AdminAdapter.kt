package com.bell.gorasa

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bell.gorasa.admin.AdminEditActivity
import com.bell.gorasa.database.Data
import com.bell.gorasa.databinding.ActivityAdminListItemBinding
import com.bell.gorasa.network.APIClient
import com.bell.gorasa.network.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminAdapter(
    private val context: Context
) : RecyclerView.Adapter<AdminAdapter.AdminViewHolder>() {

    private val apiService: APIService = APIClient.getInstance()
    private var itemList: MutableList<Data> = mutableListOf()

    // Method untuk memperbarui data itemList
    fun setItemList(newItemList: List<Data>) {
        itemList = newItemList.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val binding = ActivityAdminListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AdminViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val foodItem = itemList[position]
        holder.bind(foodItem, position)
    }

    override fun getItemCount(): Int = itemList.size

    private fun deleteItem(itemId: String, position: Int) {
        apiService.deleteMenu(itemId.toString()).enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
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

    inner class AdminViewHolder(private val binding: ActivityAdminListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(foodItem: Data, position: Int) {
            // Mengisi data ke elemen tampilan menggunakan binding
            binding.txtId.text = (position + 1).toString()
            binding.txtNama.text = foodItem.foodname ?: ""
            binding.iconEdit.setImageResource(R.drawable.ic_edit)
            binding.iconDelete.setImageResource(R.drawable.ic_delete)
            binding.iconView.setImageResource(R.drawable.ic_eyes)

            // Aksi klik untuk melihat detail
            binding.root.setOnClickListener {
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("food_name", foodItem.foodname)
                    putExtra("food_price", foodItem.price)
                    putExtra("food_description", foodItem.description)
                }
                context.startActivity(intent)
            }

            // Aksi klik untuk mengedit
            binding.iconEdit.setOnClickListener {
                val editIntent = Intent(context, AdminEditActivity::class.java).apply {
                    putExtra("_id", foodItem.id)
                    putExtra("foodname", foodItem.foodname)
                    putExtra("price", foodItem.price)
                    putExtra("description", foodItem.description)
                }
                context.startActivity(editIntent)
            }

            // Aksi klik untuk menghapus
            binding.iconDelete.setOnClickListener {
                deleteItem(foodItem.id, position)
            }
        }
    }
}
