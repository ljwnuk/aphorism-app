package pl.ljw.aphorismapp.data

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Aphorisms : Table() {
    val id: Column<Int> = integer("id")
    val userLogin: Column<String> = text("user_login").references(Users.login)
    val content: Column<String> = text("content")
    override val primaryKey = PrimaryKey(id)
}
