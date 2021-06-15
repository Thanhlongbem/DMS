package com.ziperp.dms.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ziperp.dms.R
import com.ziperp.dms.extensions.day
import com.ziperp.dms.extensions.month
import com.ziperp.dms.extensions.toSlashDateString
import com.ziperp.dms.extensions.yearValue
import kotlinx.android.synthetic.main.date_picker.*
import java.util.*


class DatePicker() : DialogFragment() {

    var date = Date()

    companion object {
        fun newInstance(date: Date): DatePicker {
            val fragment = DatePicker()
            fragment.date = date
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_Dialog_Custom)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.date_picker, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    var onValueChanged: ((String) -> Unit)? = null

    private fun setupView() {
        btn_done.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
            onValueChanged?.invoke(calendar.time.toSlashDateString())
            dismiss()
        }

        datePicker.init(
            date.yearValue(),
            date.month(),
            date.day()
        ) { datePicker, year, monthOfYear, dayOfMonth ->
        }
    }

    fun showDialog(manager: FragmentManager?, onValueChanged: ((String) -> Unit)){
        this.onValueChanged = onValueChanged
        manager?.let {
            show(it, "")
        }
    }

}