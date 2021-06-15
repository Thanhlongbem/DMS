package com.ziperp.dms.core.rest

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

data class ResponseData<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T? = null): ResponseData<T> {
            return ResponseData(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T? = null): ResponseData<T> {
            return ResponseData(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): ResponseData<T> {
            return ResponseData(Status.LOADING, data, null)
        }

    }

}
