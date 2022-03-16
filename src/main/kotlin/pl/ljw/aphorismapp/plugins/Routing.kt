package pl.ljw.aphorismapp.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.features.*
import io.ktor.application.*
import io.ktor.features.BadRequestException
import io.ktor.features.NotFoundException
import io.ktor.response.*
import pl.ljw.aphorismapp.routes.registerAphorismRoutes
import pl.ljw.aphorismapp.routes.registerRaterRoutes
import pl.ljw.aphorismapp.routes.registerRatingRoutes

fun Application.configureRouting() {

    install(AutoHeadResponse)

    routing {
        registerAphorismRoutes()
        registerRaterRoutes()
        registerRatingRoutes()
    }

    install(StatusPages) {
        exception<BadRequestException> {
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<NotFoundException> {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}
