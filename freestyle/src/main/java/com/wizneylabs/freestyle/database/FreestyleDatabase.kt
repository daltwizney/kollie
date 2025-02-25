package com.wizneylabs.freestyle.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShaderEntity::class],
    version = 1
)
abstract class FreestyleDatabase : RoomDatabase() {

    abstract fun shaderDao(): ShaderEntityDao;
}