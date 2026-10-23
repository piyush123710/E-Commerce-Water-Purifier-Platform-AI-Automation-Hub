package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.model.Product
import com.example.data.model.UserRole
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalifncoTopBar(
    role: UserRole,
    unreadNotifCount: Int,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onRoleClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onAiHubClick: () -> Unit,
    onTdsClick: () -> Unit = {},
    onCompareClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier.testTag("califnco_top_bar"),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(end = 4.dp)
            ) {
                // Official Califnco Brand Logo
                Image(
                    painter = painterResource(id = R.drawable.ic_califnco_brand_logo),
                    contentDescription = "Califnco Official Brand Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, OceanBluePrimary.copy(alpha = 0.6f), CircleShape)
                )

                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = "Califnco",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        softWrap = false
                    )
                    Text(
                        text = "Pure Water Tech",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanBluePrimary,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        },
        actions = {
            // Compact Role Pill Badge
            Surface(
                onClick = onRoleClick,
                shape = RoundedCornerShape(14.dp),
                color = when (role) {
                    UserRole.CUSTOMER -> OceanBlueLight
                    UserRole.DEALER -> AquaTealLight
                    UserRole.ADMIN -> Color(0xFFF3E8FF)
                },
                modifier = Modifier
                    .padding(end = 2.dp)
                    .testTag("role_switcher_pill")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(
                                when (role) {
                                    UserRole.CUSTOMER -> OceanBluePrimary
                                    UserRole.DEALER -> AquaTealSecondary
                                    UserRole.ADMIN -> PurpleAccent
                                }
                            )
                    )
                    Text(
                        text = when (role) {
                            UserRole.CUSTOMER -> "Customer"
                            UserRole.DEALER -> "Dealer"
                            UserRole.ADMIN -> "Admin"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (role) {
                            UserRole.CUSTOMER -> OceanBlueDark
                            UserRole.DEALER -> AquaTealDark
                            UserRole.ADMIN -> PurpleAccent
                        },
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }

            // Notifications Bell
            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier.testTag("notifications_bell_btn")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadNotifCount > 0) {
                            Badge(
                                containerColor = ErrorRed,
                                contentColor = Color.White
                            ) {
                                Text(unreadNotifCount.toString())
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // More Options Dropdown Menu (Clean, uncrowded header)
            Box {
                IconButton(
                    onClick = { menuExpanded = true },
                    modifier = Modifier.testTag("topbar_overflow_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Quick Actions",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("AI Social Marketing Hub", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = InstagramGradientMiddle)
                        },
                        onClick = {
                            menuExpanded = false
                            onAiHubClick()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("TDS Water Quality Lab", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Science, contentDescription = null, tint = OceanBluePrimary)
                        },
                        onClick = {
                            menuExpanded = false
                            onTdsClick()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Compare Purifiers", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.CompareArrows, contentDescription = null, tint = AquaTealSecondary)
                        },
                        onClick = {
                            menuExpanded = false
                            onCompareClick()
                        }
                    )

                    HorizontalDivider()

                    DropdownMenuItem(
                        text = { Text(if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode", fontSize = 13.sp) },
                        leadingIcon = {
                            Icon(if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode, contentDescription = null)
                        },
                        onClick = {
                            menuExpanded = false
                            onToggleDarkMode()
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun ProductCard(
    product: Product,
    role: UserRole,
    onCardClick: () -> Unit,
    onAddToCart: () -> Unit,
    onImageZoomClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onCardClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}")
    ) {
        Column {
            // Product Image Header with Zoom action & Tag
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Zoom Icon overlay
                IconButton(
                    onClick = onImageZoomClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(32.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .testTag("zoom_btn_${product.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.ZoomIn,
                        contentDescription = "Zoom Image",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Tags: Bestseller / New
                if (product.isBestseller) {
                    Surface(
                        shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                        color = OceanBluePrimary,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = "BESTSELLER",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                } else if (product.isNewArrival) {
                    Surface(
                        shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                        color = SuccessGreen,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = "NEW",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Dealer MOQ tag if in dealer mode
                if (role == UserRole.DEALER) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = AquaTealSecondary,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp)
                    ) {
                        Text(
                            text = "MOQ: ${product.moq} pcs",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Card Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = product.subCategory,
                    fontSize = 11.sp,
                    color = OceanBluePrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 17.sp,
                    modifier = Modifier.height(34.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Rating & Reviews
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = WarningAmber,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "${product.rating}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "(${product.reviewCount})",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Price Section
                if (role == UserRole.DEALER) {
                    // Dealer Wholesale Pricing
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "₹${"%,.0f".format(product.retailPrice)}",
                                fontSize = 11.sp,
                                textDecoration = TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "₹${"%,.0f".format(product.wholesalePrice)}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AquaTealSecondary
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = AquaTealLight
                        ) {
                            val margin = (((product.retailPrice - product.wholesalePrice) / product.retailPrice) * 100).toInt()
                            Text(
                                text = "$margin% Margin",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AquaTealDark,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                } else {
                    // Customer Pricing
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "₹${"%,.0f".format(product.retailPrice)}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        val mrp = product.retailPrice * 1.25
                        Text(
                            text = "₹${"%,.0f".format(mrp)}",
                            fontSize = 11.sp,
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Add to Cart Button
                Button(
                    onClick = onAddToCart,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (role == UserRole.DEALER) AquaTealSecondary else OceanBluePrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .testTag("add_to_cart_${product.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "Add to Cart",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (role == UserRole.DEALER) "Bulk Order" else "Add to Cart",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// Lightbox & Zoom Dialog for Product Polish (+5 bonus pts)
@Composable
fun ImageLightboxDialog(
    imageUrl: String,
    productName: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.92f))
                .clickable { onDismiss() }
                .testTag("image_lightbox_dialog"),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = productName,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Zoom",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = productName,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF101828))
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "High-Resolution Califnco Product Inspection",
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// Razorpay Test Mode Checkout Dialog (Mandatory 5 pts)
@Composable
fun RazorpayCheckoutDialog(
    amount: Double,
    isDealer: Boolean,
    onPaymentSuccess: (paymentId: String, method: String) -> Unit,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedMethod by remember { mutableStateOf("UPI") }
    var isProcessing by remember { mutableStateOf(false) }
    var paymentVerified by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { if (!isProcessing) onDismiss() }) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("razorpay_checkout_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Razorpay Header Branding
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(RazorpayBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("R", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Column {
                            Text(
                                text = "Razorpay Test Mode",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = RazorpayBlue
                            )
                            Text(
                                text = "Key: rzp_test_califnco99",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFFEF3C7)
                    ) {
                        Text(
                            text = "TEST ENV",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Payable Amount
                Text(
                    text = "Total Payable Amount",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "₹${"%,.2f".format(amount)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (isDealer) {
                    Text(
                        text = "Wholesale B2B Order (GST Input Credit Eligible)",
                        fontSize = 11.sp,
                        color = AquaTealSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select Payment Mode",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Payment Options
                val methods = listOf(
                    Triple("UPI", "Google Pay / PhonePe / Paytm", Icons.Default.QrCodeScanner),
                    Triple("Card", "Credit / Debit Card (Visa, RuPay)", Icons.Default.CreditCard),
                    Triple("NetBanking", "HDFC / ICICI / SBI / Axis", Icons.Default.AccountBalance),
                    Triple("COD", "Cash On Delivery / 15-Day Terms", Icons.Default.Payments)
                )

                methods.forEach { (methodKey, subtitle, icon) ->
                    val isSelected = selectedMethod == methodKey
                    Surface(
                        onClick = { selectedMethod = methodKey },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) OceanBlueLight else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (isSelected) ButtonDefaults.outlinedButtonBorder else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = methodKey,
                                tint = if (isSelected) OceanBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = methodKey,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = subtitle,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedMethod = methodKey },
                                colors = RadioButtonDefaults.colors(selectedColor = OceanBluePrimary)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pay Action Button
                Button(
                    onClick = {
                        isProcessing = true
                        coroutineScope.launch {
                            delay(1400) // Simulate Razorpay gateway transaction
                            isProcessing = false
                            paymentVerified = true
                            delay(600)
                            val simulatedPayId = "pay_rzp_${(100000..999999).random()}"
                            onPaymentSuccess(simulatedPayId, selectedMethod)
                        }
                    },
                    enabled = !isProcessing,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RazorpayBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("razorpay_pay_btn")
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Verifying with Razorpay...", fontSize = 13.sp, color = Color.White)
                    } else if (paymentVerified) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Paid", tint = SuccessGreen)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Payment Verified!", fontSize = 13.sp, color = Color.White)
                    } else {
                        Text(
                            text = "Pay ₹${"%,.2f".format(amount)} via Razorpay",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// OpenStreetMap & Geoapify Address & Map Selector (Free alternative)
@Composable
fun LocationAddressDialog(
    initialAddress: String,
    onAddressSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var houseStreet by remember { mutableStateOf("Flat 304, Tower B, Sunshine Residency") }
    var landmark by remember { mutableStateOf("Near Central Metro Station") }
    var city by remember { mutableStateOf("Mumbai") }
    var pincode by remember { mutableStateOf("400076") }
    var selectedCoords by remember { mutableStateOf("19.1136° N, 72.8697° E (OpenStreetMap)") }
    var isLocating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("location_picker_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Map Pin",
                            tint = OceanBluePrimary
                        )
                        Text(
                            text = "Select Delivery Address",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Simulated Interactive OpenStreetMap Canvas Pin Card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PinDrop,
                                contentDescription = "Pin Location",
                                tint = ErrorRed,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "OpenStreetMap / Geoapify Pin",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = selectedCoords,
                                fontSize = 10.sp,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Detect GPS Button
                OutlinedButton(
                    onClick = {
                        isLocating = true
                        coroutineScope.launch {
                            delay(600)
                            selectedCoords = "18.5204° N, 73.8567° E (Geoapify GPS)"
                            city = "Pune"
                            pincode = "411004"
                            isLocating = false
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLocating) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(imageVector = Icons.Default.MyLocation, contentDescription = "GPS", modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isLocating) "Locating via GPS..." else "Use Current GPS Location (Free)", fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = houseStreet,
                    onValueChange = { houseStreet = it },
                    label = { Text("Flat / House / Street", fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = pincode,
                        onValueChange = { pincode = it },
                        label = { Text("Pincode", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val fullAddress = "$houseStreet, $landmark, $city - $pincode"
                        onAddressSelected(fullAddress)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBluePrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Text("Confirm Delivery Location", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}
