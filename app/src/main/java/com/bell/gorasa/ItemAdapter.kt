package com.bell.gorasa

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bell.gorasa.database.AppDatabase
import com.bell.gorasa.database.Bookmark
import com.bell.gorasa.database.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ItemAdapter(context: Context, private val itemList: ArrayList<Data>) : ArrayAdapter<Data>(context, R.layout.activity_list_item, itemList) {

    private val bookmarkDao = AppDatabase.getInstance(context).bookmarkDao()  // Gunakan getInstance untuk mendapatkan instance database

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Memastikan tampilan baru atau tampilan lama yang akan digunakan
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_list_item, parent, false)

        // Ambil data untuk item yang sesuai
        val foodItem = getItem(position)

        // Menampilkan data ke tampilan yang sesuai
        val txtId = view.findViewById<TextView>(R.id.txtId)
        val txtNama = view.findViewById<TextView>(R.id.txtNama)
        val icBookmark = view.findViewById<ImageView>(R.id.icbookmark)

        // Set data ke komponen tampilan
        txtId.text = (position + 1).toString()  // ID item (nomor urut)
        txtNama.text = foodItem?.foodname ?: ""  // Nama item (foodname)
        icBookmark.setImageResource(R.drawable.ic_boookmark_border)  // Set gambar untuk icon bookmark

        // Set onClickListener untuk item ListView
        view.setOnClickListener {
            // Membuka DetailActivity dan mengirim data
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("food_name", foodItem?.foodname)
                putExtra("food_price", foodItem?.price)  // Asumsi data harga ada di foodprice
                putExtra("food_description", foodItem?.description)  // Asumsi ada deskripsi makanan
            }
            context.startActivity(intent)
        }

        // Menangani klik pada bookmark
        icBookmark.setOnClickListener {
            // Menyimpan item yang dibookmark ke Room Database
            foodItem?.let {
                val bookmark = Bookmark(
                    foodname = it.foodname,
                    price = it.price,
                    description = it.description)

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

        return view
    }
}
