package com.ziperp.dms.core.form.dialogpopup.view

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.widget.textChanges
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.common.adapter.DialogPopupAdapter
import com.ziperp.dms.common.paging.OnLoadMoreListener
import com.ziperp.dms.common.paging.RecyclerViewLoadMoreScroll
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.ItemControlSelection
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_dialog_form.*
import kotlinx.android.synthetic.main.search_box.*
import java.util.concurrent.TimeUnit


class DialogPopupActivity : BaseActivity() {
    private lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private var adapter = DialogPopupAdapter()
    override fun setLayoutId(): Int = R.layout.activity_dialog_form

    private val viewModel by lazy {
        getViewModel { Injection.providerDialogPopupViewModel() }
    }

    override fun initView() {
        setToolbar("Dialog Select", false)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                LogUtils.d("Load more " + viewModel.pagingParam.currentPage + " " +  viewModel.pagingParam.totalPage )
                if (viewModel.pagingParam.hasNext()) {
                    viewModel.getDialogData(true)
                }
            }
        })
        recyclerView.addOnScrollListener(scrollListener)

        search_box.textChanges()
            .skip(1)
            .map { it.toString() }
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                LogUtils.d("Search String ${it.isNotBlank()}.. " + it)
                adapter.removeAll()
                viewModel.pagingParam.clear()
                if (it.isNotBlank()) {
                    viewModel.lookUpValue = it
                } else {
                    viewModel.lookUpValue = ""
                }
                viewModel.getDialogData()
            }, {
                LogUtils.e(it.toString())
            })

        val itemControl = intent.getSerializableExtra("itemControl") as ItemControlForm?
        itemControl?.let { control ->
            initData(control)
            viewModel.itemControl = control
            viewModel.getDialogData()
        }

        adapter.onItemClicked = { keyValue, i, isSelected ->
            LogUtils.d("Item clicked ${isSelected}")
            if (isSelected) {
                adapter.removeSelectedItem(i)
            } else {
                adapter.selectedItem(i)
            }
            if (!adapter.isMultiple) {
                backWithResult()
            }
        }

        viewModel.dialogLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { data ->
                        tv_empty.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE
                        adapter.addData(data)
                    }
//                    val size = if(viewModel.pagingParam.totalPage == 1) adapter.data else
//                    setToolbar("${viewModel.pagingParam.totalItems} " +
//                            "${(itemControl?.controlName ?: " Item")}s" , true)

                }
                Status.LOADING -> {
                    loading_progressbar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    scrollListener.setLoaded()
                    loading_progressbar.visibility = View.GONE
                }
            }

        })

    }

    private fun backWithResult() {
        val result = ItemControlSelection(adapter.selectedData, viewModel.itemControl)
        val intent = Intent()
        intent.putExtra("result", result)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun initData(itemControl: ItemControlForm) {
        setToolbar(itemControl.controlName, true)
        when (itemControl.lookUpType) {
            Form.LookUpType.DIALOG -> {
                search_box.visibility = View.VISIBLE
            }
            Form.LookUpType.DIALOG_MUL -> {
                search_box.visibility = View.VISIBLE
                toolbar_choose.visibility = View.VISIBLE
            }
            Form.LookUpType.COMBO -> {
                search_box.visibility = View.GONE
            }
            Form.LookUpType.COMBO_MUL -> {
                search_box.visibility = View.GONE
                toolbar_choose.visibility = View.VISIBLE
            }
        }
        if (toolbar_choose.visibility == View.VISIBLE){
            toolbar_choose.setOnClickListener{backWithResult()}
        }
        adapter.updateType(itemControl)
        adapter.lookUpType = itemControl.lookUpType
        adapter.selectedData.addAll(itemControl.controlValue)

    }


}