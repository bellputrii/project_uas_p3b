package com.bell.gorasa.admin

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bell.gorasa.AdminAdapter
import com.bell.gorasa.LoginActivity
import com.bell.gorasa.PrefManager
import com.bell.gorasa.R
import com.bell.gorasa.database.Data
import com.bell.gorasa.databinding.FragmentHomeAdminBinding
import com.bell.gorasa.network.APIClient
import com.bell.gorasa.network.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class  HomeAdminFragment : Fragment(R.layout.fragment_home_admin) {

    private lateinit var binding: FragmentHomeAdminBinding
    private lateinit var adapterAdmin: AdminAdapter
    private lateinit var itemList: ArrayList<Data>
    private val apiService: APIService = APIClient.getInstance()

    companion object {
        private const val ADD_MENU_REQUEST_CODE = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeAdminBinding.bind(view)

        // Mengakses PrefManager
        val prefManager = PrefManager.getInstance(requireContext())

        // Inisialisasi RecyclerView dan Adapter
        itemList = ArrayList()
        adapterAdmin = AdminAdapter(requireContext())
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapterAdmin

        // Menampilkan username yang tersimpan di PrefManager
        val username = prefManager.getUsername()
        binding.titleTextView.text = "Selamat datang, $username"  // Menampilkan username di TextView

        // Tombol tambah data
        binding.btnTambahData.setOnClickListener {
            val intent = Intent(requireContext(), AdminAddActivity::class.java)
            startActivityForResult(intent, ADD_MENU_REQUEST_CODE)
        }

        // Tombol logout
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmation(prefManager)  // Menggunakan PrefManager untuk logout
        }

        // Tombol refresh
        binding.btnRefresh.setOnClickListener {
            fetchMenu(true) // Refresh dengan notifikasi
        }

        // Ambil data menu pertama kali
        fetchMenu(false)
    }

    private fun setFurniture(dataList: List<Data>) {
        itemList.clear()
        itemList.addAll(dataList)
        adapterAdmin.setItemList(itemList)
    }

    private fun fetchMenu(showNotification: Boolean) {
        apiService.getAllMenu().enqueue(object : Callback<List<Data>> {
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        setFurniture(it)
                        if (showNotification) {
                            Toast.makeText(requireContext(), "Data refreshed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLogoutConfirmation(prefManager: PrefManager) {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
            .setPositiveButton("Ya") { _, _ ->
                prefManager.clear() // Menghapus sesi
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                requireActivity().finish() // Tutup AdminActivity
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_MENU_REQUEST_CODE && resultCode == RESULT_OK) {
            fetchMenu(false)
        }
    }
}
