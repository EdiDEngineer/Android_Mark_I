/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.androidmarki.data.remote.firebase

import android.net.Uri
import androidx.lifecycle.LiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*

/**
 * This class observes the current FirebaseUser. If there is no logged in user, FirebaseUser will
 * be null.
 *
 * Note that onActive() and onInactive() will get triggered when the configuration changes (for
 * example when the device is rotated). This may be undesirable or expensive depending on the
 * nature of your LiveData object.
 */
class FirebaseUserAuthLiveData : LiveData<FirebaseUser?>() {
    private val firebaseAuth = FirebaseAuth.getInstance().apply {
        useAppLanguage()
    }
    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        value = firebaseAuth.currentUser
    }

    // When this object has an active observer, start observing the FirebaseAuth state to see if
    // there is currently a logged in user.
    override fun onActive() {
        firebaseAuth.addAuthStateListener(authStateListener)
    }

    // When this object no longer has an active observer, stop observing the FirebaseAuth state to
    // prevent memory leaks.
    override fun onInactive() {
        firebaseAuth.removeAuthStateListener(authStateListener)
    }

    //later
    val url = "http://www.mark-i-f8a94.firebaseapp.com/login" + firebaseAuth.currentUser?.uid

    private val actionCodeSettings = ActionCodeSettings.newBuilder()
        .setUrl(url)
        .setHandleCodeInApp(true)
        // The default for this is populated with the current android package name.
        .setAndroidPackageName("com.example.android.androidmarki", false, null)
        .build()

    fun signInWithGoogle(acct: GoogleSignInAccount) =
        firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(acct.idToken, null))

    fun createAccount(email: String, password: String) =
        firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun signIn(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    fun signOut() = firebaseAuth.signOut()
    fun resetPassword(emailAddress: String) =
        firebaseAuth.sendPasswordResetEmail(emailAddress)


    //move to corresponding repository
    fun updateProfile(name: String, photoUri: String) = firebaseAuth.currentUser!!.updateProfile(
        UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(Uri.parse(photoUri))
            .build()
    )

    fun deleteUser() = firebaseAuth.currentUser!!.delete()

    fun updateEmail(email: String) = firebaseAuth.currentUser!!.updateEmail(email)
    fun updatePassword(password: String) = firebaseAuth.currentUser!!.updatePassword(password)

}