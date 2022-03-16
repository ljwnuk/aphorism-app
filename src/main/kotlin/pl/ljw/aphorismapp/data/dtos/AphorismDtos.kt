package pl.ljw.aphorismapp.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AphorismDto(
    val id: Int,
    val author: String,
    val content: String
)

@Serializable
data class AphorismAverageRatingDto(
    val id: Int,
    val author: String,
    val content: String,
    val averageRating: Double
)

@Serializable
data class AphorismPopularDto(
    val id: Int,
    val author: String,
    val content: String,
    val ratingQuantity: Int
)

@Serializable
data class AphorismAddDto(
    val content: String
)
