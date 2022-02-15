package com.example.myeyes.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myeyes.model.ContactUser

class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val context by lazy {
        application.applicationContext
    }

    private var _contactList: MutableLiveData<ArrayList<ContactUser>> = MutableLiveData(ArrayList())
    val contactList = _contactList

    val isRecyclerView: MutableLiveData<Boolean> = MutableLiveData(false)
    val isEmptyImage: MutableLiveData<Boolean> = MutableLiveData(false)
    val isRefresh: MutableLiveData<Boolean> = MutableLiveData(false)


    @SuppressLint("Range")
    fun contactListLoad() {
        val dataList = ArrayList<ContactUser>()
        isRefresh.value = false

        val FILTER = ContactsContract.Contacts.DISPLAY_NAME + " NOT LIKE '%@%'"
        val ORDER = String.format("%1\$s COLLATE NOCASE", ContactsContract.Contacts.DISPLAY_NAME)
        val PROJECTION = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        )

        val cursor = context?.contentResolver?.query(
            ContactsContract.Contacts.CONTENT_URI, PROJECTION, FILTER, null, ORDER
        )

        cursor?.let { c ->
            if (c.moveToFirst()) {
                do {
                    val id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val hasPhone =
                        c.getInt(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    var phone = ""
                    if (hasPhone > 0) {
                        val cp = context?.contentResolver?.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id),
                            null
                        )

                        cp?.let {
                            if (it.moveToFirst()) {
                                phone =
                                    cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                cp.close()
                            }
                        }
                    }

                    if (!phone.isBlank()) {
                        dataList.add(ContactUser(id, name, phone))
                    }
                }while (c.moveToNext())
                c.close()
            }
        }
        _contactList.value = dataList
        if (dataList.isEmpty()) {
            isEmptyImage.value = true
            isRecyclerView.value = false
        }
        if (dataList.isNotEmpty()) {
            isRecyclerView.value = true
            isEmptyImage.value = false
        }
    }

    fun refreshData() {
        isRefresh.value = true
        contactListLoad()
    }
}