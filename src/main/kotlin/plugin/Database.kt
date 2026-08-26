package com.polaris.plugin

import com.polaris.config.AppConfig
import com.polaris.config.DatabaseFactory
import io.ktor.server.application.Application


fun Application.configureDatabase(config: AppConfig) {
    DatabaseFactory.init(config)
}