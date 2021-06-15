package com.ziperp.dms.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.common.adapter.DialogOptionsAdapter
import kotlinx.android.synthetic.main.dialog_options.*


class OptionsDialog(context: Context, themeResId: Int): Dialog(context, themeResId), View.OnClickListener {
    private lateinit var adapter: DialogOptionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(true)
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }
        setContentView(R.layout.dialog_options)
        setupView()
    }

    private fun setupView(){
        motion.setOnClickListener(this)
    }

    var action: (() -> Unit)? = null


    fun showDialog(dialogType: Int, title: String, data: List<String>, currentData: String, action: (() -> Unit)){
        show()
        adapter = DialogOptionsAdapter(data, currentData)
        if(title.isNotEmpty()){
            tvDialogTitle.visibility = View.VISIBLE
            tvDialogTitle.text = title
        }else{
            tvDialogTitle.visibility = View.GONE
            tvDialogTitle.text = ""
        }

        rvOptions?.layoutManager = LinearLayoutManager(context)

        rvOptions.adapter = adapter
        adapter.onItemDialogOptionClick = { string, position ->
            callBack?.onOptionClick(string, dialogType, position)
            dismiss()
        }
        this.action = action
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.motion -> {
                dismiss()
            }
        }
    }

    interface OnOptionClick{
        fun onOptionClick(name: String, dialogType: Int, position: Int)
    }

    var callBack: OnOptionClick? = null

}