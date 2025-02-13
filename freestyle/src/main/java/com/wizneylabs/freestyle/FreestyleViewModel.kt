package com.wizneylabs.freestyle

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FreestyleViewModel: ViewModel() {

    val TAG = FreestyleViewModel::class.simpleName;

    init {

        Log.d(TAG, "viewmodel created!");
    }

    private val _editorText = MutableStateFlow("");

    val editorText: StateFlow<String> = _editorText.asStateFlow();

    fun onEditorTextChanged(newText: String) {

        _editorText.value = newText;
    }
}