package com.yourname.mytodoapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "todo_items",
    foreignKeys = [ForeignKey(
        entity = TodoList::class,
        parentColumns = ["id"],
        childColumns = ["listId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var listId: Long = 0,      // MUST be var (not val)

    var text: String = "",     // MUST have default value

    var position: Int = 0      // Same here
) {
    // Empty constructor for Firestore
    constructor() : this(0, 0, "", 0)
}