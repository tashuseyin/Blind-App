package com.example.myeyes.fragment

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.myeyes.R
import com.example.myeyes.app.MyApp
import com.example.myeyes.bindingadapter.BindingFragment
import com.example.myeyes.config.DoubleClick
import com.example.myeyes.config.DoubleClickListener
import com.example.myeyes.databinding.FragmentMainBinding
import com.example.myeyes.util.Utils
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MainFragment : BindingFragment<FragmentMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMainBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appPermitted()
        setListener()
    }

    private fun setListener() {
        binding.apply {
            battery.setOnClickListener(DoubleClick(object : DoubleClickListener {
                override fun onSingleClick(view: View) {
                    Utils.textToSpeechFunctionBasic(
                        requireActivity(),
                        getString(R.string.battery_login)
                    )
                }

                override fun onDoubleClick(view: View) {
                    (activity?.applicationContext as MyApp).textToSpeech?.stop()
                    findNavController().navigate(MainFragmentDirections.actionMainFragmentToBatteryFragment())
                }
            }))

            message.setOnClickListener(DoubleClick(object : DoubleClickListener {
                override fun onSingleClick(view: View) {
                    Utils.textToSpeechFunctionBasic(
                        requireActivity(),
                        getString(R.string.message_login)
                    )
                }

                override fun onDoubleClick(view: View) {
                    if (appPermitted()) {
                        openSms()
                    } else {
                        Utils.textToSpeechFunctionBasic(
                            requireActivity(),
                            getString(R.string.permission_error_message)
                        )
                    }
                }
            }))

            call.setOnClickListener(DoubleClick(object : DoubleClickListener {
                override fun onSingleClick(view: View) {
                    Utils.textToSpeechFunctionBasic(
                        requireActivity(),
                        getString(R.string.call_login)
                    )
                }

                override fun onDoubleClick(view: View) {
                    if (appPermitted()) {
                        openCall()
                    } else {
                        Utils.textToSpeechFunctionBasic(
                            requireActivity(),
                            getString(R.string.permission_error_message)
                        )
                    }
                }
            }))
        }
    }

    private fun openSms() {
        (activity?.applicationContext as MyApp).textToSpeech?.stop()
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToMainMessageFragment())
    }

    private fun openCall() {
        (activity?.applicationContext as MyApp).textToSpeech?.stop()
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToMainCallFragment())
    }

    private fun appPermitted(): Boolean {
        var permitted = false
        Dexter.withContext(context)
            .withPermissions(
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_CONTACTS,
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        permitted = true
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        Utils.textToSpeechFunctionBasic(
                            requireActivity(),
                            getString(R.string.permission_settings_message)
                        )
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
        return permitted
    }
}