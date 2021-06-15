package com.ziperp.dms.core.form.combopopup.view

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.common.adapter.DialogPopupAdapter
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.ItemControlSelection
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.activity_combo_popup.*

class ComboPopupActivity : BaseActivity() {
    private var adapter = DialogPopupAdapter()
    override fun setLayoutId(): Int = R.layout.activity_combo_popup

    private val viewModel by lazy {
        getViewModel { Injection.providerComboPopupViewModel() }
    }
    lateinit var itemControl: ItemControlForm
    override fun initView() {

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        itemControl = intent.getSerializableExtra("itemControl") as ItemControlForm
        adapter.updateType(itemControl)
        initData(itemControl)
        viewModel.getComboData(itemControl)

        adapter.onItemClicked = { keyValue, i, isSelected ->
            LogUtils.d("Item clicked ${isSelected}")
            if (isSelected) {
                adapter.removeSelectedItem(i)
            } else {
                adapter.selectedItem(i)
                if (!adapter.isMultiple) {
                    backWithResult()
                }
            }
        }

        viewModel.comboLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    root_content.visibility = View.VISIBLE
                    it.data?.let { data ->
                        tv_empty.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE
                        adapter.addData(data)
                    }
                }
                Status.LOADING -> {
                    loading_progressbar.visibility = View.VISIBLE
                    root_content.visibility = View.GONE
                }
                Status.ERROR -> {
                    tv_empty.visibility = View.VISIBLE
                    loading_progressbar.visibility = View.GONE
                    root_content.visibility = View.VISIBLE
                }
            }

        })

        but_confirm.setOnClickListener { backWithResult() }
    }

    private fun backWithResult() {
        val result = ItemControlSelection(adapter.selectedData, itemControl)
        val intent = Intent()
        intent.putExtra("result", result)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun initData(itemControl: ItemControlForm) {
        adapter.lookUpType = itemControl.lookUpType
        adapter.selectedData.addAll(itemControl.controlValue)
    }


}