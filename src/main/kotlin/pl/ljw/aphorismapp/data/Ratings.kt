package pl.ljw.aphorismapp.data

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Ratings : Table() {
    val aphorismId: Column<Int> = integer("aphorism_id").references(Aphorisms.id)
    val userLogin: Column<String> = text("user_login").references(Users.login)
    val value: Column<Int> = integer("value")
    val comment: Column<String> = text("comment")
}
