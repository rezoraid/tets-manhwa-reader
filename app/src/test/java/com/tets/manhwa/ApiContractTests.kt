package com.tets.manhwa

import org.junit.Assert.assertEquals
import org.junit.Test

class ApiContractTests {
    @Test
    fun parseSuccessfulEnvelope() {
        val service = ApiEnvelope(
            success = true,
            data = mapOf("status" to "ok")
        )
        assertEquals(true, service.success)
        assertEquals("ok", service.data?.get("status"))
    }
}
