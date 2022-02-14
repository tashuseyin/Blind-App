package com.example.myeyes.viewmodel

import android.app.Application
import android.provider.Telephony
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myeyes.model.Sms

class InboxViewModel(application: Application) : AndroidViewModel(application) {

    private val context by lazy {
        application.applicationContext
    }

    private val dataList: ArrayList<Sms> = ArrayList()
    private var _smsList: MutableLiveData<ArrayList<Sms>> = MutableLiveData(dataList)
    val smsList = _smsList

    val isRecyclerView: MutableLiveData<Boolean> = MutableLiveData(false)
    val isEmptyImage: MutableLiveData<Boolean> = MutableLiveData(false)
    val isRefresh: MutableLiveData<Boolean> = MutableLiveData(false)

    fun loadSms() {
        dataList.clear()
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
                    dataList.add(smsData)
                    c.moveToNext()
                }
            }
            c.close()
        }
        _smsList.value = dataList
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
        loadSms()
    }
}