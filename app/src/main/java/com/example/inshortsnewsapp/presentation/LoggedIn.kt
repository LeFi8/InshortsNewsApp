package com.example.inshortsnewsapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.inshortsnewsapp.R
import com.example.inshortsnewsapp.theme.Ivory
import com.example.inshortsnewsapp.theme.Yellow

@Composable
fun LoggedIn(onLogoutClick: () -> Unit, userInfo: String) {
    Column(
        modifier = Modifier
            .background(Ivory)
            .padding(24.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.logged_in_as))
        Text(text = userInfo)
        Button(onClick = onLogoutClick, colors = ButtonDefaults.buttonColors(containerColor = Yellow)) {
            Text(text = "Log out")
        }
    }
}