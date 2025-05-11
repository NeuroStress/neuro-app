// app/src/main/java/com/example/stressmonitor/ui/screens/StatisticsScreen.kt
package com.example.stressmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatisticsScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Статистика",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Плейсхолдер-график
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("График здесь")
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Плейсхолдер периода
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { /* TODO: фильтр День */ }) { Text("День") }
                TextButton(onClick = { /* TODO: фильтр Месяц */ }) { Text("Месяц") }
                TextButton(onClick = { /* TODO: фильтр Год */ }) { Text("Год") }
            }
        }
    }
}
