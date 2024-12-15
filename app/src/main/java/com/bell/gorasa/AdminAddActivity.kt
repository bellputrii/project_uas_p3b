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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminAddBinding

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

            // Buat objek data baru
            val newMenu = Data(
                id = 0, // Gunakan ID default, jika ID di-generate otomatis di backend
                foodname = name,
                price = price,
                description = description
            )

            // Kirim request ke API
            apiService.addMenu(newMenu).enqueue(object : Callback<Data> {
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(this@AdminAddActivity, "Menu berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                        // Arahkan pengguna kembali ke halaman admin
                        val intent = Intent(this@AdminAddActivity, AdminActivity::class.java)
                        startActivity(intent)
//                        finish() // Tutup halaman ini
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
