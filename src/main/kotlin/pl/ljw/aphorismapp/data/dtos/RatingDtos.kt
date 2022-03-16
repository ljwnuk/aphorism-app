package pl.ljw.aphorismapp.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class RatingDto(
    val aphorism: AphorismDto,
    val rater: String,
    val rating: Int,
    val comment: String
)

@Serializable
data class RatingAddDto(
    val aphorismId: Int,
    val value: Int,
    val comment: String
)
