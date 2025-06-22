package com.yourname.mytodoapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yourname.mytodoapp.data.TodoList
import com.yourname.mytodoapp.databinding.ItemTodoListBinding

class AllListsAdapter(
    private val onListClicked: (TodoList) -> Unit,
    private val onDeleteClicked: (TodoList) -> Unit
) : ListAdapter<TodoList, AllListsAdapter.ListViewHolder>(ListDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = getItem(position).id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemTodoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding, onListClicked, onDeleteClicked)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ListViewHolder(
        private val binding: ItemTodoListBinding,
        private val onListClicked: (TodoList) -> Unit,
        private val onDeleteClicked: (TodoList) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: TodoList) {
            binding.listNameTextView.text = list.name
            binding.root.setOnClickListener { onListClicked(list) }
            binding.deleteListButton.setOnClickListener { onDeleteClicked(list) }
        }
    }

    class ListDiffCallback : DiffUtil.ItemCallback<TodoList>() {
        override fun areItemsTheSame(oldItem: TodoList, newItem: TodoList) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TodoList, newItem: TodoList) = oldItem == newItem
    }
}
