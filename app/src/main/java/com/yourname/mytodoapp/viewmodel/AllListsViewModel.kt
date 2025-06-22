package com.yourname.mytodoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yourname.mytodoapp.data.AppDatabase
import com.yourname.mytodoapp.data.TodoList
import com.yourname.mytodoapp.data.FirebaseSyncHelper
import kotlinx.coroutines.launch

class AllListsViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).todoDao()
    private val firebaseHelper = FirebaseSyncHelper(dao, application)  // pass context

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    val isLoading = MutableLiveData(false)

    val filteredLists = dao.getAllLists()  // Modify if search logic exists

    fun addList(name: String) {
        if (name.isBlank()) {
            _toastMessage.value = "List name can't be empty"
            return
        }
        viewModelScope.launch {
            dao.insertList(TodoList(name = name))
            _toastMessage.value = "List added"
        }
    }

    fun deleteList(id: Long) {
        viewModelScope.launch {
            dao.deleteListById(id)
            _toastMessage.value = "List deleted"
        }
    }

    fun searchLists(query: String) {
        // Optional: filter LiveData manually, or leave as is
    }

    fun onToastShown() {
        _toastMessage.value = null
    }

    fun backupData() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                firebaseHelper.backupToFirebase()
                _toastMessage.value = "Backup successful"
            } catch (e: Exception) {
                _toastMessage.value = "Backup failed: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun restoreData() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                firebaseHelper.restoreFromFirebase()
                _toastMessage.value = "Restore successful"
            } catch (e: Exception) {
                _toastMessage.value = "Restore failed: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }
}