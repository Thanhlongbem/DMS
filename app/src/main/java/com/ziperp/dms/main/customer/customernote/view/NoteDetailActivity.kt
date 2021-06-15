package com.ziperp.dms.main.customer.customernote.view

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.ziperp.dms.Injection
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_OBJECT
import com.ziperp.dms.base.BaseCUDActivity.Companion.UPDATE_MODE
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.extensions.reformatDate
import com.ziperp.dms.main.customer.customernote.model.CUDNoteRequest
import com.ziperp.dms.main.customer.customernote.model.NoteInfoResponse
import com.ziperp.dms.main.customer.customernote.viewmodel.CustomerNoteViewModel.Companion.TYPE_DELETE
import com.ziperp.dms.showNotificationBanner
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.DataConvertUtils
import kotlinx.android.synthetic.main.activity_note_detail.*

class NoteDetailActivity: BaseActivity() {

    lateinit var noteInfo: NoteInfoResponse.NoteInfo
    private val viewModel by lazy { getViewModel { Injection.provideCustomerNoteViewModel() } }

    override fun setLayoutId(): Int = R.layout.activity_note_detail

    override fun initView() {
        viewModel.noteInfoLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    setToolbar(R.string.str_note_detail.getString(), true)
                    it.data?.let {
                        noteInfo = it.data[0]
                        updateInfo(noteInfo)
                    }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                }
            }
        })

        viewModel.cudRequestStatus.observe(this, Observer { response ->
            when(response.status) {
                Status.LOADING -> loading_progressbar.visibility = View.VISIBLE
                Status.SUCCESS -> {
                    setResult(200)
                    finish()
                    if (response.data?.data?.get(0)?.status == "OK") {
                        showNotificationBanner(NotificationType.SUCCESS, R.string.str_success.getString())
                    } else {
                        showNotificationBanner(NotificationType.ERROR, response.data?.data?.get(0)?.errMsg?: "Something went wrong!")
                    }
                }
                Status.ERROR -> showNotificationBanner(NotificationType.ERROR, response.data?.data?.get(0)?.errMsg?: "Something went wrong!")
            }
        })

        btn_delete.setOnClickListener {
            AppUtil.showAlertDialog(this, R.string.str_question_delete.getString(), {
                viewModel.CUDNote(TYPE_DELETE, CUDNoteRequest(noteId = noteInfo.noteId, objectId = noteInfo.objectId)) })
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getNoteInfo(intent.getStringExtra(CustomerNoteActivity.EXTRA_NOTE_ID)!!)
    }

    private fun updateInfo(note: NoteInfoResponse.NoteInfo) {
        tv_note_title.text = DataConvertUtils.convertTitle(note.noteTitle)
        tv_modifier.text = note.regMan
        tv_modify_time.text = note.regDate.reformatDate()
        tv_content.text = note.noteContent
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_edit)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                val intent = Intent(this, NoteModifyActivity::class.java)
                intent.putExtra(EXTRA_CUD_MODE, UPDATE_MODE)
                intent.putExtra(EXTRA_CUD_OBJECT, noteInfo)
                startActivity(intent)
            }
        }
        return true
    }
}