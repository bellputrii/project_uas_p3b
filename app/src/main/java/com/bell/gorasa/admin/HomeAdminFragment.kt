package com.bell.gorasa.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bell.gorasa.AdminAdapter
import com.bell.gorasa.R
import com.bell.gorasa.database.Data  // Gantilah GoRasa dengan Data
import com.bell.gorasa.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeAdminFragment : Fragment(R.layout.fragment_home_admin) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemList: ArrayList<Data>  // Gantilah GoRasa dengan Data
    private lateinit var adapteradmin: AdminAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi RecyclerView
        recyclerView = view.findViewById(R.id.menuRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())  // Mengatur LayoutManager
        itemList = ArrayList()

        // Mengambil data dari API
        fetchMenu()
    }

    private fun fetchMenu() {
        val apiService = APIClient.getInstance()

        // Menggunakan Callback<List<Data>> agar tipe datanya sesuai
        apiService.getAllMenu().enqueue(object : Callback<List<Data>> {  // Gantilah GoRasa dengan Data
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
}
