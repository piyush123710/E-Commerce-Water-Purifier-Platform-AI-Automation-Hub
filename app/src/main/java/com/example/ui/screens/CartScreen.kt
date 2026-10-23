package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.data.model.CartItem
import com.example.data.model.UserRole
import com.example.ui.components.LocationAddressDialog
import com.example.ui.components.RazorpayCheckoutDialog
import com.example.ui.theme.*

@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    role: UserRole,
    onUpdateQuantity: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
    onCheckoutSuccess: (paymentId: String, method: String, address: String, discount: Double, gstin: String?) -> Unit,
    onExploreCatalog: () -> Unit,
    modifier: Modifier = Modifier
) {
    var promoCode by remember { mutableStateOf("") }
    var appliedDiscount by remember { mutableStateOf(0.0) }
    var promoMessage by remember { mutableStateOf<String?>(null) }
    var deliveryAddress by remember { mutableStateOf("Flat 402, Green Valley Apartments, Andheri West, Mumbai, MH - 400053") }
    var gstin by remember { mutableStateOf("27AAACA9921M1Z4") }
    var showAddressDialog by remember { mutableStateOf(false) }
    var showRazorpayDialog by remember { mutableStateOf(false) }

    val isDealer = role == UserRole.DEALER
    val subtotal = cartItems.sumOf {
        val price = if (isDealer) it.product.wholesalePrice else it.product.retailPrice
        price * it.quantity
    }
    val taxable = (subtotal - appliedDiscount).coerceAtLeast(0.0)
    val gst = taxable * 0.18
    val finalTotal = taxable + gst

    if (cartItems.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("empty_cart_view"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(OceanBlueLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveShoppingCart,
                        contentDescription = "Empty Cart",
                        tint = OceanBluePrimary,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Your Cart is Empty",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = if (isDealer) "Add bulk water purifiers or spares with wholesale discount to proceed."
                    else "Explore our 50+ water purifiers, filters, pumps, and spares.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onExploreCatalog,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDealer) AquaTealSecondary else OceanBluePrimary
                    )
                ) {
                    Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Explore Catalog (50+ Items)")
                }
            }
        }
        return
    }

    Scaffold(
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .navigationBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Total Payable",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "₹${"%,.2f".format(finalTotal)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isDealer) AquaTealSecondary else OceanBluePrimary
                        )
                    }

                    Button(
                        onClick = { showRazorpayDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RazorpayBlue
                        ),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("checkout_with_razorpay_btn")
                    ) {
                        Icon(Icons.Default.Payment, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pay via Razorpay", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isDealer) "Wholesale Bulk Cart (${cartItems.size} items)" else "Shopping Cart (${cartItems.size} items)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (isDealer) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AquaTealLight
                        ) {
                            Text(
                                text = "B2B WHOLESALE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AquaTealDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // Dealer Margin Profit Calculator Banner
            if (isDealer) {
                item {
                    val retailValue = cartItems.sumOf { it.product.retailPrice * it.quantity }
                    val dealerProfit = retailValue - subtotal
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = AquaTealLight),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = AquaTealDark, modifier = Modifier.size(18.dp))
                                Text(
                                    text = "Wholesale Margin Summary",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = AquaTealDark
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Retail Sale Value: ₹${"%,.0f".format(retailValue)} | Wholesale Cost: ₹${"%,.0f".format(subtotal)}",
                                fontSize = 11.sp,
                                color = AquaTealDark
                            )
                            Text(
                                text = "Estimated Dealer Margin: ₹${"%,.0f".format(dealerProfit)} (${(((dealerProfit) / retailValue) * 100).toInt()}% Profit)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AquaTealSecondary
                            )
                        }
                    }
                }
            }

            // Cart Items
            items(cartItems, key = { it.product.id }) { item ->
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
                            model = item.product.imageUrl,
                            contentDescription = item.product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(70.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.product.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                            Text(
                                text = item.product.subCategory,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            val unitPrice = if (isDealer) item.product.wholesalePrice else item.product.retailPrice
                            Text(
                                text = "₹${"%,.0f".format(unitPrice)} each",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isDealer) AquaTealSecondary else OceanBluePrimary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Quantity Modifier
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val minQty = if (isDealer) item.product.moq else 1
                                IconButton(
                                    onClick = { onUpdateQuantity(item.product.id, item.quantity - 1) },
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(14.dp))
                                }

                                Text(
                                    text = "${item.quantity}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )

                                IconButton(
                                    onClick = { onUpdateQuantity(item.product.id, item.quantity + 1) },
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(14.dp))
                                }
                            }
                        }

                        IconButton(
                            onClick = { onRemoveItem(item.product.id) }
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Remove", tint = ErrorRed)
                        }
                    }
                }
            }

            // Delivery Address Card (Matching Screenshot 2 with Home/Office options)
            item {
                var addressType by remember { mutableStateOf("Home") }
                Card(
                    shape = RoundedCornerShape(14.dp),
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = OceanBluePrimary, modifier = Modifier.size(18.dp))
                                Text("Delivery Address", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            TextButton(
                                onClick = { showAddressDialog = true },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(Icons.Default.EditLocation, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Change Pin", fontSize = 12.sp, color = OceanBluePrimary)
                            }
                        }

                        // Home vs Office Radio Selector (Screenshot 2)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { addressType = "Home" }
                            ) {
                                RadioButton(selected = addressType == "Home", onClick = { addressType = "Home" })
                                Icon(Icons.Default.Home, contentDescription = null, tint = OceanBluePrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Home", fontSize = 12.sp, fontWeight = if (addressType == "Home") FontWeight.Bold else FontWeight.Normal)
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { addressType = "Office" }
                            ) {
                                RadioButton(selected = addressType == "Office", onClick = { addressType = "Office" })
                                Icon(Icons.Default.Business, contentDescription = null, tint = OceanBluePrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Office / Warehouse", fontSize = 12.sp, fontWeight = if (addressType == "Office") FontWeight.Bold else FontWeight.Normal)
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = deliveryAddress,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Dealer GSTIN Input (if Dealer)
            if (isDealer) {
                item {
                    OutlinedTextField(
                        value = gstin,
                        onValueChange = { gstin = it },
                        label = { Text("Dealer GSTIN (For Tax Invoicing)", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.ReceiptLong, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Promo Code Card
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = promoCode,
                                onValueChange = { promoCode = it.uppercase() },
                                placeholder = { Text("Try: CALIFNCO500", fontSize = 12.sp) },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )

                            Button(
                                onClick = {
                                    if (promoCode == "CALIFNCO500") {
                                        appliedDiscount = 500.0
                                        promoMessage = "₹500 Califnco Welcome Coupon Applied!"
                                    } else if (promoCode == "CLEANWATER") {
                                        appliedDiscount = subtotal * 0.10
                                        promoMessage = "10% Pure Water Discount Applied!"
                                    } else {
                                        promoMessage = "Invalid Code. Try CALIFNCO500 or CLEANWATER"
                                    }
                                },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Apply")
                            }
                        }

                        promoMessage?.let { msg ->
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = msg,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (appliedDiscount > 0) SuccessGreen else ErrorRed
                            )
                        }
                    }
                }
            }

            // Payment Method Selection Card (Matching Screenshot 2)
            item {
                var selectedPaymentMethod by remember { mutableStateOf("Razorpay UPI / QR") }
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Payment, contentDescription = null, tint = RazorpayBlue, modifier = Modifier.size(18.dp))
                            Text("Payment Method", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }

                        val methods = listOf(
                            Pair("Razorpay UPI / QR", "Google Pay, PhonePe, Paytm, BHIM Instant"),
                            Pair("Credit & Debit Cards", "Visa, MasterCard, RuPay (Zero Extra Charge)"),
                            Pair("NetBanking & B2B NEFT", "All Indian Banks & Corporate Current Accounts"),
                            Pair("Cash / Pay on Installation", "Pay technician after water purifier setup & test")
                        )

                        methods.forEach { (name, desc) ->
                            val isSel = selectedPaymentMethod == name
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isSel) OceanBlueLight else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedPaymentMethod = name }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RadioButton(selected = isSel, onClick = { selectedPaymentMethod = name })
                                Column {
                                    Text(name, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text(desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }

            // Bill Breakdown Card
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Bill Breakdown", fontWeight = FontWeight.Bold, fontSize = 14.sp)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Items Subtotal", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("₹${"%,.2f".format(subtotal)}", fontSize = 12.sp)
                        }

                        if (appliedDiscount > 0) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Coupon Discount", fontSize = 12.sp, color = SuccessGreen)
                                Text("- ₹${"%,.2f".format(appliedDiscount)}", fontSize = 12.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("GST (18% Statutory)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("₹${"%,.2f".format(gst)}", fontSize = 12.sp)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Standard Shipping & Delivery", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("FREE", fontSize = 12.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Grand Total", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                            Text(
                                "₹${"%,.2f".format(finalTotal)}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = if (isDealer) AquaTealSecondary else OceanBluePrimary
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // OpenStreetMap Location Dialog
    if (showAddressDialog) {
        LocationAddressDialog(
            initialAddress = deliveryAddress,
            onAddressSelected = { newAddress ->
                deliveryAddress = newAddress
                showAddressDialog = false
            },
            onDismiss = { showAddressDialog = false }
        )
    }

    // Razorpay Checkout Modal
    if (showRazorpayDialog) {
        RazorpayCheckoutDialog(
            amount = finalTotal,
            isDealer = isDealer,
            onPaymentSuccess = { paymentId, method ->
                showRazorpayDialog = false
                onCheckoutSuccess(paymentId, method, deliveryAddress, appliedDiscount, if (isDealer) gstin else null)
            },
            onDismiss = { showRazorpayDialog = false }
        )
    }
}
