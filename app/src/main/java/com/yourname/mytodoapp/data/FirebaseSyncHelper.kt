package com.yourname.mytodoapp.data

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseSyncHelper(private val dao: TodoDao, private val context: Context) {

    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().uid ?: "anonymous"

    private val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        ?: "unknown_device_${System.currentTimeMillis()}"

    private val basePath get() = firestore
        .collection("users")
        .document(userId)
        .collection("devices")
        .document(deviceId)

    private val listsRef get() = basePath.collection("todo_lists")
    private val itemsRef get() = basePath.collection("todo_items")

    suspend fun backupToFirebase() {
        try {
            val lists = dao.getAllListsForBackup()
            val items = dao.getAllItemsForBackup()

            for (list in lists) {
                listsRef.document(list.id.toString()).set(list).await()
            }

            for (item in items) {
                itemsRef.document(item.id.toString()).set(item).await()
            }

            Log.d("FirebaseBackup", "Backup successful for device: $deviceId")
        } catch (e: Exception) {
            Log.e("FirebaseBackup", "Failed to backup: ${e.message}")
        }
    }

    suspend fun restoreFromFirebase() {
        try {
            val listSnap = listsRef.get().await()
            val itemSnap = itemsRef.get().await()

            dao.clearAllItems()
            dao.clearAllLists()

            for (doc in listSnap.documents) {
                val list = doc.toObject(TodoList::class.java)
                if (list != null) dao.insertList(list)
            }

            for (doc in itemSnap.documents) {
                val item = doc.toObject(TodoItem::class.java)
                if (item != null) dao.insertItem(item)
            }

            Log.d("FirebaseRestore", "Restore successful for device: $deviceId")
        } catch (e: Exception) {
            Log.e("FirebaseRestore", "Failed to restore: ${e.message}")
        }
    }
}
