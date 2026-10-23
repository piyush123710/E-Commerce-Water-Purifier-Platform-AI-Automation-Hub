package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrderInvoiceDialog(
    order: Order,
    onBookService: () -> Unit,
    onDismiss: () -> Unit
) {
    var isDownloaded by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()) }
    val formattedDate = dateFormat.format(Date(order.createdAt))

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f)
                .padding(vertical = 16.dp)
                .testTag("order_invoice_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "GST TAX INVOICE",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OceanBluePrimary
                        )
                        Text(
                            text = "Original for Recipient • HSN 842121",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                // Scrollable Invoice Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Seller Details
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("CALIFNCO WATER TECHNOLOGIES PVT LTD", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Plot 42, Water Innovation Tech Park, MIDC Phase II, Mumbai - 400093", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("GSTIN: 27AABCC8819Q1Z8 • PAN: AABCC8819Q", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = OceanBluePrimary)
                            Text("State: Maharashtra (27) • Support: support@califnco.com", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // Buyer / Consignee Details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Billed To:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(order.customerName, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text(order.customerPhone, fontSize = 10.sp)
                                order.gstin?.let {
                                    Text("GSTIN: $it", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = AquaTealDark)
                                }
                            }
                        }

                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Invoice Details:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("No: ${order.id}", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text("Date: $formattedDate", fontSize = 10.sp)
                                Text("Status: ${order.status.label}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                            }
                        }
                    }

                    // Shipping Address
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text("Delivery Address (Consignee):", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(order.shippingAddress, fontSize = 11.sp)
                    }

                    // Line Items Table
                    Text("Itemized Taxable Description", fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    ) {
                        // Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(8.dp)
                        ) {
                            Text("Description", fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.weight(2f))
                            Text("Qty", fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.weight(0.6f))
                            Text("Rate", fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.weight(1f))
                            Text("Total", fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.weight(1f))
                        }

                        order.items.forEach { cartItem ->
                            val unitPrice = if (order.isDealerOrder) cartItem.product.wholesalePrice else cartItem.product.retailPrice
                            val lineTotal = unitPrice * cartItem.quantity
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(2f)) {
                                    Text(cartItem.product.name, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                                    Text("HSN: 842121 • ${cartItem.product.warranty}", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Text("${cartItem.quantity}", fontSize = 10.sp, modifier = Modifier.weight(0.6f))
                                Text("₹${"%,.0f".format(unitPrice)}", fontSize = 10.sp, modifier = Modifier.weight(1f))
                                Text("₹${"%,.0f".format(lineTotal)}", fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                        }
                    }

                    // Financial Summary Breakdown
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Subtotal (Taxable Value)", fontSize = 11.sp)
                                Text("₹${"%,.2f".format(order.subtotal)}", fontSize = 11.sp)
                            }
                            if (order.discount > 0) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Trade / Promo Discount", fontSize = 11.sp, color = SuccessGreen)
                                    Text("- ₹${"%,.2f".format(order.discount)}", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                                }
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Central GST (CGST @ 9%)", fontSize = 11.sp)
                                Text("₹${"%,.2f".format(order.gst / 2.0)}", fontSize = 11.sp)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("State GST (SGST @ 9%)", fontSize = 11.sp)
                                Text("₹${"%,.2f".format(order.gst / 2.0)}", fontSize = 11.sp)
                            }
                            HorizontalDivider()
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Invoice Total (INR)", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                                Text("₹${"%,.2f".format(order.totalAmount)}", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = OceanBluePrimary)
                            }
                        }
                    }

                    // Payment Verification Badge
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFDCFCE7), RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
                        Column {
                            Text("PAYMENT VERIFIED & CONFIRMED", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                            Text("Gateway: ${order.paymentMethod} • Txn ID: ${order.razorpayPaymentId ?: "RZP-OK"}", fontSize = 9.sp, color = Color(0xFF166534))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action Buttons Footer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { isDownloaded = true },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(if (isDownloaded) Icons.Default.Check else Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isDownloaded) "PDF Saved" else "Download PDF", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            onDismiss()
                            onBookService()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OceanBluePrimary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Book Service", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
