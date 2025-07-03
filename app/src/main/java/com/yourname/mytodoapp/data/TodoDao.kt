package com.yourname.mytodoapp.data

import androidx.lifecycle.LiveData
import androidx.room.*

// DAO (Data Access Object) defines the database queries.
@Dao
interface TodoDao {

    // --- List Operations ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: TodoList): Long
    @Query("DELETE FROM todo_lists WHERE id = :listId")
    suspend fun deleteListById(listId: Long)
    @Query("SELECT * FROM todo_lists ORDER BY name ASC")
    fun getAllLists(): LiveData<List<TodoList>>

    // --- Item Operations ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: TodoItem)
    @Update
    suspend fun updateItem(item: TodoItem)
    @Update
    suspend fun updateItems(items: List<TodoItem>)
    @Delete
    suspend fun deleteItem(item: TodoItem)
    @Query("SELECT * FROM todo_items WHERE listId = :listId ORDER BY position ASC")
    fun getItemsForList(listId: Long): LiveData<List<TodoItem>>

    // --- Search Operation ---
    @Query("SELECT DISTINCT listId FROM todo_items WHERE text LIKE '%' || :query || '%'")
    suspend fun searchItemsAndGetListIds(query: String): List<Long>

    // --- Firebase Operations ---
    @Query("SELECT * FROM todo_lists")
    suspend fun getAllListsForBackup(): List<TodoList>
    @Query("SELECT * FROM todo_items")
    suspend fun getAllItemsForBackup(): List<TodoItem>
    @Query("DELETE FROM todo_lists")
    suspend fun clearAllLists()
    @Query("DELETE FROM todo_items")
    suspend fun clearAllItems()
}