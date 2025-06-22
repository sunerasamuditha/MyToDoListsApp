package com.yourname.mytodoapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.yourname.mytodoapp.data.AppDatabase
import com.yourname.mytodoapp.data.TodoDao
import com.yourname.mytodoapp.data.TodoItem
import kotlinx.coroutines.launch
import java.util.Collections

class TodoListViewModel(application: Application, private val listId: Long) : AndroidViewModel(application) {

    private val dao: TodoDao
    val items: LiveData<List<TodoItem>>

    init {
        dao = AppDatabase.getDatabase(application).todoDao()
        items = dao.getItemsForList(listId)
    }

    fun addItem(text: String) = viewModelScope.launch {
        if (text.isNotBlank()) {
            val position = items.value?.size ?: 0
            dao.insertItem(TodoItem(listId = listId, text = text, position = position))
        }
    }

    fun updateItem(item: TodoItem) = viewModelScope.launch {
        dao.updateItem(item)
    }

    fun deleteItem(item: TodoItem) = viewModelScope.launch {
        dao.deleteItem(item)
    }

    fun onItemsMoved(fromPosition: Int, toPosition: Int) {
        viewModelScope.launch {
            val list = items.value?.toMutableList() ?: return@launch
            val movedItem = list.removeAt(fromPosition)
            list.add(toPosition, movedItem)

            // Update positions in the database
            list.forEachIndexed { index, item ->
                item.position = index
                dao.updateItem(item)
            }
        }
    }
}

class TodoListViewModelFactory(private val application: Application, private val listId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoListViewModel(application, listId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}