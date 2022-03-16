package pl.ljw.aphorismapp.services

import io.ktor.auth.*
import io.ktor.features.*
import pl.ljw.aphorismapp.data.dtos.UserActiveDto
import pl.ljw.aphorismapp.repositories.UserRepository

interface UserService {

    fun areCredentialsValid(credentials: UserPasswordCredential): Boolean

    fun isPermittedUsername(username: String): Boolean

    fun getMostActiveRaters(quantity: Int): List<UserActiveDto>
}

class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun areCredentialsValid(credentials: UserPasswordCredential): Boolean {
        val (login, password) = credentials
        val passwordInDb = userRepository.getPasswordByLogin(login)
        passwordInDb ?: return false
        return passwordInDb == password
    }

    override fun isPermittedUsername(username: String) =
        username.isNotBlank() &&
        username.all {
            it.isLetter() ||
            it.isDigit() ||
            it == '_'
        }

    override fun getMostActiveRaters(quantity: Int): List<UserActiveDto> {
        if (quantity < 0) {
            throw BadRequestException("User quantity should not be negative.")
        }
        return userRepository.getMostActiveRaters(quantity)
    }
}
