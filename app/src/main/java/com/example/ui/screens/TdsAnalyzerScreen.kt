package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.data.model.TdsAnalysisResult
import com.example.data.repository.CalifncoRepository
import com.example.ui.components.ProductCard
import com.example.ui.theme.*

@Composable
fun TdsAnalyzerScreen(
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onBookWaterTest: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tdsInput by remember { mutableStateOf(480f) }
    val result: TdsAnalysisResult = remember(tdsInput) {
        CalifncoRepository.analyzeTds(tdsInput.toInt())
    }

    val matchedProducts = remember(result) {
        CalifncoRepository.products.filter { it.id in result.suitablePurifierIds }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .testTag("tds_analyzer_screen")
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "TDS Water Quality & Filter Life Analyzer",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Test your tap or borewell water TDS to discover the perfect purifier and filter lifespan.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 80.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Interactive TDS Slider Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Water TDS Reading", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = when (result.ratingColor) {
                                    "Good" -> Color(0xFFDCFCE7)
                                    "Moderate" -> Color(0xFFFEF3C7)
                                    "High" -> Color(0xFFFFEDD5)
                                    else -> Color(0xFFFEE2E2)
                                }
                            ) {
                                Text(
                                    text = "${tdsInput.toInt()} PPM",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = when (result.ratingColor) {
                                        "Good" -> Color(0xFF15803D)
                                        "Moderate" -> Color(0xFF92400E)
                                        "High" -> Color(0xFFC2410C)
                                        else -> ErrorRed
                                    },
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Slider(
                            value = tdsInput,
                            onValueChange = { tdsInput = it },
                            valueRange = 50f..2500f,
                            steps = 49,
                            colors = SliderDefaults.colors(
                                thumbColor = OceanBluePrimary,
                                activeTrackColor = OceanBluePrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("50 PPM (Soft)", fontSize = 10.sp, color = Color.Gray)
                            Text("500 PPM", fontSize = 10.sp, color = Color.Gray)
                            Text("1200 PPM", fontSize = 10.sp, color = Color.Gray)
                            Text("2500+ PPM (Borewell)", fontSize = 10.sp, color = Color.Gray)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quick preset buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            OutlinedButton(
                                onClick = { tdsInput = 120f },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Municipal (120)", fontSize = 10.sp)
                            }
                            OutlinedButton(
                                onClick = { tdsInput = 350f },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Mix Tank (350)", fontSize = 10.sp)
                            }
                            OutlinedButton(
                                onClick = { tdsInput = 850f },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Borewell (850)", fontSize = 10.sp)
                            }
                        }
                    }
                }
            }

            // Water Quality Diagnosis & Recommendation
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = when (result.ratingColor) {
                            "Good" -> Color(0xFFF0FDF4)
                            "Moderate" -> Color(0xFFFFFBEB)
                            "High" -> Color(0xFFFFF7ED)
                            else -> Color(0xFFFEF2F2)
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = when (result.ratingColor) {
                                    "Good" -> Icons.Default.CheckCircle
                                    "Moderate" -> Icons.Default.Info
                                    else -> Icons.Default.Warning
                                },
                                contentDescription = null,
                                tint = when (result.ratingColor) {
                                    "Good" -> SuccessGreen
                                    "Moderate" -> WarningAmber
                                    else -> ErrorRed
                                }
                            )
                            Text(
                                text = result.hardnessCategory,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = result.healthImplication,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 17.sp
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                        Text("Recommended Purification Tech:", fontSize = 11.sp, color = Color.Gray)
                        Text(
                            text = result.recommendedTech,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = OceanBluePrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Estimated RO Membrane Lifespan:", fontSize = 11.sp)
                            Text(
                                text = "~${result.estimatedMembraneLifeMonths} Months",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SuccessGreen
                            )
                        }
                    }
                }
            }

            // Book Doorstep Water Test Option
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Science, contentDescription = null, tint = OceanBluePrimary)
                            Column {
                                Text("Don't know your exact TDS?", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("Book a certified technician to test at your home for free.", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Button(
                            onClick = onBookWaterTest,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Book Test", fontSize = 11.sp)
                        }
                    }
                }
            }

            // Tailored Purifier Recommendations
            item {
                Text(
                    text = "Engineered For Your Water (${matchedProducts.size} Recommended)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }

            items(matchedProducts, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    role = CalifncoRepository.currentRole.collectAsState().value,
                    onCardClick = { onProductClick(product) },
                    onAddToCart = { onAddToCart(product) },
                    onImageZoomClick = { }
                )
            }
        }
    }
}
