package pl.ljw.aphorismapp.repositories

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import pl.ljw.aphorismapp.data.Aphorisms
import pl.ljw.aphorismapp.data.dtos.AphorismAverageRatingDto
import pl.ljw.aphorismapp.data.dtos.AphorismDto
import pl.ljw.aphorismapp.data.dtos.AphorismPopularDto
import pl.ljw.aphorismapp.data.utils.execAndMap

interface AphorismRepository {

    fun getAll(): List<AphorismDto>

    fun getById(id: Int): AphorismDto?

    fun getByAuthor(author: String): List<AphorismDto>

    fun getRandom(quantity: Int): List<AphorismDto>

    fun getBest(quantity: Int): List<AphorismAverageRatingDto>

    fun getPopular(quantity: Int): List<AphorismPopularDto>

    fun addNew(username: String, text: String)
}

class AphorismRepositoryImpl : AphorismRepository {

    override fun getAll() = transaction {
        Aphorisms
            .selectAll()
            .map(::createAphorismDto)
    }

    override fun getById(id: Int) = transaction {
        Aphorisms
            .select { Aphorisms.id eq id }
            .map(::createAphorismDto)
            .firstOrNull()
    }

    override fun getByAuthor(author: String) = transaction {
        Aphorisms
            .select { Aphorisms.userLogin eq author }
            .map(::createAphorismDto)
    }

    override fun getRandom(quantity: Int) = transaction {
        Aphorisms
            .selectAll()
            .shuffled()
            .take(quantity)
            .map(::createAphorismDto)
    }

    private fun createAphorismDto(row: ResultRow) =
        AphorismDto(
            row[Aphorisms.id],
            row[Aphorisms.userLogin],
            row[Aphorisms.content]
        )

    private fun prepareGetBestSqlStatement(quantity: Int) =
        """
            SELECT id, user_login, content, average_rating
                FROM (
                    SELECT aphorism_id, AVG(value) AS average_rating
                        FROM ratings
                        GROUP BY aphorism_id
                        ORDER BY average_rating DESC
                        LIMIT $quantity
                ) average_ratings
                INNER JOIN aphorisms ON aphorism_id = id
                ORDER BY average_rating DESC
        """.trimIndent()

    override fun getBest(quantity: Int) =
        prepareGetBestSqlStatement(quantity).execAndMap {
            AphorismAverageRatingDto(
                it.getInt("id"),
                it.getString("user_login"),
                it.getString("content"),
                it.getDouble("average_rating")
            )
        }

    private fun prepareGetPopularSqlStatement(quantity: Int) =
        """
            SELECT id, user_login, content, rating_quantity
                FROM (
                    SELECT aphorism_id, COUNT(*) AS rating_quantity
                        FROM ratings
                        GROUP BY aphorism_id
                        ORDER BY rating_quantity DESC
                        LIMIT $quantity
                ) rating_quantities
                INNER JOIN aphorisms ON aphorism_id = id
                ORDER BY rating_quantity DESC;
        """.trimIndent()

    override fun getPopular(quantity: Int) =
        prepareGetPopularSqlStatement(quantity).execAndMap {
            AphorismPopularDto(
                it.getInt("id"),
                it.getString("user_login"),
                it.getString("content"),
                it.getInt("rating_quantity")
            )
        }

    override fun addNew(username: String, text: String) {
        transaction {
            Aphorisms.insert {
                it[userLogin] = username
                it[content] = text
            }
        }
    }
}
