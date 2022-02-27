package com.example.myeyes.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.myeyes.R
import com.example.myeyes.adapter.ContactAdapter
import com.example.myeyes.bindingadapter.BindingFragment
import com.example.myeyes.databinding.FragmentInboxContactSentBinding
import com.example.myeyes.model.ContactUser
import com.example.myeyes.util.Utils.textToSpeechFunctionBasic
import com.example.myeyes.viewmodel.SharedViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ContactFragment : BindingFragment<FragmentInboxContactSentBinding>() {

    private val sharedViewModel: SharedViewModel by viewModels()
    private lateinit var adapter: ContactAdapter

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentInboxContactSentBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ContactAdapter(requireContext()) { user, clickNumber ->
            when (clickNumber) {
                1 -> speakCall(user)
                2 -> callUser(user)
                3 -> null
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

    private fun speakCall(contactUser: ContactUser) {
        textToSpeechFunctionBasic(
            requireActivity(),
            getString(R.string.user_call_speak, contactUser.user_title)
        )
    }

    private fun callUser(user: ContactUser) {
        textToSpeechFunctionBasic(requireActivity(), getString(R.string.call_user, user.user_title))
        lifecycleScope.launch {
            delay(1000)
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:" + user.phone_number)
            ContextCompat.startActivity(requireContext(), callIntent, null)

        }
    }
}