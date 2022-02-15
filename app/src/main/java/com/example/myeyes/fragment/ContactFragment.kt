package com.example.myeyes.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myeyes.adapter.ContactAdapter
import com.example.myeyes.databinding.FragmentInboxContactSentBinding
import com.example.myeyes.model.ContactUser
import com.example.myeyes.util.Utils.textToSpeechFunctionBasic
import com.example.myeyes.viewmodel.SharedViewModel

class ContactFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var adapter: ContactAdapter

    private var _binding: FragmentInboxContactSentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInboxContactSentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ContactAdapter(requireContext()) { user ->
            speakCall(user)
        }

        binding.recyclerview.adapter = adapter
        observeViewModel()
    }

    private fun observeViewModel() {
        sharedViewModel.apply {
            contactListLoad()
            isContactEmptyImage.observe(viewLifecycleOwner) {
                binding.empty.isVisible = it
            }
            isContactRecyclerView.observe(viewLifecycleOwner) {
                binding.recyclerview.isVisible = it
            }
            contactList.observe(viewLifecycleOwner) {
                adapter.addItems(it)
            }

            isContactRefresh.observe(viewLifecycleOwner) {
                binding.refresh.isRefreshing = it
            }

            binding.refresh.setOnRefreshListener {
                contactRefreshData()
            }
        }
    }
    private fun speakCall(contactUser: ContactUser) {
        textToSpeechFunctionBasic(requireActivity(), contactUser.user_title)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}