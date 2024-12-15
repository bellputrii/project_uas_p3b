package com.bell.gorasa

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bell.gorasa.database.Bookmark  // Gantilah dengan data class yang sesuai di Room DB

class BookmarkAdapter(
    context: Context,
    private val bookmarkList: List<Bookmark>
) : ArrayAdapter<Bookmark>(context, R.layout.item_bookmark, bookmarkList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Memastikan tampilan baru atau tampilan lama yang akan digunakan
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_bookmark, parent, false)

        // Mengambil data untuk item yang sesuai
        val bookmarkItem = getItem(position)

        // Menampilkan data ke tampilan yang sesuai
        val txtFoodName = view.findViewById<TextView>(R.id.txtFoodName)
        val txtFoodPrice = view.findViewById<TextView>(R.id.txtFoodPrice)

        // Set data ke komponen tampilan
        txtFoodName.text = bookmarkItem?.foodname ?: "No Name"
        txtFoodPrice.text = bookmarkItem?.price ?: "No Price"

        // Jika ada gambar makanan, bisa menampilkannya di ImageView
        // imgFood.setImageResource(bookmarkItem?.imageResId ?: R.drawable.ic_food_placeholder)

        return view
    }
}
