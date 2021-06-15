package com.ziperp.dms.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.ziperp.dms.R
import com.ziperp.dms.common.model.EditView
import com.ziperp.dms.core.form.combopopup.view.ComboPopupActivity
import com.ziperp.dms.core.form.dialogpopup.view.DialogPopupActivity
import com.ziperp.dms.core.form.model.Form
import com.ziperp.dms.core.form.model.ItemControlForm
import com.ziperp.dms.core.form.model.ItemControlSelection
import com.ziperp.dms.extensions.toast
import com.ziperp.dms.utils.AppUtil
import com.ziperp.dms.utils.LogUtils
import kotlinx.android.synthetic.main.layout_edit_view.view.*

abstract class BaseCUDActivity<T: Any>: BaseActivity(), EditView.OnValueUpdatedListener{

    lateinit var item: T

    var listOption = listOf<Pair<EditView, ItemControlForm>>()
    var listMandatoryView = arrayListOf<EditView>()


    var mode = CREATE_MODE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mode = intent.getIntExtra(EXTRA_CUD_MODE, CREATE_MODE)
        listOption = setListOption()
        listMandatoryView = setListMandatoryView()
        initOptionButtons()

        when(mode) {
            CREATE_MODE -> {
                createNewForm()
            }
            UPDATE_MODE -> {
                item = intent.getSerializableExtra(EXTRA_CUD_OBJECT) as T
                fillCurrentInfo()
            }
        }
    }

    abstract fun createNewForm()
    abstract fun fillCurrentInfo()
    abstract fun setListOption(): List<Pair<EditView, ItemControlForm>>
    abstract fun setListMandatoryView() : ArrayList<EditView>
    abstract fun requestUpdate()

    open fun onUpdateItemControl(index: Int){
    }
    override fun updatedValueListener(content: String, editView: EditView){
    }

    fun setOnUpdatedValueListener(vararg ids: EditView) {
        for (view in ids) view.listener = this
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
                            val intent = Intent(this, DialogPopupActivity::class.java)
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
                            val intent = Intent(this, ComboPopupActivity::class.java)
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
                onUpdateItemControl(index)
            }
        }
    }

    private fun checkMandatoryField() : Boolean {
        var isReadyToCUD = true
        listMandatoryView.forEachIndexed{index, editView ->
            editView.isNeedToCheckValid = true
            if (!editView.checkValid()) {
                LogUtils.d("Index invalid $index")
                isReadyToCUD = false
            }
        }
        return isReadyToCUD
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        if(mode == CREATE_MODE){
            menu?.findItem(R.id.action_save)?.title = "Add"
        }
        menu?.findItem(R.id.action_save)?.isVisible = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_save -> {
                if (checkMandatoryField()) requestUpdate() else "Please fill all mandatory field!".toast(this)
            }
        }
        return true
    }

    override fun onBackPressed() {
        AppUtil.showAlertDialog(this, "Do you want to discard this form?", {finish()}, {LogUtils.d("Cancel")})
    }



    companion object {
        const val EXTRA_CUD_MODE = "EXTRA_CUSTOMER_MODE"
        const val EXTRA_CUD_OBJECT = "EXTRA_CUSTOMER_OBJECT"
        const val CREATE_MODE = 1
        const val UPDATE_MODE = 2
        const val DELETE_MODE = 3
    }
}