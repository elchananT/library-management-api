package com.polaris

import com.polaris.config.AppConfig
import com.polaris.config.appModule
import com.polaris.plugin.configureDatabase
import com.polaris.plugin.configureSecurity
import com.polaris.plugin.configureSerialization
import com.polaris.plugin.configureStatusPages
import com.polaris.security.JwtService
import com.polaris.services.UserService
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module(config: AppConfig = AppConfig.fromEnv()) {
    install(Koin) {
        modules(appModule(config))
    }

    val config = AppConfig.fromEnv()
    configureDatabase(config)
    configureSerialization()
    configureSecurity(config, get<JwtService>())
    configureStatusPages()
    configureRouting(
        get<UserService>()
    )
}
