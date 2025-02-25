package com.wizneylabs.freestyle.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shaders")
data class ShaderEntity(

    @PrimaryKey(autoGenerate = false)
    val id: String = "",

    val fragmentShader: String
);