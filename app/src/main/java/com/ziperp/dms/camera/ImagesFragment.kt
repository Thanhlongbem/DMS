package com.ziperp.dms.camera

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.Injection
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.camera.ImageModifyActivity.Companion.EXTRA_IMAGE_LIST
import com.ziperp.dms.camera.ImageModifyActivity.Companion.EXTRA_IS_VISIT_CUSTOMER
import com.ziperp.dms.camera.ImageModifyActivity.Companion.EXTRA_POSITION
import com.ziperp.dms.core.rest.*
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.visitcustomer.adapter.ImageAdapter
import com.ziperp.dms.showNotificationBanner
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.android.synthetic.main.fragment_visit_info.loading_progressbar

class ImagesFragment : BaseFragment() {
    lateinit var adapter: ImageAdapter
    private var isListMode = true
    var customerNo = ""
    private var isVisitCustomer = false
    var layoutManager = LinearLayoutManager(context)
    private val viewModel by lazy {
        getViewModel { Injection.provideImageViewModel() }
    }

    override fun setLayoutId(): Int = R.layout.fragment_image

    @SuppressLint("SetTextI18n")
    override fun initView() {
        adapter = ImageAdapter(arrayListOf(), isListMode)
        adapter.onImageClickListener = { position ->
            val intent = Intent(activity, ImageModifyActivity::class.java)
            intent.putExtra(EXTRA_IMAGE_LIST, adapter.data)
            intent.putExtra(EXTRA_POSITION, position)
            intent.putExtra(EXTRA_IS_VISIT_CUSTOMER, isVisitCustomer)
            startActivity(intent)
        }
        adapter.onDelete = { position ->
            val item = adapter.data[position]
            viewModel.deleteImage(item)
        }
        customerNo = arguments?.getString(KEY_NO) ?: ""
        isVisitCustomer = arguments?.getBoolean(IS_VISIT_CUSTOMER) ?: false


        viewModel.imageListData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { data ->
                        tv_title.text = "${data.size} ${R.string.str_images.getString()}"
                        if (data.isNotEmpty()) {
                            adapter.addData(data)
                            rv_images.layoutManager = layoutManager
                            rv_images.setHasFixedSize(true)
                            rv_images.adapter = adapter
                            rv_images.visibility = View.VISIBLE
                            tv_empty.visibility = View.GONE
                        } else {
                            rv_images.visibility = View.GONE
                            tv_empty.visibility = View.VISIBLE
                        }
                    }
                }
                Status.LOADING -> {
                    loading_progressbar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    loading_progressbar.visibility = View.GONE
                    rv_images.visibility = View.GONE
                    tv_empty.visibility = View.VISIBLE
                }
            }
        })

        viewModel.saveFileLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        if (it.isSuccess()) {
                            showNotificationBanner(NotificationType.SUCCESS, it.message())
                        } else {
                            showNotificationBanner(NotificationType.ERROR, it.message())
                        }
                    }
                    viewModel.getListImage(this.customerNo, this.isVisitCustomer)
                }
                Status.LOADING -> { }
                Status.ERROR -> {
                    showNotificationBanner(NotificationType.ERROR, it.data?.message() ?: "Something went wrong!")
                }
            }
        })

        viewModel.deleteLiveData.observe(this, Observer { response ->
            when (response.status) {
                Status.LOADING -> showLoading(true)
                Status.SUCCESS -> {
                    showLoading(false)

                    val cudData = response.data!!
                    when (cudData.status) {
                        CUD_SUCCESS -> {
                            showNotificationBanner(NotificationType.SUCCESS, cudData.message)
                        }
                        CUD_CONFLICT -> {
                            showNotificationBanner(NotificationType.ERROR, cudData.message)
                        }
                        CUD_OFFLINE -> {
                            showNotificationBanner(NotificationType.INFO, cudData.message)
                        }
                        CUD_ERROR -> {
                            showNotificationBanner(NotificationType.ERROR, cudData.message)
                        }
                    }
                    viewModel.getListImage(this.customerNo, isVisitCustomer)
                }
                Status.ERROR -> {
                    showLoading(false)
                    showNotificationBanner(
                        NotificationType.ERROR,
                        "Could not delete image right now!"
                    )
                }
            }
        })


        btn_switch.setOnClickListener {
            if (adapter.itemCount > 0) {
                isListMode = !isListMode
                layoutManager =
                    if (isListMode) LinearLayoutManager(context) else GridLayoutManager(context, 3)
                btn_switch.setImageResource(if (isListMode) R.drawable.ic_list_black else R.drawable.ic_grid_black)
                rv_images.layoutManager = layoutManager
                rv_images.adapter = adapter
                adapter.switchViewMode(isListMode)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (customerNo.isNotBlank()) {
            viewModel.getListImage(this.customerNo, isVisitCustomer)
        }
    }

    companion object {
        val TAG = ImagesFragment::class.java.canonicalName
        const val KEY_NO = "KEY_NO"
        const val IS_VISIT_CUSTOMER = "IS_VISIT_CUSTOMER"
        fun newInstance(keyNo: String, isVisitCustomer: Boolean): ImagesFragment {
            val fr = ImagesFragment()
            val bundle = Bundle()
            bundle.putString(KEY_NO, keyNo)
            bundle.putBoolean(IS_VISIT_CUSTOMER, isVisitCustomer)
            fr.arguments = bundle
            return fr
        }
    }
}