package com.yourname.mytodoapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yourname.mytodoapp.data.TodoItem
import com.yourname.mytodoapp.databinding.ItemTodoItemBinding

class TodoItemsAdapter(
    private val onEditClicked: (TodoItem) -> Unit,
    private val onDeleteClicked: (TodoItem) -> Unit,
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit
) : ListAdapter<TodoItem, TodoItemsAdapter.ItemViewHolder>(ItemDiffCallback()) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = getItem(position).id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemTodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, onEditClicked, onDeleteClicked)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.dragHandle.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                holder.binding.dragHandle.postDelayed({
                    onStartDrag(holder)
                }, 50) // Small delay improves UX
            }
            false
        }
    }

    class ItemViewHolder(
        val binding: ItemTodoItemBinding,
        private val onEditClicked: (TodoItem) -> Unit,
        private val onDeleteClicked: (TodoItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TodoItem) {
            binding.itemTextView.text = item.text
            binding.editItemButton.setOnClickListener { onEditClicked(item) }
            binding.deleteItemButton.setOnClickListener { onDeleteClicked(item) }
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<TodoItem>() {
        override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem) = oldItem == newItem
    }
}