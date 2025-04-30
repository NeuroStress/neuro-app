// app/src/main/java/com/example/stressmonitor/MainActivity.kt
package com.example.stressmonitor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView
import com.example.stressmonitor.ui.screens.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<ComposeView>(R.id.compose_view).setContent {
            HomeScreen()
        }
    }
}
