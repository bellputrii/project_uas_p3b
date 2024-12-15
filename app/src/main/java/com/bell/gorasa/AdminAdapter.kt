package com.bell.gorasa

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bell.gorasa.database.Data

class AdminAdapter(context: Context, private val itemList: RecyclerView) : ArrayAdapter<Data>(context, R.layout.activity_list_item, itemList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Memastikan tampilan baru atau tampilan lama yang akan digunakan
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.activity_admin_list_item, parent, false)

        // Ambil data untuk item yang sesuai
        val foodItem = getItem(position)

        // Menampilkan data ke tampilan yang sesuai
        val txtId = view.findViewById<TextView>(R.id.txtId)
        val txtNama = view.findViewById<TextView>(R.id.txtNama)
        val icEdit = view.findViewById<ImageView>(R.id.iconEdit)
        val icDelete = view.findViewById<ImageView>(R.id.iconDelete)
        val icView = view.findViewById<ImageView>(R.id.iconView)

        // Set data ke komponen tampilan
        txtId.text = (position + 1).toString()  // ID item (nomor urut)
        txtNama.text = foodItem?.foodname ?: ""  // Nama item (foodname)
        icEdit.setImageResource(R.drawable.ic_edit)  // Set gambar untuk icon edit
        icDelete.setImageResource(R.drawable.ic_delete)  // Set gambar untuk icon delete
        icView.setImageResource(R.drawable.ic_eyes)

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


        return view
    }
}
