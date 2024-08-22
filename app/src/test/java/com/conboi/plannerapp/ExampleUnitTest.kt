package com.conboi.plannerapp

import com.conboi.core.data.getUniqueRequestCode
import com.conboi.core.domain.enums.AlarmType
import org.junit.Before
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private lateinit var SUT: com.conboi.features.main.MainFragment

    @Before
    fun setUp() {
        SUT = com.conboi.features.main.MainFragment()
    }

    @Test
    fun checkUniqueRequestCodeWork() {
        val result = getUniqueRequestCode(alarmType = AlarmType.REMINDER, 123) == 1111123
        assert(result)
    }
}
