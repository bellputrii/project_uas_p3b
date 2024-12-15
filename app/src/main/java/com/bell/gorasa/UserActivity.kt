package com.bell.gorasa

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bell.gorasa.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var prefManager: PrefManager

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        // Menampilkan username dan role pengguna
        val username = prefManager.getUsername()
        val role = prefManager.getRole()

        binding.txtUsername.text = "Welcome, $username"
        binding.txtRole.text = "Role: $role"

        // Menambahkan aksi logout
        binding.btnLogout.setOnClickListener {
            prefManager.setLoggedIn(false)
            prefManager.clear()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            finish() // Kembali ke halaman login
        }
    }
}