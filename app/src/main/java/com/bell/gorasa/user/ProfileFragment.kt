package com.bell.gorasa.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bell.gorasa.R

class ProfileFragment : Fragment() {

    private lateinit var userEmail: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Mengambil data email dari SharedPreferences
        val sharedPreferences = activity?.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userEmail = sharedPreferences?.getString("userEmail", "") ?: "Email not found"

        // Mengatur data ke dalam TextView
        val emailTextView: TextView = view.findViewById(R.id.user_email)
        emailTextView.text = userEmail

        return view
    }

}
