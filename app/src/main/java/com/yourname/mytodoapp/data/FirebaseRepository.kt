package com.yourname.mytodoapp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private suspend fun getUserId(): String {
        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }
        return auth.currentUser!!.uid
    }

    suspend fun backupData(lists: List<TodoList>, items: List<TodoItem>) {
        val userId = getUserId()
        val userDocRef = firestore.collection("users").document(userId)

        firestore.runBatch { batch ->
            val listsCollection = userDocRef.collection("lists")
            lists.forEach { list -> batch.set(listsCollection.document(list.id.toString()), list) }

            val itemsCollection = userDocRef.collection("items")
            items.forEach { item -> batch.set(itemsCollection.document(item.id.toString()), item) }
        }.await()
    }

    suspend fun restoreData(): Pair<List<TodoList>, List<TodoItem>> {
        val userId = getUserId()
        val userDocRef = firestore.collection("users").document(userId)

        val lists = userDocRef.collection("lists").get().await().documents.mapNotNull { it.toObject<TodoList>() }
        val items = userDocRef.collection("items").get().await().documents.mapNotNull { it.toObject<TodoItem>() }

        return Pair(lists, items)
    }
}