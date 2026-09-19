package com.polaris

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.assertEquals
import kotlin.test.Test

class ServerTest {

    @Test
    fun `test books endpoint returns 200`() = testApplication {
        // loads default configuration
        configure()
        // verify server boots, DB migrates, and /books responds
        assertEquals(HttpStatusCode.OK, client.get("/books").status)
    }
}