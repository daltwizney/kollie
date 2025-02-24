package com.wizneylabs.freestyle

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Upsert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

@Entity(tableName = "items")
data class Item(

    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val name: String,
    val age: Int
);

@Dao
interface ItemDao {

    @Upsert
    suspend fun insert(item: Item);

    @Delete
    suspend fun deleteItem(item: Item);

    @Query("SELECT * FROM items")
    suspend fun getAll(): List<Item>;
}

@Database(
    entities = [Item::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao;
}

class FreestyleEditorViewModel(private val application: Application): AndroidViewModel(application) {

    val TAG = FreestyleEditorViewModel::class.simpleName;

    val shaderIDs: List<String>
        get() {
            return _shaderMap.keys.toList();
        }

    private val _shaderMap = hashMapOf<String, MutableStateFlow<String>>();

    private var _currentShaderID = "";

    var editorText: StateFlow<String>;

    private var _shaderTemplate = _loadShaderFromAssets("shaders/test.frag");

    init {

        Log.d(TAG, "viewmodel created!");

        _currentShaderID = createNewShader();

        createNewShader();
        createNewShader();

        editorText = _shaderMap[_currentShaderID]!!.asStateFlow();

        // TODO: remove before flight - Room test
        val database = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "my-items-db"
        ).build();

        val dao = database.itemDao();

        viewModelScope.launch {

            // write data
            val age = (0..42).random();
            val name = "MyItem" + age.toString();

//            // tests updating existing item at id = 2
//            val item = Item(id = 2, name = name, age = age);

            val item = Item(name = name, age = age);

            dao.insert(item);

            // read data
            var items = dao.getAll();

            Log.d("RoomTest", "Items in db before delete: $items");

            dao.deleteItem(items.last());

            items = dao.getAll();

            Log.d("RoomTest", "Items in db before delete: $items");
        }
    }

    private fun _loadShaderFromAssets(fileName: String): String {

        Log.d(TAG, "fileName: $fileName");

        var shaderCode = "";

        try {
            shaderCode = application.assets.open(fileName)
                .bufferedReader().use { it.readText() };
        }
        catch (e: Exception) {

            Log.e(TAG, "failed to load shader: ${fileName}");
        }

        return shaderCode;
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