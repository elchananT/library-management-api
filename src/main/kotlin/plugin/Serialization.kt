package com.polaris.plugin

import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.time.Instant
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object JavaTimeSerializers {
    private val offsetDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    private object InstantSerializer : KSerializer<Instant> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("java.time.Instant", PrimitiveKind.STRING)
        override fun serialize(encoder: Encoder, value: Instant) = encoder.encodeString(value.toString())
        override fun deserialize(decoder: Decoder): Instant = Instant.parse(decoder.decodeString())
    }

    private object OffsetDateTimeSerializer : KSerializer<OffsetDateTime> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("java.time.OffsetDateTime", PrimitiveKind.STRING)
        override fun serialize(encoder: Encoder, value: OffsetDateTime) =
            encoder.encodeString(value.format(offsetDateTimeFormatter))
        override fun deserialize(decoder: Decoder): OffsetDateTime =
            OffsetDateTime.parse(decoder.decodeString(), offsetDateTimeFormatter)
    }

    val module = SerializersModule {
        contextual(Instant::class, InstantSerializer)
        contextual(OffsetDateTime::class, OffsetDateTimeSerializer)
    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            serializersModule = JavaTimeSerializers.module
        })
    }
}