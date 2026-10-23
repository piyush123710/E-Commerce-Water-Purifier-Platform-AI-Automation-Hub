package com.example.ui.screens

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Order
import com.example.data.model.OrderStatus
import com.example.data.model.UserRole
import com.example.ui.components.OrderInvoiceDialog
import com.example.ui.components.OrderTrackingDialog
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Orders screen matching Screenshot 3:
 * - Order card with Order #, Date, and Status pill on top
 * - Item line items with thumbnail, qty, and price
 * - Dual action buttons at bottom: [📍 Track Order] and [📄 Details / Invoice]
 */
@Composable
fun OrdersScreen(
    orders: List<Order>,
    role: UserRole,
    onBookServiceForOrder: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var filterRole by remember { mutableStateOf<UserRole?>(null) }
    var trackingOrder by remember { mutableStateOf<Order?>(null) }
    var invoiceOrder by remember { mutableStateOf<Order?>(null) }

    val displayedOrders = remember(orders, filterRole) {
        if (filterRole == null) orders else orders.filter { it.userRole == filterRole }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .testTag("orders_screen")
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "My Orders",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Track your Califnco purifier shipments & download GST invoices.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Role Filter Pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = filterRole == null,
                onClick = { filterRole = null },
                label = { Text("All (${orders.size})", fontSize = 11.sp) }
            )
            FilterChip(
                selected = filterRole == UserRole.CUSTOMER,
                onClick = { filterRole = UserRole.CUSTOMER },
                label = { Text("Customer", fontSize = 11.sp) }
            )
            FilterChip(
                selected = filterRole == UserRole.DEALER,
                onClick = { filterRole = UserRole.DEALER },
                label = { Text("Wholesale B2B", fontSize = 11.sp) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (displayedOrders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = "No Orders",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No Orders Found",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Your placed orders will show up here with live shipment tracking.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(displayedOrders, key = { it.id }) { order ->
                    OrderCard(
                        order = order,
                        onTrackOrder = { trackingOrder = order },
                        onViewDetails = { invoiceOrder = order }
                    )
                }
            }
        }
    }

    // Live Tracking Modal (Screenshot 3 Track Order)
    trackingOrder?.let { ord ->
        OrderTrackingDialog(
            order = ord,
            onDismiss = { trackingOrder = null }
        )
    }

    // Invoice & Details Modal (Screenshot 3 Details / Invoice)
    invoiceOrder?.let { ord ->
        OrderInvoiceDialog(
            order = ord,
            onBookService = {
                val ordId = ord.id
                invoiceOrder = null
                onBookServiceForOrder(ordId)
            },
            onDismiss = { invoiceOrder = null }
        )
    }
}

/**
 * Clean Order Card strictly adhering to Screenshot 3:
 * - Order Number #ORD001
 * - Formatted date
 * - Status pill (Delivered / Shipped / Processing)
 * - Item lines
 * - Total Paid
 * - Dual Buttons: [Track Order] & [Details / Invoice]
 */
@Composable
fun OrderCard(
    order: Order,
    onTrackOrder: () -> Unit,
    onViewDetails: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val formattedDate = dateFormat.format(Date(order.createdAt))

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("order_card_${order.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Order ID + Date on Left, Status Pill on Right (Matching Screenshot 3)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "#${order.id}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (order.isDealerOrder) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = AquaTealLight
                            ) {
                                Text(
                                    text = "B2B",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AquaTealDark,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = formattedDate,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Status Pill (e.g. Delivered, Shipped, Processing)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = when (order.status) {
                        OrderStatus.PLACED, OrderStatus.CONFIRMED -> Color(0xFFFEF3C7)
                        OrderStatus.PACKED, OrderStatus.DISPATCHED -> Color(0xFFE0E7FF)
                        OrderStatus.OUT_FOR_DELIVERY -> Color(0xFFCCFBF1)
                        OrderStatus.INSTALLED -> Color(0xFFDCFCE7)
                        OrderStatus.CANCELLED -> Color(0xFFFEE2E2)
                    }
                ) {
                    Text(
                        text = when (order.status) {
                            OrderStatus.INSTALLED -> "Delivered"
                            OrderStatus.DISPATCHED -> "Shipped"
                            OrderStatus.OUT_FOR_DELIVERY -> "Out for Delivery"
                            OrderStatus.PLACED, OrderStatus.CONFIRMED, OrderStatus.PACKED -> "Processing"
                            OrderStatus.CANCELLED -> "Cancelled"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (order.status) {
                            OrderStatus.INSTALLED -> Color(0xFF15803D)
                            OrderStatus.DISPATCHED -> PurpleAccent
                            OrderStatus.OUT_FOR_DELIVERY -> AquaTealDark
                            OrderStatus.PLACED, OrderStatus.CONFIRMED, OrderStatus.PACKED -> Color(0xFF92400E)
                            OrderStatus.CANCELLED -> ErrorRed
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            // Items Purchased List
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                order.items.forEach { cartItem ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AsyncImage(
                            model = cartItem.product.imageUrl,
                            contentDescription = cartItem.product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = cartItem.product.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            val unitPrice = if (order.isDealerOrder) cartItem.product.wholesalePrice else cartItem.product.retailPrice
                            Text(
                                text = "Qty: ${cartItem.quantity} • ₹${"%,.0f".format(unitPrice)} each",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        val lineTotal = (if (order.isDealerOrder) cartItem.product.wholesalePrice else cartItem.product.retailPrice) * cartItem.quantity
                        Text(
                            text = "₹${"%,.0f".format(lineTotal)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Total Amount Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Paid (${order.paymentMethod.take(15)})",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "₹${"%,.2f".format(order.totalAmount)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = OceanBluePrimary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dual Action Buttons (Matching Screenshot 3)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Button 1: Track Order
                Button(
                    onClick = onTrackOrder,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBluePrimary),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("track_order_btn_${order.id}")
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Track Order", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Button 2: Details / Invoice
                OutlinedButton(
                    onClick = onViewDetails,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("order_details_btn_${order.id}")
                ) {
                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Details / Invoice", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
