package com.ziperp.dms.main.customer.customernote.view

import androidx.lifecycle.Observer
import com.ziperp.dms.Injection
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseCUDActivity
import com.ziperp.dms.common.model.EditView
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.rest.Constants.EXTRA_CUSTOMER_NAME
import com.ziperp.dms.core.rest.Constants.ObjectType.ACCOUNT
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.customer.customernote.model.CUDNoteRequest
import com.ziperp.dms.main.customer.customernote.model.NoteInfoResponse
import com.ziperp.dms.main.customer.customernote.view.CustomerNoteActivity.Companion.EXTRA_OBJECT_ID
import com.ziperp.dms.main.customer.customernote.viewmodel.CustomerNoteViewModel.Companion.TYPE_CREATE
import com.ziperp.dms.main.customer.customernote.viewmodel.CustomerNoteViewModel.Companion.TYPE_UPDATE
import com.ziperp.dms.showNotificationBanner
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.activity_note_modify.*
import kotlinx.android.synthetic.main.item_detail.view.*

class NoteModifyActivity: BaseCUDActivity<NoteInfoResponse.NoteInfo>() {

    private val viewModel by lazy { getViewModel { Injection.provideCustomerNoteViewModel() } }

    override fun setLayoutId(): Int = R.layout.activity_note_modify

    override fun initView() {
        relate_object.tv_title.text = R.string.str_related_object.getString()

        viewModel.cudRequestStatus.observe(this, Observer { response ->
            when(response.status) {
                Status.LOADING -> showLoading(true)
                Status.SUCCESS -> {
                    showLoading(false)
                    setResult(200)
                    finish()
                    if (response.data?.data?.get(0)?.status == "OK") {
                        showNotificationBanner(NotificationType.SUCCESS, R.string.str_success.getString())
                    } else {
                        showNotificationBanner(NotificationType.ERROR, response.data?.data?.get(0)?.errMsg?: "Something went wrong!")
                    }
                }
                Status.ERROR -> {
                    showLoading(false)
                    showNotificationBanner(NotificationType.ERROR, response.data?.data?.get(0)?.errMsg?: "Something went wrong!")
                }
            }
        })
    }

    override fun createNewForm() {
        setToolbar(R.string.str_create_note.getString(), true)
        item = NoteInfoResponse.NoteInfo()
        item.noteId = ""
        item.objectId = intent.getStringExtra(EXTRA_OBJECT_ID)!!
        relate_object.tv_content.text = intent.getStringExtra(EXTRA_CUSTOMER_NAME)?: ""
    }

    override fun fillCurrentInfo() {
        setToolbar(R.string.str_note_modify.getString(), true)
        item = intent.getSerializableExtra(EXTRA_CUD_OBJECT) as NoteInfoResponse.NoteInfo
        note_title.setValue(DataConvertUtils.convertTitle(item.noteTitle))
        edt_content.setText(item.noteContent)

    }

    override fun setListMandatoryView(): ArrayList<EditView> = arrayListOf(note_title)

    override fun setListOption(): ArrayList<Pair<EditView, ItemControlForm>> = arrayListOf()

    override fun requestUpdate() {
        val request = CUDNoteRequest()
        request.apply {
            noteId = item.noteId
            objectId = item.objectId
            objectType = ACCOUNT.toString()
            noteTitle = note_title.content
            noteContent = edt_content.text.toString()
        }
        viewModel.CUDNote(if (mode == CREATE_MODE) TYPE_CREATE else TYPE_UPDATE, request)
    }

}