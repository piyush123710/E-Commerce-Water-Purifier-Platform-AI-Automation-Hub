package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.Product
import com.example.data.model.UserRole
import com.example.data.repository.CalifncoRepository
import com.example.ui.components.CalifncoTopBar
import com.example.ui.components.ComparePurifiersDialog
import com.example.ui.components.NotificationDialog
import com.example.ui.screens.*
import com.example.ui.theme.AquaTealLight
import com.example.ui.theme.AquaTealSecondary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.OceanBlueLight
import com.example.ui.theme.PurpleAccent
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkMode by CalifncoRepository.isDarkMode.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = isDarkMode) {
                CalifncoRootGate()
            }
        }
    }
}

enum class NavDestination(val label: String) {
    CATALOG("Store"),
    CART("Cart"),
    ORDERS("Orders"),
    SERVICE("Service"),
    TDS_LAB("TDS Lab"),
    AI_HUB("AI Social"),
    PROFILE("Profile"),
    ADMIN("Admin Desk")
}

@Composable
fun CalifncoRootGate() {
    val coroutineScope = rememberCoroutineScope()
    val currentUser by CalifncoRepository.currentUser.collectAsStateWithLifecycle()
    var showSplash by remember { mutableStateOf(true) }
    var showAuthScreen by remember { mutableStateOf(false) }

    // EXACT SEQUENCE: Splash Screen -> Welcome Screen -> Get Started -> Auth Screen -> Role-Based Dashboard
    if (currentUser != null) {
        CalifncoApp()
    } else if (showSplash) {
        SplashScreen(
            onFinished = { showSplash = false }
        )
    } else if (!showAuthScreen) {
        WelcomeScreen(
            onGetStarted = { showAuthScreen = true }
        )
    } else {
        AuthScreen(
            onRegisterCustomer = { name, email, phone, city, referral ->
                CalifncoRepository.registerCustomer(name, email, phone, city, referral)
            },
            onRegisterDealer = { bName, cPerson, email, phone, gstin, city ->
                CalifncoRepository.registerDealer(bName, cPerson, email, phone, gstin, city)
            },
            onLogin = { identifier, role ->
                CalifncoRepository.login(identifier, role)
            },
            onGoogleLogin = { role ->
                CalifncoRepository.loginWithGoogle(role)
            },
            onBackToWelcome = { showAuthScreen = false }
        )
    }
}

@Composable
fun CalifncoApp() {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentUser by CalifncoRepository.currentUser.collectAsStateWithLifecycle()
    val currentRole by CalifncoRepository.currentRole.collectAsStateWithLifecycle()
    val isDarkMode by CalifncoRepository.isDarkMode.collectAsStateWithLifecycle()
    val cartItems by CalifncoRepository.cart.collectAsStateWithLifecycle()
    val orders by CalifncoRepository.orders.collectAsStateWithLifecycle()
    val serviceBookings by CalifncoRepository.serviceBookings.collectAsStateWithLifecycle()
    val dealerApplications by CalifncoRepository.dealerApplications.collectAsStateWithLifecycle()
    val notifications by CalifncoRepository.notifications.collectAsStateWithLifecycle()
    val aiPosts by CalifncoRepository.aiPosts.collectAsStateWithLifecycle()
    val referralCount by CalifncoRepository.referralCount.collectAsStateWithLifecycle()
    val referralRewards by CalifncoRepository.referralRewards.collectAsStateWithLifecycle()
    val compareList by CalifncoRepository.compareList.collectAsStateWithLifecycle()

    var currentScreen by remember { mutableStateOf(NavDestination.CATALOG) }
    var selectedProductForDetail by remember { mutableStateOf<Product?>(null) }
    var showRoleModal by remember { mutableStateOf(false) }
    var showNotificationDialog by remember { mutableStateOf(false) }
    var showCompareDialog by remember { mutableStateOf(false) }

    // Back handling
    if (selectedProductForDetail != null) {
        BackHandler {
            selectedProductForDetail = null
        }
    } else if (currentScreen != NavDestination.CATALOG) {
        BackHandler {
            currentScreen = NavDestination.CATALOG
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CalifncoTopBar(
                role = currentRole,
                unreadNotifCount = notifications.size,
                isDarkMode = isDarkMode,
                onToggleDarkMode = { CalifncoRepository.toggleDarkMode() },
                onRoleClick = { showRoleModal = true },
                onNotificationClick = { showNotificationDialog = true },
                onTdsClick = {
                    selectedProductForDetail = null
                    currentScreen = NavDestination.TDS_LAB
                },
                onCompareClick = {
                    if (compareList.isEmpty()) {
                        // Preload 2 popular purifiers for instant evaluation
                        CalifncoRepository.toggleCompare(CalifncoRepository.products[0])
                        CalifncoRepository.toggleCompare(CalifncoRepository.products[1])
                    }
                    showCompareDialog = true
                },
                onAiHubClick = {
                    selectedProductForDetail = null
                    currentScreen = NavDestination.AI_HUB
                }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("main_navigation_bar"),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                // Catalog Tab
                NavigationBarItem(
                    selected = currentScreen == NavDestination.CATALOG && selectedProductForDetail == null,
                    onClick = {
                        selectedProductForDetail = null
                        currentScreen = NavDestination.CATALOG
                    },
                    icon = {
                        Icon(
                            imageVector = if (currentRole == UserRole.DEALER) Icons.Default.Storefront else Icons.Default.WaterDrop,
                            contentDescription = "Catalog"
                        )
                    },
                    label = {
                        Text(
                            text = if (currentRole == UserRole.DEALER) "Wholesale" else "Store",
                            fontSize = 11.sp
                        )
                    }
                )

                // Cart Tab
                NavigationBarItem(
                    selected = currentScreen == NavDestination.CART && selectedProductForDetail == null,
                    onClick = {
                        selectedProductForDetail = null
                        currentScreen = NavDestination.CART
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (cartItems.isNotEmpty()) {
                                    Badge {
                                        Text("${cartItems.sumOf { it.quantity }}")
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    },
                    label = { Text("Cart", fontSize = 11.sp) }
                )

                // Orders Tab
                NavigationBarItem(
                    selected = currentScreen == NavDestination.ORDERS && selectedProductForDetail == null,
                    onClick = {
                        selectedProductForDetail = null
                        currentScreen = NavDestination.ORDERS
                    },
                    icon = {
                        Icon(Icons.Default.ReceiptLong, contentDescription = "Orders")
                    },
                    label = { Text("Orders", fontSize = 11.sp) }
                )

                // Service Desk Tab
                NavigationBarItem(
                    selected = currentScreen == NavDestination.SERVICE && selectedProductForDetail == null,
                    onClick = {
                        selectedProductForDetail = null
                        currentScreen = NavDestination.SERVICE
                    },
                    icon = {
                        Icon(Icons.Default.Build, contentDescription = "Service")
                    },
                    label = { Text("Service", fontSize = 11.sp) }
                )

                // TDS Lab Tab (New Feature)
                NavigationBarItem(
                    selected = currentScreen == NavDestination.TDS_LAB && selectedProductForDetail == null,
                    onClick = {
                        selectedProductForDetail = null
                        currentScreen = NavDestination.TDS_LAB
                    },
                    icon = {
                        Icon(Icons.Default.Science, contentDescription = "TDS Lab")
                    },
                    label = { Text("TDS Lab", fontSize = 11.sp) }
                )

                // Admin Portal Tab (shown when in Admin role or accessible directly)
                if (currentRole == UserRole.ADMIN) {
                    NavigationBarItem(
                        selected = currentScreen == NavDestination.ADMIN && selectedProductForDetail == null,
                        onClick = {
                            selectedProductForDetail = null
                            currentScreen = NavDestination.ADMIN
                        },
                        icon = {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin", tint = PurpleAccent)
                        },
                        label = { Text("Admin", fontSize = 11.sp) }
                    )
                } else {
                    // Profile Tab
                    NavigationBarItem(
                        selected = currentScreen == NavDestination.PROFILE && selectedProductForDetail == null,
                        onClick = {
                            selectedProductForDetail = null
                            currentScreen = NavDestination.PROFILE
                        },
                        icon = {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                        },
                        label = { Text("Profile", fontSize = 11.sp) }
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (selectedProductForDetail != null) {
                ProductDetailScreen(
                    product = selectedProductForDetail!!,
                    role = currentRole,
                    onBack = { selectedProductForDetail = null },
                    onAddToCart = { prod, qty ->
                        CalifncoRepository.addToCart(prod, qty)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Added $qty × ${prod.name} to cart")
                        }
                    },
                    onBuyNow = { prod, qty, paymentId, method ->
                        CalifncoRepository.addToCart(prod, qty)
                        val order = CalifncoRepository.placeOrder(
                            customerName = currentUser?.name ?: "Piyush Nagbansi",
                            customerPhone = currentUser?.phone ?: "+91 98765 43210",
                            address = "Flat 402, Green Valley Apartments, Andheri West, Mumbai, MH - 400053",
                            paymentMethod = "Razorpay ($method)",
                            razorpayPaymentId = paymentId
                        )
                        selectedProductForDetail = null
                        currentScreen = NavDestination.ORDERS
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Order #${order.id} placed! Payment Verified via Razorpay.")
                        }
                    },
                    onServiceBookingClick = { prod ->
                        selectedProductForDetail = null
                        currentScreen = NavDestination.SERVICE
                    }
                )
            } else {
                when (currentScreen) {
                    NavDestination.CATALOG -> {
                        CatalogScreen(
                            products = CalifncoRepository.products,
                            role = currentRole,
                            onProductClick = { prod ->
                                selectedProductForDetail = prod
                            },
                            onAddToCart = { prod ->
                                CalifncoRepository.addToCart(prod, 1)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Added ${prod.name} to cart")
                                }
                            },
                            onSwitchToDealer = {
                                CalifncoRepository.setRole(UserRole.DEALER)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Switched to Dealer Wholesale Mode! 30-40% discount active.")
                                }
                            }
                        )
                    }

                    NavDestination.CART -> {
                        CartScreen(
                            cartItems = cartItems,
                            role = currentRole,
                            onUpdateQuantity = { prodId, newQty ->
                                CalifncoRepository.updateQuantity(prodId, newQty)
                            },
                            onRemoveItem = { prodId ->
                                CalifncoRepository.removeFromCart(prodId)
                            },
                            onCheckoutSuccess = { paymentId, method, address, discount, gstin ->
                                val order = CalifncoRepository.placeOrder(
                                    customerName = currentUser?.name ?: (if (currentRole == UserRole.DEALER) "AquaTech Solutions Ltd (Dealer)" else "Piyush Nagbansi"),
                                    customerPhone = currentUser?.phone ?: "+91 98765 43210",
                                    address = address,
                                    paymentMethod = "Razorpay ($method)",
                                    razorpayPaymentId = paymentId,
                                    discount = discount,
                                    gstin = gstin
                                )
                                currentScreen = NavDestination.ORDERS
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Success! Order #${order.id} confirmed. SMS & Email alert dispatched.")
                                }
                            },
                            onExploreCatalog = {
                                currentScreen = NavDestination.CATALOG
                            }
                        )
                    }

                    NavDestination.ORDERS -> {
                        OrdersScreen(
                            orders = orders,
                            role = currentRole,
                            onBookServiceForOrder = {
                                currentScreen = NavDestination.SERVICE
                            }
                        )
                    }

                    NavDestination.SERVICE -> {
                        ServiceDeskScreen(
                            bookings = serviceBookings,
                            onBookService = { name, phone, address, model, type, date, slot, notes ->
                                CalifncoRepository.bookService(
                                    customerName = name,
                                    customerPhone = phone,
                                    address = address,
                                    purifierModel = model,
                                    serviceType = type,
                                    preferredDate = date,
                                    preferredSlot = slot,
                                    notes = notes
                                )
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Service ticket created! SMS confirmation sent.")
                                }
                            }
                        )
                    }

                    NavDestination.TDS_LAB -> {
                        TdsAnalyzerScreen(
                            onProductClick = { prod ->
                                selectedProductForDetail = prod
                            },
                            onAddToCart = { prod ->
                                CalifncoRepository.addToCart(prod, 1)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Added ${prod.name} to cart")
                                }
                            },
                            onBookWaterTest = {
                                currentScreen = NavDestination.SERVICE
                            }
                        )
                    }

                    NavDestination.AI_HUB -> {
                        AiAutomationScreen(
                            products = CalifncoRepository.products,
                            aiPosts = aiPosts,
                            onGeneratePost = { prod ->
                                CalifncoRepository.generateDailyAiPost(prod)
                            },
                            onPublishPost = { postId ->
                                CalifncoRepository.publishSocialPost(postId)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Published to Instagram via Meta Graph API!")
                                }
                            },
                            onBroadcastWaha = { prod ->
                                val msg = CalifncoRepository.broadcastWhatsAppStatus(prod)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Broadcasted to WhatsApp Status via WAHA Docker!")
                                }
                                msg
                            }
                        )
                    }

                    NavDestination.PROFILE -> {
                        ProfileScreen(
                            user = currentUser,
                            currentRole = currentRole,
                            onRoleChange = { role ->
                                CalifncoRepository.setRole(role)
                                if (role == UserRole.ADMIN) {
                                    currentScreen = NavDestination.ADMIN
                                }
                            },
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { CalifncoRepository.toggleDarkMode() },
                            referralCode = CalifncoRepository.referralCode,
                            referralCount = referralCount,
                            referralRewards = referralRewards,
                            onApplyReferral = {
                                CalifncoRepository.applyReferralBonus()
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Referral Bonus: ₹500 credited to wallet!")
                                }
                            },
                            onSubmitDealerApp = { bName, cPerson, phone, email, gstin, city, volume ->
                                CalifncoRepository.submitDealerApplication(bName, cPerson, phone, email, gstin, city, volume)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Dealer Application submitted for review!")
                                }
                            },
                            onNavigateToAiHub = {
                                currentScreen = NavDestination.AI_HUB
                            },
                            onNavigateToTds = {
                                currentScreen = NavDestination.TDS_LAB
                            },
                            onLogout = {
                                CalifncoRepository.logout()
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Logged out successfully.")
                                }
                            }
                        )
                    }

                    NavDestination.ADMIN -> {
                        AdminPortalScreen(
                            orders = orders,
                            dealerApps = dealerApplications,
                            serviceBookings = serviceBookings,
                            onUpdateOrderStatus = { orderId, status ->
                                CalifncoRepository.updateOrderStatus(orderId, status)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Order $orderId status updated to ${status.label}")
                                }
                            },
                            onUpdateDealerStatus = { dealerId, status ->
                                CalifncoRepository.updateDealerStatus(dealerId, status)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Dealer $dealerId status: ${status.name}")
                                }
                            },
                            onUpdateServiceStatus = { ticketId, status, techName ->
                                CalifncoRepository.updateServiceStatus(ticketId, status, techName)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Service Ticket $ticketId: ${status.label}")
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // Role Switcher Modal Dialog
    if (showRoleModal) {
        Dialog(onDismissRequest = { showRoleModal = false }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("role_modal_dialog")
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Select Application Role",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Switch between the 3 core personas evaluated in the assignment:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    val roles = listOf(
                        Triple(UserRole.CUSTOMER, "1. Customer Store", "Retail pricing, 50+ item catalog, Cart & Razorpay checkout"),
                        Triple(UserRole.DEALER, "2. Dealer Wholesaler", "Wholesale discounted prices, bulk MOQs & profit margin calculator"),
                        Triple(UserRole.ADMIN, "3. Admin Panel Desk", "Manage all orders, approve/reject dealers & assign technicians")
                    )

                    roles.forEach { (role, title, desc) ->
                        val isSelected = currentRole == role
                        Surface(
                            onClick = {
                                CalifncoRepository.setRole(role)
                                showRoleModal = false
                                if (role == UserRole.ADMIN) {
                                    currentScreen = NavDestination.ADMIN
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) {
                                when (role) {
                                    UserRole.CUSTOMER -> OceanBlueLight
                                    UserRole.DEALER -> AquaTealLight
                                    UserRole.ADMIN -> Color(0xFFF3E8FF)
                                }
                            } else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = {
                                        CalifncoRepository.setRole(role)
                                        showRoleModal = false
                                        if (role == UserRole.ADMIN) {
                                            currentScreen = NavDestination.ADMIN
                                        }
                                    }
                                )
                                Column {
                                    Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(desc, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { showRoleModal = false },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }

    // Notifications Drawer Dialog
    if (showNotificationDialog) {
        NotificationDialog(
            notifications = notifications,
            onDismiss = { showNotificationDialog = false }
        )
    }

    // Compare Purifiers Dialog
    if (showCompareDialog) {
        ComparePurifiersDialog(
            products = compareList,
            role = currentRole,
            onAddToCart = { prod ->
                CalifncoRepository.addToCart(prod, 1)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Added ${prod.name} to cart")
                }
            },
            onDismiss = { showCompareDialog = false }
        )
    }
}
