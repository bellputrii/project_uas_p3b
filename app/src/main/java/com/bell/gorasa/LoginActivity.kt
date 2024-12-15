package com.bell.gorasa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bell.gorasa.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        with(binding) {
            btnLogin.setOnClickListener {
                val username = etUsernameLogin.text.toString()
                val password = etPasswordLogin.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (isValidUsernamePassword()) {
                        prefManager.setLoggedIn(true)
                        checkLoginStatus()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Username atau password salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            txtRegister.setOnClickListener {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        RegisterActivity::class.java
                    )
                )
            }
        }
    }

    private fun isValidUsernamePassword(): Boolean {
        val username = prefManager.getUsername()
        val password = prefManager.getPassword()
        val inputUsername = binding.etUsernameLogin.text.toString()
        val inputPassword = binding.etPasswordLogin.text.toString()
        return username == inputUsername && password == inputPassword
    }


    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        val username = binding.etUsernameLogin.text.toString() // Mengambil username dari input pengguna

        if (isLoggedIn) {
            Toast.makeText(
                this@LoginActivity, "Login berhasil",
                Toast.LENGTH_SHORT
            ).show()

            // Simpan data pengguna di SharedPreferences
            val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("userEmail", username) // Menyimpan email atau username
            editor.apply()

            if (username == "admin") {
                prefManager.saveRole("admin") // Menyimpan role sebagai admin
                startActivity(
                    Intent(
                        this@LoginActivity,
                        AdminActivity::class.java // Arahkan ke halaman admin
                    )
                )
            } else {
                prefManager.saveRole("user") // Menyimpan role sebagai user
                startActivity(
                    Intent(
                        this@LoginActivity,
                        MainActivity::class.java // Arahkan ke halaman user
                    )
                )
            }
            finish()
        } else {
            Toast.makeText(
                this@LoginActivity, "Login gagal",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}