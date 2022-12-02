package com.example.cardapp.viewmodels


import androidx.lifecycle.ViewModel
import com.example.cardapp.database.DataBase

class NameFragmentViewModel : ViewModel() {
    fun registerUserWithName(
        name: String,
        listener: com.example.cardapp.interfaces.OnAuthCompleteListener
    ) = DataBase.registerUserWithName(name, listener)
}