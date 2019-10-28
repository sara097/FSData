package DataEntity

import DataEntity.Enum.Hand
import DataEntity.Enum.MeasureType
import DataEntity.Enum.Side

class Measurement(
    val type: MeasureType,
    val settings: Settings,
    val supervisor: String,
    val user: User,
    val hand: Hand,
    val aimDisplaying: List<Pair<Long, Side>>?,
    val timeSide: List<Pair<Long, Side>>,
    val tapCoordinatesLeft: List<Pair<Long, Coordinates>?>,
    val tapCoordinatesRight: List<Pair<Long, Coordinates>?>
    ) {

}