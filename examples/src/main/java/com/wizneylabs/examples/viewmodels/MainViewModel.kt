package com.wizneylabs.examples.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.ar.core.Session

class MainViewModel: ViewModel() {

    var arSession = mutableStateOf<Session?>(null);


}