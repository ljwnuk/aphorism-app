package pl.ljw.aphorismapp.services

import io.ktor.features.*
import pl.ljw.aphorismapp.data.dtos.RatingAddDto
import pl.ljw.aphorismapp.data.dtos.RatingDto
import pl.ljw.aphorismapp.repositories.RatingRepository

interface RatingService {

    fun getByUser(username: String): List<RatingDto>

    fun getByAphorismId(id: Int): List<RatingDto>

    fun addRating(username: String, rating: RatingAddDto)
}

class RatingServiceImpl(
    private val ratingRepository: RatingRepository,
) : RatingService {

    override fun getByUser(username: String) =
        ratingRepository.getByUser(username) ?: throw NotFoundException()

    override fun getByAphorismId(id: Int) =
        ratingRepository.getByAphorismId(id) ?: throw NotFoundException()

    override fun addRating(username: String, rating: RatingAddDto) {
        ratingRepository.getByAphorismId(rating.aphorismId) ?:
            throw BadRequestException("Aphorism id does not exist.")
        if (rating.value !in 1..10) {
            throw BadRequestException("Rating value should be between 1 and 10.")
        }
        ratingRepository.addRating(username, rating)
    }
}
