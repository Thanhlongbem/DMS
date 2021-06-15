package com.ziperp.dms.main.customer.customerdetail.view

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.ziperp.dms.Injection
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseCUDActivity
import com.ziperp.dms.common.model.EditView
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.KeyValue
import com.ziperp.dms.core.rest.*
import com.ziperp.dms.extensions.convertDate
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import com.ziperp.dms.main.visitcustomer.view.LocationFragment
import com.ziperp.dms.showNotificationBanner
import com.ziperp.dms.utils.AppUtil
import kotlinx.android.synthetic.main.activity_customer_modify.*
import kotlinx.android.synthetic.main.item_detail_header.view.*

class CustomerModifyActivity : BaseCUDActivity<CustomerDetail>(){

    private val viewModel by lazy { getViewModel { Injection.provideCustomerDetailViewModel() } }
    lateinit var mapFragment: LocationFragment

    override fun setLayoutId(): Int = R.layout.activity_customer_modify

    override fun initView() {

        tv_information.tv_header.text = R.string.str_general_info.getString()
        tv_addition_info.tv_header.text = R.string.str_addition_info.getString()
        header_address.tv_header.text = R.string.str_address.getString()
        header_description.tv_header.text = R.string.str_customer_description.getString()

        viewModel.cudRequestStatus2.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> showLoading(true)
                Status.SUCCESS -> {
                    showLoading(false)

                    val cudData = response.data!!
                    val customerDetail = cudData.data as CustomerDetail
                    when (cudData.status) {
                        CUD_SUCCESS -> {
                            showNotificationBanner(NotificationType.SUCCESS, cudData.message)
                            navigateToDetail(customerDetail.cstCd)
                        }
                        CUD_CONFLICT -> {
                            showNotificationBanner(NotificationType.ERROR, cudData.message)
                        }
                        CUD_OFFLINE -> {
                            showNotificationBanner(NotificationType.INFO, cudData.message)
                            navigateToDetail(customerDetail)
                        }
                        CUD_ERROR -> {
                            showNotificationBanner(NotificationType.ERROR, cudData.message)
                        }
                    }
                }
                Status.ERROR -> {
                    showLoading(false)
                    showNotificationBanner(
                        NotificationType.ERROR,
                        "Could not create customer right now!"
                    )
                }
            }
        })


        viewModel.deleteRequestStatus2.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> showLoading(true)
                Status.SUCCESS -> {
                    showLoading(false)
                    val cudData = response.data!!
                    when (cudData.status) {
                        CUD_SUCCESS -> {
                            showNotificationBanner(NotificationType.SUCCESS, cudData.message)
                            setResult(RESULT_FINISH)// Purpose back
                            finish()
                        }
                        CUD_CONFLICT -> {
                            showNotificationBanner(NotificationType.ERROR, cudData.message)
                        }
                        CUD_ERROR -> {
                            showNotificationBanner(NotificationType.ERROR, cudData.message)
                        }
                    }
                }
            }
        })

        birthday_establish.setShowDatePicker()

        btn_delete.setOnClickListener {
            AppUtil.showAlertDialog(this, R.string.str_question_delete.getString(), {
                viewModel.deleteCustomer(item)
            })
        }

    }

    private fun navigateToDetail(cstCd: String) {
        if (mode == CREATE_MODE){
            val intent = Intent(this, CustomerDetailActivity::class.java)
            intent.putExtra(Constants.EXTRA_CUSTOMER_CODE, cstCd)
            startActivity(intent)
        }else{
            val intent = Intent()
            intent.putExtra(Constants.EXTRA_CUSTOMER_CODE, cstCd)
            setResult(Activity.RESULT_OK, intent)
        }
        finish()
    }


    private fun navigateToDetail(customerDetail: CustomerDetail) {
        if (mode == CREATE_MODE){
            val intent = Intent(this, CustomerDetailActivity::class.java)
            intent.putExtra(Constants.EXTRA_CUSTOMER_DATA, customerDetail)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent()
            intent.putExtra(Constants.EXTRA_CUSTOMER_DATA, customerDetail)
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }


    override fun createNewForm() {
        setToolbar(R.string.str_new_customer.getString(), true)
        item = CustomerDetail()
        btn_delete.visibility = View.GONE
        mapFragment = LocationFragment.newInstance()
        AppUtil.addFragmentToActivity(
            supportFragmentManager,
            mapFragment,
            R.id.layout_map,
            false,
            ""
        )
    }

    override fun fillCurrentInfo() {
        item = intent.getSerializableExtra(EXTRA_CUD_OBJECT) as CustomerDetail
        item.apply {
            setToolbar(item.cstNm, true)

            name.setValue(cstNm)
            number.setValue(cstNo)
            type.setValue(cstDivNm)
            listOption[0].second.controlValue = arrayListOf(
                KeyValue(
                    valueCode = cstDiv,
                    valueName = cstDivNm
                )
            )

            phone.setValue(officePhone)
            phone2.setValue(workTel)
            fax.setValue(officeFax)
            email_address.setValue(email)

            represent.setValue(representative)
            license.setValue(cstBizLicNo)
            tax.setValue(taxCode)

            customer_gender.visibility = if (listOption[0].second.getFilterValue() == "0") View.VISIBLE else View.GONE
            customer_gender.setValue(genderNm)
            listOption[1].second.controlValue = arrayListOf(
                KeyValue(
                    valueCode = gender,
                    valueName = genderNm
                )
            )
            birthday_establish.setValue(
                if (birthdayDate.trim().isNotEmpty()) birthdayDate.reformatDate() else ""
            )

            mapFragment = LocationFragment.newInstance()
            mapFragment.locationName = item.addrOnMap

            if (item.addrLat.isNotBlank() && item.addrLng.isNotBlank()) {
                mapFragment.latLng = LatLng(item.addrLat.toDouble(), item.addrLng.toDouble())
            }
            AppUtil.addFragmentToActivity(
                supportFragmentManager,
                mapFragment,
                R.id.layout_map,
                false,
                ""
            )

            street_address.setValue(street)
            district.setValue(districtNm)
            listOption[2].second.controlValue = arrayListOf(
                KeyValue(
                    valueCode = districtCd,
                    valueName = districtNm
                )
            )
            province.setValue(regionNm)
            listOption[3].second.controlValue = arrayListOf(
                KeyValue(
                    valueCode = regionCd,
                    valueName = regionNm
                )
            )
            country.setValue(countryNm)
            listOption[4].second.controlValue = arrayListOf(
                KeyValue(
                    valueCode = countryCd,
                    valueName = countryNm
                )
            )


            group1.setValue(cstGrp1Nm)
            listOption[5].second.controlValue = arrayListOf(
                KeyValue(
                    valueCode = cstGrp1,
                    valueName = cstGrp1Nm
                )
            )
            group2.setValue(cstGrp2Nm)
            listOption[6].second.controlValue = arrayListOf(
                KeyValue(
                    valueCode = cstGrp2,
                    valueName = cstGrp2Nm
                )
            )
            group3.setValue(cstGrp3Nm)
            listOption[7].second.controlValue = arrayListOf(
                KeyValue(
                    valueCode = cstGrp3,
                    valueName = cstGrp3Nm
                )
            )
            group4.setValue(cstGrp4Nm)
            listOption[8].second.controlValue = arrayListOf(
                KeyValue(
                    valueCode = cstGrp4,
                    valueName = cstGrp4Nm
                )
            )
            customer_owner.setValue(regManNm)
            listOption[9].second.controlValue = arrayListOf(
                KeyValue(
                    valueCode = regMan,
                    valueName = regManNm
                )
            )


            tv_description.setText(if (remark.isEmpty()) R.string.str_not_available.getString() else remark)
        }
    }

    override fun setListMandatoryView(): ArrayList<EditView> = arrayListOf(name, type, phone, street_address, province)

    override fun setListOption(): ArrayList<Pair<EditView, ItemControlForm>> = arrayListOf(
        type to ItemControlForm( //0
            formId = Form.FORM_ID_CUSTOMER,
            controlId = "cmbAccType",
            controlName = "Customer Type",
            lookUpCode = "0018",
            lookUpType = Form.LookUpType.COMBO,
            findType = Form.FindType.CODE
        ),
        customer_gender to ItemControlForm(//1
            formId = Form.FORM_ID_CUSTOMER,
            controlId = "cmbGender",
            controlName = "Gender",
            lookUpCode = "0074",
            lookUpType = Form.LookUpType.COMBO,
            findType = Form.FindType.CODE
        ),
        district to ItemControlForm(//2
            formId = Form.FORM_ID_CUSTOMER,
            controlId = "txtDistrict",
            controlName = "Province",
            lookUpCode = "0266",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.CODE
        ),
        province to ItemControlForm(//3
            formId = Form.FORM_ID_CUSTOMER,
            controlId = "txtRegionCd",
            controlName = "City",
            lookUpCode = "0054",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.CODE
        ),
        country to ItemControlForm(//4
            formId = Form.FORM_ID_CUSTOMER,
            controlId = "txtCountryCd",
            controlName = "Country",
            lookUpCode = "0053",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.CODE
        ),
        group1 to ItemControlForm(//5
            formId = Form.FORM_ID_CUSTOMER,
            controlId = "txtCstGrp1",
            controlName = "Customer Group 1",
            lookUpCode = "0058",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.CODE
        ),
        group2 to ItemControlForm(//6
            formId = Form.FORM_ID_CUSTOMER,
            controlId = "txtCstGrp2",
            controlName = "Customer Group 2",
            lookUpCode = "0059",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.CODE
        ),
        group3 to ItemControlForm(//7
            formId = Form.FORM_ID_CUSTOMER,
            controlId = "txtCstGrp3",
            controlName = "Customer Group 3",
            lookUpCode = "0060",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.CODE
        ),
        group4 to ItemControlForm(//8
            formId = Form.FORM_ID_CUSTOMER,
            controlId = "txtCstGrp4",
            controlName = "Customer Group 4",
            lookUpCode = "0061",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.CODE
        ),
        customer_owner to ItemControlForm(//9
            formId = Form.FORM_ID_CUSTOMER,
            controlId = "txtRegMan",
            controlName = "Customer Owner",
            lookUpCode = "STAF",
            lookUpType = Form.LookUpType.DIALOG,
            findType = Form.FindType.NAME
        )
    )

    override fun requestUpdate() {

        var cstCdVal = if (mode == CREATE_MODE){
            if(item.cstCd.isBlank()){
                "Cached_"+System.currentTimeMillis()
            }else{
                item.cstCd
            }
        }else{
            item.cstCd
        }

        item.apply {
            cstCd = cstCdVal
            cstNm = name.content
            cstNo = number.content
            cstDiv = listOption[0].second.getFilterValue()
            cstDivNm = listOption[0].second.getDisplayText()

            cstUseYn = item.cstUseYn
            cstUseYnNm = item.cstUseYnNm

            officePhone = phone.content
            officeFax = fax.content
            workTel = phone2.content
            email = email_address.content

            representative = represent.content
            cstBizLicNo = license.content

            taxCode = tax.content
            gender = listOption[1].second.getFilterValue()
            genderNm = listOption[1].second.getDisplayText()

            birthdayDate = birthday_establish.content.convertDate()

            regMan = listOption[9].second.getFilterValue()
            regManNm = listOption[9].second.getDisplayText()

            cstGrp1 = listOption[5].second.getFilterValue()
            cstGrp1Nm = listOption[5].second.getDisplayText()

            cstGrp2 = listOption[6].second.getFilterValue()
            cstGrp2Nm = listOption[6].second.getDisplayText()

            cstGrp3 = listOption[7].second.getFilterValue()
            cstGrp3Nm = listOption[7].second.getDisplayText()

            cstGrp4 = listOption[8].second.getFilterValue()
            cstGrp4Nm = listOption[8].second.getDisplayText()

            numNotes = item.numNotes
            numFiles = item.numFiles
            numRoutes = item.numRoutes

            street = street_address.content

            districtCd = listOption[2].second.getFilterValue()
            districtNm = listOption[2].second.getDisplayText()

            regionCd = listOption[3].second.getFilterValue()
            regionNm = listOption[3].second.getDisplayText()

            countryCd = listOption[4].second.getFilterValue()
            countryNm = listOption[4].second.getDisplayText()


            mapFragment.latLng?.let {
                addrLat = it.latitude.toString()
                addrLng = it.longitude.toString()
            }

            addrOnMap = mapFragment.locationName
            remark = tv_description.text.toString()
        }


        viewModel.saveCustomer(item)
    }

    override fun onUpdateItemControl(index: Int) {
        when(index){
            0 -> { //type
                if(listOption[0].second.getFilterValue() == "0") { // 0 is Individual
                    customer_gender.visibility = View.VISIBLE
                    listMandatoryView.add(customer_gender)
                } else {
                    customer_gender.visibility = View.GONE
                    listMandatoryView.remove(customer_gender)
                }

            }
        }
    }

}