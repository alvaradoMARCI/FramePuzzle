package com.jhoel.framepuzzle.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jhoel.framepuzzle.core.domain.repository.UserRepository
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun OnboardingScreen(onDone: () -> Unit) {
    val userRepository: UserRepository = koinInject()
    val existingUser by userRepository.observeUser().collectAsStateWithLifecycle(initialValue = null)
    var name by remember { mutableStateOf("") }
    var isCreating by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(existingUser) {
        if (existingUser != null && !isCreating) {
            onDone()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "FramePuzzle",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Arma tus recuerdos",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(32.dp))
            Text(
                text = "¿Cómo te llamas?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Tu nombre") },
                singleLine = true,
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    isCreating = true
                    scope.launch {
                        userRepository.createUser(name)
                        onDone()
                    }
                },
                enabled = name.isNotBlank() && !isCreating,
                modifier = Modifier.height(52.dp),
            ) {
                Text("Empezar")
            }
        }
    }
}
