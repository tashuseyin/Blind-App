package com.example.myeyes.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.myeyes.adapter.ContactAdapter
import com.example.myeyes.databinding.FragmentInboxContactSentBinding
import com.example.myeyes.model.ContactUser
import com.example.myeyes.util.Utils.textToSpeechFunctionBasic
import com.example.myeyes.viewmodel.SharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        adapter = ContactAdapter(requireContext()) { user, bool ->
            if (!bool) {
                speakCall(user)
            } else {
                textToSpeechFunctionBasic(requireActivity(), "${user.user_title} aranıyor")
                lifecycleScope.launch {
                    delay(1000)
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:" + user.phone_number)
                    ContextCompat.startActivity(requireContext(), callIntent, null)

                }
            }
        }
        binding.recyclerview.adapter = adapter
        observeViewModel()
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
            contactList.observe(viewLifecycleOwner) {
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

    private fun speakCall(contactUser: ContactUser) {
        textToSpeechFunctionBasic(
            requireActivity(),
            "${contactUser.user_title} adli kişiye tıkladınız, aramak istiyorsanız çift tıklayın"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}