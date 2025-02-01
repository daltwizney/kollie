package com.wizneylabs.freestyle

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class FreestyleViewModel: ViewModel() {

    val text = mutableStateOf("");

    fun onTextChanged(newText: String) {

        text.value = newText;
    }
}