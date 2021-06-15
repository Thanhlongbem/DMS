package com.ziperp.dms.core.rest

import com.ziperp.dms.core.form.model.ComboMetaResponse
import com.ziperp.dms.core.form.model.DialogMetaResponse
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.rest.Constants.PREF_CURRENT_LANGUAGE
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.utils.DMSPreference

enum class RequestType {
    VISIT_CUSTOMER, VISIT_CUSTOMER_INFO, SAVE_VISIT_CUSTOMER_INFO, CUD_ITEM_COUNT, ITEM_COUNT_INFO, VISIT_CUSTOMER_ORDER,
    SALE_ORDER, SALE_ORDER_INFO, CUD_SALES_ORDER, CUD_SALES_ORDER_LINES, CONFIRM_SALE_ORDER,
    UNIT_PRICE, AVAILABLE_STOCK, EXCHANGE_RATE, APPLY_PROMOTION,
    TIME_KEEPING, SAVE_TIME_KEEPING,
    ITEM_MASTER, ITEM_MASTER_DETAIL, ITEM_MASTER_FILE,
    SALE_PRICE, SALE_PRICE_INFO, PROMOTION, PROMOTION_INFO,
    CUSTOMER, CUSTOMER_DETAIL, CUD_CUSTOMER, CUSTOMER_NOTE, NOTE_INFO, CUD_NOTE, CUSTOMER_ROUTE, ADD_ROUTE, CUSTOMER_IMAGE,
    TRACKING_REPORT, STAFF_TRACKING, VISIT_CUSTOMER_TRACKING, PICTURE_TRACKING, CUSTOMER_NOT_ORDER, STAFF_SALES_RESULT, DAILY_PERFORMANCE, NEW_SALES_POINT, SALES_ON_ROUTE, REPORT_SUMMATION,
    PERSONAL_REPORT, VISIT_RESULT, VISIT_DETAIL, TIME_KEEPING_DIARY, YOUR_SALES_RESULT, DATA_SUMMARY,
    STOCK_QTY,
    STOCK_QTY_BY_LOT_SERIAL,
    USER_INFO, USER_PASSWORD
}


object FormParamFactory {

    const val DIALOG_META = 0
    const val DIALOG_DATA = 1
    const val COMBO_META = 2
    const val COMBO_DATA = 3


    fun getDialogMetaParam(itemControlForm: ItemControlForm): Map<String, String> {
        val map = hashMapOf<String, String>()
        map["pCodeCd"] = itemControlForm.lookUpCode
        map["pUserId"] = DmsUserManager.userInfo.userId //"son.dn@ziperp.vn"
        map["pLangId"] = DMSPreference.getInt(PREF_CURRENT_LANGUAGE, 1).toString()
        map["pFormId"] = itemControlForm.formId
        map["pCtrlId"] = itemControlForm.controlId
        map["pConnStr"] = ""
        return map
    }

    fun getDialogDataParam(
        itemControlForm: ItemControlForm,
        metaResponse: DialogMetaResponse,
        pageNumber : Int,
        lookUpValue: String = ""
    ): Map<String, String> {
        val map = hashMapOf<String, String>()
        map["pCodeCd"] = itemControlForm.lookUpCode
        metaResponse.selections[0].searchFirst.apply{
            if(isBlank()){
                map["pFindType"] = "N"
            }else{
                map["pFindType"] = this
            }
        }
        map["pLookupVal"] = lookUpValue
        map["pSubCondCd"] = ""
        map["pMasterLocCd"] = DmsUserManager.userInfo.masterLocCd //"0001"
        map["pFormId"] = itemControlForm.formId
        map["pCtrlId"] = itemControlForm.controlId

        val pRowspPage = if(pageNumber == 0) 0 else 50
        map["pRowspPage"] = pRowspPage.toString()
        map["pPageNumber"] = pageNumber.toString()

        //Map param
        itemControlForm.param.apply {
            map["pItem1"] = item1
            map["pItem2"] = item2
            map["pItem3"] = item3
            map["pItem4"] = item4
            map["pItem5"] = item5
            map["pItem6"] = item6
            map["pItem7"] = item7
            map["pItem8"] = item8
            map["pItem9"] = item9
            map["pItem10"] = item10
        }

        map["pDlgQrySP"] = metaResponse.selections[0].dlgQrySP
        map["pUserId"] = DmsUserManager.userInfo.userId//"son.dn@ziperp.vn"
        map["pLangId"] = DMSPreference.getInt(PREF_CURRENT_LANGUAGE, 1).toString()

        map["pConnStr"] = ""
        return map
    }

    fun getComboMetaParam(itemControlForm: ItemControlForm): Map<String, String> {
        val map = hashMapOf<String, String>()
        map["pCodeCd"] = itemControlForm.lookUpCode
        map["pUserId"] = DmsUserManager.userInfo.userId//"son.dn@ziperp.vn"
        map["pLangId"] = DMSPreference.getInt(PREF_CURRENT_LANGUAGE, 1).toString()
        map["pFormId"] = itemControlForm.formId
        map["pCmbId"] = itemControlForm.controlId
        map["pConnStr"] = ""
        return map
    }

    fun getComboDataParam(itemControlForm: ItemControlForm, metaResponse: ComboMetaResponse?): Map<String, String> {
        val map = hashMapOf<String, String>()
        map["pType"] = itemControlForm.findType
        map["pCodeCd"] = itemControlForm.lookUpCode

        map["pFormId"] = itemControlForm.formId
        map["pCmbId"] = itemControlForm.controlId

        map["pParentCd"] = ""
        map["pDlgCd"] = ""
        map["pJoinCd"] = ""
        map["pCmbCd"] = ""
        map["pMasterLoc"] = DmsUserManager.userInfo.masterLocCd//"0001"


        //Map param
        itemControlForm.param.apply {
            map["pItem1"] = item1
            map["pItem2"] = item2
            map["pItem3"] = item3
            map["pItem4"] = item4
            map["pItem5"] = item5
            map["pItem6"] = item6
            map["pItem7"] = item7
            map["pItem8"] = item8
            map["pItem9"] = item9
            map["pItem10"] = item10
        }

        map["pSPGetComboData"] = metaResponse?.queries?.get(0)?.cmbQryStr ?: ""
        map["pUserId"] = DmsUserManager.userInfo.userId//"son.dn@ziperp.vn"
        map["pLangId"] = DMSPreference.getInt(PREF_CURRENT_LANGUAGE, 1).toString()

        map["pConnStr"] = ""
        return map
    }

}

class BodyRequestFactory {
    companion object {
        fun get(type: RequestType, pJsonData: String, pPageMode: String = "Q"): BodyRequest {
            val bodyRequest = BodyRequest(
                pUserId = DmsUserManager.userInfo.userId,
                pLangId = DMSPreference.getInt(PREF_CURRENT_LANGUAGE, 1).toString(),
                pConnStr = "",
                pPageMode = pPageMode,
                pJSONData = pJsonData
            )
            bodyRequest.apply {
                when (type) {
                    RequestType.USER_INFO -> {
                        pSPName = "SP_Save_UserProfile"
                        pFormId = "frmRStaff"
                    }
                    RequestType.USER_PASSWORD -> {
                        pSPName = "SP_Save_UserPassword"
                        pFormId = "frmRStaff"
                    }
                    RequestType.VISIT_CUSTOMER -> {
                        pSPName = "SP_Get_qryDVisitCstList_Mobile"
                        pFormId = "qryDVisitCstList"
                    }
                    RequestType.VISIT_CUSTOMER_INFO -> {
                        pSPName = "SP_Get_frmDVisitCst"
                        pFormId = "frmDVisitCst"
                    }
                    RequestType.CUD_ITEM_COUNT -> {
                        pSPName = "SP_Save_frmDVisitCstLines"
                        pFormId = "frmDVisitCst"
                    }
                    RequestType.ITEM_COUNT_INFO -> {
                        pSPName = "SP_Get_frmDVisitCstLines"
                        pFormId = "frmDVisitCst"
                    }
                    RequestType.VISIT_CUSTOMER_ORDER -> {
                        pSPName = "SP_Get_frmDVisitCstOrders"
                        pFormId = "frmDVisitCst"
                    }
                    RequestType.SAVE_VISIT_CUSTOMER_INFO -> {
                        pSPName = "SP_Save_frmDVisitCst"
                        pFormId = "frmDVisitCst"
                    }
                    RequestType.SALE_ORDER -> {
                        pSPName = "SP_Get_qrySOrder_Mobile"
                        pFormId = "qrySOrder"
                    }
                    RequestType.SALE_ORDER_INFO -> {
                        pSPName = "SP_Get_frmSOrder_Mobile"
                        pFormId = "frmSOrder"
                    }
                    RequestType.CUD_SALES_ORDER-> {
                        pSPName = "SP_Save_frmSOrder_Mobile"
                        pFormId = "frmSOrder"
                    }
                    RequestType.CUD_SALES_ORDER_LINES-> {
                        pSPName = "SP_Save_frmSOrderLine_Mobile"
                        pFormId = "frmSOrder"
                    }
                    RequestType.TIME_KEEPING-> {
                        pSPName = "SP_Get_frmDTimekeeping"
                        pFormId = "frmDTimekeeping"
                    }
                    RequestType.SAVE_TIME_KEEPING-> {
                        pSPName = "SP_Save_frmDTimekeeping"
                        pFormId = "frmDTimekeeping"
                    }
                    RequestType.CUSTOMER -> {
                        pSPName = "SP_Get_qryCAccount_MobileApp"
                        pFormId = "qryCAccount"
                    }
                    RequestType.CUSTOMER_DETAIL -> {
                        pSPName = "SP_Get_frmCAccount"
                        pFormId = "frmCAccount"
                    }
                    RequestType.CUD_CUSTOMER -> {
                        pSPName = "SP_Save_frmCAccount"
                        pFormId = "frmCAccount"
                    }
                    RequestType.CUSTOMER_NOTE -> {
                        pSPName = "SP_Get_frmCAccountLines_7"
                        pFormId = "frmCAccount"
                    }
                    RequestType.NOTE_INFO -> {
                        pSPName = "SP_Get_dlgYNote"
                        pFormId = "dlgYNote"
                    }
                    RequestType.CUSTOMER_ROUTE -> {
                        pSPName = "SP_frmYBizPartner_GetRoutes"
                        pFormId = "frmDRoute"
                    }
                    RequestType.ADD_ROUTE -> {
                        pSPName = "SP_frmYBizPartner_AddRoute"
                        pFormId = "frmDRoute"
                    }
                    RequestType.CUSTOMER_IMAGE -> {
                        pSPName = "SP_Get_frmYListAttachFiles"
                    }
                    RequestType.ITEM_MASTER -> {
                        pSPName = "SP_Get_qryYItemMaster_Mobile"
                        pFormId = "frmCAccount"
                    }
                    RequestType.ITEM_MASTER_DETAIL -> {
                        pSPName = "SP_Get_frmYItemMaster_Mobile"
                        pFormId = "frmYItemMaster"
                    }
                    RequestType.ITEM_MASTER_FILE -> {
                        pSPName = "SP_Get_frmYItemPhoto_Mobile"
                        pFormId = "frmYItemMaster"
                    }
                    RequestType.SALE_PRICE -> {
                        pSPName = "SP_Get_qrySSalesPriceList_Mobile"
                        pFormId = "qrySSalesPriceList"
                    }
                    RequestType.SALE_PRICE_INFO -> {
                        pSPName = "SP_Get_frmSSalesPriceList_Mobile"
                        pFormId = "frmSSalesPriceList"
                    }
                    RequestType.PROMOTION -> {
                        pSPName = "SP_Get_qrySSalesPromotion_Mobile"
                        pFormId = "qrySSalesPromotion"
                    }
                    RequestType.PROMOTION_INFO -> {
                        pSPName = "SP_Get_frmSSalesPromotion_Mobile"
                        pFormId = "frmSSalesPromotion"
                    }
                    RequestType.CUD_NOTE -> {
                        pSPName = "SP_Save_dlgYNote"
                        pFormId = "dlgYNote"
                    }
                    RequestType.STOCK_QTY -> {
                        pSPName = "SP_Get_qryDItemStk_Mobile"
                        pFormId = "qryWBizStkbyLoc"
                    }
                    RequestType.STOCK_QTY_BY_LOT_SERIAL -> {
                        pSPName = "SP_Get_qryDItemStkByLotSeri_Mobile"
                        pFormId = "frmWStkBizSumRpt"
                    }
                    RequestType.UNIT_PRICE -> {
                        pSPName = "SP_Get_ItemsPrice_Mobile"
                        pFormId = "frmSOrder"
                    }
                    RequestType.AVAILABLE_STOCK -> {
                        pSPName = "SP_Check_CurrentStk_Mobile"
                        pFormId = "frmSOrder"
                    }
                    RequestType.EXCHANGE_RATE -> {
                        pSPName = "SP_Get_ExRate_Mobile"
                        pFormId = "frmYExRate"
                    }
                    RequestType.CONFIRM_SALE_ORDER -> {
                        pSPName = "SP_Common_Cfm_Mobile"
                        pFormId = "frmSOrder"
                    }
                    RequestType.APPLY_PROMOTION -> {
                        pSPName = "SP_Calc_OrderPromo_Mobile"
                        pFormId = "frmSOrder"
                    }
                    RequestType.STAFF_TRACKING -> {
                        pSPName = "SP_Get_qryDStaffTrackingLines"
                        pFormId = "qryDStaffTracking"
                    }
                    RequestType.VISIT_CUSTOMER_TRACKING -> {
                        pSPName = "SP_Get_qryDVisitTrackingLines"
                        pFormId = "qryDVisitTracking"
                    }
                    RequestType.PICTURE_TRACKING -> {
                        pSPName = "SP_Get_qryDVisitImgTracking"
                        pFormId = "qryDVisitImgTracking"
                    }
                    RequestType.CUSTOMER_NOT_ORDER -> {
                        pSPName = "SP_Get_qryCNewAccount"
                        pFormId = "qryCNewAccount"
                    }
                    RequestType.STAFF_SALES_RESULT -> {
                        pSPName = "SP_Get_qrySSalesAnalyMnStaff_Mobile"
                        pFormId = "qrySSalesAnalyMnStaff"
                    }
                    RequestType.DAILY_PERFORMANCE -> {
                        pSPName = "SP_Get_qryDOrderAndWorkEffect"
                        pFormId = "qryDOrderAndWorkEffect"
                    }
                    RequestType.NEW_SALES_POINT -> {
                        pSPName = "SP_Get_qryCNewCstSummary_Mobile"
                        pFormId = "qryCNewCstSummary"
                    }
                    RequestType.SALES_ON_ROUTE -> {
                        pSPName = "SP_Get_qryDSalesEffectByRoute_Mobile"
                        pFormId = "qryDSalesEffectByRoute"
                    }
                    RequestType.REPORT_SUMMATION -> {
                        pSPName = "SP_Get_qryCCstSummaryRpt_Mobile"
                        pFormId = "qryCCstSummaryRpt"
                    }
                    RequestType.VISIT_RESULT -> {
                        pSPName = "SP_Get_qryDVisitTrackingLines"
                        pFormId = "qryDVisitTracking"
                    }
                    RequestType.VISIT_DETAIL -> {
                        pSPName = "SP_Get_qryDVisitTrackingLines"
                        pFormId = "qryDVisitTracking"
                    }
                    RequestType.TIME_KEEPING_DIARY -> {
                        pSPName = "SP_Get_qryDTimeKeepingDiary_Mobile"
                        pFormId = "qryDTimeKeepingDiary"
                    }
                    RequestType.YOUR_SALES_RESULT -> {
                        pSPName = "SP_Get_qrySPersonalSalesResult_Mobile"
                        pFormId = "qrySSalesAnalyMnStaff"
                    }
                    RequestType.DATA_SUMMARY -> {
                        pSPName = "SP_Get_qryDYourDataSummary_Mobile"
                        pFormId = "qryDYourDataSummary"
                    }

                    else -> {

                    }
                }

            }

            return bodyRequest
        }

    }

}