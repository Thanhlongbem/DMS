package com.ziperp.dms.main.saleorder.modify

import android.content.Intent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.NotificationType
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseCUDActivity.Companion.CREATE_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.EXTRA_CUD_OBJECT
import com.ziperp.dms.base.BaseCUDActivity.Companion.UPDATE_MODE
import com.ziperp.dms.base.BaseFragment
import com.ziperp.dms.base.BottomBarItem
import com.ziperp.dms.core.rest.ResponseData
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.toast
import com.ziperp.dms.main.saleorder.adapter.OrderItemAdapter
import com.ziperp.dms.main.saleorder.model.CUDLinesRequest
import com.ziperp.dms.main.saleorder.model.CUDSalesOrderResponse
import com.ziperp.dms.main.saleorder.model.SaleOrderItem
import com.ziperp.dms.main.saleorder.viewmodel.SaleOrderViewModel
import com.ziperp.dms.showNotificationBanner
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_items.*

class ItemFragment : BaseFragment() {

    lateinit var adapter: OrderItemAdapter
    lateinit var observer: Observer<ResponseData<CUDSalesOrderResponse>>

    val saleActivity by lazy { activity as SalesOrderModifyActivity }
    val sharedVM: SaleOrderViewModel by activityViewModels()

    var items: List<SaleOrderItem> = emptyList()
    var orderNo: String = ""

    override fun setLayoutId(): Int = R.layout.fragment_items

    override fun initView() {
        initRecyclerView()
        initBottomBar()

        sharedVM.cudLinesStatus.observe(this, Observer { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    if (response.data?.data?.get(0)?.status == "OK") {
                        showNotificationBanner(NotificationType.SUCCESS, R.string.str_success.getString())
                    } else {
                        showNotificationBanner(NotificationType.ERROR, response.data?.data?.get(0)?.errMsg?: "Something went wrong!")
                    }
                    sharedVM.getSaleOrderInfo(orderNo)
                }
                Status.LOADING -> {

                }
                Status.ERROR -> showNotificationBanner(NotificationType.ERROR, response.data?.data?.get(0)?.errMsg?: "Something went wrong!")
            }
        })

    }

    private fun initRecyclerView() {
        adapter = OrderItemAdapter()
        adapter.addData(items)
        val layoutManager = LinearLayoutManager(context)
        rv_items.layoutManager = layoutManager
        rv_items.adapter = adapter
        rv_items.setHasFixedSize(true)

        adapter.onDelete = { position ->
            "Deleting".toast(requireContext())
            val item = adapter.getItem(position)
            val request = CUDLinesRequest(
                orderNo = orderNo,
                itemCd = item.itemCd,
                orderLineNo = item.serl
            )
            sharedVM.cudSalesOrderLines("D", request)
        }

        adapter.onClickListener = {position ->
            val item = adapter.data[position]
            when(item.priceChk) {
                "A" -> {
                    val intent = Intent(activity, AccessoryModifyActivity::class.java)
                    intent.putExtra(EXTRA_CUD_MODE, UPDATE_MODE)
                    intent.putExtra(AccessoryModifyActivity.EXTRA_ORDER_NO, orderNo)
                    intent.putExtra(EXTRA_CUD_OBJECT, item)
                    intent.putExtra(OrderItemModifyActivity.EXTRA_SALE_ORDER_INFO, saleActivity.getCUDInfo())
                    activity?.startActivityForResult(intent, 101)
                }
                "G" -> {
                    val intent = Intent(activity, OrderItemModifyActivity::class.java)
                    intent.putExtra(EXTRA_CUD_MODE, UPDATE_MODE)
                    intent.putExtra(OrderItemModifyActivity.EXTRA_ORDER_NO, orderNo)
                    intent.putExtra(EXTRA_CUD_OBJECT, item)
                    intent.putExtra(OrderItemModifyActivity.EXTRA_SALE_ORDER_INFO, saleActivity.getCUDInfo())
                    activity?.startActivityForResult(intent, 101)
                }
            }
        }
    }

    private fun initBottomBar() {
        bottom_bar.items = arrayListOf(
            BottomBarItem(
                R.string.str_add_item_sales.getString(),
                R.drawable.ic_new_item_sale,
                R.color.color_visit_item_1
            ),
            BottomBarItem(
                R.string.str_add_accessory.getString(),
                R.drawable.ic_accessory,
                R.color.color_tag_opening
            )
        )

        bottom_bar.onBottomItemClickListener = { position ->
            if (saleActivity.createNewSaleOrderIfNeed()) {
                if (orderNo.isEmpty()) {
                    observer = Observer {
                        when (it.status) {
                            Status.LOADING -> loading_progressbar.visibility = View.VISIBLE
                            Status.SUCCESS -> {
                                val orderMngtNo = it.data?.status?.get(0)?.orderMngtNo ?: ""
                                if(orderMngtNo.isNotBlank()){
                                    saleActivity.setToolbar(orderMngtNo, true)
                                }
                                loading_progressbar.visibility = View.GONE
                                sharedVM.cudSaleOrderLiveData.removeObserver(observer)
                                orderNo = it.data?.status?.get(0)?.orderNo ?: ""
                                LogUtils.d("Fragment orderNo $orderNo")
                                navigateToScreen(position)
                            }
                            Status.ERROR -> {
                                loading_progressbar.visibility = View.GONE
                                "Something wrong happened!".toast(
                                    requireContext()
                                )
                            }
                        }
                    }
                    sharedVM.cudSaleOrderLiveData.observe(this, observer)
                } else {
                    navigateToScreen(position)
                }
            } else {
                "Please complete common info firstly".toast(requireContext())
            }
        }
    }

    private fun navigateToScreen(position: Int) {
        val saleOrderInfo = saleActivity.getCUDInfo()
        if (orderNo.isNotBlank()) {
            when (position) {
                0 -> {
                    val intent = Intent(activity, OrderItemModifyActivity::class.java)
                    intent.putExtra(EXTRA_CUD_MODE, CREATE_MODE)
                    intent.putExtra(OrderItemModifyActivity.EXTRA_ORDER_NO, orderNo)
                    intent.putExtra(OrderItemModifyActivity.EXTRA_SALE_ORDER_INFO, saleOrderInfo)
                    activity?.startActivityForResult(intent, 101)
                }
                1 -> {
                    val intent = Intent(activity, AccessoryModifyActivity::class.java)
                    intent.putExtra(EXTRA_CUD_MODE, CREATE_MODE)
                    intent.putExtra(AccessoryModifyActivity.EXTRA_ORDER_NO, orderNo)
                    intent.putExtra(OrderItemModifyActivity.EXTRA_SALE_ORDER_INFO, saleOrderInfo)
                    activity?.startActivityForResult(intent, 101)
                }
            }
        }
    }

    fun updateItems(orderItems: List<SaleOrderItem>) {
        adapter.data.clear()
        adapter.addData(orderItems)
    }
}