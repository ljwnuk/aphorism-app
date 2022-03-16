package pl.ljw.aphorismapp.plugins

import io.ktor.features.*
import io.ktor.application.*

fun Application.configureHTTP() {
    install(DefaultHeaders) {
        header("X-Engine", "Ktor")
    }
}
