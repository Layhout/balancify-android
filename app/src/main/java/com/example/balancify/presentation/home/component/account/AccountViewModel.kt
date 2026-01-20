package com.example.balancify.presentation.home.component.account

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.balancify.R
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _state = MutableStateFlow(AccountState())
    val state = _state
        .onStart { loadData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = AccountState()
        )

    private fun loadData() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoggedIn = auth.currentUser != null
                )
            }
        }
    }

    private fun handleSignIn(credential: Credential) {
        // Check if credential is of type Google ID
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w(TAG, "Credential is not of type Google ID!")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->

                println("task.isSuccessful ${task.isSuccessful}")
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
////                    Log.d(TAG, "signInWithCredential:success")
////                    val user = auth.currentUser
////                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user
////                    Log.w(TAG, "signInWithCredential:failure", task.exception)
////                    updateUI(null)
//                }
            }

    }

    suspend fun onLoginClick(context: Context) {
        try {
            val option =
                GetSignInWithGoogleOption.Builder(
                    context.getString(R.string.default_web_client_id)
                )
                    .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(option)
                .build()
            
            val result = CredentialManager.create(context).getCredential(
                request = request,
                context = context
            )

            handleSignIn(result.credential)
        } catch (e: GetCredentialException) {
            Log.d(TAG, e.message.orEmpty())
        }

    }

    suspend fun signOut(context: Context) {
        // Firebase sign out
        auth.signOut()

        // When a user signs out, clear the current user credential state from all credential providers.
        try {
            val clearRequest = ClearCredentialStateRequest()
            CredentialManager.create(context).clearCredentialState(clearRequest)
        } catch (e: ClearCredentialException) {
            Log.e(TAG, "Couldn't clear user credentials: ${e.localizedMessage}")
        }
    }
}