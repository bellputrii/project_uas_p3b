package com.bell.gorasa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bell.gorasa.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        with(binding) {
            btnRegister.setOnClickListener {
                val username = etUsernameRegister.text.toString()
                val password = etPasswordRegister.text.toString()
                val confirmPassword = etConfirmpassRegister.text.toString()
                if (username.isEmpty() || password.isEmpty() ||
                    confirmPassword.isEmpty()) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(this@RegisterActivity, "Password tidak sama",
                        Toast.LENGTH_SHORT)
                        .show()
                } else {
                    prefManager.saveUsername(username)
                    prefManager.savePassword(password)
                    prefManager.setLoggedIn(true)
                    checkLoginStatus()
                }
            }
            txtLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity,
                    LoginActivity::class.java))
            }
        }
    }
    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            Toast.makeText(this@RegisterActivity, "Registrasi berhasil",
                Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        } else {
            Toast.makeText(this@RegisterActivity, "Registrasi gagal",
                Toast.LENGTH_SHORT).show()
        }
    }
}