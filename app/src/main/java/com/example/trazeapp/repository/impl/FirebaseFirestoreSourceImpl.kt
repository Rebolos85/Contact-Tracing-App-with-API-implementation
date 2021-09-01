import com.example.trazeapp.data.model.SingleLiveEvent
import com.example.trazeapp.data.model.User
import com.example.trazeapp.repository.source.ProfileSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FirebaseFirestoreSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,

    ) : ProfileSource {
    val _hasPhoneNumber = SingleLiveEvent<String?>()
//    val hasNoPhoneNumberExist = SingleLiveEvent<Boolean?>()

    companion object {
        const val USERS_COLLECTION = "Users"
    }

    private val userReference = firestore.collection(USERS_COLLECTION)

    override suspend fun createUserRecord(user: User) {
        // kanang document nga paramater para userID siya like primary key
        userReference.document(user.emailUserId).set(user).await()

    }

    override suspend fun getUserRecord(
        userId: String,
    ): User {
        // kuhaon ang userID
        return userReference.document(userId).get().await().toObject()!!
    }

    override suspend fun getUserPhoneAuthId(phoneNumberId: String): User {
        return userReference.document(phoneNumberId).get().await().toObject()!!
    }

    override suspend fun checkUserPhoneNumberExist(phoneNumberInput: String): QuerySnapshot {
//        var hasUserPhoneNumber: Boolean? = null
//        var query: QueryDocumentSnapshot? = null
        //            .addOnCompleteListener { phoneNumberResult ->
//
////                hasPhoneNumber = phoneNumberResult
//                if (phoneNumberResult.isSuccessful) {
//                    for (documentSnapshot in phoneNumberResult.result!!) {
//                        val phoneNumber = documentSnapshot.getString("phoneNumber")
//                        if (phoneNumber == phoneNumberInput) {
//
//                            _hasPhoneNumber.value =
//                                "Please enter another phone number because the phone number you entered is already use by different account"
//
//                        }
//
//                        if (phoneNumberResult.result!!.isEmpty || phoneNumberResult.result!!.size() == 0) {
////                            hasUserPhoneNumber = true
//                            Log.d("MainActivity", "FALSE")
//
//                        }
//
//                    }
//
//
//                }
//
//            }
        return userReference.whereEqualTo("phoneNumber", phoneNumberInput).get().await()

    }

    override fun getPhoneExistMessage(): SingleLiveEvent<String?> {
       return _hasPhoneNumber
    }
}