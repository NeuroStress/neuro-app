// app/src/main/java/com/example/stressmonitor/ui/screens/HomeScreen.kt
package com.example.stressmonitor.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stressmonitor.domain.model.StressLevel
import com.example.stressmonitor.ui.viewmodel.StressViewModel

@Composable
fun HomeScreen(
    viewModel: StressViewModel = viewModel()
) {
    val stressLevel by viewModel.stressLevel.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchStressLevel()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Монитор стресса",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator()
                }
                error != null -> {
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = { viewModel.fetchStressLevel() }
                    ) {
                        Text("Повторить")
                    }
                }
                stressLevel != null -> {
                    StressLevelCard(stressLevel!!)
                }
            }
        }
    }
}

@Composable
fun StressLevelCard(stressLevel: StressLevel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Уровень стресса:",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when (stressLevel) {
                    StressLevel.CALM -> "Спокойствие"
                    StressLevel.LIGHT_STRESS -> "Легкий стресс"
                    StressLevel.HIGH_STRESS -> "Высокий стресс"
                    StressLevel.FATIGUE -> "Усталость"
                    StressLevel.EXCITEMENT -> "Возбуждение"
                },
                style = MaterialTheme.typography.headlineSmall,
                color = when (stressLevel) {
                    StressLevel.CALM -> MaterialTheme.colorScheme.primary
                    StressLevel.LIGHT_STRESS -> MaterialTheme.colorScheme.secondary
                    StressLevel.HIGH_STRESS -> MaterialTheme.colorScheme.error
                    StressLevel.FATIGUE -> MaterialTheme.colorScheme.tertiary
                    StressLevel.EXCITEMENT -> MaterialTheme.colorScheme.primary
                }
            )
        }
    }
}
