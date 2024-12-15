package com.bell.gorasa.admin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bell.gorasa.R
import com.bell.gorasa.database.Data
import com.bell.gorasa.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminEditActivity : AppCompatActivity() {

    private lateinit var etFoodName: EditText
    private lateinit var etFoodPrice: EditText
    private lateinit var etFoodDescription: EditText
    private lateinit var btnUpdate: Button
    private lateinit var btnCancel: Button
    private var foodId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_edit)

        etFoodName = findViewById(R.id.et_foodname)
        etFoodPrice = findViewById(R.id.et_foodprice)
        etFoodDescription = findViewById(R.id.et_fooddescription)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnCancel = findViewById(R.id.btnCancel)

        // Terima data dari Intent
        foodId = intent.getStringExtra("_id") // Menerima menu_id yang dikirim dari HomeAdminFragment
        Log.d("AdminEditActivity", "Received food ID: $foodId")

        // Jika foodId tidak ada, beri peringatan dan keluar dari activity
        if (foodId == null) {
            Toast.makeText(this, "Invalid food ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Ambil data menu berdasarkan ID yang diterima
        fetchMenuData(foodId!!)

        // Tombol untuk update data menu
        btnUpdate.setOnClickListener {
            if (foodId != null) {
                updateMenu(foodId!!)
            }
        }

        // Tombol untuk cancel atau kembali
        btnCancel.setOnClickListener {
            finish()
        }
    }

    // Fungsi untuk mengambil data menu berdasarkan ID
    // Fungsi untuk mengambil data menu berdasarkan ID
    private fun fetchMenuData(id: String) {
        val apiService = APIClient.getInstance()

        apiService.getMenuById(id).enqueue(object : Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        Log.d("AdminAdapter", "Data received: $data") // Log data
                        etFoodName.setText(data.foodname)
                        etFoodPrice.setText(data.price)
                        etFoodDescription.setText(data.description)
                    } else {
                        Toast.makeText(this@AdminEditActivity, "Data not found", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@AdminEditActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                Toast.makeText(this@AdminEditActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Fungsi untuk update data menu
    private fun updateMenu(id: String) {
        val updatedName = etFoodName.text.toString().trim()
        val updatedPrice = etFoodPrice.text.toString().trim()
        val updatedDescription = etFoodDescription.text.toString().trim()

        if (updatedName.isNotBlank() && updatedPrice.isNotBlank() && updatedDescription.isNotBlank()) {
            // Buat objek Data baru untuk update
            val updatedData = Data(
                id = id,
                foodname = updatedName,
                price = updatedPrice,
                description = updatedDescription
            )

            val apiService = APIClient.getInstance()
            apiService.updateMenu(id, updatedData).enqueue(object : Callback<Data> {
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdminEditActivity, "Menu updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AdminEditActivity, "Failed to update menu", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    Toast.makeText(this@AdminEditActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}