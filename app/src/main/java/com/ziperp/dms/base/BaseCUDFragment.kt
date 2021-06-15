package com.ziperp.dms.base

import android.app.Activity
import android.content.Intent
import android.view.View
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseCUDActivity.Companion.CREATE_MODE
import com.ziperp.dms.base.BaseCUDActivity.Companion.UPDATE_MODE
import com.ziperp.dms.common.model.EditView
import com.ziperp.dms.core.form.combopopup.view.ComboPopupActivity
import com.ziperp.dms.core.form.dialogpopup.view.DialogPopupActivity
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.ItemControlSelection
import kotlinx.android.synthetic.main.layout_edit_view.view.*

abstract class BaseCUDFragment<T: Any>(val mode: Int): BaseFragment(), EditView.OnValueUpdatedListener {

    lateinit var item: T

    var listOption = arrayListOf<Pair<EditView, ItemControlForm>>()
    var listMandatoryView = arrayListOf<EditView>()

    override fun initView() {
        listOption = setListOption()
        listMandatoryView = setListMandatoryView()

        when(mode) {
            CREATE_MODE -> {
                createNewForm()
            }
            UPDATE_MODE -> {
                fillCurrentInfo()
            }
        }

        initOptionButtons()
    }


    abstract fun createNewForm()
    abstract fun fillCurrentInfo()
    abstract fun setListOption(): ArrayList<Pair<EditView, ItemControlForm>>
    abstract fun setListMandatoryView() : ArrayList<EditView>
    abstract fun getCUDInfo(): Any

    fun setOnUpdatedValueListener(vararg ids: EditView) {
        for (view in ids) view.listener = this
    }

    override fun updatedValueListener(content: String, editView: EditView){
    }

    open fun onUpdateItemControl(index: Int){
    }

    private fun initOptionButtons() {
        listOption.forEach { pair ->
            val view = pair.first
            val itemControlForm = pair.second

            view.tv_edit_content.isEnabled = false
            when(itemControlForm.lookUpType) {
                Form.LookUpType.DIALOG,
                Form.LookUpType.DIALOG_MUL -> {
                    view.img_option.setImageResource(R.drawable.ic_search_black)
                    view.layout_click.setOnClickListener {
                        if (view.isEnable) {
                            val intent = Intent(activity, DialogPopupActivity::class.java)
                            intent.putExtra("itemControl", itemControlForm)
                            startActivityForResult(intent, 100)
                        }
                    }
                }

                Form.LookUpType.COMBO,
                Form.LookUpType.COMBO_MUL -> {
                    view.img_option.setImageResource(R.drawable.ic_down_black)
                    view.layout_click.setOnClickListener {
                        if (view.isEnable) {
                            val intent = Intent(activity, ComboPopupActivity::class.java)
                            intent.putExtra("itemControl", itemControlForm)
                            startActivityForResult(intent, 100)
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            data?.let {
                val result: ItemControlSelection = it.getSerializableExtra("result") as ItemControlSelection
                val index = listOption.indexOf(listOption.find { item -> item.second.controlId == result.itemControlForm.controlId })
                listOption[index].second.controlValue = result.keyValues
                listOption[index].first.setValue(listOption[index].second.getDisplayText())
                listOption[index].first.tv_warning.visibility = View.GONE

                listOption[index].second.controlValue.apply {
                    if(isNotEmpty()){
                        onUpdateItemControl(index)
                    }
                }
            }
        }
    }

    fun checkMandatoryField() : Boolean {
        var isReadyToCUD = true
        for (view in listMandatoryView) {
            view.isNeedToCheckValid = true
            view.checkValid()
            if (!view.checkValid()) isReadyToCUD = false
        }
        return isReadyToCUD
    }

}