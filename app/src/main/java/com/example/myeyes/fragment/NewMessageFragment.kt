package com.example.myeyes.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myeyes.activites.CallActivity
import com.example.myeyes.databinding.FragmentNewMessageBinding
import com.example.myeyes.model.ContactUser
import com.example.myeyes.util.Utils


class NewMessageFragment : Fragment() {
    private var _binding: FragmentNewMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Utils.textToSpeechFunctionBasic(
            requireActivity(),
            "Mesaj göndereceğiniz kişinin telefon numarasını giriniz. Rehberden kişi seçmek için ekrana uzunca tıklayınız."
        )
        binding.messageText.setOnLongClickListener {
            val intent = Intent(activity, CallActivity::class.java)
            intent.putExtra("key", 1)
            startActivity(intent);
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}