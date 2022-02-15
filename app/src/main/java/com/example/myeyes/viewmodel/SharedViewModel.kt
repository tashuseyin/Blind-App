package com.example.myeyes.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.provider.ContactsContract
import android.provider.Telephony
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myeyes.model.ContactUser
import com.example.myeyes.model.Sms

class SharedViewModel(application: Application): AndroidViewModel(application) {
    private val context by lazy {
        application.applicationContext
    }

    private var _smsList: MutableLiveData<ArrayList<Sms>> = MutableLiveData(ArrayList())
    val smsList = _smsList

    val isSmsRecyclerView: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSmsEmptyImage: MutableLiveData<Boolean> = MutableLiveData(false)
    val isSmsRefresh: MutableLiveData<Boolean> = MutableLiveData(false)

    private var _contactList: MutableLiveData<ArrayList<ContactUser>> = MutableLiveData(ArrayList())
    val contactList = _contactList

    val isContactRecyclerView: MutableLiveData<Boolean> = MutableLiveData(false)
    val isContactEmptyImage: MutableLiveData<Boolean> = MutableLiveData(false)
    val isContactRefresh: MutableLiveData<Boolean> = MutableLiveData(false)



    fun loadSms() {
        val smsDataList = ArrayList<Sms>()
        isSmsRefresh.value = false
        val cursor = context.contentResolver?.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            null,
            null,
            null,
            Telephony.Sms.Inbox.DEFAULT_SORT_ORDER
        )

        cursor?.let { c ->
            if (c.moveToFirst()) {
                for (i in 1..c.count) {
                    val smsData = Sms(
                        c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)),
                        c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY)),
                        c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    )
                    smsDataList.add(smsData)
                    c.moveToNext()
                }
            }
            c.close()
        }
        _smsList.value = smsDataList
        if (smsDataList.isEmpty()) {
            isSmsEmptyImage.value = true
            isSmsRecyclerView.value = false
        }
        if (smsDataList.isNotEmpty()) {
            isSmsRecyclerView.value = true
            isSmsEmptyImage.value = false
        }
    }

    fun smsRefreshData() {
        isSmsRefresh.value = true
        loadSms()
    }

    @SuppressLint("Range")
    fun contactListLoad() {
        val contactDataList = ArrayList<ContactUser>()
        isContactRefresh.value = false

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
                        contactDataList.add(ContactUser(id, name, phone))
                    }
                }while (c.moveToNext())
                c.close()
            }
        }
        _contactList.value = contactDataList
        if (contactDataList.isEmpty()) {
            isContactEmptyImage.value = true
            isContactRecyclerView.value = false
        }
        if (contactDataList.isNotEmpty()) {
            isContactRecyclerView.value = true
            isContactEmptyImage.value = false
        }
    }

    fun contactRefreshData() {
        isContactRefresh.value = true
        contactListLoad()
    }
}