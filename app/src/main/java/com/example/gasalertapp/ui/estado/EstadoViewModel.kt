package com.example.gasalertapp.ui.estado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EstadoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is estado Fragment"
    }
    val text: LiveData<String> = _text
}