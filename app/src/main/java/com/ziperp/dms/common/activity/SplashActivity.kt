package com.ziperp.dms.common.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.ziperp.dms.Injection
import com.ziperp.dms.R
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.extensions.getViewModel
import com.ziperp.dms.main.home.HomeActivity
import com.ziperp.dms.main.login.view.LoginActivity

class SplashActivity : AppCompatActivity() {

    private val viewModel by lazy {
        getViewModel { Injection.provideLoginViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            DmsUserManager.accessToken?.let{
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } ?: run {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 2000)
    }

}
