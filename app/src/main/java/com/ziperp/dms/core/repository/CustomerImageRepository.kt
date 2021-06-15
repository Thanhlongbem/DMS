package com.ziperp.dms.core.repository

import com.ziperp.dms.camera.CustomerImagesResponse
import com.ziperp.dms.core.rest.BodyRequest
import com.ziperp.dms.core.service.CustomerImageService
import io.reactivex.Single

class CustomerImageRepository(private val customerImageService: CustomerImageService) {
    fun getCustomerImages(request: BodyRequest): Single<CustomerImagesResponse> {
        return customerImageService.getImageList(request)
    }
}