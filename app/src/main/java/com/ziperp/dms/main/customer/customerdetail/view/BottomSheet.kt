package com.ziperp.dms.main.customer.customerdetail.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ziperp.dms.R
import kotlinx.android.synthetic.main.sheet_customer_detail.view.*

class BottomSheet: BottomSheetDialogFragment() {

    private var onClickListener: BottomSheetListener?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.sheet_customer_detail, container, false)
        v.apply {
            layout_sales_order.setOnClickListener { onClickListener?.onOptionClick(0) }
            layout_check_in.setOnClickListener { onClickListener?.onOptionClick(1) }
            layout_take_note.setOnClickListener { onClickListener?.onOptionClick(2) }
            layout_take_picture.setOnClickListener { onClickListener?.onOptionClick(3) }
            layout_report.setOnClickListener { onClickListener?.onOptionClick(4) }
            layout_delete.setOnClickListener { onClickListener?.onOptionClick(5) }
            btn_close.setOnClickListener { dismiss() }
        }
        return v
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try { onClickListener = context as BottomSheetListener? }
        catch (e: ClassCastException){ throw ClassCastException(context.toString()) }
    }

    interface BottomSheetListener{
        fun onOptionClick(position: Int)
    }

}