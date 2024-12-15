package com.bell.gorasa;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bell.gorasa.admin.AdminEditActivity
import com.bell.gorasa.database.Data;

class AdminAdapter(
    private val context: Context,
    private val itemList: List<Data>  // Menggunakan List<Data> untuk item data
) : RecyclerView.Adapter<AdminAdapter.AdminViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_admin_list_item, parent, false)
        return AdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val foodItem = itemList[position]

        // Menampilkan data ke tampilan yang sesuai
        holder.txtId.text = (position + 1).toString()  // ID item (nomor urut)
        holder.txtNama.text = foodItem.foodname ?: ""  // Nama item (foodname)
        holder.icEdit.setImageResource(R.drawable.ic_edit)  // Set gambar untuk icon edit
        holder.icDelete.setImageResource(R.drawable.ic_delete)  // Set gambar untuk icon delete
        holder.icView.setImageResource(R.drawable.ic_eyes)

        // Set onClickListener untuk item RecyclerView
        holder.itemView.setOnClickListener {
            // Membuka DetailActivity dan mengirim data
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("food_name", foodItem.foodname)
                putExtra("food_price", foodItem.price)  // Asumsi data harga ada di foodprice
                putExtra("food_description", foodItem.description)  // Asumsi ada deskripsi makanan
            }
            context.startActivity(intent)
        }

        // Set onClickListener untuk icEdit
        holder.icEdit.setOnClickListener {
            val editIntent = Intent(context, AdminEditActivity::class.java).apply {
                putExtra("_id", foodItem.id)  // Mengirim ID item
                putExtra("foodname", foodItem.foodname)  // Mengirim nama item
                putExtra("price", foodItem.price)  // Mengirim harga item
                putExtra("description", foodItem.description)  // Mengirim deskripsi item
            }
            context.startActivity(editIntent)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    // ViewHolder untuk item RecyclerView
    class AdminViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtId: TextView = view.findViewById(R.id.txtId)
        val txtNama: TextView = view.findViewById(R.id.txtNama)
        val icEdit: ImageView = view.findViewById(R.id.iconEdit)
        val icDelete: ImageView = view.findViewById(R.id.iconDelete)
        val icView: ImageView = view.findViewById(R.id.iconView)
    }
}
