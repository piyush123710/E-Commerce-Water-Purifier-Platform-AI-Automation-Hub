package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.ui.theme.*

@Composable
fun OrderTrackingDialog(
    order: Order,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 20.dp)
                .testTag("order_tracking_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Live Shipment Tracking",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = order.id,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OceanBluePrimary
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // ETA & Courier Card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = OceanBlueLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estimated Delivery",
                                fontSize = 11.sp,
                                color = OceanBlueDark
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = OceanBluePrimary
                            ) {
                                Text(
                                    text = order.status.label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "Tomorrow by 04:00 PM IST",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OceanBlueDark
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = OceanBluePrimary.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Carrier", fontSize = 10.sp, color = OceanBlueDark)
                                Text("Califnco Express Fleet", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OceanBlueDark)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("AWB Tracking Code", fontSize = 10.sp, color = OceanBlueDark)
                                Text("CF-${order.id.takeLast(6)}-IND", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OceanBlueDark)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Detailed Vertical Milestones
                Text(
                    text = "Shipment Milestones",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                val trackingSteps = listOf(
                    TrackingMilestone(
                        title = "Order Placed & Payment Confirmed",
                        description = "Payment verified via ${order.paymentMethod}. Order sent to warehouse fulfillment.",
                        time = "22 Oct, 10:30 AM",
                        isCompleted = true
                    ),
                    TrackingMilestone(
                        title = "Factory Inspection & Quality Packed",
                        description = "100% pressure-tested, RO membrane checked, securely crated with bubble wrap.",
                        time = "22 Oct, 03:15 PM",
                        isCompleted = true
                    ),
                    TrackingMilestone(
                        title = "Shipped from Bhiwandi Logistics Center",
                        description = "Dispatched on Califnco Logistics vehicle #MH-04-AX-8812.",
                        time = "23 Oct, 08:45 AM",
                        isCompleted = order.status != OrderStatus.PLACED && order.status != OrderStatus.CONFIRMED
                    ),
                    TrackingMilestone(
                        title = "Out for Delivery & Installation Team Assigned",
                        description = "Technician Rajesh Verma assigned for same-day free doorstep installation.",
                        time = "Expected Today, 01:00 PM",
                        isCompleted = order.status == OrderStatus.OUT_FOR_DELIVERY || order.status == OrderStatus.INSTALLED
                    ),
                    TrackingMilestone(
                        title = "Delivered & Purifier Installed",
                        description = "TDS water test verified in front of customer, warranty registered.",
                        time = "Pending Final Sign-off",
                        isCompleted = order.status == OrderStatus.INSTALLED
                    )
                )

                trackingSteps.forEachIndexed { index, milestone ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Icon & connecting vertical line
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.width(32.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (milestone.isCompleted) SuccessGreen else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (milestone.isCompleted) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color.Gray)
                                    )
                                }
                            }

                            if (index < trackingSteps.size - 1) {
                                Box(
                                    modifier = Modifier
                                        .width(2.dp)
                                        .height(42.dp)
                                        .background(if (milestone.isCompleted) SuccessGreen else Color.LightGray)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Details
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = milestone.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (milestone.isCompleted) FontWeight.Bold else FontWeight.Normal,
                                    color = if (milestone.isCompleted) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = milestone.description,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = milestone.time,
                                fontSize = 10.sp,
                                color = if (milestone.isCompleted) OceanBluePrimary else Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Done Button
                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBluePrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close Tracking")
                }
            }
        }
    }
}

private data class TrackingMilestone(
    val title: String,
    val description: String,
    val time: String,
    val isCompleted: Boolean
)
