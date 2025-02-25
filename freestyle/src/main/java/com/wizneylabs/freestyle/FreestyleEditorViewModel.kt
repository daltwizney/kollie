package com.wizneylabs.freestyle

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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

    val shaderIDs: List<String>
        get() {
            return _shaderMap.keys.toList();
        }

    private val _shaderMap = hashMapOf<String, MutableStateFlow<String>>();

    private var _currentShaderID = "";

    var editorText: StateFlow<String>;

    private var _shaderTemplate = ApplicationUtils.loadShaderFromAssets(application, "shaders/test.frag");

    private var _database: FreestyleDatabase? = null;

    var isLoading = mutableStateOf(true);

    init {

        Log.d(TAG, "viewmodel created!");

        editorText = MutableStateFlow<String>("").asStateFlow();

        this.openDatabase();

        viewModelScope.launch {

            val dao = _database!!.shaderDao();

            val shaderEntities = dao.getAll();

            if (shaderEntities.size == 0)
            {
                _currentShaderID = createNewShader();

                editorText = _shaderMap[_currentShaderID]!!.asStateFlow();
            }
            else
            {
                val shaderEntity = shaderEntities.first();

                _currentShaderID = shaderEntity.id;

                _shaderMap[_currentShaderID] = MutableStateFlow<String>(shaderEntity.fragmentShader);

                editorText = _shaderMap[_currentShaderID]!!.asStateFlow();
            }

            isLoading.value = false;
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

    suspend fun createNewShader(): String {

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