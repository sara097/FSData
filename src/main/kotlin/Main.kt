import DataEntity.Coordinates
import DataEntity.Enum.*
import DataEntity.Measurement
import DataEntity.Settings
import DataEntity.User
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import java.io.FileInputStream


fun main() {
    val serviceAccount = FileInputStream("C:\\Users\\User\\Desktop\\FSData\\fingertapping-3b183-dcecc93630a4.json")
    val credentials = GoogleCredentials.fromStream(serviceAccount)
    val options = FirebaseOptions.Builder()
        .setCredentials(credentials)
        .build()
    FirebaseApp.initializeApp(options)

    val db = FirestoreClient.getFirestore()

    val query = db.collection("measurements").get()

    val querySnapshot = query.get()

    val documents = querySnapshot.documents

    val users = mutableListOf<User>()
    val measurements = mutableListOf<Measurement>()
    for (document in documents) {
        val userData = document.data["user data"] as Map<String, String> //kk
        val settings = document.data["settings"] as Map<String, Long>
        val aimTime = document.data["aimTime"] as List<Long>
        val aimsCoordinates = document.data["aimsCoordinates"] as Map<String, String>
        val shownAim = document.data["shownAim"] as List<String>
        val sideValue = document.data["sideValue"] as List<String>
        val time = document.data["time"] as List<Long>
        val xL = document.data["xL"] as List<Double>
        val xR = document.data["xR"] as List<Double>
        val yL = document.data["yL"] as List<Double>
        val yR = document.data["yR"] as List<Double>

        val user = createUser(userData)

        val measurementType = when (settings["category"] as Long) {
            MeasureType.CLASSIC.id -> MeasureType.CLASSIC
            MeasureType.SYNCH.id -> MeasureType.SYNCH
            else -> MeasureType.RAND
        }
        val s = Settings(
            settings["time"] as Long * 1000,
            settings["interval"] as Long,
            Coordinates(aimsCoordinates["XL"] as Double, aimsCoordinates["YL"] as Double),
            Coordinates(aimsCoordinates["XR"] as Double, aimsCoordinates["YR"] as Double)
        )

        val hand = when (userData["hand"]) {
            Hand.LEFT.label -> Hand.LEFT
            else -> Hand.RIGHT
        }

        println(aimTime.size)
        println(shownAim.size)

        //czasów dwa razy wiecej, bo jak sie chowa to tez
        val aimsDisp = mutableListOf<Pair<Long, Side>>()
        var aa = -1
        if (measurementType != MeasureType.CLASSIC)
            aimTime.forEachIndexed { idx, s ->
                if (idx % 2 == 0) aa++
                println(aa)
                val side = when (shownAim[aa]) {
                    Side.LEFT.id -> Side.LEFT
                    Side.RIGHT.id -> Side.RIGHT
                    else -> Side.UP
                }
                aimsDisp.add(
                    s to side
                )
            }

        val sideTapped = mutableListOf<Pair<Long, Side>>()
        val tapCoordinatesLeft = mutableListOf<Pair<Long, Coordinates>?>()
        val tapCoordinatesRight = mutableListOf<Pair<Long, Coordinates>?>()

        time.forEachIndexed { idx, s ->
            val t = s
            val side = when (sideValue[idx]) {
                Side.LEFT.id -> Side.LEFT
                Side.RIGHT.id -> Side.RIGHT
                else -> Side.UP
            }

            val cR = Coordinates(xR[idx], yR[idx])
            val cL = Coordinates(xL[idx], yL[idx])

            sideTapped.add(t to side)
            if (side == Side.LEFT) tapCoordinatesLeft.add(t to cL)
            else if (side == Side.RIGHT) tapCoordinatesRight.add(t to cR)
            else {
                if (sideTapped[idx - 1].second == Side.LEFT) tapCoordinatesLeft.add(t to cL)
                else tapCoordinatesRight.add(t to cR)
            }
        }

        val measurement = Measurement(
            measurementType,
            s,
            userData["id"] ?: "",
            user,
            hand,
            if (measurementType == MeasureType.CLASSIC) null else aimsDisp,
            sideTapped,
            tapCoordinatesLeft,
            tapCoordinatesRight
            )
        measurements.add(measurement)
        users.add(user)
    }

    measurements.forEach {
        println(it.user)
        println(it.timeSide)
        println(it.aimDisplaying)
        println(it.tapCoordinatesLeft)
        println(it.tapCoordinatesRight)
    }

}

private fun createUser(userData: Map<String, String>): User {
    val user = User(
        userData["patientName"] ?: "",
        userData["patientLastName"] ?: "",
        when (userData["gender"]) {
            Gender.MALE.label -> Gender.MALE
            else -> Gender.FEMALE
        },
        Integer.valueOf(userData["age"] ?: "0"),
        when (userData["dominantHand"]) {
            Hand.LEFT.label2 -> Hand.LEFT
            else -> Hand.RIGHT
        },
        when (userData["group"]) {
            Group.PD.label -> Group.PD
            else -> Group.CONTROL
        },
        when (userData["tremor"]) {
            Tremor.BOTH.label -> Tremor.BOTH
            Tremor.LEFT.label -> Tremor.LEFT
            Tremor.RIGHT.label -> Tremor.RIGHT
            else -> Tremor.NONE
        },
        userData["description"]
    )
    return user
}