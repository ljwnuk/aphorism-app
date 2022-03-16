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
import pl.ljw.aphorismapp.data.dtos.AphorismAddDto
import pl.ljw.aphorismapp.services.AphorismService
import pl.ljw.aphorismapp.services.UserService

private const val DEFAULT_QUANTITY = 5

private fun ApplicationCall.extractQuantity(): Int {
    val quantityParameter = request.queryParameters["quantity"]
    quantityParameter ?: return DEFAULT_QUANTITY
    return quantityParameter.toIntOrNull() ?:
        throw BadRequestException("Aphorism quantity should be an integer.")
}

fun Routing.registerAphorismRoutes() {

    val aphorismService: AphorismService by inject()
    val userService: UserService by inject()

    route("/aphorisms") {

        get {
            val author = call.request.queryParameters["author"]
            author ?: return@get run {
                call.respond(aphorismService.getAll())
            }
            if (!userService.isPermittedUsername(author)) {
                throw BadRequestException("The author name is not valid.")
            }
            call.respond(aphorismService.getByAuthor(author))
        }

        get("/random") {
            val quantity = call.extractQuantity()
            call.respond(aphorismService.getRandom(quantity))
        }

        get("/best") {
            val quantity = call.extractQuantity()
            call.respond(aphorismService.getBest(quantity))
        }

        get("/popular") {
            val quantity = call.extractQuantity()
            call.respond(aphorismService.getPopular(quantity))
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            id ?: throw BadRequestException("Aphorism id should be an integer.")
            call.respond(aphorismService.getById(id))
        }

        authenticate {
            post {
                val username = call.principal<UserIdPrincipal>()!!.name
                try {
                    val newAphorism = call.receive<AphorismAddDto>()
                    aphorismService.addNew(username, newAphorism.content)
                    call.response.status(HttpStatusCode.Created)
                } catch (e: ContentTransformationException) {
                    throw BadRequestException("New aphorism is malformed.")
                }
            }
        }
    }
}
