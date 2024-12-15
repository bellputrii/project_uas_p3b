package com.bell.gorasa.user

import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bell.gorasa.ItemAdapter
import com.bell.gorasa.R
import com.bell.gorasa.database.Data  // Gantilah GoRasa dengan Data
import com.bell.gorasa.network.APIClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeUserFragment : Fragment(R.layout.fragment_home_user) {

    private lateinit var listView: ListView
    private lateinit var itemList: ArrayList<Data>  // Gantilah GoRasa dengan Data
    private lateinit var adapter: ItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ListView
        listView = view.findViewById(R.id.menuListView)
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

                        // Menghubungkan data ke ListView menggunakan adapter
                        adapter = ItemAdapter(requireContext(), itemList)
                        listView.adapter = adapter
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
