package com.ziperp.dms.main.customer.customernote.view

import android.annotation.SuppressLint
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.Injection
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.base.BaseCUDActivity.Companion.CREATE_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_MODE
import com.ziperp.dms.core.rest.Constants.EXTRA_CUSTOMER_CODE
import com.ziperp.dms.core.rest.Constants.EXTRA_CUSTOMER_NAME
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.customer.customernote.model.CUDNoteRequest
import com.ziperp.dms.main.customer.customernote.viewmodel.CustomerNoteViewModel.Companion.TYPE_DELETE
import com.ziperp.dms.showNotificationBanner
import kotlinx.android.synthetic.main.activity_customer_note.*

class CustomerNoteActivity: BaseActivity() {

    lateinit var adapter: CustomerNoteAdapter
    private val viewModel by lazy { getViewModel { Injection.provideCustomerNoteViewModel() } }
    var customerCode = ""
    var customerType = 0
    var customerName = ""

    override fun setLayoutId(): Int = R.layout.activity_customer_note

    @SuppressLint("SetTextI18n")
    override fun initView() {
        setToolbar(R.string.str_notes.getString(), true)

        customerCode = intent.getStringExtra(EXTRA_CUSTOMER_CODE)
        customerName = intent.getStringExtra(EXTRA_CUSTOMER_NAME)

        tv_title.text = String.format(R.string.str_note_of.getString(), customerName)

        adapter = CustomerNoteAdapter()
        val layoutManager = LinearLayoutManager(this)
        rv_notes.layoutManager = layoutManager
        rv_notes.adapter = adapter
        rv_notes.setHasFixedSize(true)

        adapter.onClickListener = { position ->
            val intent = Intent(this, NoteDetailActivity::class.java)
            val note = adapter.data[position]
            intent.putExtra(EXTRA_NOTE_ID, note.noteId)
            startActivity(intent)
        }

        adapter.deleteFunction = {
            tv_numbs_note.text = "${adapter.data.size - 1} ${R.string.str_notes.getString()}"
            viewModel.CUDNote(TYPE_DELETE, CUDNoteRequest(noteId = adapter.data[it].noteId, objectId = adapter.data[it].objectId))
        }

        viewModel.listNoteLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let {
                        adapter.data.clear()
                        adapter.addData(it.data)
                        tv_numbs_note.text = it.data.size.toString() + " " + R.string.str_notes.getString()
                    }
                }
                Status.LOADING -> {
                    loading_progressbar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    loading_progressbar.visibility = View.GONE
                }
            }
        })

        viewModel.cudRequestStatus.observe(this, Observer { response ->
            when(response.status) {
                Status.LOADING -> showLoading(true)
                Status.SUCCESS -> {
                    showLoading(false)
                    if (response.data?.data?.get(0)?.status == "OK") {
                        showNotificationBanner(NotificationType.SUCCESS, response.data.message())
                    } else {
                        showNotificationBanner(NotificationType.ERROR, response.data?.data?.get(0)?.errMsg?: "Something went wrong!")
                    }
                    viewModel.getCustomerNotes(customerCode)
                }
                Status.ERROR -> {
                    showLoading(false)
                    showNotificationBanner(NotificationType.ERROR, response.data?.data?.get(0)?.errMsg?: "Something went wrong!")
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.getCustomerNotes(customerCode)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        menu?.findItem(R.id.action_add)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                val intent = Intent(this, NoteModifyActivity::class.java)
                intent.putExtra(EXTRA_CUD_MODE, CREATE_MODE)
                intent.putExtra(EXTRA_OBJECT_ID, customerCode)
                intent.putExtra(EXTRA_CUSTOMER_NAME, customerName)
                startActivityForResult(intent, 100)
            }
        }
        return true
    }

    companion object {
        const val EXTRA_NOTE_ID = "EXTRA_NOTE_ID"
        const val EXTRA_OBJECT_ID = "EXTRA_OBJECT_ID"
    }
}