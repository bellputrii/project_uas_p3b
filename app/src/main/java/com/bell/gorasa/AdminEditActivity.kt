package com.bell.gorasa.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bell.gorasa.database.Data
import com.bell.gorasa.databinding.ActivityAdminEditBinding
import com.bell.gorasa.network.APIClient
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminEditBinding  // Menggunakan ViewBinding
    private var foodId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Terima data dari Intent
        foodId = intent.getStringExtra("_id")?.toIntOrNull() // Mengonversi String ke Int
        if (foodId == null) {
            Toast.makeText(this, "Invalid food ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Ambil data menu berdasarkan ID yang diterima
        fetchMenuData(foodId!!)

        // Tombol untuk update data menu
        binding.btnUpdate.setOnClickListener {
            if (foodId != null) {
                updateMenu(foodId!!)
            }
        }

        // Tombol untuk cancel atau kembali
        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    // Fungsi untuk mengambil data menu berdasarkan ID
    private fun fetchMenuData(id: Int) {
        val apiService = APIClient.getInstance()

        apiService.getMenuById(id.toString()).enqueue(object : Callback<Data> {  // Kirim id sebagai String ke API
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        // Set data ke ViewBinding
                        binding.etFoodname.setText(data.foodname)
                        binding.etFoodprice.setText(data.price)
                        binding.etFooddescription.setText(data.description)
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
    private fun updateMenu(id: Int) {
        val updatedName = binding.etFoodname.text.toString().trim()
        val updatedPrice = binding.etFoodprice.text.toString().trim()
        val updatedDescription = binding.etFooddescription.text.toString().trim()

        if (updatedName.isNotBlank() && updatedPrice.isNotBlank() && updatedDescription.isNotBlank()) {
            // Buat objek Data baru untuk update
            val updatedData = Data(
                id = id,
                foodname = updatedName,
                price = updatedPrice,
                description = updatedDescription
            )

            val apiService = APIClient.getInstance()

            // Mengonversi objek menjadi RequestBody menggunakan toRequestBody
            val jsonRequest = updatedData.toJson().toRequestBody("application/json".toMediaType())

            apiService.updateMenu(id.toString(), jsonRequest).enqueue(object : Callback<Data> {  // Kirim id sebagai String
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
