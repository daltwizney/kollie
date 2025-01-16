package com.wizneylabs.kollie

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _count = mutableStateOf(0);

    public val Count = derivedStateOf {
        "Count: ${_count.value}"
    }

    fun increment() {
        _count.value++;
    }

    fun decrement() {
        _count.value--;
    }
}