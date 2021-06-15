package com.ziperp.dms.main.login.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.core.rest.Constants
import com.ziperp.dms.core.rest.Constants.PREF_CURRENT_LANGUAGE
import com.ziperp.dms.core.rest.Status
import com.ziperp.dms.extensions.getString
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.home.HomeActivity
import com.ziperp.dms.main.login.model.Server
import com.ziperp.dms.utils.AppConstant
import com.ziperp.dms.utils.DMSPreference
import com.ziperp.dms.utils.OptionsDialog
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*


class LoginActivity : BaseActivity(), OptionsDialog.OnOptionClick {

    private var isPasswordVisible = false
    private lateinit var dialogOption: OptionsDialog
    private lateinit var listServer: List<Server>
    private var currentServer: Server? = null

    override fun setLayoutId() = R.layout.activity_login

    private val viewModel by lazy {
        getViewModel { Injection.provideLoginViewModel() }
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        val currentLang = DMSPreference.getInt(PREF_CURRENT_LANGUAGE, 1)
        resources.apply {
            configuration.setLocale(Locale(if(currentLang == 1) "en" else "vi"))
            updateConfiguration(configuration, displayMetrics)
        }

        txt_select_language.text = if (currentLang == 1) R.string.str_english.getString() else R.string.str_vietnamese.getString()
        select_server.text = String.format(R.string.str_server.getString(), "")
        setOnClickListener(but_login, img_hide_password, txt_select_language, select_server)

        viewModel.userInfoLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    Toast.makeText(this, "Login successfully ", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                }
                Status.LOADING -> {
                    loading_progressbar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    loading_progressbar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
        viewModel.getListServer()

        viewModel.listServerLiveData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    loading_progressbar.visibility = View.GONE
                    it.data?.let { response ->
                        listServer = response.listServer
                        val defaultServer = listServer.find { item -> item.defDomain == 1 }
                        if (currentServer == null) {
                            currentServer = defaultServer!!
                            select_server.text = String.format(R.string.str_server.getString(), defaultServer.serverName)
                        }
                    }
                }

                Status.LOADING -> { loading_progressbar.visibility = View.VISIBLE }

                Status.ERROR -> {
                    loading_progressbar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.txt_select_language -> {
                val data: MutableList<String> = arrayListOf()
                data.add(R.string.str_vietnamese.getString())
                data.add(R.string.str_english.getString())
                dialogOption = OptionsDialog(this, R.style.FullScreenDialogTheme)
                dialogOption.callBack = this
                dialogOption.showDialog(
                    Constants.TYPE_LANGUAGE,
                    "",
                    data,
                    txt_select_language.text.trim().toString()
                ) {}
            }
            R.id.select_server -> {
                dialogOption = OptionsDialog(this, R.style.FullScreenDialogTheme)
                dialogOption.callBack = this
                dialogOption.showDialog(
                    Constants.TYPE_LIST_SERVER,
                    R.string.str_select_server_login.getString(),
                    listServer.map { item -> item.serverName},
                    currentServer!!.serverName
                ) {}
            }
            R.id.img_hide_password -> {
                if (isPasswordVisible) {
                    isPasswordVisible = false
                    txt_password.transformationMethod = PasswordTransformationMethod();
                    img_hide_password.setImageResource(R.drawable.ic_baseline_visibility_off_24)
                } else {
                    isPasswordVisible = true
                    txt_password.transformationMethod = null
                    img_hide_password.setImageResource(R.drawable.ic_baseline_visibility_24)
                }
            }
            R.id.but_login -> {
                viewModel.login(txt_username.text.toString(), txt_password.text.toString())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onOptionClick(name: String, dialogType: Int, position: Int) {
        when (dialogType) {
            Constants.TYPE_LIST_SERVER -> {
                select_server.text = String.format(R.string.str_server.getString(), name)
                Constants.API_BASE_PATH = listServer[position].domain
                DMSPreference.setString(AppConstant.BASE_URL, listServer[position].domain)
                currentServer = listServer[position]
            }
            Constants.TYPE_LANGUAGE -> {
                if(name == R.string.str_vietnamese.getString()){
                    setLocale("vi")
                } else {
                    setLocale("en")
                }
            }
        }
    }

    private fun setLocale(localeName: String) {
        DMSPreference.setInt(PREF_CURRENT_LANGUAGE, if (localeName == "vi") 0 else 1)
        val myLocale = Locale(localeName)
        resources.apply {
            configuration.setLocale(myLocale)
            updateConfiguration(configuration, displayMetrics)
        }
        recreate()
    }
}
