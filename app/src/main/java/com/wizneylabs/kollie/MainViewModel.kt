package com.wizneylabs.kollie

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    public var frameCounter = mutableStateOf(0);

    private val _count = mutableStateOf(0);

    public val Count = derivedStateOf {
        "Count: ${_count.value}"
    }

    init {

        _startSimulation();
    }

    private var simJob: Job? = null;

    private fun _startSimulation() {

        Log.d("MainViewModel", "started simulation!");

        simJob?.cancel();

        simJob = viewModelScope.launch {

            Log.d("MainViewModel", "launching coroutines!");

            while (true) {

                delay(100);

                frameCounter.value++;
            }
        }
    }

    fun increment() {
        _count.value++;
    }

    fun decrement() {
        _count.value--;
    }
}