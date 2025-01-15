package com.pmdk.gymapp.data.entities
import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val age: Int?,
    val height: Int?,
    val weight: Int?,
    val profilePicture: Uri? = null
)