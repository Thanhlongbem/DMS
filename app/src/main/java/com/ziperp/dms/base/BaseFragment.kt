package com.ziperp.dms.base

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ziperp.dms.common.model.LoadingDialog
import com.ziperp.dms.extensions.call
import com.ziperp.dms.extensions.find
import com.ziperp.dms.extensions.mail
import com.ziperp.dms.extensions.setClipboard
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment(), View.OnClickListener {

    val compositeDisposable = CompositeDisposable()
    private var listPhoneView: List<View> = arrayListOf()
    private var listMailView: List<View> = arrayListOf()
    private var loadingDialog: LoadingDialog? = null

    abstract fun setLayoutId(): Int

    abstract fun initView()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(setLayoutId(), container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

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

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.order) {
            1 -> {
                context?.setClipboard(activity?.find<TextView>(item.groupId)?.text.toString()) ?: return true
                return true
            }
            2 -> {
                context?.call(activity?.find<TextView>(item.groupId)?.text.toString()) ?: return true
                return true
            }
            3 -> context?.mail(activity?.find<TextView>(item.groupId)?.text.toString()) ?: return true
        }
        return super.onContextItemSelected(item)
    }

    override fun onClick(view: View?) {
    }

    fun showLoading(show: Boolean) {
        if(loadingDialog == null){
            loadingDialog = LoadingDialog(requireContext())
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