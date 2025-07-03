package com.yourname.mytodoapp.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourname.mytodoapp.R
import com.yourname.mytodoapp.databinding.FragmentAllListsBinding
import com.yourname.mytodoapp.viewmodel.AllListsViewModel

class AllListsFragment : Fragment() {

    private var _binding: FragmentAllListsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AllListsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAllListsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.allListsRecyclerView.apply {
            setHasFixedSize(true)
            itemAnimator?.changeDuration = 0
            setItemViewCacheSize(20)
            layoutManager = LinearLayoutManager(requireContext())
        }

        val adapter = AllListsAdapter(
            onListClicked = { list ->
                val action = AllListsFragmentDirections
                    .actionAllListsFragmentToTodoListFragment(list.id, list.name)
                findNavController().navigate(action)
            },
            onDeleteClicked = { list -> viewModel.deleteList(list.id) }
        )

        binding.allListsRecyclerView.adapter = adapter

        viewModel.filteredLists.observe(viewLifecycleOwner, Observer { lists ->
            // submitList uses DiffUtil on background thread
            adapter.submitList(lists)
        })

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                viewModel.onToastShown()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        binding.addListButton.setOnClickListener {
            val listName = binding.newListEditText.text.toString()
            viewModel.addList(listName)
            binding.newListEditText.text.clear()
        }

        binding.searchBar.addTextChangedListener {
            viewModel.searchLists(it.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_backup  -> { viewModel.backupData(); true }
            R.id.action_restore -> { viewModel.restoreData(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
