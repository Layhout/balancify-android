package com.example.balancify.service

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import com.example.balancify.R
import com.example.balancify.core.constant.BG_COLORS
import com.example.balancify.domain.model.UserModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import io.viascom.nanoid.NanoId
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

data class AuthResult(
    val successful: Boolean = false,
    val isNewUser: Boolean = false,
    val userId: String? = null,
    val user: UserModel? = null
)

class AuthService {
    private val auth: FirebaseAuth = Firebase.auth

    val isLoggedIn: Boolean
        get() = auth.currentUser != null

    val userId: String
        get() = auth.currentUser?.uid ?: ""

    suspend fun signInWithBottomSheet(context: Context): AuthResult {
        try {
            val option = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .build()

            val credentialResponse = createCredential(context, option)

            return signInWithGoogle(context, credentialResponse.credential)
        } catch (e: Exception) {
            Log.w(TAG, e.message.orEmpty())
            return AuthResult()
        }
    }

    suspend fun signInWithDialog(context: Context): AuthResult {
        try {
            val option =
                GetSignInWithGoogleOption.Builder(
                    context.getString(R.string.default_web_client_id)
                )
                    .build()

            val credentialResponse = createCredential(context, option)
            return signInWithGoogle(context, credentialResponse.credential)
        } catch (e: Exception) {
            Log.w(TAG, e.message.orEmpty())
            return AuthResult()
        }
    }

    suspend fun signOut(context: Context): Boolean {
        try {
            auth.signOut()

            val clearRequest = ClearCredentialStateRequest()
            CredentialManager.create(context).clearCredentialState(clearRequest)

            return true
        } catch (e: ClearCredentialException) {
            Toast.makeText(
                context,
                "Couldn't clear user credentials: ${e.localizedMessage}",
                Toast.LENGTH_LONG
            )
                .show()

            return false
        }
    }

    private suspend fun createCredential(
        context: Context,
        option: CredentialOption
    ): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(option)
            .build()

        return CredentialManager.create(context).getCredential(
            request = request,
            context = context
        )
    }

    private suspend fun signInWithGoogle(
        context: Context,
        credential: Credential
    ): AuthResult {
        if (credential !is CustomCredential || credential.type != TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            Toast.makeText(context, "Credential is not of type Google ID!", Toast.LENGTH_LONG)
                .show()
            return AuthResult()
        }

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        val firebaseCredential =
            GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

        return try {
            val signInResult = auth.signInWithCredential(firebaseCredential).await()
            AuthResult(
                successful = true,
                userId = signInResult.user?.uid,
                isNewUser = signInResult.additionalUserInfo?.isNewUser ?: false,
                user = if (signInResult.additionalUserInfo?.isNewUser ?: false) UserModel(
                    documentId = signInResult.user?.uid.orEmpty(),
                    id = signInResult.user?.uid.orEmpty(),
                    email = signInResult.user?.email.orEmpty(),
                    imageUrl = signInResult.user?.photoUrl.toString(),
                    name = signInResult.user?.displayName.orEmpty(),
                    profileBgColor = BG_COLORS[Random.nextInt(BG_COLORS.size)],
                    referralCode = NanoId.generate(9)
                ) else null
            )
        } catch (e: Exception) {
            Toast.makeText(context, e.message.orEmpty(), Toast.LENGTH_LONG).show()
            AuthResult()
        }
    }
}
