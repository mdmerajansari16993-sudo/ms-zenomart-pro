package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.Product
import com.example.ui.ZenoMartViewModel
import com.example.ui.components.AdminDashboard
import com.example.ui.components.BottomFooterSection
import com.example.ui.components.CartBottomSheet
import com.example.ui.components.CategoryQuickGrid
import com.example.ui.components.HeroBannerCarousel
import com.example.ui.components.OrderSuccessDialog
import com.example.ui.components.PaymentQrDialog
import com.example.ui.components.ProductCard
import com.example.ui.components.ProfileDialog
import com.example.ui.components.TopStickyNavBar
import com.example.ui.components.TrustBadgesSection
import com.example.ui.components.WishlistBottomSheet
import com.example.ui.components.ZenoMartWebView
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.MSZenoMartTheme
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateLight
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MSZenoMartTheme {
                MSZenoMartApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MSZenoMartApp(viewModel: ZenoMartViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // State collections from ViewModel
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val allProducts by viewModel.products.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val wishlistIds by viewModel.wishlistIds.collectAsState()
    val promoDiscountPercent by viewModel.promoDiscountPercent.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val users by viewModel.users.collectAsState()
    val lastMessage by viewModel.lastMessage.collectAsState()

    // UI state
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Storefront, 1: Admin Dashboard, 2: Web Template
    var showCartSheet by remember { mutableStateOf(false) }
    var showWishlistSheet by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var showPaymentQrDialog by remember { mutableStateOf(false) }
    var showOrderSuccessDialog by remember { mutableStateOf(false) }
    var pendingOrderNumber by remember { mutableStateOf("") }
    var pendingOrderAmount by remember { mutableDoubleStateOf(0.0) }
    var isSortDropdownOpen by remember { mutableStateOf(false) }

    val cartSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val wishlistSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val listState = rememberLazyListState()

    val totalCartCount = cartItems.sumOf { it.quantity }
    val cartSubtotal = cartItems.sumOf { it.product.price * it.quantity }

    // Listen for feedback messages to show snackbars with instant VIEW CART action
    LaunchedEffect(lastMessage) {
        lastMessage?.let { msg ->
            if (msg.contains("cart", ignoreCase = true) && !msg.contains("removed", ignoreCase = true)) {
                val result = snackbarHostState.showSnackbar(
                    message = msg,
                    actionLabel = "VIEW CART",
                    duration = SnackbarDuration.Short
                )
                if (result == SnackbarResult.ActionPerformed) {
                    showCartSheet = true
                }
            } else {
                snackbarHostState.showSnackbar(msg)
            }
            viewModel.clearMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (selectedTab == 0 && totalCartCount > 0) {
                FloatingActionButton(
                    onClick = { showCartSheet = true },
                    containerColor = OrangeAccent,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("floating_cart_fab")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$totalCartCount item${if (totalCartCount > 1) "s" else ""}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        },
        topBar = {
            Column {
                TopStickyNavBar(
                    searchQuery = searchQuery,
                    onSearchChanged = viewModel::onSearchQueryChanged,
                    selectedCategory = selectedCategory,
                    onCategoryChanged = viewModel::onCategorySelected,
                    cartCount = totalCartCount,
                    wishlistCount = wishlistIds.size,
                    cartSubtotal = cartSubtotal,
                    onCartClicked = { showCartSheet = true },
                    onWishlistClicked = { showWishlistSheet = true },
                    onProfileClicked = { showProfileDialog = true }
                )

                // Navigation Mode Switcher (Storefront vs Owner Admin Panel vs Web Template)
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = NavyDark,
                    contentColor = GoldAccent,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = GoldAccent
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text("Storefront", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text("Admin Panel", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                if (orders.isNotEmpty()) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(OrangeAccent)
                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                    ) {
                                        Text(
                                            text = "${orders.size}",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }
                            }
                        }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                                Text("Web View", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SlateLight)
        ) {
            when (selectedTab) {
                1 -> {
                    AdminDashboard(
                        orders = orders,
                        products = allProducts,
                        users = users,
                        onBackToStore = { selectedTab = 0 },
                        onAddNewProduct = viewModel::addNewProduct,
                        onDeleteProduct = viewModel::deleteProduct,
                        onToggleProductStock = viewModel::toggleProductStock,
                        onUpdateOrderStatus = viewModel::updateOrderStatus,
                        onToggleUserStatus = viewModel::toggleUserStatus,
                        onAddNewUser = viewModel::addNewUser
                    )
                }
                2 -> {
                    // Live HTML/CSS/JS single-file front-end template preview inside the app!
                    ZenoMartWebView()
                }
                else -> {
                    // Native Jetpack Compose store view
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                    // Promotional Banner / Hero Carousel
                    item {
                        HeroBannerCarousel(
                            onShopNowClicked = {
                                coroutineScope.launch {
                                    listState.animateScrollToItem(3)
                                }
                            }
                        )
                    }

                    // Category Grid / Quick Icons
                    item {
                        CategoryQuickGrid(
                            categories = viewModel.sampleCategories,
                            selectedCategory = selectedCategory,
                            onCategorySelected = viewModel::onCategorySelected
                        )
                    }

                    // Section Header: Featured Products & Sort Dropdown
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "POPULAR PICKS",
                                    color = OrangeAccent,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = if (selectedCategory == "all") "Trending Products" else "$selectedCategory Collection",
                                    color = NavyPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            // Sort Option Selector
                            Box {
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White)
                                        .border(1.dp, SlateBorder, RoundedCornerShape(8.dp))
                                        .clickable { isSortDropdownOpen = true }
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Sort,
                                        contentDescription = "Sort",
                                        tint = NavyPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = when (sortOption) {
                                            "price-low" -> "Price: Low to High"
                                            "price-high" -> "Price: High to Low"
                                            "rating" -> "Top Rated"
                                            else -> "Featured"
                                        },
                                        color = NavyPrimary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                DropdownMenu(
                                    expanded = isSortDropdownOpen,
                                    onDismissRequest = { isSortDropdownOpen = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Featured", fontSize = 12.sp) },
                                        onClick = {
                                            viewModel.onSortChanged("featured")
                                            isSortDropdownOpen = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Price: Low to High", fontSize = 12.sp) },
                                        onClick = {
                                            viewModel.onSortChanged("price-low")
                                            isSortDropdownOpen = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Price: High to Low", fontSize = 12.sp) },
                                        onClick = {
                                            viewModel.onSortChanged("price-high")
                                            isSortDropdownOpen = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Top Rated", fontSize = 12.sp) },
                                        onClick = {
                                            viewModel.onSortChanged("rating")
                                            isSortDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Empty search or filter fallback
                    if (filteredProducts.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = null,
                                        tint = Color(0xFF94A3B8),
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "No products found for \"$searchQuery\"",
                                        color = NavyPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Try clearing search or picking another category.",
                                        color = Color(0xFF64748B),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    // Product Grid (2 columns per row)
                    val chunkedProducts = filteredProducts.chunked(2)
                    items(chunkedProducts, key = { chunk -> chunk.map { it.id }.joinToString("_") }) { rowProducts ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            for (product in rowProducts) {
                                Box(modifier = Modifier.weight(1f)) {
                                    ProductCard(
                                        product = product,
                                        isWishlisted = wishlistIds.contains(product.id),
                                        onAddToCart = viewModel::addToCart,
                                        onToggleWishlist = viewModel::toggleWishlist
                                    )
                                }
                            }
                            if (rowProducts.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    // Trust Badges Section
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        TrustBadgesSection()
                    }

                    // Bottom Footer with Founder Recognition & Trust Badges
                    item {
                        BottomFooterSection()
                    }
                }
            }
        }
    }
}

    // Cart Bottom Sheet
    if (showCartSheet) {
        CartBottomSheet(
            sheetState = cartSheetState,
            cartItems = cartItems,
            promoDiscountPercent = promoDiscountPercent,
            onQuantityChange = viewModel::updateCartQuantity,
            onRemoveItem = viewModel::removeFromCart,
            onApplyPromo = viewModel::applyPromoCode,
            onCheckout = {
                val subtotal = cartItems.sumOf { it.product.price * it.quantity }
                val discount = subtotal * (promoDiscountPercent / 100.0)
                val tax = if (subtotal > 0) (subtotal - discount) * 0.05 else 0.0
                val shipping = if (subtotal >= 50.0 || subtotal == 0.0) 0.0 else 9.99
                pendingOrderAmount = (subtotal - discount + tax + (if (subtotal > 0) shipping else 0.0)).coerceAtLeast(0.0)
                pendingOrderNumber = (100000..999999).random().toString()
                coroutineScope.launch {
                    cartSheetState.hide()
                    showCartSheet = false
                    showPaymentQrDialog = true
                }
            },
            onDismiss = {
                showCartSheet = false
            }
        )
    }

    // Simulated Secure UPI Payment QR Dialog
    if (showPaymentQrDialog) {
        PaymentQrDialog(
            totalAmount = pendingOrderAmount,
            orderNumber = pendingOrderNumber,
            onPaymentSuccess = {
                val subtotal = cartItems.sumOf { it.product.price * it.quantity }
                val discount = subtotal * (promoDiscountPercent / 100.0)
                val tax = if (subtotal > 0) (subtotal - discount) * 0.05 else 0.0
                val shipping = if (subtotal >= 50.0 || subtotal == 0.0) 0.0 else 9.99

                viewModel.recordOrder(
                    orderId = pendingOrderNumber,
                    customerName = "MD Meraj Ansari",
                    customerEmail = "mdmerajansari16993@gmail.com",
                    customerPhone = "+1 (555) 789-0142",
                    shippingAddress = "MS ZenoMart HQ, Executive Suite 4B, Silicon Plaza",
                    items = cartItems,
                    subtotal = subtotal,
                    discount = discount,
                    tax = tax,
                    shippingFee = shipping,
                    totalAmount = pendingOrderAmount,
                    paymentMethod = "UPI Instant Pay"
                )
                showPaymentQrDialog = false
                showOrderSuccessDialog = true
            },
            onDismiss = {
                showPaymentQrDialog = false
            }
        )
    }

    // Order Success Confirmation Dialog
    if (showOrderSuccessDialog) {
        OrderSuccessDialog(
            orderNumber = pendingOrderNumber,
            totalAmount = pendingOrderAmount,
            onDismiss = {
                showOrderSuccessDialog = false
            },
            onViewInAdmin = {
                showOrderSuccessDialog = false
                selectedTab = 1
            }
        )
    }

    // Wishlist Bottom Sheet
    if (showWishlistSheet) {
        val savedProducts = allProducts.filter { wishlistIds.contains(it.id) }
        WishlistBottomSheet(
            sheetState = wishlistSheetState,
            savedProducts = savedProducts,
            onAddToCart = { product ->
                viewModel.addToCart(product)
            },
            onRemoveWishlist = viewModel::toggleWishlist,
            onDismiss = {
                showWishlistSheet = false
            }
        )
    }

    // Founder Profile Dialog
    if (showProfileDialog) {
        ProfileDialog(
            onDismiss = { showProfileDialog = false },
            onOpenAdmin = {
                showProfileDialog = false
                selectedTab = 1
            }
        )
    }
}
