package com.bell.gorasa.admin

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bell.gorasa.AdminAdapter
import com.bell.gorasa.R
import com.bell.gorasa.database.Data
import com.bell.gorasa.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeAdminFragment : Fragment(R.layout.fragment_home_admin) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemList: ArrayList<Data>
    private lateinit var adapteradmin: AdminAdapter
    private lateinit var btnLogout: Button  // Tambahkan variabel untuk tombol logout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.menuRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        itemList = ArrayList()

        // Inisialisasi tombol logout
        btnLogout = view.findViewById(R.id.btnLogout)

        // Tambahkan event listener untuk tombol logout
        btnLogout.setOnClickListener {
            showLogoutConfirmation() // Panggil metode untuk menampilkan dialog konfirmasi
        }

        // Mengambil data dari API
        fetchMenu()
    }

    private fun fetchMenu() {
        val apiService = APIClient.getInstance()

        apiService.getAllMenu().enqueue(object : Callback<List<Data>> {
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                if (response.isSuccessful) {
                    val menu = response.body()
                    if (menu != null) {
                        itemList.addAll(menu)

                        // Menghubungkan data ke RecyclerView menggunakan adapter
                        adapteradmin = AdminAdapter(requireContext(), itemList)
                        recyclerView.adapter = adapteradmin
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

    private fun showLogoutConfirmation() {
        // Menampilkan dialog konfirmasi logout
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
            .setPositiveButton("Ya") { _, _ ->
                requireActivity().finishAffinity() // Menutup semua aktivitas dan keluar dari aplikasi
            }
            .setNegativeButton("Tidak", null)
            .show()
    }
}
