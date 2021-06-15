package com.ziperp.dms.main.customer.customerdetail.view

import com.google.android.gms.maps.model.LatLng
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import com.ziperp.dms.main.visitcustomer.view.LocationFragment
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.fragment_customer_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*
import kotlinx.android.synthetic.main.item_detail_header.view.*


class DetailFragment : BaseFragment() {

    var data: CustomerDetail? = null
    var mapFragment: LocationFragment? = null

    override fun setLayoutId(): Int = R.layout.fragment_customer_detail

    override fun initView() {
        data?.let {
            updateData(it)
        }
    }

    private fun initFields(data: CustomerDetail){
        tv_information.tv_header.text = R.string.str_general_info.getString()

        usage_status.tv_title.text = R.string.str_usage_status.getString()
        usage_status.tv_content.text = data.cstUseYnNm

        type.tv_title.text = R.string.str_customer_type.getString()
        type.tv_content.text = data.cstDivNm
        phone1.tv_title.text = R.string.str_phone_number1.getString()
        phone1.tv_content.text = data.officePhone

        phone2.tv_title.text = R.string.str_phone_number2.getString()
        phone2.tv_content.text = data.workTel
        fax.tv_title.text = R.string.str_fax.getString()
        fax.tv_content.text = data.officeFax
        email.tv_title.text = R.string.str_email.getString()
        email.tv_content.text = data.email
        gender.tv_title.text = R.string.str_gender.getString()
        gender.tv_content.text = data.genderNm
        birthday_establish.tv_title.text = R.string.str_birthday_establish.getString()
        birthday_establish.tv_content.text = data.birthdayDate.reformatDate()

        header_address.tv_header.text = R.string.str_address.getString()
        street.tv_title.text = R.string.str_street.getString()
        street.tv_content.text = data.street
        district.tv_title.text = R.string.str_district.getString()
        district.tv_content.text = data.districtNm
        province.tv_title.text = R.string.str_province.getString()
        province.tv_content.text = data.regionNm
        country.tv_title.text = R.string.str_country.getString()
        country.tv_content.text = data.countryNm

        tv_addition_info.tv_header.text = R.string.str_addition_info.getString()
        represent.tv_title.text = R.string.str_representation.getString()
        represent.tv_content.text = data.representative
        license.tv_title.text = R.string.str_license.getString()
        license.tv_content.text = data.cstBizLicNo
        tax.tv_title.text = R.string.str_tax.getString()
        tax.tv_content.text = data.taxCode
        owner.tv_title.text = R.string.str_owner.getString()
        owner.tv_content.text = data.regManNm
        group1.tv_title.text = String.format(R.string.str_customer_group.getString(), 1)
        group1.tv_content.text = data.cstGrp1Nm
        group2.tv_title.text = String.format(R.string.str_customer_group.getString(), 2)
        group2.tv_content.text = data.cstGrp2Nm
        group3.tv_title.text = String.format(R.string.str_customer_group.getString(), 3)
        group3.tv_content.text = data.cstGrp3Nm
        group4.tv_title.text = String.format(R.string.str_customer_group.getString(), 4)
        group4.tv_content.text = data.cstGrp4Nm

        header_description.tv_header.text = R.string.str_customer_description.getString()
        tv_description.text = data.remark
        setOnRegisterPhoneContextMenu(phone1.tv_content, phone2.tv_content)
        setOnRegisterMailContextMenu(email.tv_content)
    }

    fun updateData(data: CustomerDetail) {
        this.data = data
        initFields(data)

        val isFirstTime = if(mapFragment == null){
            mapFragment = LocationFragment.newInstance()
            mapFragment!!.isGetLocation = false
            true
        } else false

        mapFragment?.locationName = data.addrOnMap
        if (data.addrLat.isNotBlank() && data.addrLng.isNotBlank()) {
            mapFragment?.latLng = LatLng(data.addrLat.toDouble(), data.addrLng.toDouble())
            mapFragment!!.isDisableMap = false
        } else {
            mapFragment!!.isDisableMap = true
        }
        if (isFirstTime) {
            AppUtil.addFragmentToActivity(
                childFragmentManager,
                mapFragment!!,
                R.id.map_layout,
                false,
                ""
            )
        } else {
            mapFragment?.updateMap()
        }

    }

}