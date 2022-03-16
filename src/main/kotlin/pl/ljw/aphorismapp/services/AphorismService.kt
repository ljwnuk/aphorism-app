package pl.ljw.aphorismapp.services

import io.ktor.features.*
import pl.ljw.aphorismapp.data.dtos.AphorismAverageRatingDto
import pl.ljw.aphorismapp.data.dtos.AphorismDto
import pl.ljw.aphorismapp.data.dtos.AphorismPopularDto
import pl.ljw.aphorismapp.repositories.AphorismRepository

interface AphorismService {

    fun getAll(): List<AphorismDto>

    fun getById(id: Int): AphorismDto

    fun getByAuthor(author: String): List<AphorismDto>

    fun getRandom(quantity: Int): List<AphorismDto>

    fun getBest(quantity: Int): List<AphorismAverageRatingDto>

    fun getPopular(quantity: Int): List<AphorismPopularDto>

    fun addNew(username: String, text: String)
}

class AphorismServiceImpl(
    private val aphorismRepository: AphorismRepository
) : AphorismService {

    override fun getAll() = aphorismRepository.getAll()

    override fun getById(id: Int) =
        aphorismRepository.getById(id) ?: throw NotFoundException()

    override fun getByAuthor(author: String) = aphorismRepository.getByAuthor(author)

    private fun <T> invokeIfQuantityNotNegative(quantity: Int, invocable: (Int) -> T) =
        if (quantity < 0)
            throw BadRequestException("Aphorism quantity should be positive.")
        else
            invocable(quantity)

    override fun getRandom(quantity: Int) =
        invokeIfQuantityNotNegative(quantity, aphorismRepository::getRandom)

    override fun getBest(quantity: Int) =
        invokeIfQuantityNotNegative(quantity, aphorismRepository::getBest)

    override fun getPopular(quantity: Int) =
        invokeIfQuantityNotNegative(quantity, aphorismRepository::getPopular)

    override fun addNew(username: String, text: String) =
        aphorismRepository.addNew(username, text)
}
