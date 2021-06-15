package com.ziperp.dms.main.customer.customerdetail.model


import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.ziperp.dms.camera.CustomerImage
import com.ziperp.dms.main.customer.customerroute.model.CustomerRoute
import com.ziperp.dms.main.visitcustomer.model.VisitCustomer
import java.io.Serializable
import java.util.*

data class CustomerDetailResponse(
    @SerializedName("Table")
    var data: List<CustomerDetail> = listOf()
)

@Entity(tableName = "customerDetail", indices = [Index(value = ["CstCd"], unique = true)])
data class CustomerDetail(
    @ColumnInfo(name = "CstCd")
    @SerializedName("CstCd")
    var cstCd: String = "",

    @ColumnInfo(name = "CstNm")
    @SerializedName("CstNm")
    var cstNm: String = "",

    @ColumnInfo(name = "CstNo")
    @SerializedName("CstNo")
    var cstNo: String = "",

    @ColumnInfo(name = "CstDiv")
    @SerializedName("CstDiv")
    var cstDiv: String = "",

    @ColumnInfo(name = "CstDivNm")
    @SerializedName("CstDivNm")
    var cstDivNm: String = "",

    @ColumnInfo(name = "CstUseYn")
    @SerializedName("CstUseYn")
    var cstUseYn: String = "",

    @ColumnInfo(name = "CstUseYnNm")
    @SerializedName("CstUseYnNm")
    var cstUseYnNm: String = "",

    @ColumnInfo(name = "btnActiveCst")
    @SerializedName("btnActiveCst")
    var btnActiveCst: String = "",

    @ColumnInfo(name = "btnLockCst")
    @SerializedName("btnLockCst")
    var btnLockCst: String = "",

    @ColumnInfo(name = "CstClss")
    @SerializedName("CstClss")
    var cstClss: String = "",

    @ColumnInfo(name = "CstClssNm")
    @SerializedName("CstClssNm")
    var cstClssNm: String = "",

    @ColumnInfo(name = "OfficePhone")
    @SerializedName("OfficePhone")
    var officePhone: String = "",

    @ColumnInfo(name = "OfficePhonePrivate")
    @SerializedName("OfficePhonePrivate")
    var officePhonePrivate: String = "",

    @ColumnInfo(name = "WorkTel")
    @SerializedName("WorkTel")
    var workTel: String = "",

    @ColumnInfo(name = "WorkTelPrivate")
    @SerializedName("WorkTelPrivate")
    var workTelPrivate: String = "",

    @ColumnInfo(name = "Email")
    @SerializedName("Email")
    var email: String = "",

    @ColumnInfo(name = "EmailPrivate")
    @SerializedName("EmailPrivate")
    var emailPrivate: String = "",

    @ColumnInfo(name = "PrivateMobileNoYn")
    @SerializedName("PrivateMobileNoYn")
    var privateMobileNoYn: Int = 0,

    @ColumnInfo(name = "ContactPerson")
    @SerializedName("ContactPerson")
    var contactPerson: String = "",

    @ColumnInfo(name = "ContactPhone")
    @SerializedName("ContactPhone")
    var contactPhone: String = "",

    @ColumnInfo(name = "OfficeFax")
    @SerializedName("OfficeFax")
    var officeFax: String = "",

    @ColumnInfo(name = "Website")
    @SerializedName("Website")
    var website: String = "",

    @ColumnInfo(name = "Job")
    @SerializedName("Job")
    var job: String = "",

    @ColumnInfo(name = "Representative")
    @SerializedName("Representative")
    var representative: String = "",

    @ColumnInfo(name = "CstBizLicNo")
    @SerializedName("CstBizLicNo")
    var cstBizLicNo: String = "",

    @ColumnInfo(name = "TaxCode")
    @SerializedName("TaxCode")
    var taxCode: String = "",

    @ColumnInfo(name = "RegMan")
    @SerializedName("RegMan")
    var regMan: String = "",

    @ColumnInfo(name = "RegManNm")
    @SerializedName("RegManNm")
    var regManNm: String = "",

    @ColumnInfo(name = "CstGrp1")
    @SerializedName("CstGrp1")
    var cstGrp1: String = "",

    @ColumnInfo(name = "CstGrp2")
    @SerializedName("CstGrp2")
    var cstGrp2: String = "",

    @ColumnInfo(name = "CstGrp3")
    @SerializedName("CstGrp3")
    var cstGrp3: String = "",

    @ColumnInfo(name = "CstGrp4")
    @SerializedName("CstGrp4")
    var cstGrp4: String = "",

    @ColumnInfo(name = "CstGrp1Nm")
    @SerializedName("CstGrp1Nm")
    var cstGrp1Nm: String = "",

    @ColumnInfo(name = "CstGrp2Nm")
    @SerializedName("CstGrp2Nm")
    var cstGrp2Nm: String = "",

    @ColumnInfo(name = "CstGrp3Nm")
    @SerializedName("CstGrp3Nm")
    var cstGrp3Nm: String = "",

    @ColumnInfo(name = "CstGrp4Nm")
    @SerializedName("CstGrp4Nm")
    var cstGrp4Nm: String = "",

    @ColumnInfo(name = "Gender")
    @SerializedName("Gender")
    var gender: String = "",

    @ColumnInfo(name = "GenderNm")
    @SerializedName("GenderNm")
    var genderNm: String = "",

    @ColumnInfo(name = "BirthdayDate")
    @SerializedName("BirthdayDate")
    var birthdayDate: String = "",

    @ColumnInfo(name = "NumFutureActivities")
    @SerializedName("NumFutureActivities")
    var numFutureActivities: Int = 0,

    @ColumnInfo(name = "NumDueActivities")
    @SerializedName("NumDueActivities")
    var numDueActivities: Int = 0,

    @ColumnInfo(name = "NumNotes")
    @SerializedName("NumNotes")
    var numNotes: Int = 0,

    @ColumnInfo(name = "NumFiles")
    @SerializedName("NumFiles")
    var numFiles: Int = 0,

    @ColumnInfo(name = "NumOpps")
    @SerializedName("NumOpps")
    var numOpps: Int = 0,

    @ColumnInfo(name = "NumRoutes")
    @SerializedName("NumRoutes")
    var numRoutes: Int = 0,

    @ColumnInfo(name = "Street")
    @SerializedName("Street")
    var street: String = "",

    @ColumnInfo(name = "DistrictCd")
    @SerializedName("DistrictCd")
    var districtCd: String = "",

    @ColumnInfo(name = "RegionCd")
    @SerializedName("RegionCd")
    var regionCd: String = "",

    @ColumnInfo(name = "CountryCd")
    @SerializedName("CountryCd")
    var countryCd: String = "",

    @ColumnInfo(name = "DistrictNm")
    @SerializedName("DistrictNm")
    var districtNm: String = "",

    @ColumnInfo(name = "RegionNm")
    @SerializedName("RegionNm")
    var regionNm: String = "",

    @ColumnInfo(name = "CountryNm")
    @SerializedName("CountryNm")
    var countryNm: String = "",

    @ColumnInfo(name = "AddrOnMap")
    @SerializedName("AddrOnMap")
    var addrOnMap: String = "",

    @ColumnInfo(name = "AddrLng")
    @SerializedName("AddrLng")
    var addrLng: String = "",

    @ColumnInfo(name = "AddrLat")
    @SerializedName("AddrLat")
    var addrLat: String = "",

    @ColumnInfo(name = "HiddenAddr")
    @SerializedName("HiddenAddr")
    var hiddenAddr: String = "",

    @ColumnInfo(name = "Remark")
    @SerializedName("Remark")
    var remark: String = "",

    @ColumnInfo(name = "isSynchonized")
    var isSynchonized: Boolean = false

): Serializable{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    lateinit var id: Integer  // Purpose for cache

    @Ignore
    var listImages =  listOf<CustomerImage>()

    @Ignore
    var listRoutes =  listOf<CustomerRoute>()

    @Ignore
    var listVisitCustomers =  listOf<VisitCustomer>()

    override fun equals(other: Any?): Boolean {
        (other as? CustomerDetail)?.let{
            return cstCd == it.cstCd
        } ?: kotlin.run { return false }
    }
    fun isNeedSynchronized(): Boolean{
        return !isSynchonized
    }


    fun isValidCustomerCode(): Boolean{
        return cstCd.isNotBlank() && !cstCd.contains("Cached_")
    }

}