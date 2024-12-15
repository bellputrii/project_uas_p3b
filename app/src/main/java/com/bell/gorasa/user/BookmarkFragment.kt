package com.bell.gorasa.user

import android.os.Bundle
import android.view.View
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bell.gorasa.BookmarkAdapter
import com.bell.gorasa.R
import com.bell.gorasa.database.AppDatabase
import com.bell.gorasa.database.Bookmark
import kotlinx.coroutines.launch

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {

    private lateinit var listView: ListView
    private lateinit var adapter: BookmarkAdapter
    private lateinit var bookmarkList: List<Bookmark>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ListView
        listView = view.findViewById(R.id.bookmarkListView)

        // Ambil data dari Room Database
        fetchBookmarks()
    }

    private fun fetchBookmarks() {
        // Menjalankan fungsi suspend di dalam lifecycleScope untuk fragment
        viewLifecycleOwner.lifecycleScope.launch {
            val bookmarkDao = AppDatabase.getInstance(requireContext()).bookmarkDao()
            val bookmarkList = bookmarkDao.getAllBookmarks()

            // Perbarui UI dengan hasil dari database (harus dilakukan di UI thread)
            adapter = BookmarkAdapter(requireContext(), bookmarkList)
            listView.adapter = adapter
        }
    }
}
