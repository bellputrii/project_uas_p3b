package com.bell.gorasa

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bell.gorasa.database.AppDatabase
import com.bell.gorasa.database.Bookmark
import com.bell.gorasa.database.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemAdapter(
    private val context: Context,
    private val itemList: ArrayList<Data> // Menyimpan data yang akan ditampilkan
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val bookmarkDao = AppDatabase.getInstance(context).bookmarkDao()  // Gunakan getInstance untuk mendapatkan instance database

    // ViewHolder untuk item dalam RecyclerView
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtId: TextView = view.findViewById(R.id.txtId)
        val txtNama: TextView = view.findViewById(R.id.txtNama)
        val icBookmark: ImageView = view.findViewById(R.id.icbookmark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_list_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val foodItem = itemList[position]

        // Menampilkan data ke tampilan yang sesuai
        holder.txtId.text = (position + 1).toString()  // ID item (nomor urut)
        holder.txtNama.text = foodItem.foodname ?: ""  // Nama item (foodname)
        holder.icBookmark.setImageResource(R.drawable.ic_boookmark_border)  // Set gambar untuk icon bookmark

        // Set onClickListener untuk item ListView
        holder.itemView.setOnClickListener {
            // Membuka DetailActivity dan mengirim data
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("food_name", foodItem.foodname)
                putExtra("food_price", foodItem.price)  // Asumsi data harga ada di foodprice
                putExtra("food_description", foodItem.description)  // Asumsi ada deskripsi makanan
            }
            context.startActivity(intent)
        }

        // Menangani klik pada bookmark
        holder.icBookmark.setOnClickListener {
            // Menyimpan item yang dibookmark ke Room Database
            foodItem.let {
                val bookmark = Bookmark(
                    foodname = it.foodname,
                    price = it.price,
                    description = it.description
                )

                // Menyimpan bookmark dalam background thread
                GlobalScope.launch(Dispatchers.IO) {
                    bookmarkDao.insertBookmark(bookmark)
                    // Menggunakan runOnUiThread untuk Toast
                    (context as? Activity)?.runOnUiThread {
                        Toast.makeText(context, "${it.foodname} added to bookmarks", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
