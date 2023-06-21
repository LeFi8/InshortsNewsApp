package com.example.inshortsnewsapp

import android.content.SharedPreferences
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
import androidx.preference.PreferenceManager
import com.example.inshortsnewsapp.authentication.FirebaseManager
import com.example.inshortsnewsapp.presentation.LoggedIn
import com.example.inshortsnewsapp.presentation.Login
import com.example.inshortsnewsapp.presentation.ScreenState
import com.example.inshortsnewsapp.presentation.SignUp
import com.example.inshortsnewsapp.presentation.rememberFirebaseGoogleAuthLauncher
import com.example.inshortsnewsapp.theme.LoginJetpackComposeTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    private val authenticationService = FirebaseManager()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        setContent {
            val isLoggedIn = remember {
                mutableStateOf(
                    sharedPreferences.getBoolean(getString(R.string.is_logged_in), false)
                )
            }
            val screenState =
                remember { mutableStateOf(if (isLoggedIn.value) ScreenState.LOGGED_IN else ScreenState.LOGIN) }
            var user by remember { mutableStateOf(Firebase.auth.currentUser) }
            val launcher = rememberFirebaseGoogleAuthLauncher(
                onAuthComplete = { result ->
                    user = result.user
                    isLoggedIn.value = true
                    screenState.value = ScreenState.LOGGED_IN
                    Toast.makeText(
                        this,
                        getString(R.string.google_login_successful),
                        Toast.LENGTH_SHORT
                    ).show()

                    sharedPreferences.edit().putBoolean(
                        getString(R.string.is_logged_in), true
                    ).apply()
                },
                onAuthError = {
                    user = null
                }
            )

            LoginJetpackComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BackHandler(
                        enabled = (screenState.value == ScreenState.SIGNUP) &&
                                (screenState.value != ScreenState.LOGIN)
                    ) {
                        screenState.value = ScreenState.LOGIN
                    }

                    when (screenState.value) {
                        ScreenState.LOGIN -> {
                            Login(
                                onLoginClick = { email, password ->
                                    lifecycleScope.launch {
                                        isLoggedIn.value = authenticationService.login(
                                            email, password, this@LoginActivity
                                        )
                                        if (isLoggedIn.value) {
                                            screenState.value = ScreenState.LOGGED_IN
                                            sharedPreferences.edit().putBoolean(
                                                getString(R.string.is_logged_in), true
                                            ).apply()
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
                                        if (registrationSuccessful)
                                            screenState.value = ScreenState.LOGIN
                                    }
                                }
                            )
                        }

                        ScreenState.LOGGED_IN -> {
                            LoggedIn(
                                onLogoutClick = {
                                    isLoggedIn.value = false
                                    authenticationService.logout(this)
                                    screenState.value = ScreenState.LOGIN

                                    sharedPreferences.edit().putBoolean(
                                        getString(R.string.is_logged_in), false
                                    ).apply()
                                },
                                authenticationService.getUserMail()
                            )
                        }
                    }
                }
            }
        }
    }
}
