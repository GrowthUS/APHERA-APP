package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.TealPrimary
import com.example.ui.viewmodel.SkinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineTrackerScreen(
    viewModel: SkinViewModel,
    onNavigateBack: () -> Unit
) {
    val latestScan by viewModel.latestScan.collectAsState()
    val todayChecks by viewModel.todayRoutineChecks.collectAsState()

    val morningSteps = if (latestScan != null) {
        viewModel.parseJsonList(latestScan!!.morningRoutineJson)
    } else {
        listOf(
            "1. Limpiador gel suave pH 5.5",
            "2. Sérum con Niacinamida / Vitamina C",
            "3. Crema hidratante ligera sin aceites",
            "4. Protector solar SPF 50+ diario"
        )
    }

    val nightSteps = if (latestScan != null) {
        viewModel.parseJsonList(latestScan!!.nightRoutineJson)
    } else {
        listOf(
            "1. Doble limpieza (aceite limpiador + agua micelar)",
            "2. Tónico calmante reequilibrante",
            "3. Sérum reparador con Ceramidas",
            "4. Crema nutritiva nocturna"
        )
    }

    val totalSteps = morningSteps.size + nightSteps.size
    val completedCount = todayChecks.size
    val progressFraction = if (totalSteps > 0) completedCount.toFloat() / totalSteps else 0f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Rutina Diaria", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Progress Header
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Progreso de Hoy",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                            Text(
                                text = "$completedCount / $totalSteps completados",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                        LinearProgressIndicator(
                            progress = { progressFraction },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Morning Routine Section
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.WbSunny, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(text = "Mañana (AM)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }

            itemsIndexed(morningSteps) { index, step ->
                val stepId = "am_$index"
                val isChecked = todayChecks.any { it.stepId == stepId }

                RoutineCheckCard(
                    stepId = stepId,
                    stepText = step,
                    isChecked = isChecked,
                    onToggle = { checked -> viewModel.toggleRoutineCheck(stepId, checked) }
                )
            }

            // Night Routine Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.NightsStay, contentDescription = null, tint = TealPrimary)
                    Text(text = "Noche (PM)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }

            itemsIndexed(nightSteps) { index, step ->
                val stepId = "pm_$index"
                val isChecked = todayChecks.any { it.stepId == stepId }

                RoutineCheckCard(
                    stepId = stepId,
                    stepText = step,
                    isChecked = isChecked,
                    onToggle = { checked -> viewModel.toggleRoutineCheck(stepId, checked) }
                )
            }
        }
    }
}

@Composable
private fun RoutineCheckCard(
    stepId: String,
    stepText: String,
    isChecked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Card(
        onClick = { onToggle(!isChecked) },
        modifier = Modifier
            .fillMaxWidth()
            .testTag("routine_check_$stepId"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onToggle(it) }
            )
            Text(
                text = stepText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Medium
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
