package com.example.inshortsnewsapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inshortsnewsapp.R
import com.example.inshortsnewsapp.presentation.InputType
import com.example.inshortsnewsapp.presentation.TextInput
import com.example.inshortsnewsapp.theme.Ivory
import com.example.inshortsnewsapp.theme.Poppins
import com.example.inshortsnewsapp.theme.Yellow

@Composable
fun SignUp(onSignUpButtonClick: (String, String) -> Unit) {
    var user by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ivory)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(
                id = R.string.create_account
            ),
            fontSize = 36.sp,
            fontFamily = Poppins,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            textAlign = TextAlign.Center
        )
        Divider(modifier = Modifier.padding(bottom = 24.dp))
        Text(
            text = stringResource(id = R.string.your_mail),
            modifier = Modifier.padding(start = 12.dp),
            fontFamily = Poppins
        )
        TextInput(inputType = InputType.Username) { user = it }
        Text(
            text = stringResource(id = R.string.your_password),
            modifier = Modifier.padding(start = 12.dp, top = 24.dp),
            fontFamily = Poppins
        )
        TextInput(inputType = InputType.Password) { password = it }
        Text(
            text = stringResource(id = R.string.repeat_password),
            modifier = Modifier.padding(start = 12.dp, top = 24.dp),
            fontFamily = Poppins
        )
        TextInput(inputType = InputType.Password) {
            passwordError = it != password
        }

        if (passwordError) {
            Text(
                text = stringResource(id = R.string.passwords_not_matching),
                color = Color.Red,
                fontFamily = Poppins,
                modifier = Modifier.padding(start = 12.dp),
                fontSize = 12.sp
            )
        }

        Divider(modifier = Modifier.padding(vertical = 32.dp))
        Button(
            onClick = { onSignUpButtonClick(user, password) },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Yellow),
            enabled = !passwordError
        ) {
            Text(text = stringResource(id = R.string.signup), Modifier.padding(vertical = 8.dp))
        }
    }
}