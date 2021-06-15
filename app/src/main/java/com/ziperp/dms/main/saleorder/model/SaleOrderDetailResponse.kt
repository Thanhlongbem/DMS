package com.ziperp.dms.main.saleorder.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SaleOrderDetailResponse(

	@SerializedName("Table")
	var commonInfo: List<SaleOrderCommonInfo> = arrayListOf(),

	@SerializedName("Table2")
	var table2: List<Any> = arrayListOf(),

	@SerializedName("Table1")
	var orderItems: List<SaleOrderItem> = arrayListOf()
) : Serializable {
    data class SaleOrderCommonInfo(
		@SerializedName("MasterLocCd")
		val masterLocCd: String = "",
		@SerializedName("MasterLocNm")
		val masterLocNm: String = "",
		@SerializedName("OrderDate")
		val orderDate: String = "",
		@SerializedName("OrderNo")
		val orderNo: String = "",
		@SerializedName("OrderMngtNo")
		val orderMngtNo: String = "",
		@SerializedName("CstCd")
		val cstCd: String = "",
		@SerializedName("CstNm")
		val cstNm: String = "",
		@SerializedName("CstCdDataLink")
		val cstCdDataLink: String = "",
		@SerializedName("CstNo")
		val cstNo: String = "",
		@SerializedName("CstAttrCd")
		val cstAttrCd: String = "",
		@SerializedName("CstAttr")
		val cstAttr: String? = null,
		@SerializedName("CstPhone")
		val cstPhone: String = "",
		@SerializedName("ProjectNm")
		val projectNm: String = "",
		@SerializedName("ProjectCd")
		val projectCd: String = "",
		@SerializedName("ProjectCdDataLink")
		val projectCdDataLink: String = "",
		@SerializedName("Remark")
		val remark: String = "",
		@SerializedName("Confirmer")
		val confirmer: String = "",
		@SerializedName("Confirm")
		var confirm: Int = 0,
		@SerializedName("ConfirmDate")
		val confirmDate: String = "",
		@SerializedName("btnCfmSO")
		val btnCfmSO: String = "",
		@SerializedName("chkApplyPromotionYn")
		var chkApplyPromotionYn: Int = 0,
		@SerializedName("btnApplyPromotion")
		val btnApplyPromotion: String = "",
		@SerializedName("OrderStt")
		val orderStt: String = "",
		@SerializedName("StaffId")
		val staffId: String = "",
		@SerializedName("StaffNm")
		val staffNm: String = "",
		@SerializedName("DeptNm")
		val deptNm: String = "",
		@SerializedName("DeptCd")
		val deptCd: String = "",
		@SerializedName("SChannels")
		val sChannels: String = "",
		@SerializedName("PaymentTerm")
		val paymentTerm: String = "",
		@SerializedName("CurrencyCd")
		val currencyCd: String = "",
		@SerializedName("ExRate")
		val exRate: Double = 0.0,
		@SerializedName("PrepaidAmt")
		val prepaidAmt: Double = 0.0,
		@SerializedName("PrepaidBcAmt")
		val prepaidBcAmt: Double = 0.0,
		@SerializedName("PrepaidRate")
		val prepaidRate: Double = 0.0,
		@SerializedName("ShipMethod")
		val shipMethod: String = "",
		@SerializedName("ContactName")
		val contactName: String = "",
		@SerializedName("ContactPhone")
		val contactPhone: String = "",
		@SerializedName("DelvDate")
		val delvDate: String = "",
		@SerializedName("DelvTime")
		val delvTime: String = "",
		@SerializedName("DelvDateTime")
		val delvDateTime: String = "",
		@SerializedName("DelvAddress")
		val delvAddress: String = "",
		@SerializedName("DelvAddrLat")
		val delvAddrLat: String = "",
		@SerializedName("DelvAddrLng")
		val delvAddrLng: String = "",
		@SerializedName("QuotaNo")
		val quotaNo: String = "",
		@SerializedName("QuotaMngtNo")
		val quotaMngtNo: String = "",
		@SerializedName("QuotaMngtNoDataLink")
		val quotaMngtNoDataLink: String = "",
		@SerializedName("QuotationVer")
		val quotationVer: String = "",
		@SerializedName("Creator")
		val creator: String = "",
		@SerializedName("CreateDate")
		val createDate: String = "",
		@SerializedName("DocNo")
		val docNo: String = "",
		@SerializedName("SrcType")
		val srcType: String = "",
		@SerializedName("SourceNm")
		val sourceNm: String = "",
		@SerializedName("SrcMgntNo")
		val srcMgntNo: String = "",
		@SerializedName("SrcMgntNoDataLink")
		val srcMgntNoDataLink: String = "",
		@SerializedName("PoNo")
		val poNo: String = "",
		@SerializedName("PoNm")
		val poNm: String = "",
		@SerializedName("WhType")
		val whType: String = "",
		@SerializedName("SalesChannelName")
		val salesChannelName: String = "",
		@SerializedName("TotalAmount")
		val totalAmount: Double = 0.0,
		@SerializedName("SalesProcessStsNm")
		val salesProcessStsNm: String = "",
		@SerializedName("SalesProcessSts")
		val salesProcessSts: Int = 0,
		@SerializedName("ShipMethodNm")
		val shipMethodNm: String = ""
	): Serializable


}

data class SaleOrderItem(
	@SerializedName("ItemNo")
	val itemNo: String = "",
	@SerializedName("ItemNm")
	val itemNm: String = "",
	@SerializedName("CstItemNo")
	val cstItemNo: String = "",
	@SerializedName("CstItemNm")
	val cstItemNm: String = "",
	@SerializedName("ItemSpec")
	val itemSpec: String = "",
	@SerializedName("UoMNm")
	var uoMNm: String = "",
	@SerializedName("AvailStk")
	val availStk: Int = 0,
	@SerializedName("UnitPrice")
	val unitPrice: Double = 0.0,
	@SerializedName("OrderQty")
	val orderQty: Double = 0.0,
	@SerializedName("ExtendedPrice")
	val extendedPrice: Double = 0.0,
	@SerializedName("DiscType")
	val discType: String = "",
	@SerializedName("Discount")
	val discount: Double = 0.0,
	@SerializedName("DiscountAmt")
	val discountAmt: Double = 0.0,
	@SerializedName("SubTotal")
	val subTotal: Double = 0.0,
	@SerializedName("VatType")
	val vatType: String = "",
	@SerializedName("VatRate")
	val vatRate: Double = 0.0,
	@SerializedName("VatAmount")
	val vatAmount: Double = 0.0,
	@SerializedName("TotalAmount")
	val totalAmount: Double = 0.0,
	@SerializedName("PrepaidAmt")
	val prepaidAmt: Double = 0.0,
	@SerializedName("PrepaidBcAmt")
	val prepaidBcAmt: Double = 0.0,
	@SerializedName("PrepaidRate")
	val prepaidRate: Double = 0.0,
	@SerializedName("DelvDate")
	val delvDate: String = "",
	@SerializedName("DelvTimeDate")
	val delvTimeDate: String = "",
	@SerializedName("DelvAdd")
	val delvAdd: String = "",
	@SerializedName("DistrCenter")
	val distrCenter: String = "",
	@SerializedName("Remark")
	val remark: String = "",
	@SerializedName("UoMCd")
	var uoMCd: String = "",
	@SerializedName("WHUnitNm")
	val wHUnitNm: String = "",
	@SerializedName("WHUnitCd")
	val wHUnitCd: String = "",
	@SerializedName("ConvStkQty")
	var convStkQty: Double = 0.0,
	@SerializedName("ConvQty1")
	var convQty1: Double = 0.0,
	@SerializedName("ConvQty2")
	var convQty2: Double = 0.0,
	@SerializedName("StkQty")
	val stkQty: Double = 0.0,
	@SerializedName("PLUoMCd")
	val pLUoMCd: String = "",
	@SerializedName("PLUnitPrice")
	val pLUnitPrice: Double = 0.0,
	@SerializedName("PLQty")
	val pLQty: Double = 0.0,
	@SerializedName("PurchaseCost")
	val purchaseCost: Double = 0.0,
	@SerializedName("TotalCost")
	val totalCost: Double = 0.0,
	@SerializedName("ListPrice")
	val listPrice: Double = 0.0,
	@SerializedName("StdPrice")
	val stdPrice: Double = 0.0,
	@SerializedName("LimitPrice")
	val limitPrice: Double = 0.0,
	@SerializedName("PriceListCd")
	val priceListCd: String = "",
	@SerializedName("DiscountCd")
	val discountCd: String = "",
	@SerializedName("PromoCd")
	val promoCd: String = "",
	@SerializedName("PriceChk")
	val priceChk: String = "",
	@SerializedName("PriceChkNm")
	val priceChkNm: String = "",
	@SerializedName("ItemType")
	val itemType: Int = 0,
	@SerializedName("ItemTypeNm")
	val itemTypeNm: String = "",
	@SerializedName("QuotaNo")
	val quotaNo: String = "",
	@SerializedName("QuotaMngtNo")
	val quotaMngtNo: String = "",
	@SerializedName("QuotaLineNo")
	val quotaLineNo: String = "",
	@SerializedName("QuotationVer")
	val quotationVer: String = "",
	@SerializedName("DistrCenterCd")
	val distrCenterCd: String = "",
	@SerializedName("WhNm")
	var whNm: String = "",
	@SerializedName("WhCd")
	var whCd: String = "",
	@SerializedName("VatTypeCd")
	val vatTypeCd: String = "",
	@SerializedName("ItemCd")
	val itemCd: String = "",
	@SerializedName("ParentItem")
	val parentItem: String = "",
	@SerializedName("Serl")
	val serl: String = "",
	@SerializedName("SellVATinPriceYn")
	val sellVATinPriceYn: Int = 0,
	@SerializedName("DiscTypeNm")
	val discTypeNm: String = ""
): Serializable

data class StockItemUnit(
	@SerializedName("UoMCd")
	val uoMCd: String = "",
	@SerializedName("UoMNm")
	val uoMNm: String = "",
	@SerializedName("ConvStkQty")
	val convStkQty: Double = 0.0,
	@SerializedName("ConvQty1")
	val convQty1: Double = 0.0,
	@SerializedName("ConvQty2")
	val convQty2: Double = 0.0,
	@SerializedName("StockUoMNm")
	val stockUoMNm: String = "",
	@SerializedName("PurPackYn")
	val purPackYn: Int = 0,
	@SerializedName("SalesPackYn")
	val salesPackYn: Int = 0,
	@SerializedName("StockUoMCd")
	val stockUoMCd: String = ""
)

