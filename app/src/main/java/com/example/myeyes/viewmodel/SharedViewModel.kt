package com.example.myeyes.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.provider.ContactsContract
import android.provider.Telephony
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myeyes.db.ContactRepository
import com.example.myeyes.model.ContactUser
import com.example.myeyes.model.Sms
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) :
    AndroidViewModel(application) {

    private val repository = ContactRepository

    private val context by lazy {
        application.applicationContext
    }

    private var smsDataList = ArrayList<Sms>()
    private var _smsList: MutableLiveData<ArrayList<Sms>> = MutableLiveData(smsDataList)
    val smsList = _smsList

    val isRecyclerView: MutableLiveData<Boolean> = MutableLiveData(false)
    val isEmptyImage: MutableLiveData<Boolean> = MutableLiveData(false)
    val isRefresh: MutableLiveData<Boolean> = MutableLiveData(false)

    private var contactDataList = ArrayList<ContactUser>()
    private var _contactList: LiveData<List<ContactUser>>? = getContactUser()
    val contactList = _contactList

    private var sentSmsDataList = ArrayList<Sms>()
    private var _sentSmsList: MutableLiveData<ArrayList<Sms>> = MutableLiveData(sentSmsDataList)
    val sentSmsList = _sentSmsList


    fun loadSms() {
        smsDataList.clear()
        isRefresh.value = false
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
            isEmptyImage.value = true
            isRecyclerView.value = false
        }
        if (smsDataList.isNotEmpty()) {
            isRecyclerView.value = true
            isEmptyImage.value = false
            usernameCheck()
        }
    }

    fun smsRefreshData() {
        isRefresh.value = true
        loadSms()
    }

    @SuppressLint("Range")
    fun contactListLoad() {
        contactDataList.clear()
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

                    if (phone.isNotBlank()) {
                        contactDataList.add(ContactUser(id, name, phone))
                        viewModelScope.launch {
                            insert(ContactUser(id, name, phone.replace(" ","")))
                        }
                    }
                } while (c.moveToNext())
                c.close()
            }
        }
        if (contactDataList.isEmpty()) {
            isEmptyImage.value = true
            isRecyclerView.value = false
        }
        if (contactDataList.isNotEmpty()) {
            isRecyclerView.value = true
            isEmptyImage.value = false
        }
    }

    fun contactRefreshData() {
        isRefresh.value = true
        contactListLoad()
    }

    suspend fun insert(contactUser: ContactUser) {
        repository.insert(contactUser)
    }

    fun getContactUser() = repository.getContactUser()

    fun searchPhoneNumberDatabase(searchNumber: String) = repository.searchPhoneNumber(searchNumber)


    private fun usernameCheck() {
        contactListLoad()
        for (i in smsDataList) {
            for (j in contactDataList) {
                if (i.address == j.phone_number.replace(" ", "")) {
                    i.address = j.user_title
                }
            }
        }
    }

    fun sentMessage() {
        sentSmsDataList.clear()
        isRefresh.value = false
        val cursor = context.contentResolver?.query(
            Telephony.Sms.Sent.CONTENT_URI,
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
                    sentSmsDataList.add(smsData)
                    c.moveToNext()
                }
            }
            c.close()
        }
        _sentSmsList.value = sentSmsDataList
        if (sentSmsDataList.isEmpty()) {
            isEmptyImage.value = true
            isRecyclerView.value = false
        }
        if (sentSmsDataList.isNotEmpty()) {
            isRecyclerView.value = true
            isEmptyImage.value = false
        }
    }

    fun sentSmsRefreshData() {
        isRefresh.value = true
        sentMessage()
    }
}