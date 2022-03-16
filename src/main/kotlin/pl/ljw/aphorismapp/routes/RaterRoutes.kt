package pl.ljw.aphorismapp.routes

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import pl.ljw.aphorismapp.services.UserService

private const val DEFAULT_QUANTITY = 5

fun Routing.registerRaterRoutes() {

    val userService: UserService by inject()

    route("/prolific-raters") {
        get {
            val quantityParameter = call.request.queryParameters["quantity"]
            val quantity =
                if (quantityParameter == null)
                    DEFAULT_QUANTITY
                else
                    quantityParameter.toIntOrNull()
            quantity ?: throw BadRequestException("Author quantity should be an integer.")
            call.respond(userService.getMostActiveRaters(quantity))
        }
    }
}
