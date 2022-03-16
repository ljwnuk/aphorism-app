package pl.ljw.aphorismapp

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

private const val PROPERTIES_RESOURCE_PATH = "src/main/resources/hikari.properties"

fun configureDatabase() {
    val hikariConfig = HikariConfig(PROPERTIES_RESOURCE_PATH)
    hikariConfig.transactionIsolation = "TRANSACTION_SERIALIZABLE"
    val dataSource = HikariDataSource(hikariConfig)
    Database.connect(dataSource)
}
