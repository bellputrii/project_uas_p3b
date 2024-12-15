package com.bell.gorasa.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bell.gorasa.AdminActivity
import com.bell.gorasa.database.Data
import com.bell.gorasa.databinding.ActivityAdminAddBinding
import com.bell.gorasa.network.APIClient
import com.bell.gorasa.network.APIService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminAddBinding

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService: APIService = APIClient.getInstance() // Instance dari API Service

        binding.btnSave.setOnClickListener {
            val name = binding.etFoodname.text.toString().trim()
            val price = binding.etFoodprice.text.toString().trim()
            val description = binding.etFooddescription.text.toString().trim()

            // Validasi input
            if (name.isBlank() || price.isBlank() || description.isBlank()) {
                Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Buat objek JSON
            val jsonObject = JSONObject().apply {
                put("foodname", name)
                put("price", price)
                put("description", description)
            }

            // Membuat request body dengan tipe application/json
            val requestBody = RequestBody.create(
                "application/json".toMediaTypeOrNull(),
                jsonObject.toString()
            )

            // Kirim request ke API
            apiService.addMenu(requestBody).enqueue(object : Callback<Data> {
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(this@AdminAddActivity, "Menu berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                        // Arahkan pengguna kembali ke halaman admin
                        val intent = Intent(this@AdminAddActivity, AdminActivity::class.java)
                        startActivity(intent)
                        finish() // Menutup halaman ini
                    } else {
                        Toast.makeText(this@AdminAddActivity, "Gagal menambahkan menu: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    Toast.makeText(this@AdminAddActivity, "Terjadi kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
