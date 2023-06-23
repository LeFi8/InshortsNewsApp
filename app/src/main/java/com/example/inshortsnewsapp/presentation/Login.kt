package com.example.inshortsnewsapp.presentation

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.inshortsnewsapp.R
import com.example.inshortsnewsapp.theme.EmperorDark
import com.example.inshortsnewsapp.theme.Ivory
import com.example.inshortsnewsapp.theme.Poppins
import com.example.inshortsnewsapp.theme.Yellow
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun Login(onLoginClick: (String, String) -> Unit, onNoAccountClick: () -> Unit, onGoogleLoginClick: () -> Unit) {
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ivory),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.welcome), fontSize = 48.sp, fontFamily = Poppins)
            LottieIcon(animationRes = R.raw.lock_lottie)
            TextInput(inputType = InputType.Username) { user = it }
            TextInput(inputType = InputType.Password) { password = it }
            Button(
                onClick = { onLoginClick(user, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Yellow)
            ) {
                Text(text = stringResource(id = R.string.login), Modifier.padding(vertical = 8.dp))
            }
            DividerWithText(stringResource(id = R.string.or))

            Button(
                onClick = onGoogleLoginClick,
                modifier = Modifier
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = EmperorDark
                ),
                border = BorderStroke(1.dp, color = EmperorDark)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google_logo),
                    contentDescription = stringResource(id = R.string.google_logo),
                    modifier = Modifier
                        .height(24.dp)
                        .width(24.dp)
                )
                Text(
                    text = stringResource(id = R.string.login_w_google),
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(id = R.string.dont_have_account), color = EmperorDark)
                TextButton(onClick = onNoAccountClick) {
                    Text(text = stringResource(id = R.string.signup), color = Yellow)
                }
            }
        }
    }
}

@Composable
fun DividerWithText(text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier = Modifier.weight(1f))
        Text(
            text = text,
            color = EmperorDark,
            modifier = Modifier.padding(horizontal = 8.dp),
            fontSize = 12.sp
        )
        Divider(modifier = Modifier.weight(1f))
    }
}

@Composable
fun LottieIcon(animationRes: Int,
               iconSize: Dp = 192.dp,
               repeatCount: Int = LottieConstants.IterateForever
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(animationRes)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = repeatCount,
        restartOnPlay = true
    )
    LottieAnimation(composition = composition, progress = progress, Modifier.size(iconSize))
}

sealed class InputType (
    val labelId: Int,
    val icon: ImageVector,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object Username : InputType(
        labelId = R.string.email,
        icon = Icons.Default.Person,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None
    )

    object Password : InputType(
        labelId = R.string.password,
        icon = Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(inputType: InputType, onValueChange: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    TextField(
        value = value,
        onValueChange = {
            value = it
            onValueChange(it)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        leadingIcon = { Icon(inputType.icon, contentDescription = "Icon") },
        label = { Text(text = stringResource(id = inputType.labelId)) },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        ),
        keyboardOptions = inputType.keyboardOptions,
        visualTransformation = inputType.visualTransformation
    )
}

//log in with google
@Composable
fun rememberFirebaseGoogleAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}