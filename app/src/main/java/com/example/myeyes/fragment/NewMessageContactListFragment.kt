package com.example.myeyes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.myeyes.R
import com.example.myeyes.adapter.ContactAdapter
import com.example.myeyes.bindingadapter.BindingFragment
import com.example.myeyes.config.DoubleClick
import com.example.myeyes.config.DoubleClickListener
import com.example.myeyes.databinding.FragmentNewMessageContactListBinding
import com.example.myeyes.model.ContactUser
import com.example.myeyes.util.Utils
import com.example.myeyes.viewmodel.SharedViewModel

class NewMessageContactListFragment : BindingFragment<FragmentNewMessageContactListBinding>() {
    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var adapter: ContactAdapter

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewMessageContactListBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Utils.textToSpeechFunctionBasic(requireActivity(), getString(R.string.message_start_text))
        setAdapter()
        observeViewModel()
        setListener()
    }


    private fun setAdapter() {
        adapter = ContactAdapter(requireContext()) { user, clickNumber, bool ->
            when (clickNumber) {
                1 -> speakMessage(user)
                2 -> {
                    findNavController().navigate(
                        MainMessageFragmentDirections.actionMainMessageFragmentToNewMessageFragment(
                            user
                        )
                    )
                }
            }
            if (bool) {
                findNavController().navigate(
                    MainMessageFragmentDirections.actionMainMessageFragmentToNewMessageFragment(
                        null
                    )
                )
            }
        }
        binding.recyclerview.adapter = adapter
    }

    private fun setListener(){
        binding.apply {
            screen.setOnClickListener {
                findNavController().navigate(MainMessageFragmentDirections.actionMainMessageFragmentToNewMessageFragment(null))
            }

            fab.setOnClickListener(DoubleClick(object: DoubleClickListener{
                override fun onSingleClick(view: View) {
                    Utils.textToSpeechFunctionBasic(requireActivity(), getString(R.string.new_message_fab_message))
                }

                override fun onDoubleClick(view: View) {
                    findNavController().navigate(MainMessageFragmentDirections.actionMainMessageFragmentToNewMessageFragment(null))
                }
            }))
        }
    }

    private fun observeViewModel() {
        sharedViewModel.apply {
            contactListLoad()
            isEmptyImage.observe(viewLifecycleOwner) {
                binding.empty.isVisible = it
            }
            isRecyclerView.observe(viewLifecycleOwner) {
                binding.recyclerview.isVisible = it
            }
            contactList?.observe(viewLifecycleOwner) {
                adapter.addItems(it)
            }

            isRefresh.observe(viewLifecycleOwner) {
                binding.refresh.isRefreshing = it
            }

            binding.refresh.setOnRefreshListener {
                contactRefreshData()
            }
        }
    }

    private fun speakMessage(contactUser: ContactUser) {
        Utils.textToSpeechFunctionBasic(
            requireActivity(),
            getString(R.string.user_message_speak, contactUser.user_title)
        )
    }
}