package com.ziperp.dms.core.tracking

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.ziperp.dms.R
import kotlinx.android.synthetic.main.dialog_time_keeping.*

class ErrorLocationDialog(context: Context): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_error_location)
        btn_ok.setOnClickListener {
            dismiss()
        }
    }
}