package com.bell.gorasa.admin

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
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
import com.bell.gorasa.network.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class HomeAdminFragment : Fragment(R.layout.fragment_home_admin) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemList: ArrayList<Data>
    private lateinit var adapteradmin: AdminAdapter
    private lateinit var btnLogout: Button
    private lateinit var btnTambahData: Button
    private lateinit var btnRefresh: Button // Tambahkan tombol refresh

    private val apiService: APIService = APIClient.getInstance()

    companion object {
        private const val ADD_MENU_REQUEST_CODE = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.menuRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        itemList = ArrayList()

        // Inisialisasi tombol-tombol
        btnLogout = view.findViewById(R.id.btnLogout)
        btnTambahData = view.findViewById(R.id.btnTambahData)
        btnRefresh = view.findViewById(R.id.btnRefresh) // Inisialisasi tombol refresh

        // Event listener untuk tombol tambah data
        btnTambahData.setOnClickListener {
            val intent = Intent(requireContext(), AdminAddActivity::class.java)
            startActivityForResult(intent, ADD_MENU_REQUEST_CODE)
        }

        // Event listener untuk tombol logout
        btnLogout.setOnClickListener {
            showLogoutConfirmation()
        }

        // Event listener untuk tombol refresh
        btnRefresh.setOnClickListener {
            fetchMenu() // Panggil untuk mengambil data ulang dari API
            Toast.makeText(requireContext(), "Data refreshed", Toast.LENGTH_SHORT).show()
        }

        // Mengambil data dari API
        fetchMenu()

        // Set listener untuk mendengarkan hasil dari AdminAddActivity
        requireActivity().supportFragmentManager.setFragmentResultListener("menuAdded", viewLifecycleOwner) { _, _ ->
            fetchMenu()
        }
    }

    private fun fetchMenu() {
        apiService.getAllMenu().enqueue(object : Callback<List<Data>> {
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                if (response.isSuccessful) {
                    val menu = response.body()
                    if (menu != null) {
                        itemList.clear()
                        itemList.addAll(menu)

                        // Menghubungkan data ke RecyclerView
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
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
            .setPositiveButton("Ya") { _, _ -> requireActivity().finishAffinity() }
            .setNegativeButton("Tidak", null)
            .show()
    }

    // Menangani hasil dari AdminAddActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_MENU_REQUEST_CODE && resultCode == RESULT_OK) {
            fetchMenu() // Panggil ulang untuk mengambil data yang baru ditambahkan
        }
    }
}