package pl.ljw.aphorismapp.repositories

import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import pl.ljw.aphorismapp.data.Ratings
import pl.ljw.aphorismapp.data.Users
import pl.ljw.aphorismapp.data.dtos.UserActiveDto

interface UserRepository {

    fun getPasswordByLogin(login: String): String?

    fun getMostActiveRaters(quantity: Int): List<UserActiveDto>
}

class UserRepositoryImpl : UserRepository {

    override fun getPasswordByLogin(login: String) =
        transaction {
            Users
                .select { Users.login eq login }
                .map { it[Users.password] }
        }.firstOrNull()

    override fun getMostActiveRaters(quantity: Int) =
        transaction {
            Ratings
                .slice(Ratings.userLogin, Ratings.userLogin.count())
                .selectAll()
                .groupBy(Ratings.userLogin)
                .orderBy(Ratings.userLogin.count() to SortOrder.DESC)
                .limit(quantity)
                .map {
                    UserActiveDto(
                        it[Ratings.userLogin],
                        it[Ratings.userLogin.count()].toInt()
                    )
                }
        }
}
