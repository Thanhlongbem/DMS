package com.ziperp.dms.core.filter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ziperp.dms.R
import com.ziperp.dms.common.adapter.FilterDialogAdapter
import com.ziperp.dms.core.form.combopopup.view.ComboPopupActivity
import com.ziperp.dms.core.form.dialogpopup.view.DialogPopupActivity
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.ItemControlSelection
import com.ziperp.dms.core.form.model.KeyValue
import com.ziperp.dms.extensions.convertDate
import com.ziperp.dms.extensions.slashToDate
import com.ziperp.dms.extensions.toDateTimeString
import com.ziperp.dms.utils.DatePicker
import com.ziperp.dms.utils.DateTimePicker
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.dialog_filter.*
import java.util.*

class FilterDialogFragment : DialogFragment(), View.OnClickListener,
    FilterDialogAdapter.FilterDialogCallback {
    private lateinit var adapter: FilterDialogAdapter
    private lateinit var data: List<ItemControlForm>
    var onResult: ((data: List<ItemControlForm>) -> Unit)? = null

    companion object {
        fun newInstance(data: List<ItemControlForm>): FilterDialogFragment {
            val fragment = FilterDialogFragment()
            fragment.data = data
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
        return inflater.inflate(R.layout.dialog_filter, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        initData()
    }

    private fun setupView() {
        layoutClearFilter.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
        btnOk.setOnClickListener(this)
    }

    private fun initData() {
        adapter = FilterDialogAdapter(data)
        adapter.callback = this
        rvListFilter?.layoutManager = LinearLayoutManager(context)
        rvListFilter.adapter = adapter
    }


    private fun updateData(result: ItemControlSelection) {
        adapter.updateItem(result.itemControlForm, result.keyValues)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnCancel -> this.dismiss()
            btnOk -> {
                onResult?.invoke(adapter.data)
                this.dismiss()
            }
            layoutClearFilter ->{
                adapter.clearValue()
            }
        }
    }

    override fun onItemFilterTypeClick(data: ItemControlForm, position: Int) {
        LogUtils.d("Data ${data.lookUpType}")
        when (data.lookUpType) {
            Form.LookUpType.COMBO -> {
                val intent = Intent(requireContext(), ComboPopupActivity::class.java)
                intent.putExtra("itemControl", data)
                startActivityForResult(intent, 100)
            }
            Form.LookUpType.COMBO_MUL -> {
                val intent = Intent(requireContext(), ComboPopupActivity::class.java)
                intent.putExtra("itemControl", data)
                startActivityForResult(intent, 100)
            }
            Form.LookUpType.DIALOG -> {
                val intent = Intent(requireContext(), DialogPopupActivity::class.java)
                intent.putExtra("itemControl", data)
                startActivityForResult(intent, 100)
            }
            Form.LookUpType.DIALOG_MUL -> {
                val intent = Intent(requireContext(), DialogPopupActivity::class.java)
                intent.putExtra("itemControl", data)
                startActivityForResult(intent, 100)
            }
            Form.LookUpType.DATE -> {
                val content = data.getDisplayText()
                val date: Date = content.slashToDate() ?: Date()

                DatePicker.newInstance (date).showDialog(requireActivity().supportFragmentManager){
                    if(it.isNotBlank()){
                        data.controlValue = arrayListOf(KeyValue(valueCode = it.convertDate(), valueName = it))
                    }else{
                        data.controlValue = arrayListOf(KeyValue(valueCode = "", valueName = ""))
                    }
                    adapter.updateItem(data, data.controlValue)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            data?.let {
                val result: ItemControlSelection =
                    it.getSerializableExtra("result") as ItemControlSelection
                updateData(result)
            }
        }
    }


}