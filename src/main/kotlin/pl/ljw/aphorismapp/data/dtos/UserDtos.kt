package pl.ljw.aphorismapp.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class UserActiveDto(
    val username: String,
    val commentQuantity: Int
)
