package com.uc3m.searchyourrecipe.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
        @PrimaryKey
        val email: String,
        val name: String,
        val image: String,
        val ivImage: String?,
        val age: String,
        val ivAge: String?,
        val height: String,
        val ivHeight: String?,
        val weight: String,
        val ivWeight: String?,
)
