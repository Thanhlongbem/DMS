package com.ziperp.dms.base

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.transition.Explode
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.ziperp.dms.R
import com.ziperp.dms.common.model.LoadingDialog
import com.ziperp.dms.extensions.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_sale_order.*


abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {
    val compositeDisposable = CompositeDisposable()
    private val toolbar: Toolbar? by lazy { find<Toolbar>(R.id.toolbar) }
    private var listPhoneView: List<View> = arrayListOf()
    private var listMailView: List<View> = arrayListOf()
    private val MODE_SUCCESS = 1
    private val MODE_ERROR = -1
    private val MODE_INFO= 0
    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//        with(window) {
//            requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            // set an exit transition
//            exitTransition = Explode()
//        }
        setContentView(setLayoutId())
        initView()
    }

    abstract fun setLayoutId(): Int

    abstract fun initView()

    fun setOnClickListener(vararg ids: View) {
        for (view in ids) view.setOnClickListener(this)
    }

    fun setOnRegisterPhoneContextMenu(vararg ids: View) {
        listPhoneView = ids.toList()
        for (view in ids) registerForContextMenu(view)
    }

    fun setOnRegisterMailContextMenu(vararg ids: View) {
        listMailView = ids.toList()
        for (view in ids) registerForContextMenu(view)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.clear()
        v.id.let { menu.add(v.id, it, 1, "Copy") }
        if(listPhoneView.contains(v)) {
            v.id.let { menu.add(v.id, it, 2, "Call") }
        } else if (listMailView.contains(v)){
            v.id.let { menu.add(v.id, it, 3, "Email") }
        }
    }

    override fun onClick(view: View?) {
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.order) {
            1 -> {
                setClipboard(find<TextView>(item.groupId).text.toString())
                return true
            }
            2 -> {
                call(find<TextView>(item.groupId).text.toString())
                return true
            }
            3 -> mail(find<TextView>(item.groupId).text.toString())
        }
        return super.onContextItemSelected(item)
    }

    fun setToolbar(title: String, isHomeUpEnable: Boolean) {
        toolbar?.let {
            setSupportActionBar(it)
            it.setNavigationOnClickListener { onBackPressed() }
            toolbar_title.text = title
        }
        supportActionBar?.let {
            it.title = ""
            it.setDisplayHomeAsUpEnabled(isHomeUpEnable)
            it.setDisplayShowHomeEnabled(isHomeUpEnable)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            currentFocus?.let { v ->
                if (v is EditText) {
                    val outRect = Rect()
                    v.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        v.clearFocus()
                        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                        inputMethodManager?.hideSoftInputFromWindow(v.getWindowToken(), 0)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    fun showLoading(show: Boolean) {
        if(loadingDialog == null){
            loadingDialog = LoadingDialog(this)
        }
        if (show) {
            loadingDialog!!.show()
        } else {
            if (loadingDialog!!.isShowing) loadingDialog!!.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

}