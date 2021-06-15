package com.ziperp.dms.core.rest

object Constants {
    const val DEFAULT_SERVER = "https://staging.ziperp.vn"
    var API_BASE_PATH = DEFAULT_SERVER
    const val API_KEY = "9188a340aa76474493f47e9617bf9c65"
    const val TYPE_LANGUAGE = 0
    const val TYPE_LIST_SERVER = 1

    const val PREF_CURRENT_LANGUAGE = "PREF_CURRENT_LANGUAGE"

    const val EXTRA_CUSTOMER_CODE = "EXTRA_CUSTOMER_CODE"
    const val EXTRA_CUSTOMER_DATA = "EXTRA_CUSTOMER_DATA"
    const val EXTRA_CUSTOMER_NAME = "EXTRA_CUSTOMER_NAME"
    const val EXTRA_SALE_ORDER_DETAIL = "EXTRA_SALE_ORDER_DETAIL"
    const val EXTRA_SALE_ORDER_VISIT = "EXTRA_SALE_ORDER_VISIT"
    const val CLIPBOARD = "CLIPBOARD"


    object Service{
        const val NOTIFICATION_CHANNEL = "DMS_CHANNEL"
        const val SYNC_CHANNEL = "SYNC_CHANNEL"
        const val STOP_SERVICE_ACTION = "STOP_SERVICE_ACTION"
    }

    object API{
        const val PATH_QUERY_DATA = "/api/CRUDDataWithJSON/QueryWithJSONData"
        const val PATH_CRUD_FORM = "/api/CRUDDataWithJSON/CUDWithJSONData"
        const val PATH_GET_DIALOG_META = "/api/TYDlgMeta/GetDlgMetaMaster"
        const val PATH_GET_DIALOG_DATA = "/api/TYDlgMeta/GetDlgMetaGrid"
        const val PATH_GET_COMBO_META = "/api/ComboData/GetComboMeta"
        const val PATH_GET_COMBO_DATA = "/api/ComboData/GetComboData"
        const val PATH_UPLOAD_FILE = "api/UploadFileMaster/UploadFile"
        const val PATH_UPDATE_AVATAR = "api/StaffBase/UploadImgProfile"
        const val PATH_SAVE_FILE_INFO = "/api/UploadFileMaster/SaveToSQLFileInfo"

    }

    object CUD {
        const val TYPE_CREATE = "A"
        const val TYPE_UPDATE = "U"
        const val TYPE_DELETE = "D"

        const val STATE_LOADING = 0
        const val STATE_SUCCESS = 1
        const val STATE_ERROR = -1
    }

    object ObjectType {
        const val LEAD = 1
        const val CONTACT = 2
        const val OPPORTUNITY = 3
        const val ACCOUNT = 4
    }

    const val SP_KEY_USER_INFO = "SP_KEY_USER_INFO"
}

data class CudData(val status: Int, val message: String, var data: Any? = null){
    fun isSuccess() = (status == CUD_SUCCESS)
}

const val DAO_SUCCESS = 1
const val DAO__ERROR = -1

const val CUD_SUCCESS = 1
const val CUD_CONFLICT = 0
const val CUD_ERROR = -1
const val CUD_OFFLINE = -2

const val RESULT_RELOAD = 2
const val RESULT_FINISH = -2
