package pl.ljw.aphorismapp.repositories

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import pl.ljw.aphorismapp.data.Aphorisms
import pl.ljw.aphorismapp.data.Ratings
import pl.ljw.aphorismapp.data.Users
import pl.ljw.aphorismapp.data.dtos.AphorismDto
import pl.ljw.aphorismapp.data.dtos.RatingAddDto
import pl.ljw.aphorismapp.data.dtos.RatingDto
import pl.ljw.aphorismapp.data.utils.execAndMap
import java.sql.ResultSet

interface RatingRepository {

    fun getByUser(username: String): List<RatingDto>?

    fun getByAphorismId(id: Int): List<RatingDto>?

    fun addRating(username: String, rating: RatingAddDto)
}

class RatingRepositoryImpl : RatingRepository {

    private fun prepareGetByUserSqlStatement(username: String) =
        """
            SELECT id, aphorisms.user_login AS author, content,
                ratings_by_user.user_login AS rater, value, comment
                FROM aphorisms
                INNER JOIN (
                    SELECT *
                        FROM ratings
                        WHERE user_login = '$username'
                ) ratings_by_user ON aphorism_id = id
        """.trimIndent()

    override fun getByUser(username: String): List<RatingDto>? {
        transaction {
            Users
                .select { Users.login eq username }
                .firstOrNull()
        } ?: return null
        return prepareGetByUserSqlStatement(username).execAndMap(::createRatingDto)
    }

    private fun prepareGetByAphorismIdSqlStatement(id: Int) =
        """
            SELECT id, aphorisms.user_login AS author, content,
                ratings_by_user.user_login AS rater, value, comment
                FROM aphorisms
                INNER JOIN (
                    SELECT *
                        FROM ratings
                        WHERE aphorism_id = $id
                ) ratings_by_user ON aphorism_id = id
        """.trimIndent()

    override fun getByAphorismId(id: Int): List<RatingDto>? {
        transaction {
            Aphorisms
                .select { Aphorisms.id eq id }
                .firstOrNull()
        } ?: return null
        return prepareGetByAphorismIdSqlStatement(id).execAndMap(::createRatingDto)
    }

    private fun createRatingDto(resultSet: ResultSet) =
        RatingDto(
            AphorismDto(
                resultSet.getInt("id"),
                resultSet.getString("author"),
                resultSet.getString("content")
            ),
            resultSet.getString("rater"),
            resultSet.getInt("value"),
            resultSet.getString("comment")
        )

    override fun addRating(username: String, rating: RatingAddDto) {
        transaction {
            Ratings
                .insert {
                    it[aphorismId] = rating.aphorismId
                    it[userLogin] = username
                    it[value] = rating.value
                    it[comment] = rating.comment
                }
        }
    }
}
