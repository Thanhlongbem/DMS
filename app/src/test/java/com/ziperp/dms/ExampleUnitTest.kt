package com.ziperp.dms

import com.ziperp.dms.utils.LogUtils
import io.reactivex.Observable.fromIterable
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        val data = arrayListOf<String>()
        data.add("A")
//        for(i in 0..30){
//            data.add("${i}")
//        }

        data.chunked(50)
            .forEach {list ->
                val data = list.reduce{str1, str2 -> "$str1&$str2"}
                println("data ${data} List size ${list.size}")
            }


    }

    fun duration(HHmm1: String, HHmm2: String): Double{
        try{
            val date1 = SimpleDateFormat("HHmm", Locale.US).parse(HHmm1) as Date
            val date2 = SimpleDateFormat("HHmm", Locale.US).parse(HHmm2) as Date
            return (date2.time - date1.time).toDouble()/(3600*1000)
        }catch (e: Exception){
            return 0.0
        }
    }
}
