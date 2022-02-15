package com.example.myeyes.fragmet

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myeyes.adapter.ContactAdapter
import com.example.myeyes.app.MyApp
import com.example.myeyes.databinding.FragmentInboxContactSentBinding
import com.example.myeyes.model.ContactUser
import com.example.myeyes.util.Utils
import com.example.myeyes.viewmodel.ContactViewModel

class ContactFragment : Fragment() {

    private val contactViewModel: ContactViewModel by viewModels()

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

        val adapter = ContactAdapter(requireContext()) { user ->
            speakCall(user)
        }
        binding.recyclerview.adapter = adapter

        contactViewModel.apply {
            contactListLoad()
            isEmptyImage.observe(viewLifecycleOwner) {
                binding.empty.isVisible = it
            }
            isRecyclerView.observe(viewLifecycleOwner) {
                binding.recyclerview.isVisible = it
            }
            contactList.observe(viewLifecycleOwner) {
                adapter.addItems(it)
            }

            isRefresh.observe(viewLifecycleOwner) {
                binding.refresh.isRefreshing = it
            }

            binding.refresh.setOnRefreshListener {
                refreshData()
            }
        }
    }

    private fun speakCall(contactUser: ContactUser) {
        ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
            contactUser.user_title, TextToSpeech.QUEUE_FLUSH, null
        )

        ((activity?.applicationContext as MyApp)).textToSpeech?.speak(
            contactUser.phone_number, TextToSpeech.QUEUE_ADD, null
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}