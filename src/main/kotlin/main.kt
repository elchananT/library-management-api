package com.polaris

import com.polaris.config.AppConfig
import com.polaris.plugin.configureDatabase
import io.ktor.server.application.Application

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val config = AppConfig.fromEnv()
    configureDatabase(config)
}
