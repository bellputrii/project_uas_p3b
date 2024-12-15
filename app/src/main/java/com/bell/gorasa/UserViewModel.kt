package com.bell.gorasa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    val userEmail = MutableLiveData<String>()

    fun setUserEmail(email: String) {
        userEmail.value = email
    }

    fun getUserEmail(): LiveData<String> {
        return userEmail
    }
}
