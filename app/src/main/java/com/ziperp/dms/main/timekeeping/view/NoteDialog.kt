package com.ziperp.dms.main.timekeeping.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.ziperp.dms.R
import com.ziperp.dms.extensions.getString
import kotlinx.android.synthetic.main.dialog_time_keeping.*

class NoteDialog(context: Context, val mode: Int, val location: String) : Dialog(context) {

    lateinit var onConfirmed: (String) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_time_keeping)
        tv_title.text = if (mode == TimekeepingActivity.CHECK_IN_MODE) R.string.str_check_in_note.getString() else R.string.str_check_out_note.getString()
        tv_location.text = location
        btn_ok.setOnClickListener {
            onConfirmed.invoke(tv_note.text.toString())
            dismiss()
        }
        btn_cancel.setOnClickListener { dismiss() }
    }
}