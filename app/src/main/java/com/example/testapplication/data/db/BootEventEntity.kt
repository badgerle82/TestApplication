package com.example.testapplication.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boot_table")
class BootEventEntity (
    var timestamp: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}