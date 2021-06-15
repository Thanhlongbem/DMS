package com.ziperp.dms

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ziperp.dms.camera.CustomerImage
import com.ziperp.dms.core.rest.TrackingRequest
import com.ziperp.dms.extensions.toJsonString
import com.ziperp.dms.main.customer.customerdetail.model.CustomerDetail
import com.ziperp.dms.main.customer.customerroute.model.CustomerRoute
import com.ziperp.dms.main.timekeeping.model.TimeKeeping
import com.ziperp.dms.main.visitcustomer.model.VisitCustomer
import com.ziperp.dms.utils.LogUtils

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.

        val customerDetail = VisitCustomer()
        LogUtils.d("json data ${customerDetail.toJsonString()}")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.ziperp.dms", appContext.packageName)
    }
}
