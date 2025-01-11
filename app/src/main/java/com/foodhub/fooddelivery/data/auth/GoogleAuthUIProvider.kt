package com.foodhub.fooddelivery.data.auth

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.foodhub.fooddelivery.GoogleServerClientId
import com.foodhub.fooddelivery.data.models.GoogleAccount
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleAuthUIProvider {
    suspend fun signIn(
        activityContext: Context,
        credentialManager: CredentialManager
    ) : GoogleAccount {
        val creds = credentialManager.getCredential(
            activityContext,
            getCredentialRequest()
        ).credential

        return handleCredentials(creds)
    }

    fun handleCredentials(credential: Credential) : GoogleAccount {
        when {
            credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                val googleIdTokenCredential = credential as GoogleIdTokenCredential
                Log.d("GoogleAuth", googleIdTokenCredential.toString())
                return GoogleAccount(
                    token = googleIdTokenCredential.idToken,
                    displayName = googleIdTokenCredential.displayName ?: "",
                    profileImageUrl = googleIdTokenCredential.profilePictureUri.toString()
                )
            }
            else -> {
                throw IllegalStateException("Invalid credential type")
            }
        }
    }

    fun getCredentialRequest() : GetCredentialRequest {
        return GetCredentialRequest.Builder()
            .addCredentialOption(
                GetSignInWithGoogleOption.Builder(
                    GoogleServerClientId
                ).build()
            ).build()
    }
}