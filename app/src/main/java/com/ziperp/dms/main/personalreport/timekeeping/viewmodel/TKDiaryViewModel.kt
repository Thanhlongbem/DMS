package com.ziperp.dms.main.personalreport.timekeeping.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.repository.TKDiaryRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.main.personalreport.timekeeping.model.TKDiaryRequest
import com.ziperp.dms.main.personalreport.timekeeping.model.TKDiaryResponse
import java.text.SimpleDateFormat
import java.util.*

class TKDiaryViewModel(private val repository: TKDiaryRepository) : BaseViewModel() {
    val timeKeepingDiaryData = MutableLiveData<ResponseData<TKDiaryResponse>>()
    var latestItemControls: List<ItemControlForm>? = null

    @SuppressLint("SimpleDateFormat")
    val currentMonth = SimpleDateFormat("yyyyMM").format(Date())
    var requestBody = TKDiaryRequest(staffId = DmsUserManager.userInfo.staffId, month = currentMonth )

    fun getTimeKeepingDiary() {
        timeKeepingDiaryData.postValue(ResponseData.loading(null))

        val pJSONData = Gson().toJson(requestBody)
        val bodyRequest = BodyRequestFactory.get(RequestType.TIME_KEEPING_DIARY, pJSONData)

        repository
            .getTimeKeepingDiary(bodyRequest)
            .applyOn()
            .subscribe({
                timeKeepingDiaryData.postValue(ResponseData.success(it))
            }, {
                timeKeepingDiaryData.postValue(ResponseData.error("Something Went Wrong. Error message " + it.message, null))
            }).disposedBy(compositeDisposable)
    }

    fun applyFilter(itemControls: List<ItemControlForm>) {
        latestItemControls = itemControls

//        requestBody.month = itemControls[0].getFilterValue()

        getTimeKeepingDiary()
    }
}