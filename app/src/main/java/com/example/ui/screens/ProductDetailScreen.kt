package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Product
import com.example.data.model.UserRole
import com.example.data.repository.CalifncoRepository
import com.example.ui.components.ImageLightboxDialog
import com.example.ui.components.RazorpayCheckoutDialog
import com.example.ui.components.ReviewsAndRatingsSection
import com.example.ui.components.WriteReviewDialog
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    role: UserRole,
    onBack: () -> Unit,
    onAddToCart: (Product, Int) -> Unit,
    onBuyNow: (Product, Int, paymentId: String, method: String) -> Unit,
    onServiceBookingClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var quantity by remember { mutableStateOf(if (role == UserRole.DEALER) product.moq else 1) }
    var showLightbox by remember { mutableStateOf(false) }
    var showRazorpayDialog by remember { mutableStateOf(false) }
    var showWriteReviewDialog by remember { mutableStateOf(false) }

    val allReviews by CalifncoRepository.reviews.collectAsState()
    val productReviews = remember(allReviews, product.id) {
        CalifncoRepository.getReviewsForProduct(product.id)
    }

    val unitPrice = if (role == UserRole.DEALER) product.wholesalePrice else product.retailPrice
    val totalPrice = unitPrice * quantity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showLightbox = true }) {
                        Icon(Icons.Default.ZoomIn, contentDescription = "Zoom")
                    }
                }
            )
        },
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
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Total (${quantity} pcs)",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "₹${"%,.0f".format(totalPrice)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (role == UserRole.DEALER) AquaTealSecondary else OceanBluePrimary
                        )
                    }

                    OutlinedButton(
                        onClick = { onAddToCart(product, quantity) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(46.dp)
                    ) {
                        Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add to Cart")
                    }

                    Button(
                        onClick = { showRazorpayDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (role == UserRole.DEALER) AquaTealSecondary else OceanBluePrimary
                        ),
                        modifier = Modifier
                            .height(46.dp)
                            .testTag("buy_now_btn")
                    ) {
                        Text("Buy Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Product Image with Zoom Tap
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { showLightbox = true }
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Lightbox prompt pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.ZoomIn, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Text("Tap to Zoom", color = Color.White, fontSize = 11.sp)
                    }
                }
            }

            // Product Information
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Category & Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.subCategory.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanBluePrimary
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                        Text("${product.rating}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("(${product.reviewCount} customer reviews)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = product.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Price Card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (role == UserRole.DEALER) AquaTealLight else OceanBlueLight
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            if (role == UserRole.DEALER) {
                                Text(
                                    text = "Wholesale B2B Price",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AquaTealDark
                                )
                                Text(
                                    text = "₹${"%,.0f".format(product.wholesalePrice)} / unit",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AquaTealDark
                                )
                                Text(
                                    text = "Retail MRP: ₹${"%,.0f".format(product.retailPrice)} (Save ₹${"%,.0f".format(product.retailPrice - product.wholesalePrice)}/pc)",
                                    fontSize = 11.sp,
                                    color = AquaTealDark
                                )
                            } else {
                                Text(
                                    text = "Special Offer Price",
                                    fontSize = 11.sp,
                                    color = OceanBlueDark
                                )
                                Text(
                                    text = "₹${"%,.0f".format(product.retailPrice)}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = OceanBlueDark
                                )
                                val mrp = product.retailPrice * 1.25
                                Text(
                                    text = "MRP: ₹${"%,.0f".format(mrp)} (Inclusive of all taxes)",
                                    fontSize = 11.sp,
                                    color = OceanBlueDark
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (role == UserRole.DEALER) AquaTealSecondary else OceanBluePrimary
                        ) {
                            Text(
                                text = if (role == UserRole.DEALER) "MOQ: ${product.moq} pcs" else "In Stock",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quantity Selector Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Quantity",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        if (role == UserRole.DEALER) {
                            Text(
                                text = "Wholesale Minimum: ${product.moq} pcs",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val minAllowed = if (role == UserRole.DEALER) product.moq else 1
                        IconButton(
                            onClick = { if (quantity > minAllowed) quantity -= 1 },
                            enabled = quantity > minAllowed,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                        }

                        Text(
                            text = "$quantity",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = { quantity += 1 },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                Text(
                    text = "Description",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = product.description,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Key Highlights
                Text(
                    text = "Key Highlights",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                product.keyFeatures.forEach { feature ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = feature,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Technical Specifications
                Text(
                    text = "Specifications",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        product.specifications.forEach { (key, value) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(key, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Warranty & Service Card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Verified, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(24.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = product.warranty,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF166534)
                            )
                            Text(
                                text = "Genuine Califnco certified parts & professional technician support.",
                                fontSize = 11.sp,
                                color = Color(0xFF15803D)
                            )
                        }

                        TextButton(onClick = { onServiceBookingClick(product) }) {
                            Text("Book Service", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Customer Reviews & Ratings Section (Matching Screenshot 4)
                ReviewsAndRatingsSection(
                    product = product,
                    reviews = productReviews,
                    onWriteReviewClick = { showWriteReviewDialog = true }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Write a Review Modal (Screenshot 4)
    if (showWriteReviewDialog) {
        WriteReviewDialog(
            product = product,
            onSubmit = { rating, comment ->
                CalifncoRepository.addReview(product.id, "", rating, comment)
            },
            onDismiss = { showWriteReviewDialog = false }
        )
    }

    // Lightbox modal
    if (showLightbox) {
        ImageLightboxDialog(
            imageUrl = product.imageUrl,
            productName = product.name,
            onDismiss = { showLightbox = false }
        )
    }

    // Razorpay Instant Checkout modal
    if (showRazorpayDialog) {
        RazorpayCheckoutDialog(
            amount = totalPrice,
            isDealer = role == UserRole.DEALER,
            onPaymentSuccess = { payId, method ->
                showRazorpayDialog = false
                onBuyNow(product, quantity, payId, method)
            },
            onDismiss = { showRazorpayDialog = false }
        )
    }
}
