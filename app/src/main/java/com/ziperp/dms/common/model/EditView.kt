package com.ziperp.dms.common.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.InputFilter
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.FragmentManager
import com.jakewharton.rxbinding3.widget.textChanges
import com.ziperp.dms.R
import com.ziperp.dms.extensions.*
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.DatePicker
import com.ziperp.dms.utils.DateTimePicker
import com.ziperp.dms.utils.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.layout_edit_view.view.*
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class EditView@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val compositeDisposable = CompositeDisposable()

    var isMandatory = false
    var content: String = ""
    var isNeedToCheckValid = false
    var ignoreCallback = false
    var isNumberInput = false
    var isPasswordInput = false
    var isHidePassword = false

    var listener: OnValueUpdatedListener? = null

    var isEnable: Boolean = true
        set(value) {
            field = value
            dim_view.visibility = if (value) View.GONE else View.VISIBLE
        }

    var ignoreUpdate = false
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        tv_edit_content.textChanges()
            .skip(1)
            .map { it.toString() }
            .filter{!ignoreUpdate}
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ text ->
                if(isNumberInput){
                    ignoreUpdate = true
                    if(text.isNotBlank()){
                        if (text.count { c -> c == '.' } == 1 && text.last() == '.') {
                            // Do nothing
                        } else {
                            content = text.toDoubleValue().format()
                            tv_edit_content.setText(content)
                            tv_edit_content.setSelection(content.length)
                        }
                    }
                    ignoreUpdate = false
                } else {
                    content = text
                }

                if(ignoreCallback){
                    ignoreCallback = false
                } else {
                    listener?.updatedValueListener(content, this)
                }
                checkValid()
            }, {

            }).disposedBy(compositeDisposable)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        compositeDisposable.dispose()
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_edit_view, this, true)

        val typedArray: TypedArray = getContext().obtainStyledAttributes(
            attrs,
            R.styleable.EditView
        )
        val title = AppUtil.getMultipleAttr(context, typedArray, R.styleable.EditView_title)
        val hint = AppUtil.getMultipleAttr(context, typedArray, R.styleable.EditView_hint)
        val isLocationPicker = typedArray.getBoolean(R.styleable.EditView_isLocationPicker, false)
        val isDateTimePicker = typedArray.getBoolean(R.styleable.EditView_isDateTimePicker, false)
        isMandatory = typedArray.getBoolean(R.styleable.EditView_isMandatory, false)
        isEnable = typedArray.getBoolean(R.styleable.EditView_isEnable, true)
        isNumberInput =  typedArray.getBoolean(R.styleable.EditView_isNumberInput, false)
        isPasswordInput =  typedArray.getBoolean(R.styleable.EditView_isPasswordInput, false)
        val isShowDivider = typedArray.getBoolean(R.styleable.EditView_isPasswordInput, true)
        val isPhoneInput =  typedArray.getBoolean(R.styleable.EditView_isPhoneInput, false)

        tv_edit_title.text = title
        tv_edit_content.hint = hint
        dim_view.setOnClickListener {  }
        mandatory_mark.visibility = if (isMandatory) View.VISIBLE else View.GONE
        divider.visibility = if (isShowDivider) View.VISIBLE else View.INVISIBLE
        if (isLocationPicker) {
            img_option.setImageResource(R.drawable.ic_location_blue)
            tv_edit_content.isEnabled = false
        }
        if (isDateTimePicker) {
            img_option.setImageResource(R.drawable.icon_calendar)
            tv_edit_content.isEnabled = false
        }
        if (isNumberInput) {
            tv_edit_content.keyListener = DigitsKeyListener.getInstance("0123456789.")
            tv_edit_content.filters = arrayOf(InputFilter.LengthFilter(19))
            tv_edit_content.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    if(content.isNotBlank()){
                        ignoreUpdate = true
                        content = content.toDoubleValue().format()
                        tv_edit_content.setText(content)
                        ignoreUpdate = false
                    }
                }
            }
        }
        if(isPasswordInput) {
            img_option.setImageResource(R.drawable.icon_not_visible)
            tv_edit_content.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            img_option.setOnClickListener {
                if (isHidePassword) {
                    img_option.setImageResource(R.drawable.icon_visible)
                    tv_edit_content.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                } else {
                    img_option.setImageResource(R.drawable.icon_not_visible)
                    tv_edit_content.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                isHidePassword = !isHidePassword
            }
        }

        if (isPhoneInput) {
            tv_edit_content.filters = arrayOf(InputFilter.LengthFilter(19))
            tv_edit_content.inputType = InputType.TYPE_CLASS_PHONE
        }

    }


    fun setShowDatePicker() {
        layout_click.setOnClickListener {
            var date: Date = content.slashToDate() ?: Date()
            if (isEnable) {
                DatePicker.newInstance (date).showDialog(getFragmentManager(context)){selectedDate ->
                    setValue(selectedDate)
                }
            }
        }
    }

    fun setShowDateTimePicker() {
        layout_click.setOnClickListener {
            var date: Date = content.slashToDateTime() ?: Date()
            if (isEnable) {
                DateTimePicker.newInstance (date).showDialog(getFragmentManager(context)){selectedDate ->
                    setValue(selectedDate)
                }
            }
        }
    }

    fun getFragmentManager(context: Context?): FragmentManager? {
        return when (context) {
            is AppCompatActivity -> context.supportFragmentManager
            is ContextThemeWrapper -> getFragmentManager(context.baseContext)
            else -> null
        }
    }

    fun setValue(value: String, ignoreCallback: Boolean = false) {
        this.ignoreCallback = ignoreCallback
        tv_edit_content.setText(value)
        content = value
    }

    fun checkValid(): Boolean {
        if (!isNeedToCheckValid) return true
        if (isMandatory && content.isEmpty()) {
            LogUtils.d("Invalid in control " + tv_edit_title.text)
            tv_warning.visibility = View.VISIBLE
            return false
        } else {
            tv_warning.visibility = View.GONE
        }
        return true
    }

    //MARK: Properties
    var doubleValue: Double = 0.0// Get double value of editText
    get() = if (content.isNotBlank()) {
            try {
                content.toDoubleValue()
            }catch (e: Exception){
                0.0
            }

        } else 0.0

    var intValue: Int = 0// Get Int value of editText
    get() = if (content.isNotBlank()) {
            try {
                content.toDoubleValue().toInt()
            }catch (e: Exception) {
                 0
            }
        } else 0



    interface OnValueUpdatedListener{
        fun updatedValueListener(content: String, editView: EditView)
    }

}