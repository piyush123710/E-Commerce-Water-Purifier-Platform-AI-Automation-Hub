package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.*
import com.example.data.repository.CalifncoRepository
import coil.compose.AsyncImage
import com.example.ui.theme.*

@Composable
fun AdminPortalScreen(
    orders: List<Order>,
    dealerApps: List<DealerApplication>,
    serviceBookings: List<ServiceBooking>,
    onUpdateOrderStatus: (orderId: String, status: OrderStatus) -> Unit,
    onUpdateDealerStatus: (dealerId: String, status: DealerApprovalStatus) -> Unit,
    onUpdateServiceStatus: (ticketId: String, status: ServiceStatus, techName: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Orders, 1: Dealers, 2: Service Desk, 3: Analytics

    val totalRevenue = orders.sumOf { it.totalAmount }
    val pendingDealers = dealerApps.count { it.status == DealerApprovalStatus.PENDING }
    val openTickets = serviceBookings.count { it.status != ServiceStatus.COMPLETED }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .testTag("admin_portal_screen")
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Califnco Admin Desk",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleAccent
                )
                Text(
                    text = "Live operations: Orders, dealer approvals & service desk",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF3E8FF)
            ) {
                Text(
                    text = "VERCEL SYNC",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = PurpleAccent,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Executive Metrics Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Total Sales", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("₹${"%,.0f".format(totalRevenue)}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = SuccessGreen)
                }
            }

            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Pending Dealers", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("$pendingDealers", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = WarningAmber)
                }
            }

            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Service Tickets", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("$openTickets", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = OceanBluePrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Navigation Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = PurpleAccent
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Orders (${orders.size})", fontSize = 11.sp) })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Dealers (${dealerApps.size})", fontSize = 11.sp) })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Service Desk", fontSize = 11.sp) })
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Stock & Alerts", fontSize = 11.sp) })
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            0 -> {
                // Orders Management
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(orders, key = { it.id }) { order ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(order.id, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("${order.customerName} (${order.userRole.name})", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Text(
                                        "₹${"%,.2f".format(order.totalAmount)}",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = OceanBluePrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Items: ${order.items.joinToString { "${it.product.name} (x${it.quantity})" }}", fontSize = 11.sp, maxLines = 2)
                                Text("Pay: ${order.paymentMethod} • Razorpay ID: ${order.razorpayPaymentId ?: "N/A"}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Status: ${order.status.label}", fontSize = 11.sp, fontWeight = FontWeight.Bold)

                                    var statusMenuExpanded by remember { mutableStateOf(false) }
                                    Box {
                                        Button(
                                            onClick = { statusMenuExpanded = true },
                                            colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Text("Update Status", fontSize = 11.sp)
                                        }

                                        DropdownMenu(
                                            expanded = statusMenuExpanded,
                                            onDismissRequest = { statusMenuExpanded = false }
                                        ) {
                                            OrderStatus.values().forEach { st ->
                                                DropdownMenuItem(
                                                    text = { Text(st.label, fontSize = 12.sp) },
                                                    onClick = {
                                                        onUpdateOrderStatus(order.id, st)
                                                        statusMenuExpanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Dealers Management
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(dealerApps, key = { it.id }) { app ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(app.businessName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text("Contact: ${app.contactPerson} • ${app.phone}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = when (app.status) {
                                            DealerApprovalStatus.APPROVED -> Color(0xFFDCFCE7)
                                            DealerApprovalStatus.PENDING -> Color(0xFFFEF3C7)
                                            DealerApprovalStatus.REJECTED -> Color(0xFFFEE2E2)
                                        }
                                    ) {
                                        Text(
                                            text = app.status.name,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (app.status) {
                                                DealerApprovalStatus.APPROVED -> Color(0xFF15803D)
                                                DealerApprovalStatus.PENDING -> Color(0xFF92400E)
                                                DealerApprovalStatus.REJECTED -> ErrorRed
                                            },
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text("GSTIN: ${app.gstin} • City: ${app.city}", fontSize = 11.sp)
                                Text("Target Volume: ${app.expectedVolume} • Credit Limit: ₹${"%,.0f".format(app.creditLimit)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (app.status != DealerApprovalStatus.APPROVED) {
                                        Button(
                                            onClick = { onUpdateDealerStatus(app.id, DealerApprovalStatus.APPROVED) },
                                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Approve Dealer", fontSize = 11.sp)
                                        }
                                    }

                                    if (app.status != DealerApprovalStatus.REJECTED) {
                                        OutlinedButton(
                                            onClick = { onUpdateDealerStatus(app.id, DealerApprovalStatus.REJECTED) },
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Reject", fontSize = 11.sp, color = ErrorRed)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Service Desk Operations
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(serviceBookings, key = { it.id }) { booking ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("${booking.id} - ${booking.serviceType.title}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("Customer: ${booking.customerName} (${booking.customerPhone})", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = when (booking.status) {
                                            ServiceStatus.PENDING -> Color(0xFFFEF3C7)
                                            ServiceStatus.SCHEDULED -> Color(0xFFE0E7FF)
                                            ServiceStatus.TECHNICIAN_ASSIGNED -> Color(0xFFEDE9FE)
                                            ServiceStatus.COMPLETED -> Color(0xFFDCFCE7)
                                        }
                                    ) {
                                        Text(
                                            text = booking.status.label,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = when (booking.status) {
                                                ServiceStatus.PENDING -> Color(0xFF92400E)
                                                ServiceStatus.SCHEDULED -> Color(0xFF3730A3)
                                                ServiceStatus.TECHNICIAN_ASSIGNED -> PurpleAccent
                                                ServiceStatus.COMPLETED -> Color(0xFF15803D)
                                            },
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text("Model: ${booking.purifierModel}", fontSize = 11.sp)
                                Text("Address: ${booking.address}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                booking.assignedTechnician?.let {
                                    Text("Technician: $it", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OceanBluePrimary)
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            val techs = listOf("Rajesh Verma (Senior Tech)", "Amit Sharma (Filter Expert)", "Sandeep Roy (RO Specialist)")
                                            onUpdateServiceStatus(booking.id, ServiceStatus.TECHNICIAN_ASSIGNED, techs.random())
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = OceanBluePrimary),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Assign Technician", fontSize = 11.sp)
                                    }

                                    Button(
                                        onClick = {
                                            onUpdateServiceStatus(booking.id, ServiceStatus.COMPLETED, booking.assignedTechnician)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Mark Done", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            3 -> {
                // Stock Management & Low-Stock Alerts
                val stockOverrides by CalifncoRepository.stockOverrides.collectAsState()
                val lowStockItems = CalifncoRepository.products.filter {
                    CalifncoRepository.getStock(it) < 15
                }
                val totalInventoryUnits = CalifncoRepository.products.sumOf { CalifncoRepository.getStock(it) }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(20.dp))
                                    Text(
                                        text = "Warehouse Low-Stock Alert System",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFF92400E)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${lowStockItems.size} products are below critical threshold (< 15 units). Replenish inventory to avoid fulfillment delays.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF78350F)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Total Active Catalog Items: 57 • Total Tracked Stock: $totalInventoryUnits units",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Critical Low Stock Items (< 15 Units)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    items(lowStockItems, key = { it.id }) { product ->
                        val currentUnits = CalifncoRepository.getStock(product)
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = product.imageUrl,
                                    contentDescription = product.name,
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(product.name, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
                                    Text("SKU: ${product.id} • ${product.category.displayName}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFFEE2E2)
                                    ) {
                                        Text(
                                            text = "ALERT: Only $currentUnits units left",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ErrorRed,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Button(
                                        onClick = {
                                            CalifncoRepository.restockProduct(product.id, 25)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = OceanBluePrimary),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("+25 Restock", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    OutlinedButton(
                                        onClick = {
                                            CalifncoRepository.restockProduct(product.id, 50)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("+50 Bulk", fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Healthy Inventory (15+ Units)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    items(CalifncoRepository.products.filter { CalifncoRepository.getStock(it) >= 15 }.take(10), key = { it.id }) { product ->
                        val currentUnits = CalifncoRepository.getStock(product)
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(product.name, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                                    Text("Retail: ₹${"%,.0f".format(product.retailPrice)} • Wholesale: ₹${"%,.0f".format(product.wholesalePrice)}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFDCFCE7)
                                ) {
                                    Text(
                                        text = "$currentUnits In Stock",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF15803D),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
