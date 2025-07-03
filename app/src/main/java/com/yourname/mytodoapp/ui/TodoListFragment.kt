package com.yourname.mytodoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourname.mytodoapp.data.TodoItem
import com.yourname.mytodoapp.databinding.FragmentTodoListBinding
import com.yourname.mytodoapp.viewmodel.TodoListViewModel
import com.yourname.mytodoapp.viewmodel.TodoListViewModelFactory

class TodoListFragment : Fragment() {

    private var _binding: FragmentTodoListBinding? = null
    private val binding get() = _binding!!
    private val args: TodoListFragmentArgs by navArgs()
    private lateinit var itemTouchHelper: ItemTouchHelper

    private val viewModel: TodoListViewModel by viewModels {
        TodoListViewModelFactory(requireActivity().application, args.listId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1️⃣ Adapter + RecyclerView setup with caching & fixed size
        val adapter = TodoItemsAdapter(
            onEditClicked = { item -> showEditDialog(item) },
            onDeleteClicked = { item -> viewModel.deleteItem(item) },
            onStartDrag = { vh -> itemTouchHelper.startDrag(vh) }
        )

        binding.todoItemsRecyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)                       // speed up measuring
            itemAnimator?.changeDuration = 0            // no change animations
            setItemViewCacheSize(10)                    // reduced cache size
            // Remove deprecated drawing cache
        }

        // Observe & submit list using DiffUtil in background
        viewModel.items.observe(viewLifecycleOwner, Observer { items ->
            adapter.submitList(items)
        })

        // 3️⃣ Add-button handler
        binding.addItemButton.setOnClickListener {
            val text = binding.newItemEditText.text.toString()
            if (text.isNotBlank()) {
                viewModel.addItem(text)
                binding.newItemEditText.text.clear()
            }
        }

        // 4️⃣ Drag-and-drop
        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            0
        ) {
            override fun onMove(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                vh: androidx.recyclerview.widget.RecyclerView.ViewHolder,
                target: androidx.recyclerview.widget.RecyclerView.ViewHolder
            ): Boolean {
                viewModel.onItemsMoved(vh.adapterPosition, target.adapterPosition)
                return true
            }
            override fun onSwiped(vh: androidx.recyclerview.widget.RecyclerView.ViewHolder, direction: Int) {
                // no-op
            }
        }
        itemTouchHelper = ItemTouchHelper(callback).apply {
            attachToRecyclerView(binding.todoItemsRecyclerView)
        }
    }

    private fun showEditDialog(item: TodoItem) {
        val editText = EditText(requireContext()).apply { setText(item.text) }
        AlertDialog.Builder(requireContext())
            .setTitle("Edit Item")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val newText = editText.text.toString()
                if (newText.isNotBlank()) {
                    item.text = newText
                    viewModel.updateItem(item)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
