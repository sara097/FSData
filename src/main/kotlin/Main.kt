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

    for (document in documents) println("User: " + document.id)

}