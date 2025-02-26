package com.wizneylabs.freestyle

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

import com.wizneylabs.freestyle.database.FreestyleDatabase;
import com.wizneylabs.freestyle.database.ShaderEntity;
import com.wizneylabs.freestyle.utils.ApplicationUtils

class FreestyleEditorViewModel(private val application: Application): AndroidViewModel(application) {

    val TAG = FreestyleEditorViewModel::class.simpleName;

    private val _shaderIDs = MutableStateFlow<MutableList<String>>(mutableListOf<String>());
    val shaderIDFlow = _shaderIDs.asStateFlow();

    private var _currentShaderID = "";

    private val _editorText = MutableStateFlow<String>("");
    var editorText: StateFlow<String> = _editorText.asStateFlow();

    private var _shaderTemplate = ApplicationUtils.loadShaderFromAssets(application, "shaders/test.frag");

    private var _database: FreestyleDatabase? = null;

    var isFullyLoaded = mutableStateOf(false);

    init {

        Log.d(TAG, "viewmodel created!");

        this.openDatabase();

        viewModelScope.launch {

            val dao = _database!!.shaderDao();

            val shaderEntities = dao.getAll();

            if (shaderEntities.size == 0)
            {
                _currentShaderID = _createNewShader();
            }
            else
            {
                shaderEntities.forEach({ entity ->

                    _shaderIDs.value += entity.id;
                });
            }

            isFullyLoaded.value = true;
        }
    }

    fun deleteDatabase() {

        if (_database == null)
        {
            Log.e(TAG, "_database is null!");
            return;
        }

        _database?.close();

        application.applicationContext.deleteDatabase("freestyle-db");

        _database = null;
    }

    fun openDatabase() {

        if (_database != null)
        {
            this.deleteDatabase();
        }

        _database = Room.databaseBuilder(
            application,
            FreestyleDatabase::class.java,
            "freestyle-db"
        ).build();
    }

    fun createNewShader() {

        viewModelScope.launch {

            _createNewShader();
        }
    }

    private suspend fun _createNewShader(): String {

        val dao = _database!!.shaderDao();

        val shaderID = UUID.randomUUID().toString();

        dao.insert(ShaderEntity(
            id = shaderID,
            fragmentShader = _shaderTemplate
        ));

        _shaderIDs.value.add(shaderID);

        return shaderID;
    }

    fun deleteShader(id: String) {

        viewModelScope.launch {
            _deleteShader(id);
        }
    }

    private suspend fun _deleteShader(id: String) {

        val dao = _database!!.shaderDao();

        val entity = dao.getById(id);

        if (entity != null)
        {
            dao.delete(entity);
        }

        _shaderIDs.value = _shaderIDs.value.filter({ it != id }).toMutableList();
    }

    fun editShader(id: String) {

        if (!_shaderIDs.value.contains(id))
        {
            Log.w(TAG, "no shader map with ID: ${id}");
            return;
        }

        if (_currentShaderID == id)
        {
            return;
        }

        Log.d(TAG, "calling editShader!");

        _currentShaderID = id;

        isFullyLoaded.value = false;

        _editorText.value = "";

        val dao = _database!!.shaderDao();

        viewModelScope.launch {

            val entity = dao.getById(id);

            if (entity == null)
            {
                // TODO: tell user shader failed to load
            }

            val shaderSource = entity?.fragmentShader ?: "";

            _editorText.value = shaderSource;

            isFullyLoaded.value = true;
        }
    }

    fun onEditorTextChanged(newText: String) {

        _editorText.value = newText;
    }
}