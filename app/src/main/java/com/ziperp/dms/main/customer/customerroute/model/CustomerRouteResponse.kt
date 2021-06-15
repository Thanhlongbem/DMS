package com.ziperp.dms.main.customer.customerroute.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.ziperp.dms.main.timekeeping.model.TimeKeeping
import java.io.Serializable

data class CustomerRouteResponse(
    @SerializedName("Table")
    val data: List<CustomerRoute> = listOf()
)

@Entity(tableName = "customerRoute", indices = [Index(value = ["routeId"], unique = true)])
data class CustomerRoute(
    @ColumnInfo(name = "routeId")
    @SerializedName("RouteId")
    var routeId: String = "",

    @ColumnInfo(name = "cstCd")
    @SerializedName("CstCd")
    var cstCd: String = "",

    @ColumnInfo(name = "routeNo")
    @SerializedName("RouteNo")
    var routeNo: String = "",

    @ColumnInfo(name = "routeNm")
    @SerializedName("RouteNm")
    var routeNm: String = "",

    @ColumnInfo(name = "startDate")
    @SerializedName("StartDate")
    var startDate: String = "",

    @ColumnInfo(name = "endDate")
    @SerializedName("EndDate")
    var endDate: String = "",

    @ColumnInfo(name = "visitDay")
    @SerializedName("VisitDay")
    var visitDay: String = "",

    @ColumnInfo(name = "visitDayNm")
    @SerializedName("VisitDayNm")
    var visitDayNm: String = "",

    @ColumnInfo(name = "routeStaffNm")
    @SerializedName("RouteStaffNm")
    var routeStaffNm: String = "",

    @ColumnInfo(name = "routeStaff")
    @SerializedName("RouteStaff")
    var routeStaff: String = "",

    @ColumnInfo(name = "routeDeptNm")
    @SerializedName("RouteDeptNm")
    var routeDeptNm: String = "",

    @ColumnInfo(name = "routeDept")
    @SerializedName("RouteDept")
    var routeDept: String = "",

    @ColumnInfo(name = "RouteSts")
    @SerializedName("RouteSts")
    var routeSts: String = "",

    @ColumnInfo(name = "routeStsNm")
    @SerializedName("RouteStsNm")
    var routeStsNm: String = "",

    @ColumnInfo(name = "cfmSts")
    @SerializedName("CfmSts")
    var cfmSts: Int = 0,

    @ColumnInfo(name = "cfmDate")
    @SerializedName("CfmDate")
    var cfmDate: String = "",

    @ColumnInfo(name = "confirmer")
    @SerializedName("Confirmer")
    var confirmer: String = "",

    @ColumnInfo(name = "regManNm")
    @SerializedName("RegManNm")
    var regManNm: String = "",

    @ColumnInfo(name = "createDate")
    @SerializedName("CreateDate")
    var createDate: String = "",

    @ColumnInfo(name = "remark")
    @SerializedName("Remark")
    var remark: String = "",

    @ColumnInfo(name = "isSynchonized")
    var isSynchonized: Boolean = false


): Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    lateinit var id: Integer  // Purpose for cache

    override fun equals(other: Any?): Boolean {
        return (other as? CustomerRoute)?.routeId == routeId
    }

    fun isValidCustomerCode(): Boolean{
        return cstCd.isNotBlank() && !cstCd.contains("Cached_")
    }
}