package com.ziperp.dms.common.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.gson.GsonBuilder
import com.ziperp.dms.AppConfiguration
import com.ziperp.dms.R
import com.ziperp.dms.base.BaseActivity
import com.ziperp.dms.core.cache.CacheManager
import com.ziperp.dms.core.token.DmsUserManager
import com.ziperp.dms.core.tracking.DmsService
import com.ziperp.dms.extensions.format
import com.ziperp.dms.extensions.toJsonString
import com.ziperp.dms.extensions.toast
import com.ziperp.dms.main.login.view.LoginActivity
import com.ziperp.dms.main.user.view.CachedDataActivity
import java.io.File


class SettingsActivity : BaseActivity() {

    override fun setLayoutId(): Int = R.layout.activity_settings

    override fun initView() {
        setToolbar("Setting", true)

        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = preferences.edit()
        editor.putBoolean("enable_service", isServiceRunning(DmsService::class.java))
        editor.putBoolean("post_location", AppConfiguration.IS_POST_LOCATION)
        editor.putString("image_options", AppConfiguration.IMAGE_QUALITY)
        editor.apply()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SettingsFragment())
            .commit()

    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            (applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            android.R.id.home -> finish()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            findPreference<Preference>("cached_size")?.summary = "${(analyseStorage(requireContext())/1024).toDouble().format()} KB"
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            findPreference<Preference>("key_logout")?.setOnPreferenceClickListener {
                DmsUserManager.logout()
                CacheManager.clearAllData()
                stopLocationService()

                startActivity(
                    Intent(
                        requireContext().applicationContext,
                        LoginActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )

                requireActivity().finish()
                return@setOnPreferenceClickListener true
            }


            findPreference<Preference>("user_setting")?.setOnPreferenceClickListener {
                showUserSetting()
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>("cached_control_form")?.setOnPreferenceClickListener {
                startActivity(CachedDataActivity.newInstance(requireContext(), 2))
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>("cached_time_keeping")?.setOnPreferenceClickListener {
                startActivity(CachedDataActivity.newInstance(requireContext(), 0))
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>("cached_tracking_background")?.setOnPreferenceClickListener {
                startActivity(CachedDataActivity.newInstance(requireContext(), 1))
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>("cached_customer")?.setOnPreferenceClickListener {
                startActivity(CachedDataActivity.newInstance(requireContext(), 3))
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>("cached_customer_image")?.setOnPreferenceClickListener {
                startActivity(CachedDataActivity.newInstance(requireContext(), 4))
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>("cached_customer_route")?.setOnPreferenceClickListener {
                startActivity(CachedDataActivity.newInstance(requireContext(), 5))
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>("cached_visit_customer")?.setOnPreferenceClickListener {
                startActivity(CachedDataActivity.newInstance(requireContext(), 6))
                return@setOnPreferenceClickListener true
            }


            findPreference<Preference>("cached_image_visit")?.setOnPreferenceClickListener {
                startActivity(CachedDataActivity.newInstance(requireContext(), 8))
                return@setOnPreferenceClickListener true
            }

            findPreference<Preference>("preload_control")?.setOnPreferenceClickListener {
                "Fetch data from server".toast()
                CacheManager.preload()
                return@setOnPreferenceClickListener true
            }
        }

        override fun onResume() {
            super.onResume()
            preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(preferences: SharedPreferences, key: String) {
            when (key) {
                "enable_service" -> updateService(preferences.getBoolean(key, true))
                "post_location" -> {
                    AppConfiguration.IS_POST_LOCATION = preferences.getBoolean(
                        key,
                        true
                    )
                }
                "image_options" -> {
                    AppConfiguration.IMAGE_QUALITY = preferences.getString(key, "0") ?: "0"
                }
            }
        }

        private fun updateService(isEnable: Boolean) {
            if (isEnable) startLocationService() else stopLocationService()
        }

        private fun startLocationService() {
            if (!isServiceRunning(DmsService::class.java)) {
                val intent = Intent(context, DmsService::class.java)
                context?.applicationContext?.startService(intent)
            }
        }

        private fun stopLocationService() {
            if (isServiceRunning(DmsService::class.java)) {
                val intent = Intent(context, DmsService::class.java)
                context?.applicationContext?.stopService(intent)
            }
        }

        private fun isServiceRunning(serviceClass: Class<*>): Boolean {
            val manager =
                (context?.applicationContext?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name == service.service.className) {
                    return true
                }
            }
            return false
        }

        fun analyseStorage(context: Context): Long{
            val appBaseFolder: File = context.filesDir.parentFile
            return browseFiles(appBaseFolder)
        }

        fun browseFiles(dir: File): Long {
            var dirSize: Long = 0
            for (f in dir.listFiles()) {
                dirSize += f.length()
                if (f.isDirectory) {
                    dirSize += browseFiles(f)
                }
            }
            return dirSize
        }

        private fun showUserSetting() {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val json = gson.toJson(DmsUserManager.userInfo)

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("User Setting")
            val view = layoutInflater.inflate(R.layout.dialog_setting_info,null, false)
            view.findViewById<TextView>(R.id.user_setting)?.text = json

            builder.setView(view)
            builder.setNegativeButton("Close") { dialogInterface, i -> // dismiss dialog
                dialogInterface.dismiss()
            }
            builder.show()
        }

    }
}