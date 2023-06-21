package com.example.inshortsnewsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.inshortsnewsapp.firebase.FirebaseManager
import com.example.inshortsnewsapp.presentation.Login
import com.example.inshortsnewsapp.presentation.ScreenState
import com.example.inshortsnewsapp.presentation.SignUp
import com.example.inshortsnewsapp.presentation.rememberFirebaseGoogleAuthLauncher
import com.example.inshortsnewsapp.theme.InshortsNewsAppTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    private val authenticationService = FirebaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val screenState = remember { mutableStateOf(ScreenState.LOGIN) }
            var user by remember { mutableStateOf(Firebase.auth.currentUser) }
            val launcher = rememberFirebaseGoogleAuthLauncher(
                onAuthComplete = { result ->
                    user = result.user
                    Toast.makeText(
                        this,
                        getString(R.string.google_login_successful),
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            MainActivity::class.java
                        )
                    )
                    finish()
                },
                onAuthError = {
                    user = null
                }
            )

            InshortsNewsAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BackHandler(
                        enabled = (screenState.value == ScreenState.SIGNUP)
                    ) {
                        screenState.value = ScreenState.LOGIN
                    }

                    when (screenState.value) {
                        ScreenState.LOGIN -> {
                            Login(
                                onLoginClick = { email, password ->
                                    lifecycleScope.launch {
                                        val loginSuccess = authenticationService.login(
                                            email, password, this@LoginActivity
                                        )
                                        if (loginSuccess) {
                                            startActivity(
                                                Intent(
                                                    this@LoginActivity,
                                                    MainActivity::class.java
                                                )
                                            )
                                            finish()
                                        }
                                    }
                                },
                                onNoAccountClick = {
                                    screenState.value = ScreenState.SIGNUP
                                },
                                onGoogleLoginClick = {
                                    val googleSignInClient = GoogleSignIn.getClient(
                                        applicationContext,
                                        authenticationService.googleSignInOptionsBuilder(this)
                                    )
                                    launcher.launch(googleSignInClient.signInIntent)
                                }
                            )
                        }

                        ScreenState.SIGNUP -> {
                            SignUp(
                                onSignUpButtonClick = { email, password ->
                                    lifecycleScope.launch {
                                        val registrationSuccessful = authenticationService.register(
                                            email,
                                            password,
                                            this@LoginActivity
                                        )
                                        if (registrationSuccessful) {
                                            startActivity(
                                                Intent(
                                                    this@LoginActivity,
                                                    MainActivity::class.java
                                                )
                                            )
                                            finish()
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
