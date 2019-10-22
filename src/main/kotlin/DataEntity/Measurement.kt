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
    val aimDisplaying: List<Pair<String, Int>>?,
    val timeOnSide: List<Pair<Side, Int>>,
    val tapCoordinatesLeft: List<Pair<Int, Coordinates>?>,
    val tapCoordinatesRight: List<Pair<Int, Coordinates>?>
    ) {

}