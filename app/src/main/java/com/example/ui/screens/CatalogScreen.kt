package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Product
import com.example.data.model.ProductCategory
import com.example.data.model.UserRole
import com.example.ui.components.ImageLightboxDialog
import com.example.ui.components.ProductCard
import com.example.ui.components.SmartFinderDialog
import com.example.ui.theme.*

@Composable
fun CatalogScreen(
    products: List<Product>,
    role: UserRole,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit,
    onSwitchToDealer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ProductCategory.ALL) }
    var selectedSort by remember { mutableStateOf("Popularity") }
    var zoomImageUrl by remember { mutableStateOf<String?>(null) }
    var zoomProductName by remember { mutableStateOf("") }
    var showSmartFinderDialog by remember { mutableStateOf(false) }

    // Filter & Sort Products
    val filteredProducts = remember(searchQuery, selectedCategory, selectedSort, products) {
        var list = products

        if (selectedCategory != ProductCategory.ALL) {
            list = list.filter { it.category == selectedCategory }
        }

        if (searchQuery.isNotBlank()) {
            val q = searchQuery.trim().lowercase()
            list = list.filter {
                it.name.lowercase().contains(q) ||
                        it.subCategory.lowercase().contains(q) ||
                        it.description.lowercase().contains(q)
            }
        }

        when (selectedSort) {
            "Price: Low to High" -> list.sortedBy { if (role == UserRole.DEALER) it.wholesalePrice else it.retailPrice }
            "Price: High to Low" -> list.sortedByDescending { if (role == UserRole.DEALER) it.wholesalePrice else it.retailPrice }
            "Rating" -> list.sortedByDescending { it.rating }
            else -> list.sortedByDescending { it.reviewCount }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("catalog_screen")
    ) {
        // Ultra-Compact Announcement / Dealer Notice Banner (36-40 dp)
        Surface(
            color = if (role == UserRole.DEALER) AquaTealLight else OceanBlueLight,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (role == UserRole.DEALER) Icons.Default.Storefront else Icons.Default.WaterDrop,
                        contentDescription = "Notice",
                        tint = if (role == UserRole.DEALER) AquaTealDark else OceanBluePrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (role == UserRole.DEALER) "Wholesale B2B Active (Up to 40% Margin)" else "Free Home Installation on all RO Purifiers",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (role == UserRole.DEALER) AquaTealDark else OceanBlueDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (role != UserRole.DEALER) {
                    Text(
                        text = "Dealer?",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = OceanBluePrimary,
                        modifier = Modifier
                            .clickable { onSwitchToDealer() }
                            .padding(2.dp)
                    )
                }
            }
        }

        // Compact Search Bar & Filter Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search 50+ purifiers, filters, spares...", fontSize = 12.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(14.dp))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("catalog_search_bar")
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Category Chips Row (Horizontal Scrollable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ProductCategory.values().forEach { category ->
                    val isSelected = selectedCategory == category
                    val count = if (category == ProductCategory.ALL) products.size else products.count { it.category == category }

                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = "${category.displayName} ($count)",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (role == UserRole.DEALER) AquaTealSecondary else OceanBluePrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.height(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Ultra-Compact Smart Purifier Finder Card (~52 dp height)
            Card(
                onClick = { showSmartFinderDialog = true },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = OceanBlueLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("open_smart_finder_btn")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = OceanBluePrimary, modifier = Modifier.size(16.dp))
                        Text("Find Your Perfect Purifier in 30 sec (Quiz)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = OceanBlueDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = OceanBluePrimary, modifier = Modifier.size(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Horizontal Scrolling Offer Ticker Carousel (Smooth Scroll System)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("promo_scroll_carousel")
            ) {
                items(
                    listOf(
                        Triple("⚡ Free Installation", "On all RO Water Purifiers", OceanBluePrimary),
                        Triple("🔥 Dealer B2B Mode", "Up to 40% Wholesale Margin", AquaTealDark),
                        Triple("🛡️ Califnco Care AMC", "100% Free Filter Replacements", PurpleAccent),
                        Triple("🎁 Referral Bonus", "Earn ₹500 Per Friend Invited", SuccessGreen)
                    )
                ) { (title, subtitle, color) ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)),
                        modifier = Modifier
                            .width(200.dp)
                            .height(64.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = color)
                            Text(text = subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Sort & Results Counter Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredProducts.size} Products Found",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Sort:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    var sortExpanded by remember { mutableStateOf(false) }
                    Box {
                        TextButton(
                            onClick = { sortExpanded = true },
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = selectedSort,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Sort",
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = sortExpanded,
                            onDismissRequest = { sortExpanded = false }
                        ) {
                            listOf("Popularity", "Price: Low to High", "Price: High to Low", "Rating").forEach { sortOpt ->
                                DropdownMenuItem(
                                    text = { Text(sortOpt, fontSize = 13.sp) },
                                    onClick = {
                                        selectedSort = sortOpt
                                        sortExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2-Column Product Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredProducts, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    role = role,
                    onCardClick = { onProductClick(product) },
                    onAddToCart = { onAddToCart(product) },
                    onImageZoomClick = {
                        zoomImageUrl = product.imageUrl
                        zoomProductName = product.name
                    }
                )
            }
        }
    }

    // Lightbox Dialog
    zoomImageUrl?.let { imgUrl ->
        ImageLightboxDialog(
            imageUrl = imgUrl,
            productName = zoomProductName,
            onDismiss = { zoomImageUrl = null }
        )
    }

    // Smart Purifier Finder Modal
    if (showSmartFinderDialog) {
        SmartFinderDialog(
            onProductSelected = { prod ->
                showSmartFinderDialog = false
                onProductClick(prod)
            },
            onAddToCart = { prod ->
                onAddToCart(prod)
            },
            onDismiss = { showSmartFinderDialog = false }
        )
    }
}
