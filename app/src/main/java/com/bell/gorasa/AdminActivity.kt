// AdminActivity.kt
package com.bell.gorasa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bell.gorasa.admin.HomeAdminFragment
import com.bell.gorasa.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Memasukkan fragment HomeAdminFragment saat AdminActivity pertama kali dijalankan
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_admin, HomeAdminFragment())
                .commit()
        }
    }
}
