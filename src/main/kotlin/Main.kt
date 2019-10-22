import DataEntity.Enum.Gender
import DataEntity.Enum.Group
import DataEntity.Enum.Hand
import DataEntity.Enum.Tremor
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
        for (document in documents) {
            val userData = document.data["user data"] as Map<String, String>

            val user = User(
                userData["patientName"] ?: "",
                userData["patientLastName"] ?:"",
                when(userData["gender"]){
                    Gender.MALE.label -> Gender.MALE
                    else -> Gender.FEMALE
                },
                Integer.valueOf(userData["age"]?:"0"),
                when(userData["dominantHand"]){
                    Hand.LEFT.label2 -> Hand.LEFT
                    else -> Hand.RIGHT
                },
                when(userData["group"]){
                    Group.PD.label -> Group.PD
                    else -> Group.CONTROL
                },
                when(userData["tremor"]){
                    Tremor.BOTH.label -> Tremor.BOTH
                    Tremor.LEFT.label -> Tremor.LEFT
                    Tremor.RIGHT.label -> Tremor.RIGHT
                    else -> Tremor.NONE
                },
                userData["description"]
            )
            users.add(user)
    }

    users.forEach{println(it.firstName+it.group)}

}