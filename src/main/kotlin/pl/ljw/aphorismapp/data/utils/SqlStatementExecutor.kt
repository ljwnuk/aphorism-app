package pl.ljw.aphorismapp.data.utils

import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.ResultSet

fun <T> String.execAndMap(transform: (ResultSet) -> T): List<T> {
    val result = mutableListOf<T>()
    transaction {
        TransactionManager
            .current()
            .exec(this@execAndMap) {
                while(it.next()) {
                    result += transform(it)
                }
            }
    }
    return result
}
