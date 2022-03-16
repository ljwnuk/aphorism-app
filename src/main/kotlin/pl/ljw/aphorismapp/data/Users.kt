package pl.ljw.aphorismapp.data

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val login: Column<String> = text("login")
    val password: Column<String> = text("password")
    override val primaryKey = PrimaryKey(login)
}
