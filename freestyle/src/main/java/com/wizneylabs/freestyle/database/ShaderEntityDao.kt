package com.wizneylabs.freestyle.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ShaderEntityDao {

    @Query("SELECT * FROM shaders WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ShaderEntity?;

    suspend fun entityExists(id: String): Boolean {

        return getById(id) != null;
    }

    @Upsert
    suspend fun insert(entity: ShaderEntity);

    @Delete
    suspend fun delete(entity: ShaderEntity);

    @Query("SELECT * FROM shaders")
    suspend fun getAll(): List<ShaderEntity>;
}