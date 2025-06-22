package com.yourname.mytodoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Represents a single To-Do List, which is a table in our Room database.
@Entity(tableName = "todo_lists")
data class TodoList(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String = ""
) {
    constructor() : this(0, "")
}