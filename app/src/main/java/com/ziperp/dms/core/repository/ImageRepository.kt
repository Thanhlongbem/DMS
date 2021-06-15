package com.ziperp.dms.core.repository

import com.google.gson.Gson
import com.ziperp.dms.camera.CustomerImage
import com.ziperp.dms.camera.CustomerImagesResponse
import com.ziperp.dms.camera.SaveToSQLFileRequest
import com.ziperp.dms.camera.SaveToSQLFileResponse
import com.ziperp.dms.core.rest.*
import com.ziperp.dms.core.service.ImageService
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.dao.CustomerImageDao
import com.ziperp.dms.extensions.execute
import com.ziperp.dms.main.customer.customerimage.model.CustomerImageRequest
import com.ziperp.dms.main.visitcustomer.model.UploadFileResponse
import com.ziperp.dms.utils.LogUtils
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ImageRepository(private val imageService: ImageService, private val imageDao: CustomerImageDao) {

    fun uploadFile(image: MultipartBody.Part, itemCd: RequestBody): Single<UploadFileResponse> {
        return imageService.uploadFile(image, itemCd)
    }

    fun saveSQLFileInfo(request: SaveToSQLFileRequest): Single<SaveToSQLFileResponse> {
        return imageService.saveSQLFileInfo(request)
    }

    fun getListImage(customerCode: String, isVisitCustomer: Boolean): Single<List<CustomerImage>>{
        return Single.zip(
            getImagesFromApi(customerCode, isVisitCustomer),
            getImageInDB(customerCode, isVisitCustomer),
            BiFunction {
                    apiList: List<CustomerImage>, cachedList: List<CustomerImage> ->
                val result = arrayListOf<CustomerImage>()

                if(apiList.isNotEmpty()){
                    result.addAll(apiList)
                    result.addAll(cachedList)

                    LogUtils.d("Combine api and cached")
                }else if(cachedList.isNotEmpty()){
                    result.addAll(cachedList)
                    LogUtils.d("Get from cached")
                }
                return@BiFunction result
            })

    }

    private fun getImagesFromApi(
        customerCode: String,
        isVisitCustomer: Boolean
    ): Single<List<CustomerImage>> {
        val pJsonData = Gson().toJson(CustomerImageRequest(customerCode))
        val bodyRequest = BodyRequestFactory.get(RequestType.CUSTOMER_IMAGE, pJsonData)
        bodyRequest.pFormId = if (isVisitCustomer) "frmDVisitCst" else "frmCAccount"
        return imageService.getFileOfVisitCustomer(bodyRequest)
            .map {
                it.table.forEach { item ->
                    item.isSynchonized = true
                }
                return@map it.table
            }
            .onErrorResumeNext(Single.just(arrayListOf()))
    }

    fun uploadFile(image: CustomerImage): Single<CudData>{
            return saveImageToDB(image)
            .doOnSuccess { image.id = Integer(it.toInt()) }
            .flatMap {
                return@flatMap if(image.isValidCustomerCode()){
                    uploadFileApi(image)
                }else{
                    Single.just(CudData(CUD_OFFLINE, "Save your update in offline"))
                }
            } //always success
            .onErrorResumeNext { Single.just(CudData(CUD_ERROR, "Could not save customer in cache"))}
    }


    fun uploadFiles(customerImages: List<CustomerImage>): Single<CudData> {
        return Observable.fromIterable(customerImages)
            .flatMap {
                return@flatMap uploadFile(it).toObservable()
            }.toList()
            .map {
                CudData(CUD_SUCCESS, "Save Images successfully")
            }
    }


    fun syncAllCustomerImages(): Single<CudData> {
        return imageDao
            .getCustomerImages(false)
            .onErrorResumeNext {
                Single.just(arrayListOf())
            }.flatMap { it ->
                val list = it.filter { it.isValidCustomerCode() }
                return@flatMap if (list.isNotEmpty()) {
                    uploadFiles(list)
                } else {
                    Single.just(CudData(CUD_SUCCESS, "Empty data"))
                }
            }
    }

    fun syncAllVisitCustomerImages(): Single<CudData> {
        return imageDao
            .getCustomerImages(true)
            .onErrorResumeNext {
                Single.just(arrayListOf())
            }.flatMap { it ->
                val list = it.filter { it.isValidCustomerCode() }
                return@flatMap if (list.isNotEmpty()) {
                    uploadFiles(list)
                } else {
                    Single.just(CudData(CUD_SUCCESS, "Empty data"))
                }
            }
    }

    fun uploadFileApi(image: CustomerImage): Single<CudData>{
        //image part
        val file = File(image.localPath)
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val imageRequestBody = MultipartBody.Part.createFormData("", file.name, requestFile)

        // itemCd part
        val siteId = DmsUserManager.userInfo.siteID
        val itemCd = if (image.isVisitCustomer) {
            "$siteId/frmDVisitCst/${image.keyNo}"// visitCustomerNo same as cstCd
        } else {
            "$siteId/frmCAccount/${image.keyNo}"
        }

        val requestBody = itemCd.toRequestBody("text/plain".toMediaTypeOrNull())

        return uploadFile(imageRequestBody, requestBody)
            .flatMap {
                if (it.status == "OK") {
                    val request = SaveToSQLFileRequest(
                        pFileNote = image.fileNote,
                        pPageMode = "A",
                        pKeyNo = image.keyNo,
                        pFileNm = it.name,
                        pFileURL = it.pathFolder,
                        pFormId = if (image.isVisitCustomer) "frmDVisitCst" else "frmCAccount",
                        pUserId = DmsUserManager.userInfo.userId
                    )
                    return@flatMap saveSQLFileInfo(request)
                        .map {
                            imageDao.delete(image)// delete
                            if (it.isSuccess()) {
                                image.isSynchonized = true
                                return@map CudData(CUD_SUCCESS, it.message())
                            } else CudData(CUD_CONFLICT, it.message())
                        }
                } else {
                    return@flatMap Single.just(CudData(CUD_CONFLICT, "Could not upload"))
                }
            }
            .onErrorResumeNext {
                Single.just(CudData(CUD_OFFLINE, "Save your update in offline"))
            }
    }

    fun deleteFile(image: CustomerImage): Single<CudData> {
        return if(image.isValidCustomerCode() && image.isSynchonized){
            deleteFileApi(image)
        }else{
            deleteImageInDB(image).execute()
            Single.just(CudData(CUD_SUCCESS,"Delete in offline"))
        }
    }

    private fun deleteFileApi(image: CustomerImage): Single<CudData>{
        val request = SaveToSQLFileRequest()
        request.apply {
            pPageMode = "C"
            pSeq = image.seq.toString()
            pKeyNo = image.keyNo
            pFileNm = image.fileNm
            pFileNmOld = image.fileNmOld
            pFileURL = image.fileURL
            pFileURLOld = image.fileURLOld
            pIsSaveAfterUpload = "1"
            pFormId = if (image.isVisitCustomer) "frmDVisitCst" else "frmCAccount"
        }


        return saveSQLFileInfo(request)
            .map {
                if (it.isSuccess()) {
                    return@map CudData(CUD_SUCCESS, it.message())
                } else {
                    return@map CudData(CUD_ERROR, it.message())
                }
            }
            .onErrorResumeNext {
                Single.just(CudData(CUD_ERROR, "Could not deleting image"))
            }
    }

    private fun saveImageToDB(customerImage: CustomerImage): Single<Long> {
        return imageDao.insert(customerImage)
    }

    fun deleteImageInDB(customerImage: CustomerImage): Single<Int> {
        return Single.fromCallable{imageDao.delete(customerImage)}
            .map { DAO_SUCCESS }
    }

    private fun getImageInDB(customerCode: String, isVisitCustomer: Boolean): Single<List<CustomerImage>>{
        return imageDao
            .getCustomerImages(customerCode, isVisitCustomer)
            .onErrorResumeNext {
                Single.just(arrayListOf())
            }
    }

}