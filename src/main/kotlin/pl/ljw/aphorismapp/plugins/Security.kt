package pl.ljw.aphorismapp.plugins

import io.ktor.auth.*
import io.ktor.application.*
import org.koin.ktor.ext.inject
import pl.ljw.aphorismapp.services.UserService

fun Application.configureSecurity() {
    val userService: UserService by inject()
    install(Authentication) {
        basic {
            realm = "Aphorism app"
            validate { credentials ->
                if (userService.areCredentialsValid(credentials)) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}
