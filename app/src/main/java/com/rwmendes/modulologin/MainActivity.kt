package com.rwmendes.modulologin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rwmendes.modulologin.ui.theme.ModuloLoginTheme
import com.rwmendes.modulodelogin.ui.login.LoginActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ModuloLoginTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android") {
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, onLoginClick: () -> Unit) {
    Button(onClick = onLoginClick) {
        Text(text = "Hello $name! Click here to log in.")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ModuloLoginTheme {
        Greeting("Android") {}
    }
}
