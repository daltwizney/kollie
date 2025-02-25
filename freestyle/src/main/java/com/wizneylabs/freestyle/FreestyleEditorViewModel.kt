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

    private val _shaderMap = hashMapOf<String, MutableStateFlow<String>>();

    private val _shaderIDFlow = MutableStateFlow(_shaderMap.keys.toList());
    val shaderIDFlow: StateFlow<List<String>> = _shaderIDFlow.asStateFlow();

    private var _currentShaderID = "";

    var editorText: StateFlow<String>;

    private var _shaderTemplate = ApplicationUtils.loadShaderFromAssets(application, "shaders/test.frag");

    private var _database: FreestyleDatabase? = null;

    var isFullyLoaded = mutableStateOf(false);

    init {

        Log.d(TAG, "viewmodel created!");

        editorText = MutableStateFlow<String>("").asStateFlow();

        this.openDatabase();

        viewModelScope.launch {

            val dao = _database!!.shaderDao();

            val shaderEntities = dao.getAll();

            if (shaderEntities.size == 0)
            {
                _currentShaderID = _createNewShader();

                editorText = _shaderMap[_currentShaderID]!!.asStateFlow();
            }
            else
            {
                val shaderEntity = shaderEntities.first();

                shaderEntities.forEach({ entity ->

                    _shaderMap[entity.id] = MutableStateFlow<String>(shaderEntity.fragmentShader);
                });

                _currentShaderID = shaderEntities.first().id;

                // send shader ID flow update to observers
                _shaderIDFlow.value = _shaderMap.keys.toList();

                editorText = _shaderMap[_currentShaderID]!!.asStateFlow();
            }

            isFullyLoaded.value = true;
        }

        // TODO: remove before flight - Room test
        if (false && _database != null)
        {
            viewModelScope.launch {

                val dao = _database!!.shaderDao();

                // write data
                val shaderSource = "MyShader" + (0..42).random();

//            // tests updating existing shader at id = 2
//            val entity = ShaderEntity(id = 2, fragmentShader = shaderSource);

                val entity = ShaderEntity(id = UUID.randomUUID().toString(), fragmentShader = shaderSource);

                dao.insert(entity);

                // read data
                var entities = dao.getAll();

                Log.d("RoomTest", "Shaders in db before delete: $entities");
            }
            .invokeOnCompletion {

                viewModelScope.launch {

                    val dao = _database!!.shaderDao();

                    // delete data
                    var entities = dao.getAll();

                    dao.delete(entities.last());

                    entities = dao.getAll();

                    Log.d("RoomTest", "Shaders in db before delete: $entities");
                }
            }
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

        var shaderSource: String;

        if (dao.entityExists(shaderID))
        {
            val shaderEntity = dao.getById(shaderID);

            shaderSource = shaderEntity?.fragmentShader ?: "";
        }
        else
        {
            shaderSource = _shaderTemplate;

            dao.insert(ShaderEntity(
                id = shaderID,
                fragmentShader = shaderSource
            ));
        }

        _shaderMap[shaderID] = MutableStateFlow(shaderSource);

        // send shader ID flow update to observers
        _shaderIDFlow.value = _shaderMap.keys.toList();

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

        _shaderMap.remove(id);

        // send shader ID flow update to observers
        _shaderIDFlow.value = _shaderMap.keys.toList();
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