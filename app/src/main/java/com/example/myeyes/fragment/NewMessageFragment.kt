package com.example.myeyes.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.myeyes.activites.CallActivity
import com.example.myeyes.bindingadapter.BindingFragment
import com.example.myeyes.databinding.FragmentNewMessageBinding
import com.example.myeyes.util.Utils


class NewMessageFragment : BindingFragment<FragmentNewMessageBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentNewMessageBinding::inflate

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

}