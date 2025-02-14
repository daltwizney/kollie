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

import java.util.UUID;

class FreestyleViewModel: ViewModel() {

    val TAG = FreestyleViewModel::class.simpleName;

    val shaderIDs: List<String>
        get() {
            return _shaderMap.keys.toList();
        }

    private val _shaderMap = hashMapOf<String, MutableStateFlow<String>>();

    private var _currentShaderID = "";

    var editorText: StateFlow<String>;

    private val _shaderTemplate =
        "void main() {\n" +
        "    gl_FragColor = vec4(0.0, 1.0, 0.0, 1.0);\n" +
        "}";

    init {

        Log.d(TAG, "viewmodel created!");

        _currentShaderID = createNewShader();

        createNewShader();
        createNewShader();

        editorText = _shaderMap[_currentShaderID]!!.asStateFlow();
    }

    fun createNewShader(): String {

        val shaderID = UUID.randomUUID().toString();

        _shaderMap[shaderID] = MutableStateFlow(_shaderTemplate);

        return shaderID;
    }

    fun editShader(id: String) {

        if (!_shaderMap.contains(id))
        {
            Log.w(TAG, "no shader map with ID: ${id}");
            return;
        }

        _currentShaderID = id;
        editorText = _shaderMap[id]!!.asStateFlow();
    }

    fun onEditorTextChanged(newText: String) {

        _shaderMap[_currentShaderID]!!.value = newText;
    }
}