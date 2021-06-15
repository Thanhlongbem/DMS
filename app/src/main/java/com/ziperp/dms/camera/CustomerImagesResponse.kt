package com.ziperp.dms.camera

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CustomerImagesResponse(
	@SerializedName("Table")
	val table: List<CustomerImage> = arrayListOf()
)

@Entity(tableName = "customerImage", indices = arrayOf(Index(value = ["fileNm"], unique = true)))
data class CustomerImage(

	@ColumnInfo(name = "keyNo")
	@SerializedName("KeyNo")
	var keyNo: String = "", // cstCd or visit customer No

	@ColumnInfo(name = "fileNmOld")
	@SerializedName("FileNmOld")
	var fileNmOld: String = "",

	@ColumnInfo(name = "fileNote")
	@SerializedName("FileNote")
	var fileNote: String = "",

	@ColumnInfo(name = "fileLength")
	@SerializedName("FileLength")
	var fileLength: String = "",

	@ColumnInfo(name = "fileURL")
	@SerializedName("FileURL")
	var fileURL: String = "",

	@ColumnInfo(name = "fileType")
	@SerializedName("FileType")
	var fileType: String = "",

	@ColumnInfo(name = "fileNm")
	@SerializedName("FileNm")
	var fileNm: String = "",

	@ColumnInfo(name = "fileURLOld")
	@SerializedName("FileURLOld")
	var fileURLOld: String = "",

	@ColumnInfo(name = "openFile")
	@SerializedName("OpenFile")
	var openFile: String = "",

	@ColumnInfo(name = "fileTypeNm")
	@SerializedName("FileTypeNm")
	var fileTypeNm: String = "",

	@ColumnInfo(name = "attachedDate")
	@SerializedName("AttachedDate")
	var attachedDate: String = "",

	@ColumnInfo(name = "download")
	@SerializedName("Download")
	var download: String = "",

	@ColumnInfo(name = "seq")
	@SerializedName("Seq")
	var seq: Int = 0,

	@ColumnInfo(name = "isVisitCustomer")
	var isVisitCustomer: Boolean = false,

	@ColumnInfo(name = "isSynchonized")
	var isSynchonized: Boolean = false,

	@ColumnInfo(name = "localPath")
	var localPath: String = ""


): Serializable {

	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	lateinit var id: Integer  // Purpose for cache

	fun isValidCustomerCode(): Boolean{
		return keyNo.isNotBlank() && !keyNo.contains("Cached_")
	}

}


