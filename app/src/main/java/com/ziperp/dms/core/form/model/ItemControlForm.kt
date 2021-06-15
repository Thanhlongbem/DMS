package com.ziperp.dms.core.form.model

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.Serializable

data class ItemControlForm(
    var formId: String = "",
    var controlId: String = "",
    var controlName: String = "",
    var controlValue: ArrayList<KeyValue> = arrayListOf(),
    var lookUpCode: String = "",
    var lookUpType: Int = Form.LookUpType.NONE,
    var findType: String = Form.FindType.NONE,
    var param: Param = Param()
) : Serializable {

    data class Param(
        var item1: String = "",
        var item2: String = "",
        var item3: String = "",
        var item4: String = "",
        var item5: String = "",
        var item6: String = "",
        var item7: String = "",
        var item8: String = "",
        var item9: String = "",
        var item10: String = "",
        var cmbCd: String = "",
        var parentCd: String = "",
        var dlgCd: String = "",
        var joinCd: String = ""
    ):Serializable

    companion object{
        const val TYPE_COMMON = 1
        const val TYPE_ITEM_COUNT = 2
        const val TYPE_ROUTE = 3
    }

    fun getControlFormId(): String{//controlId_formId
        return controlId + "_" + formId
    }


    fun getType(): Int{
        return when (lookUpCode) {
            "ITNO", "ITNM" -> {
                TYPE_ITEM_COUNT
            }
            "ROUT" -> {
                TYPE_ROUTE
            }
            else -> {
                TYPE_COMMON
            }
        }
    }

    fun getDisplayText(): String {
        if (controlValue.isNotEmpty()) {
            if (getType() == TYPE_COMMON) {
                return controlValue.map { it.valueName }.reduce { s1, s2 -> "$s1&$s2" }
            } else if (getType() == TYPE_ITEM_COUNT){
                val jsonObject = JsonParser().parse(controlValue[0].jsonData) as? JsonObject
                if(jsonObject != null) {
                    return jsonObject.get("ItemNo").asString + " - " +  jsonObject.get("ItemNm").asString
                }
            } else if (getType() == TYPE_ROUTE){
                 val result = controlValue.map { JsonParser().parse(it.jsonData) as? JsonObject }
                     .filterNotNull()
                if(result.isNotEmpty()) {
                    return result.map { it.get("RouteNo").asString }.reduce { s1, s2 -> "$s1&$s2" }
                }
            }
        }
        return ""
    }

    fun getFilterValue(): String {
        return if (controlValue.isNotEmpty()) {
            controlValue.map { it.valueCode }.reduce { s1, s2 -> "$s1&$s2" }
        }else ""
    }

    fun isUsedCached(): Boolean{
        return lookUpCode == "0054"  || lookUpCode =="ROUT"
    }

}

object FormDataFactory {

    fun get(formId: String): ArrayList<ItemControlForm> {
        val result = ArrayList<ItemControlForm>()
        when (formId) {
            Form.FORM_ID_VISIT_CUSTOMER -> return Form.getVisitCustomerForm()
            Form.FORM_ID_CUSTOMER -> return Form.getCustomerForm()
            Form.FORM_ID_ITEM_MASTER -> return Form.getItemMasterForm()
            Form.FORM_ID_SALE_ORDER -> return Form.getSaleOrderForm()
            Form.FORM_ID_SALES_PRICE -> return Form.getSalesPriceForm()
            Form.FORM_ID_TRACKING_STAFF -> return Form.getStaffTrackingForm()
            Form.FORM_ID_TRACKING_VISIT_CUSTOMER -> return Form.getVisitCustomerTrackingForm()
            Form.FORM_ID_TRACKING_PICTURE -> return Form.getPictureTrackingForm()
            Form.FORM_ID_NEW_NOT_ORDER -> return Form.getNewNotOrderForm()
            Form.FORM_ID_SALES_RESULT -> return Form.getSalesResultForm()
            Form.FORM_ID_DAILY_PERFORMANCE -> return Form.getDailyPerformanceForm()
            Form.FORM_ID_REPORT_SUMMARY -> return Form.getReportSummationForm()
            Form.FORM_ID_NEW_SALES_POINT -> return Form.getNewSalesPointForm()
            Form.FORM_ID_VISIT_RESULT -> return Form.getVisitResultForm()
            Form.FORM_ID_YOUR_SALES_RESULT -> return Form.getYourSalesResultForm()
        }
        return result
    }

}

object Form {
    const val FORM_ID_CUSTOMER = "qryCAccount"
    const val FORM_ID_VISIT_CUSTOMER = "qryDVisitCst"
    const val FORM_ID_STOCK_COUNT = "frmDVisitCst"
    const val FORM_ID_SALE_ORDER = "qrySOrder"
    const val FORM_ID_SALE_ORDER_MODIFY = "frmSOrder"
    const val FORM_ID_ITEM_MASTER = "qryYItemMaster"
    const val FORM_ID_SALES_PRICE = "qrySSalesPriceList"
    const val FORM_ID_TRACKING_STAFF = "qryDStaffTracking"
    const val FORM_ID_TRACKING_VISIT_CUSTOMER = "qryDVisitTracking"
    const val FORM_ID_TRACKING_PICTURE = "qryDVisitImgTracking"
    const val FORM_ID_NEW_NOT_ORDER = "qryCNewAccount"
    const val FORM_ID_SALES_RESULT = "qrySSalesAnalyMnStaff"
    const val FORM_ID_DAILY_PERFORMANCE = "qryDOrderAndWorkEffect"
    const val FORM_ID_REPORT_SUMMARY = "qryCCstSummaryRpt"
    const val FORM_ID_NEW_SALES_POINT = "qryCNewCstSummary"
    const val FORM_ID_VISIT_RESULT = "FORM_ID_VISIT_RESULT"
    const val FORM_ID_TIMEKEEPING_DIARY = "qryDTimeKeepingDiary"
    const val FORM_ID_YOUR_SALES_RESULT = "FORM_ID_YOUR_SALES_RESULT"
    const val FORM_ID_USER_INFO = "frmYUserProfile"

    object FindType {
        const val NONE = ""
        const val NAME = "N"
        const val CODE = "C"
    }

    object LookUpType {
        const val NONE = 0
        const val DIALOG = 1
        const val DIALOG_MUL = 2
        const val COMBO = 3
        const val COMBO_MUL = 4
        const val DATE = 5
        const val MONTH = 6
    }

    fun getCustomerForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll(
            listOf(
                ItemControlForm(
                    formId = FORM_ID_CUSTOMER,
                    controlId = "txtRegMan",
                    controlName = "Customer Owner",
                    lookUpCode = "STAF",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_CUSTOMER,
                    controlId = "cmbAccType",
                    controlName = "Customer Type",
                    lookUpCode = "0018",
                    lookUpType = LookUpType.COMBO,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_CUSTOMER,
                    controlId = "txtCstGrp1",
                    controlName = "Customer Group 1",
                    lookUpCode = "0058",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_CUSTOMER,
                    controlId = "txtCstGrp2",
                    controlName = "Customer Group 2",
                    lookUpCode = "0059",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_CUSTOMER,
                    controlId = "txtCstGrp3",
                    controlName = "Customer Group 3",
                    lookUpCode = "0060",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_CUSTOMER,
                    controlId = "txtCstGrp4",
                    controlName = "Customer Group 4",
                    lookUpCode = "0061",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.CODE
                )
            )
        )
        return result
    }

    fun getVisitCustomerForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.add(
            ItemControlForm(
                formId = FORM_ID_VISIT_CUSTOMER,
                controlId = "txtRoute",
                controlName = "Route",
                lookUpCode = "ROUT",
                lookUpType = LookUpType.DIALOG_MUL,
                findType = FindType.NAME
            )
        )
        result.add(
            ItemControlForm(
                formId = FORM_ID_VISIT_CUSTOMER,
                controlId = "cmbVisitSts",
                controlName = "Visit Status",
                lookUpCode = "0382",
                lookUpType = LookUpType.COMBO,
                findType = FindType.CODE
            )
        )
        result.add(
            ItemControlForm(
                formId = FORM_ID_VISIT_CUSTOMER,
                controlId = "cmbSortOrderBy",
                controlName = "Sorted By",
                lookUpCode = "0384",
                lookUpType = LookUpType.COMBO,
                findType = FindType.CODE
            )
        )

        result.add(
            ItemControlForm(
                formId = FORM_ID_VISIT_CUSTOMER,
                controlId = "cmbVisitDay",
                controlName = "Visit Day",
                lookUpCode = "0383",
                lookUpType = LookUpType.COMBO_MUL,
                findType = FindType.CODE
            )
        )

        result.add(
            ItemControlForm(
                formId = FORM_ID_VISIT_CUSTOMER,
                controlId = "txtCstGrp1",
                controlName = "Customer Group 1",
                lookUpCode = "0058",
                lookUpType = LookUpType.DIALOG,
                findType = FindType.CODE
            )
        )

        result.add(
            ItemControlForm(
                formId = FORM_ID_VISIT_CUSTOMER,
                controlId = "txtCstGrp2",
                controlName = "Customer Group 2",
                lookUpCode = "0059",
                lookUpType = LookUpType.DIALOG,
                findType = FindType.CODE
            )
        )

        result.add(
            ItemControlForm(
                formId = FORM_ID_VISIT_CUSTOMER,
                controlId = "txtCstGrp3",
                controlName = "Customer Group 3",
                lookUpCode = "0060",
                lookUpType = LookUpType.DIALOG,
                findType = FindType.CODE
            )
        )

        result.add(
            ItemControlForm(
                formId = FORM_ID_VISIT_CUSTOMER,
                controlId = "txtCstGrp4",
                controlName = "Customer Group 4",
                lookUpCode = "0061",
                lookUpType = LookUpType.DIALOG,
                findType = FindType.CODE
            )
        )

        return result
    }

    fun getItemMasterForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll(
            listOf(
                ItemControlForm(
                    formId = FORM_ID_ITEM_MASTER,
                    controlId = "txtItemCate",
                    controlName = "Item Category",
                    lookUpCode = "0066",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_ITEM_MASTER,
                    controlId = "txtItemGrp1",
                    controlName = "Item Group",
                    lookUpCode = "0062",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_ITEM_MASTER,
                    controlId = "txtItemBrand",
                    controlName = "Item Brand",
                    lookUpCode = "0067",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_ITEM_MASTER,
                    controlId = "txtItemModel",
                    controlName = "Item Model",
                    lookUpCode = "MODL",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_ITEM_MASTER,
                    controlId = "txtVendor",
                    controlName = "Vendor",
                    lookUpCode = "VENM",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_ITEM_MASTER,
                    controlId = "txtMaker",
                    controlName = "Maker",
                    lookUpCode = "MAKM",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                )
            )
        )
        return result
    }

    fun getSaleOrderForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll(
            listOf(
                ItemControlForm(
                    formId = FORM_ID_SALE_ORDER,
                    controlId = "dateFrom",// Optional
                    controlName = "From Date",
                    lookUpType = LookUpType.DATE
                ),
                ItemControlForm(
                    formId = FORM_ID_SALE_ORDER,
                    controlId = "dateTo",// Optional
                    controlName = "To Date",
                    lookUpType = LookUpType.DATE
                ),
                ItemControlForm(
                    formId = FORM_ID_SALE_ORDER,
                    controlId = "txtSalesman",
                    controlName = "Salesman",
                    lookUpCode = "STAF",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_SALE_ORDER,
                    controlId = "txtSalesDept",
                    controlName = "Sales Department",
                    lookUpCode = "DEPT",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_SALE_ORDER,
                    controlId = "txtCustomer",
                    controlName = "Customer",
                    lookUpCode = "CSTM",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_SALE_ORDER,
                    controlId = "cmbMasterLocCd",
                    controlName = "Business Unit",
                    lookUpCode = "0001",
                    lookUpType = LookUpType.COMBO_MUL,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_SALE_ORDER,
                    controlId = "cmbOrderSts",
                    controlName = "Order Status",
                    lookUpCode = "0106",
                    lookUpType = LookUpType.COMBO,
                    findType = FindType.CODE
                )
            )
        )
        return result
    }

    fun getSalesPriceForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll(
            listOf(
                ItemControlForm(
                    formId = FORM_ID_SALES_PRICE,
                    controlId = "dateFrom",// Optional
                    controlName = "Date",
                    lookUpType = LookUpType.DATE
                ),
                ItemControlForm(
                    formId = FORM_ID_SALES_PRICE,
                    controlId = "cmbBizUnit",
                    controlName = "Business Unit",
                    lookUpCode = "0001",
                    lookUpType = LookUpType.COMBO_MUL,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_SALES_PRICE,
                    controlId = "cmbActiveSts",
                    controlName = "Active Status",
                    lookUpCode = "0045",
                    lookUpType = LookUpType.COMBO,
                    findType = FindType.CODE
                )
            )
        )
        return result
    }

    fun getStaffTrackingForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll( listOf(
            ItemControlForm(
                formId = FORM_ID_TRACKING_STAFF,
                controlId = "cmbTimeTrack",
                controlName = "Time Get Data",
                lookUpCode = "DM02",
                lookUpType = LookUpType.COMBO,
                findType = FindType.CODE
            ),
            ItemControlForm(
                formId = FORM_ID_TRACKING_STAFF,
                controlId = "txtStaff",
                controlName = "Staff",
                lookUpCode = "STAF",
                lookUpType = LookUpType.DIALOG,
                findType = FindType.NAME
            ),
            ItemControlForm(
                formId = FORM_ID_TRACKING_STAFF,
                controlId = "txtDept",
                controlName = "Department",
                lookUpCode = "DEPT",
                lookUpType = LookUpType.DIALOG,
                findType = FindType.NAME
            ),
            ItemControlForm(
                formId = FORM_ID_TRACKING_STAFF,
                controlId = "cmbBizUnit",
                controlName = "Business Unit",
                lookUpCode = "0001",
                lookUpType = LookUpType.COMBO_MUL,
                findType = FindType.CODE
            ),
            ItemControlForm(
                formId = FORM_ID_TRACKING_STAFF,
                controlId = "comLocationSts",
                controlName = "Location Status",
                lookUpCode = "DM01",
                lookUpType = LookUpType.COMBO,
                findType = FindType.CODE
            )
        ))
        return result
    }

    fun getVisitCustomerTrackingForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll(listOf(
            ItemControlForm(
                formId = FORM_ID_TRACKING_VISIT_CUSTOMER,
                controlId = "cmbTimeTrack",
                controlName = "Time Get Data",
                lookUpCode = "DM02",
                lookUpType = LookUpType.COMBO,
                findType = FindType.CODE
            ),
            ItemControlForm(
                formId = FORM_ID_TRACKING_VISIT_CUSTOMER,
                controlId = "cmbRouteSts",
                controlName = "Route Status",
                lookUpCode = "DM03",
                lookUpType = LookUpType.COMBO,
                findType = FindType.CODE
            ),
            ItemControlForm(
                formId = FORM_ID_TRACKING_VISIT_CUSTOMER,
                controlId = "cmbValidSts",
                controlName = "Visit Result",
                lookUpCode = "DM04",
                lookUpType = LookUpType.COMBO,
                findType = FindType.CODE
            ),
            ItemControlForm(
                formId = FORM_ID_TRACKING_VISIT_CUSTOMER,
                controlId = "cmbBizUnit",
                controlName = "Business Unit",
                lookUpCode = "0001",
                lookUpType = LookUpType.COMBO_MUL,
                findType = FindType.CODE
            ),
            ItemControlForm(
                formId = FORM_ID_TRACKING_VISIT_CUSTOMER,
                controlId = "txtDept",
                controlName = "Department",
                lookUpCode = "DEPT",
                lookUpType = LookUpType.DIALOG,
                findType = FindType.NAME
            ),
            ItemControlForm(
                formId = FORM_ID_TRACKING_VISIT_CUSTOMER,
                controlId = "cmbImageSts",
                controlName = "Image Status",
                lookUpCode = "DM05",
                lookUpType = LookUpType.COMBO,
                findType = FindType.CODE
            ),
            ItemControlForm(
                formId = FORM_ID_TRACKING_VISIT_CUSTOMER,
                controlId = "cmbOrderSts",
                controlName = "Order Status",
                lookUpCode = "DM06",
                lookUpType = LookUpType.COMBO,
                findType = FindType.CODE
            )
        ))
        return result
    }

    fun getPictureTrackingForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll(
            listOf(
                ItemControlForm(
                    formId = FORM_ID_TRACKING_PICTURE,
                    controlId = "cmbTimeTrack",
                    controlName = "Time Get Data\n",
                    lookUpCode = "DM02",
                lookUpType = LookUpType.COMBO,
                findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_TRACKING_PICTURE,
                    controlId = "txtDept",
                    controlName = "Department",
                    lookUpCode = "DEPT",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_TRACKING_PICTURE,
                    controlId = "cmbBizUnit",
                    controlName = "Business Unit",
                    lookUpCode = "0001",
                    lookUpType = LookUpType.COMBO_MUL,
                    findType = FindType.CODE
                )
            )
        )
        return result
    }

    fun getNewNotOrderForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll(
            listOf(
                ItemControlForm(
                    formId = FORM_ID_NEW_NOT_ORDER,
                    controlId = "cmbTimeTrack",
                    controlName = "Time Get Data",
                    lookUpCode = "DM02",
                lookUpType = LookUpType.COMBO,
                findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_NEW_NOT_ORDER,
                    controlId = "txtDept",
                    controlName = "Department",
                    lookUpCode = "DEPT",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_NEW_NOT_ORDER,
                    controlId = "cmbBizUnit",
                    controlName = "Business Unit",
                    lookUpCode = "0001",
                    lookUpType = LookUpType.COMBO_MUL,
                    findType = FindType.CODE
                )
            )
        )
        return result
    }

    fun getSalesResultForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll(
            listOf(
                ItemControlForm(
                    formId = FORM_ID_SALES_RESULT,
                    controlId = "cmbAnalysisBasic",
                    controlName = "Analysis Basic",
                    lookUpCode = "0123",
                    lookUpType = LookUpType.COMBO,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_SALES_RESULT,
                    controlId = "cmbBizUnit",
                    controlName = " Business Unit",
                    lookUpCode = "0001",
                    lookUpType = LookUpType.COMBO_MUL,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_SALES_RESULT,
                    controlId = "txtDept",
                    controlName = "Department",
                    lookUpCode = "DEPT",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_SALES_RESULT,
                    controlId = "txtItemCate",
                    controlName = "Item Category",
                    lookUpCode = "0066",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_SALES_RESULT,
                    controlId = "txtItemModel",
                    controlName = "Item Model",
                    lookUpCode = "MODL",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_SALES_RESULT,
                    controlId = "txtItemBrand",
                    controlName = "Item Brand",
                    lookUpCode = "0067",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_SALES_RESULT,
                    controlId = "txtItemNm",
                    controlName = "Item Name",
                    lookUpCode = "ITNM",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.CODE
                )
            )
        )

        return result
    }

    fun getDailyPerformanceForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll(
            listOf(
                ItemControlForm(
                    formId = FORM_ID_DAILY_PERFORMANCE,
                    controlId = "txtStaff",
                    controlName = "Staff",
                    lookUpCode = "STAF",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_DAILY_PERFORMANCE,
                    controlId = "txtDept",
                    controlName = "Department",
                    lookUpCode = "DEPT",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_DAILY_PERFORMANCE,
                    controlId = "cmbBizUnit",
                    controlName = "Business Unit",
                    lookUpCode = "0001",
                    lookUpType = LookUpType.COMBO_MUL,
                    findType = FindType.CODE
                )
            )
        )
        return result
    }

    fun getReportSummationForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll(
            listOf(
                ItemControlForm(
                    formId = FORM_ID_REPORT_SUMMARY,
                    controlId = "txtCustomer",
                    controlName = "Customer",
                    lookUpCode = "CSTM",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_REPORT_SUMMARY,
                    controlId = "dateFrom",// Optional
                    controlName = "From Date",
                    lookUpType = LookUpType.DATE
                ),
                ItemControlForm(
                    formId = FORM_ID_REPORT_SUMMARY,
                    controlId = "dateTo",// Optional
                    controlName = "To Date",
                    lookUpType = LookUpType.DATE
                )
            )
        )
        return result
    }

    fun getNewSalesPointForm(): java.util.ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll(
            listOf(
                ItemControlForm(
                    formId = FORM_ID_NEW_SALES_POINT,
                    controlId = "txtStaff",
                    controlName = "Staff",
                    lookUpCode = "STAF",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_NEW_SALES_POINT,
                    controlId = "txtDept",
                    controlName = "Department",
                    lookUpCode = "DEPT",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_NEW_SALES_POINT,
                    controlId = "cmbBizUnit",
                    controlName = "Business Unit",
                    lookUpCode = "0001",
                    lookUpType = LookUpType.COMBO_MUL,
                    findType = FindType.CODE
                )
            )
        )

        return result
    }

    fun getVisitResultForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll(
            listOf(
                ItemControlForm(
                    formId = FORM_ID_TRACKING_VISIT_CUSTOMER,
                    controlId = "cmbTimeTrack",
                    controlName = "Time Get Data",
                    lookUpCode = "DM02",
                    lookUpType = LookUpType.COMBO,
                    findType = FindType.CODE
                )
            )
        )

        return result
    }

    fun getYourSalesResultForm(): ArrayList<ItemControlForm> {
        val result = arrayListOf<ItemControlForm>()
        result.addAll(
            listOf(
                ItemControlForm(
                    formId = FORM_ID_SALES_RESULT,
                    controlId = "cmbAnalysisBasic",
                    controlName = "Analysis Basic",
                    lookUpCode = "0123",
                    lookUpType = LookUpType.COMBO,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_SALES_RESULT,
                    controlId = "txtCustomer",
                    controlName = "Customer",
                    lookUpCode = "CSTM",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_SALES_RESULT,
                    controlId = "txtItemCate",
                    controlName = "Item Category",
                    lookUpCode = "0066",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_SALES_RESULT,
                    controlId = "txtItemModel",
                    controlName = "Item Model",
                    lookUpCode = "MODL",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                ),
                ItemControlForm(
                    formId = FORM_ID_SALES_RESULT,
                    controlId = "txtItemBrand",
                    controlName = "Item brand",
                    lookUpCode = "0067",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.CODE
                ),
                ItemControlForm(
                    formId = FORM_ID_SALES_RESULT,
                    controlId = "txtItemNm",
                    controlName = "Item Name",
                    lookUpCode = "ITNM",
                    lookUpType = LookUpType.DIALOG,
                    findType = FindType.NAME
                )
            )
        )

        return result
    }
}
