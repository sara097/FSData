package DataEntity

import DataEntity.Enum.Gender
import DataEntity.Enum.Group
import DataEntity.Enum.Hand
import DataEntity.Enum.Tremor

class User(
    val firstName: String,
    val lastName: String,
    val gender: Gender,
    val Age: Int,
    val dominantHand: Hand,
    val group: Group,
    val tremor: Tremor,
    val description: String?
) {
}