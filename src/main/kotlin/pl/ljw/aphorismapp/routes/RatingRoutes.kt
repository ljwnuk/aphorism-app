package pl.ljw.aphorismapp.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.request.ContentTransformationException
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import pl.ljw.aphorismapp.data.dtos.RatingAddDto
import pl.ljw.aphorismapp.services.RatingService
import pl.ljw.aphorismapp.services.UserService

fun Routing.registerRatingRoutes() {

    val ratingService: RatingService by inject()
    val userService: UserService by inject()

    route("/ratings") {

        get {
            val username = call.request.queryParameters["user"]
            val aphorismIdParameter = call.request.queryParameters["aphorismId"]
            if (username != null) {
                return@get if (userService.isPermittedUsername(username)) {
                    call.respond(ratingService.getByUser(username))
                } else {
                    throw BadRequestException("Username is not valid.")
                }
            }
            aphorismIdParameter ?:
                throw BadRequestException("Either username of aphorism id should be specified.")
            val aphorismId = aphorismIdParameter.toIntOrNull()
            aphorismId ?: throw BadRequestException("Aphorism id should be an integer.")
            call.respond(ratingService.getByAphorismId(aphorismId))
        }

        authenticate {
            post {
                val username = call.principal<UserIdPrincipal>()!!.name
                try {
                    val newRating = call.receive<RatingAddDto>()
                    ratingService.addRating(username, newRating)
                    call.response.status(HttpStatusCode.Created)
                } catch (e: ContentTransformationException) {
                    throw BadRequestException("New rating is malformed.")
                }
            }
        }
    }
}