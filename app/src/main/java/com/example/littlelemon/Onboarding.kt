package com.example.littlelemon

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable

@Composable
fun Onboarding(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.fillMaxWidth().height(100.dp).padding(20.dp)
        )

        Text(
            text = "Let's get to know you",
            fontSize = 24.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().background(Color(0xFF495E57)).padding(vertical = 40.dp)
        )

        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Text(text = "Personal information", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
                        Toast.makeText(context, "Registration unsuccessful. Please enter all data.", Toast.LENGTH_SHORT).show()
                    } else {
                        sharedPreferences.edit()
                            .putString("firstName", firstName)
                            .putString("lastName", lastName)
                            .putString("email", email)
                            .putBoolean("userRegistered", true)
                            .apply()

                        Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()

                        // Navigate to Home and clear backstack so user can't go back to Onboarding
                        navController.navigate(Home.route) {
                            popUpTo(Onboarding.route) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4CE14))
            ) {
                Text("Register", color = Color.Black)
            }
        }
    }
}