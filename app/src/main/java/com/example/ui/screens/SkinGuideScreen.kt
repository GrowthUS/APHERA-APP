package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.RoseContainer
import com.example.ui.theme.TealContainer

data class SkinTypeInfo(
    val name: String,
    val description: String,
    val keyNeeds: String,
    val recommendedIngredients: String
)

data class IngredientInfo(
    val name: String,
    val benefit: String,
    val bestFor: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkinGuideScreen(
    onNavigateBack: () -> Unit
) {
    val skinTypes = listOf(
        SkinTypeInfo(
            name = "Piel Grasa",
            description = "Producción excesiva de sebo en todo el rostro, brillo característico y poros más perceptibles.",
            keyNeeds = "Seborregulación, limpieza profunda no destructiva y fotoprotección oil-free.",
            recommendedIngredients = "Niacinamida, Ácido Salicílico (BHA), Zinc PCA, Té Verde."
        ),
        SkinTypeInfo(
            name = "Piel Seca",
            description = "Falta de lípidos naturales y humedad, tirantez frecuente y textura fina.",
            keyNeeds = "Nutrición intensa, restauración de la barrera cutánea lipídica y sellado de humedad.",
            recommendedIngredients = "Ceramidas, Ácido Hialurónico, Escualano, Manteca de Karité."
        ),
        SkinTypeInfo(
            name = "Piel Mixta",
            description = "Zona T (frente, nariz, barbilla) con tendencia a grasa o poros dilatados y mejillas equilibradas o secas.",
            keyNeeds = "Equilibrio zonal, hidratación ligera en mejillas y control de sebo en centro facial.",
            recommendedIngredients = "Niacinamida al 5%, Ácido Hialurónico, Centella Asiática."
        ),
        SkinTypeInfo(
            name = "Piel Sensible",
            description = "Reactividad elevada frente a cambios climáticos, cosméticos o fricción, con enrojecimiento puntual.",
            keyNeeds = "Calmar la inflamación, fórmulas hipoalergénicas sin fragancia y barrera protectora.",
            recommendedIngredients = "Pantenol (Pro-Vitamina B5), Alantoína, Madecassoside, Avena Coloidal."
        )
    )

    val ingredients = listOf(
        IngredientInfo("Niacinamida (Vitamina B3)", "Regula la producción de grasa, minimiza poros dilatados y fortalece la barrera.", "Piel Grasa, Mixta y con Manchas"),
        IngredientInfo("Ácido Hialurónico", "Atrae y retiene hasta 1000 veces su peso en agua para hidratación profunda.", "Todos los tipos de piel"),
        IngredientInfo("Ácido Salicílico (BHA)", "Exfoliante liposoluble que penetra dentro de los poros disolviendo la grasa.", "Poros obstruidos, Piel Grasa y Tendencia Acneica"),
        IngredientInfo("Ceramidas (NP, AP, EOP)", "Lípidos esenciales que reparan y mantienen unida la capa protectora del rostro.", "Piel Seca, Deshidratada y Sensible"),
        IngredientInfo("Vitamina C (Ácido L-Ascórbico)", "Antioxidante potente que aporta luminosidad y estimula el colágeno.", "Piel apagada, Tono Irregular y Envejecimiento")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diccionario Dermatológico", fontWeight = FontWeight.Bold) },
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
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(text = "Tipos de Piel & Características", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }

            items(skinTypes) { info ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TealContainer
                        ) {
                            Text(
                                text = info.name,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Text(text = info.description, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "Necesidades clave: ${info.keyNeeds}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Ingredientes estrella: ${info.recommendedIngredients}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Science, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Text(text = "Glosario de Ingredientes Cosméticos", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }

            items(ingredients) { ing ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = RoseContainer.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = ing.name, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                        Text(text = ing.benefit, style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Recomendado para: ${ing.bestFor}", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.secondary))
                    }
                }
            }
        }
    }
}
