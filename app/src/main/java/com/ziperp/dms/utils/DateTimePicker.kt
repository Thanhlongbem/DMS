package com.ziperp.dms.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ziperp.dms.R
import com.ziperp.dms.core.filter.FilterDialogFragment
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.extensions.*
import kotlinx.android.synthetic.main.date_time_picker.*
import java.util.*

class DateTimePicker() : DialogFragment(){

    var date = Date()
    companion object {
        fun newInstance(date: Date): DateTimePicker {
            val fragment = DateTimePicker()
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
        return inflater.inflate(R.layout.date_time_picker, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }


    var onValueChanged: ((String) -> Unit)? = null

    private fun setupView(){

        layoutDate.isSelected = true
        layoutTime.isSelected = false
        datePicker.visibility = View.VISIBLE
        timePicker.visibility = View.GONE

        tv_date_time.text = date.toSlashDateTimeString()
        btn_done.setOnClickListener {
            onValueChanged?.invoke(parseDateTime().toSlashDateTimeString())
            dismiss()
        }
        layoutDate.setOnClickListener {
            layoutDate.isSelected = true
            layoutTime.isSelected = false
            datePicker.visibility = View.VISIBLE
            timePicker.visibility = View.GONE
        }
        layoutTime.setOnClickListener {
            layoutDate.isSelected = false
            layoutTime.isSelected = true
            datePicker.visibility = View.GONE
            timePicker.visibility = View.VISIBLE
        }

        //update current value
        val cal = Calendar.getInstance()
        cal.time = date

        datePicker.init(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ) { _, _, _, _ ->
            updateDateTime()
        }


        timePicker.setIs24HourView(true)
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            timePicker.currentHour = cal.get(Calendar.HOUR_OF_DAY)
            timePicker.currentMinute = cal.get(Calendar.MINUTE)
        }else{
            timePicker.hour = cal.get(Calendar.HOUR_OF_DAY)
            timePicker.minute = cal.get(Calendar.MINUTE)
        }
        timePicker.setOnTimeChangedListener { _, _, _ ->
            updateDateTime()
        }
    }


    private fun updateDateTime(){
        tv_date_time.text = parseDateTime().toSlashDateTimeString()
    }

    private fun parseDateTime(): Date{
        val calendar = Calendar.getInstance()
        val hour =  if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) timePicker.currentHour else  timePicker.hour
        val minute =  if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) timePicker.currentMinute else  timePicker.minute
        calendar.set(
            datePicker.year,
            datePicker.month,
            datePicker.dayOfMonth,
            hour,
            minute
        )
        return calendar.time
    }

    fun showDialog(manager: FragmentManager?, onValueChanged: ((String) -> Unit)){
        this.onValueChanged = onValueChanged

        manager?.let {
            show(it, "")
        }
    }
}