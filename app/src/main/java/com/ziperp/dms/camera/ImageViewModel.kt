package com.ziperp.dms.camera

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.ziperp.dms.base.BaseViewModel
import com.ziperp.dms.core.repository.ImageRepository
import com.ziperp.dms.core.rest.BodyRequestFactory
import com.ziperp.dms.core.rest.CudData
import com.ziperp.dms.core.rest.RequestType
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.applyOn
import com.ziperp.dms.extensions.disposedBy
import com.ziperp.dms.extensions.execute
import com.ziperp.dms.main.customer.customerimage.model.CustomerImageRequest
import com.ziperp.dms.main.visitcustomer.model.GetFileOfVisitCustomerRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ImageViewModel(val imageRepository: ImageRepository): BaseViewModel() {
    val saveFileLiveData = MutableLiveData<ResponseData<SaveToSQLFileResponse>>()
    val deleteLiveData = MutableLiveData<ResponseData<CudData>>()
    val imageListData = MutableLiveData<ResponseData<List<CustomerImage>>>()

    val uploadFileLiveData2 = MutableLiveData<ResponseData<CudData>>()

    fun updateImageInfo(request: SaveToSQLFileRequest) {
        saveFileLiveData.postValue(ResponseData.loading(null))
        imageRepository
            .saveSQLFileInfo(request)
            .applyOn()
            .subscribe({
                saveFileLiveData.postValue(ResponseData.success(it))
            }, {
                saveFileLiveData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }

    fun deleteOfflineImage(customerImage: CustomerImage) {
        imageRepository
            .deleteImageInDB(customerImage)
            .execute()
            .disposedBy(compositeDisposable)
    }

    fun deleteImage(customerImage: CustomerImage){
        deleteLiveData.postValue(ResponseData.loading())
        imageRepository
            .deleteFile(customerImage)
            .applyOn()
            .subscribe({
                deleteLiveData.postValue(ResponseData.success(it))
            }, {
                deleteLiveData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message)
                )
            }).disposedBy(compositeDisposable)
    }


    fun getListImage(customerCode: String, isVisitCustomer: Boolean) {
        imageListData.postValue(ResponseData.loading(null))

        imageRepository
            .getListImage(customerCode, isVisitCustomer)
            .applyOn()
            .subscribe({
                imageListData.postValue(ResponseData.success(it))
            }, {
                imageListData.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message,
                        null
                    )
                )
            }).disposedBy(compositeDisposable)
    }


    fun uploadFile2(customerImage: CustomerImage) {

        uploadFileLiveData2.postValue(ResponseData.loading())
        imageRepository
            .uploadFile(customerImage)
            .applyOn()
            .subscribe({
                uploadFileLiveData2.postValue(ResponseData.success(it))
            }, {
                uploadFileLiveData2.postValue(
                    ResponseData.error(
                        "Something Went Wrong. Error message " + it.message
                    )
                )
            }).disposedBy(compositeDisposable)
    }


}