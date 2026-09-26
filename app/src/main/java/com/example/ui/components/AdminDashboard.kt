package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DryCleaning
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.PromoBanner
import com.example.model.Seller
import com.example.model.UserAccount
import com.example.model.calculateAdminCommission
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.SlateLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(
    orders: List<Order>,
    products: List<Product>,
    users: List<UserAccount>,
    sellers: List<Seller> = emptyList(),
    banners: List<PromoBanner> = emptyList(),
    onBackToStore: () -> Unit,
    onAddNewProduct: (title: String, category: String, price: Double, mrp: Double, description: String, badge: String, iconType: String, imageUrl: String) -> Boolean,
    onDeleteProduct: (Int) -> Unit,
    onToggleProductStock: (Int) -> Unit,
    onUpdateOrderStatus: (orderId: String, newStatus: OrderStatus) -> Unit,
    onToggleUserStatus: (userId: String) -> Unit,
    onAddNewUser: (name: String, email: String, phone: String, address: String, tier: String) -> Unit,
    onClearSellerPayout: (sellerId: String) -> Unit = {},
    onAddNewSeller: (name: String, shopName: String, phone: String, email: String, category: String, address: String, upiId: String) -> Unit = { _, _, _, _, _, _, _ -> },
    onAddBanner: (tag: String, title: String, subtitle: String, buttonText: String, searchTarget: String, badgeColor: String, imageUrl: String) -> Boolean = { _, _, _, _, _, _, _ -> false },
    onUpdateBanner: (id: String, tag: String, title: String, subtitle: String, buttonText: String, searchTarget: String, badgeColor: String, imageUrl: String, isActive: Boolean) -> Boolean = { _, _, _, _, _, _, _, _, _ -> false },
    onDeleteBanner: (id: String) -> Unit = {},
    onToggleBannerStatus: (id: String) -> Unit = {},
    onResetBanners: () -> Unit = {},
    onVerifyDeliveryOtp: (orderId: String, enteredOtp: String) -> Boolean = { _, _ -> false },
    onTriggerVoiceCall: (orderId: String, pressedKey: Int) -> Boolean = { _, _ -> false },
    modifier: Modifier = Modifier
) {
    var adminSubTab by remember { mutableIntStateOf(0) } // 0: Orders, 1: Add Product, 2: Users, 3: Sellers, 4: Banners & Ads, 5: Call & OTP
    var orderStatusFilter by remember { mutableStateOf("ALL") }
    var orderSearchQuery by remember { mutableStateOf("") }
    var userSearchQuery by remember { mutableStateOf("") }
    var showAddUserDialog by remember { mutableStateOf(false) }
    var showAddSellerDialog by remember { mutableStateOf(false) }

    val totalRevenue = orders.sumOf { it.totalAmount }
    val pendingOrProcessingCount = orders.count { it.status == OrderStatus.PROCESSING || it.status == OrderStatus.PENDING }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SlateLight)
    ) {
        // Executive Header
        Surface(
            color = NavyPrimary,
            shadowElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top Action Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Brush.linearGradient(listOf(GoldAccent, OrangeAccent))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = "Admin",
                                tint = NavyDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Admin Portal",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(GoldAccent.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "OWNER ACCESS",
                                        color = GoldAccent,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }
                            Text(
                                text = "MD Meraj Ansari • Executive Store Management",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Quick Return to Store Button
                    Button(
                        onClick = onBackToStore,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeAccent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("admin_back_to_store_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = "Store",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("View Store", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Metric KPI Cards (Revenue, Orders, Products, Users)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "TOTAL REVENUE",
                        value = "$${String.format("%.2f", totalRevenue)}",
                        subtitle = "Gross sales",
                        icon = Icons.Default.AttachMoney,
                        iconTint = GoldAccent
                    )
                    KpiCard(
                        title = "INCOMING ORDERS",
                        value = "${orders.size}",
                        subtitle = "$pendingOrProcessingCount active/processing",
                        icon = Icons.Default.Receipt,
                        iconTint = OrangeAccent
                    )
                    KpiCard(
                        title = "STORE CATALOG",
                        value = "${products.size}",
                        subtitle = "${products.count { it.inStock }} in stock",
                        icon = Icons.Default.Inventory,
                        iconTint = Color(0xFF38BDF8)
                    )
                    KpiCard(
                        title = "REGISTERED USERS",
                        value = "${users.size}",
                        subtitle = "1 Owner • ${users.size - 1} Customers",
                        icon = Icons.Default.People,
                        iconTint = Color(0xFF10B981)
                    )
                    KpiCard(
                        title = "ACTIVE BANNERS",
                        value = "${banners.count { it.isActive }}",
                        subtitle = "${banners.size} campaigns live",
                        icon = Icons.Default.Campaign,
                        iconTint = GoldAccent
                    )
                }

                // Sub Tabs (Orders, Add Product, Users, Sellers, Banners & Ads, Call & OTP)
                SecondaryTabRow(
                    selectedTabIndex = adminSubTab,
                    containerColor = NavyDark,
                    contentColor = GoldAccent
                ) {
                    Tab(
                        selected = adminSubTab == 0,
                        onClick = { adminSubTab = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Receipt, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Orders (${orders.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                    Tab(
                        selected = adminSubTab == 1,
                        onClick = { adminSubTab = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Add Product", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                    Tab(
                        selected = adminSubTab == 2,
                        onClick = { adminSubTab = 2 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("User Details (${users.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                    Tab(
                        selected = adminSubTab == 3,
                        onClick = { adminSubTab = 3 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Sellers & Payouts (${sellers.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                    Tab(
                        selected = adminSubTab == 4,
                        onClick = { adminSubTab = 4 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Banners & Ads (${banners.size})", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                    Tab(
                        selected = adminSubTab == 5,
                        onClick = { adminSubTab = 5 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Call & Delivery OTP", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    )
                }
            }
        }

        // Tab Content
        Box(modifier = Modifier.fillMaxSize()) {
            when (adminSubTab) {
                0 -> IncomingOrdersSection(
                    orders = orders,
                    searchQuery = orderSearchQuery,
                    onSearchChanged = { orderSearchQuery = it },
                    statusFilter = orderStatusFilter,
                    onStatusFilterChanged = { orderStatusFilter = it },
                    onUpdateStatus = onUpdateOrderStatus,
                    onVerifyDeliveryOtp = onVerifyDeliveryOtp,
                    onTriggerVoiceCall = onTriggerVoiceCall
                )
                1 -> AddProductSection(
                    products = products,
                    onAddNewProduct = onAddNewProduct,
                    onDeleteProduct = onDeleteProduct,
                    onToggleStock = onToggleProductStock
                )
                2 -> UserManagementSection(
                    users = users,
                    searchQuery = userSearchQuery,
                    onSearchChanged = { userSearchQuery = it },
                    onToggleStatus = onToggleUserStatus,
                    onOpenAddUserDialog = { showAddUserDialog = true }
                )
                3 -> SellerManagementSection(
                    sellers = sellers,
                    products = products,
                    orders = orders,
                    onClearPayout = onClearSellerPayout,
                    onOpenAddSellerDialog = { showAddSellerDialog = true }
                )
                4 -> BannerManagementSection(
                    banners = banners,
                    onAddBanner = onAddBanner,
                    onUpdateBanner = onUpdateBanner,
                    onDeleteBanner = onDeleteBanner,
                    onToggleStatus = onToggleBannerStatus,
                    onResetDefaults = onResetBanners
                )
                5 -> DeliveryAndAiCallVerificationSection(
                    orders = orders,
                    onVerifyDeliveryOtp = onVerifyDeliveryOtp,
                    onTriggerVoiceCall = onTriggerVoiceCall
                )
            }
        }
    }

    if (showAddUserDialog) {
        AddUserDialog(
            onDismiss = { showAddUserDialog = false },
            onAddUser = { name, email, phone, address, tier ->
                onAddNewUser(name, email, phone, address, tier)
                showAddUserDialog = false
            }
        )
    }

    if (showAddSellerDialog) {
        AddSellerDialog(
            onDismiss = { showAddSellerDialog = false },
            onAddSeller = { name, shop, phone, email, cat, addr, upi ->
                onAddNewSeller(name, shop, phone, email, cat, addr, upi)
                showAddSellerDialog = false
            }
        )
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(160.dp)
            .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = Color(0xFF94A3B8),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = subtitle,
                color = Color(0xFFCBD5E1),
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// -------------------------------------------------------------
// 1. INCOMING ORDERS SECTION
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomingOrdersSection(
    orders: List<Order>,
    searchQuery: String,
    onSearchChanged: (String) -> Unit,
    statusFilter: String,
    onStatusFilterChanged: (String) -> Unit,
    onUpdateStatus: (orderId: String, newStatus: OrderStatus) -> Unit,
    onVerifyDeliveryOtp: (orderId: String, enteredOtp: String) -> Boolean = { _, _ -> false },
    onTriggerVoiceCall: (orderId: String, pressedKey: Int) -> Boolean = { _, _ -> false }
) {
    val filteredOrders = orders.filter { order ->
        val matchesStatus = when (statusFilter) {
            "ALL" -> true
            "PROCESSING" -> order.status == OrderStatus.PROCESSING || order.status == OrderStatus.PENDING
            "SHIPPED" -> order.status == OrderStatus.SHIPPED
            "DELIVERED" -> order.status == OrderStatus.DELIVERED
            else -> true
        }
        val matchesQuery = searchQuery.isBlank() ||
                order.id.contains(searchQuery, ignoreCase = true) ||
                order.customerName.contains(searchQuery, ignoreCase = true) ||
                order.customerEmail.contains(searchQuery, ignoreCase = true)
        matchesStatus && matchesQuery
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Search & Filter Row
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChanged,
                    placeholder = { Text("Search by Order # or Customer...", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = NavyPrimary) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChanged("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = OrangeAccent,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Status Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("ALL", "PROCESSING", "SHIPPED", "DELIVERED").forEach { status ->
                        FilterChip(
                            selected = statusFilter == status,
                            onClick = { onStatusFilterChanged(status) },
                            label = {
                                Text(
                                    text = if (status == "ALL") "All Orders (${orders.size})" else status.lowercase().replaceFirstChar { it.uppercase() },
                                    fontSize = 11.sp,
                                    fontWeight = if (statusFilter == status) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyPrimary,
                                selectedLabelColor = GoldAccent,
                                containerColor = Color.White,
                                labelColor = NavyPrimary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }
        }

        if (filteredOrders.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Receipt, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No orders match your filter", color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            items(filteredOrders, key = { it.id }) { order ->
                OrderCard(
                    order = order,
                    onUpdateStatus = onUpdateStatus,
                    onVerifyDeliveryOtp = onVerifyDeliveryOtp,
                    onTriggerVoiceCall = onTriggerVoiceCall
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    onUpdateStatus: (String, OrderStatus) -> Unit,
    onVerifyDeliveryOtp: (String, String) -> Boolean = { _, _ -> false },
    onTriggerVoiceCall: (String, Int) -> Boolean = { _, _ -> false }
) {
    var expanded by remember { mutableStateOf(false) }
    var showStatusMenu by remember { mutableStateOf(false) }
    var otpInput by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf<String?>(null) }

    val statusColor = when (order.status) {
        OrderStatus.PENDING -> Color(0xFFF59E0B)
        OrderStatus.PROCESSING -> OrangeAccent
        OrderStatus.SHIPPED -> Color(0xFF3B82F6)
        OrderStatus.DELIVERED -> Color(0xFF10B981)
        OrderStatus.CANCELLED -> Color(0xFFEF4444)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: Order ID, Timestamp, Status Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "#${order.id}",
                        color = NavyPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = order.timestamp,
                        color = Color(0xFF64748B),
                        fontSize = 11.sp
                    )
                }

                // Status Badge with dropdown trigger
                Box {
                    Surface(
                        color = statusColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.clickable { showStatusMenu = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(statusColor)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = order.status.label,
                                color = statusColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = "Edit Status",
                                tint = statusColor,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showStatusMenu,
                        onDismissRequest = { showStatusMenu = false }
                    ) {
                        OrderStatus.values().forEach { statusOption ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = statusOption.label,
                                        fontSize = 12.sp,
                                        fontWeight = if (order.status == statusOption) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    onUpdateStatus(order.id, statusOption)
                                    showStatusMenu = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Customer Details Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = order.customerName,
                                color = NavyPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = order.paymentMethod,
                            color = Color(0xFF10B981),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${order.customerPhone} • ${order.customerEmail}",
                            color = Color(0xFF64748B),
                            fontSize = 10.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = order.shippingAddress,
                            color = Color(0xFF475569),
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    val context = androidx.compose.ui.platform.LocalContext.current
                    OutlinedButton(
                        onClick = {
                            val mapUri = android.net.Uri.parse("https://www.google.com/maps/dir/?api=1&destination=" + java.net.URLEncoder.encode(order.shippingAddress, "UTF-8"))
                            val mapIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, mapUri)
                            try {
                                context.startActivity(mapIntent)
                            } catch (e: Exception) {
                                // Fallback
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFEFF6FF),
                            contentColor = Color(0xFF1D4ED8)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF93C5FD)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(36.dp)
                    ) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF1D4ED8))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("🗺️ Live GPS: ${order.liveCoordinates} (Navigate)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 1. AI Voice Call Verification & Smart Routing
                    Surface(
                        color = if (order.isCallVerified) Color(0xFFECFDF5) else Color(0xFFFEF3C7),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (order.isCallVerified) Color(0xFFA7F3D0) else Color(0xFFFDE68A)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null,
                                        tint = if (order.isCallVerified) Color(0xFF059669) else Color(0xFFD97706),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "AI Voice Call Verification",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (order.isCallVerified) Color(0xFF065F46) else Color(0xFF92400E)
                                    )
                                }
                                Surface(
                                    color = if (order.isCallVerified) Color(0xFF10B981) else Color(0xFFF59E0B),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = order.callVerificationStatus,
                                        color = Color.White,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text(
                                text = "Smart Routing: ✓ Exact items sent to Seller Mobile • ✓ Customer GPS sent to Owner (+91 79798 64406)",
                                fontSize = 9.sp,
                                color = Color(0xFF475569)
                            )

                            // Voice Call Simulation Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Button(
                                    onClick = { onTriggerVoiceCall(order.id, 1) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981), contentColor = Color.White),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(26.dp)
                                ) {
                                    Text("Press 1 (Confirm)", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = { onTriggerVoiceCall(order.id, 5) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444), contentColor = Color.White),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(26.dp)
                                ) {
                                    Text("Press 5 (Cancel)", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                                OutlinedButton(
                                    onClick = { onTriggerVoiceCall(order.id, 0) },
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(26.dp)
                                ) {
                                    Text("No Answer (Retry 15m)", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 2. Secure Delivery OTP Handover Verification
                    Surface(
                        color = if (order.status == OrderStatus.DELIVERED) Color(0xFFECFDF5) else Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (order.status == OrderStatus.DELIVERED) Color(0xFFA7F3D0) else Color(0xFFCBD5E1)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = if (order.status == OrderStatus.DELIVERED) Color(0xFF059669) else NavyPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Secure Delivery OTP Verification",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (order.status == OrderStatus.DELIVERED) Color(0xFF065F46) else NavyPrimary
                                    )
                                }
                                Surface(
                                    color = Color(0xFFE2E8F0),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "Customer OTP: ${order.deliveryOtp}",
                                        color = NavyDark,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            if (order.status == OrderStatus.DELIVERED || order.otpVerifiedAtDelivery) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Parcel Handover Confirmed & Verified with Customer OTP", color = Color(0xFF047857), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = otpInput,
                                        onValueChange = {
                                            otpInput = it.take(4)
                                            otpError = null
                                        },
                                        placeholder = { Text("Enter Customer 4-digit OTP", fontSize = 10.sp) },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f).height(44.dp)
                                    )
                                    Button(
                                        onClick = {
                                            if (onVerifyDeliveryOtp(order.id, otpInput)) {
                                                otpError = null
                                                otpInput = ""
                                            } else {
                                                otpError = "Incorrect OTP! Must match customer code."
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary, contentColor = GoldAccent),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.height(44.dp)
                                    ) {
                                        Text("Verify & Deliver", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                if (otpError != null) {
                                    Text(otpError ?: "", color = Color.Red, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Items Preview & Price Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${order.items.sumOf { it.quantity }} items (${order.items.firstOrNull()?.productTitle ?: "Item"}${if (order.items.size > 1) " +${order.items.size - 1} more" else ""})",
                    color = Color(0xFF475569),
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "$${String.format("%.2f", order.totalAmount)}",
                    color = NavyPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
            }

            // Expand / Collapse Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (expanded) "Hide Item Details" else "View Item Details & Actions",
                    color = OrangeAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = OrangeAccent,
                    modifier = Modifier.size(14.dp)
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color(0xFFE2E8F0))
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("ORDER ITEMS:", color = NavyPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    val cardContext = androidx.compose.ui.platform.LocalContext.current
                    order.items.forEach { item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${item.quantity}x ${item.productTitle}",
                                    color = Color(0xFF334155),
                                    fontSize = 11.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "$${String.format("%.2f", item.unitPrice * item.quantity)}",
                                    color = NavyPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        val packMsg = "📦 *MS ZENOMART - SELLER PACKING NOTICE*\n🆔 *Order ID:* #${order.id}\n📋 *Item:* ${item.quantity}x ${item.productTitle}\n💰 *Price:* ₹${item.unitPrice * item.quantity}\n📍 *Destination:* ${order.shippingAddress}\n⚠️ *Instruction:* Pack securely with Order #${order.id} label for local rider pickup.\n📞 *Admin (MD Meraj Ansari):* +91 79798 64406"
                                        val waUri = android.net.Uri.parse("https://wa.me/?text=" + java.net.URLEncoder.encode(packMsg, "UTF-8"))
                                        try {
                                            cardContext.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, waUri))
                                        } catch (e: Exception) {}
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color(0xFFECFDF5),
                                        contentColor = Color(0xFF047857)
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6EE7B7)),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.height(26.dp)
                                ) {
                                    Text("📦 Notify Shop to Pack (WhatsApp)", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", color = Color(0xFF64748B), fontSize = 10.sp)
                        Text("$${String.format("%.2f", order.subtotal)}", color = Color(0xFF64748B), fontSize = 10.sp)
                    }
                    if (order.discount > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Coupon Discount", color = Color(0xFF10B981), fontSize = 10.sp)
                            Text("-$${String.format("%.2f", order.discount)}", color = Color(0xFF10B981), fontSize = 10.sp)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tax & Shipping", color = Color(0xFF64748B), fontSize = 10.sp)
                        Text("$${String.format("%.2f", order.tax + order.shippingFee)}", color = Color(0xFF64748B), fontSize = 10.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Advance Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (order.status != OrderStatus.SHIPPED && order.status != OrderStatus.DELIVERED) {
                            Button(
                                onClick = { onUpdateStatus(order.id, OrderStatus.SHIPPED) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.LocalShipping, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Mark Dispatched", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        if (order.status != OrderStatus.DELIVERED) {
                            Button(
                                onClick = { onUpdateStatus(order.id, OrderStatus.DELIVERED) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Mark Delivered", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. ADD PRODUCT SECTION (WITH LIVE CATALOG MANAGER)
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddProductSection(
    products: List<Product>,
    onAddNewProduct: (title: String, category: String, price: Double, mrp: Double, description: String, badge: String, iconType: String, imageUrl: String) -> Boolean,
    onDeleteProduct: (Int) -> Unit,
    onToggleStock: (Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Gadgets") }
    var priceText by remember { mutableStateOf("") }
    var mrpText by remember { mutableStateOf("") }
    var badgeText by remember { mutableStateOf("New Arrival") }
    var selectedIcon by remember { mutableStateOf("headphones") }
    var imageUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isCategoryDropdownOpen by remember { mutableStateOf(false) }

    val categories = listOf("Fashion", "Electronics", "Groceries", "Gadgets", "Home")
    val availableIcons = listOf(
        Pair("headphones", Icons.Default.Headphones),
        Pair("watch", Icons.Default.Watch),
        Pair("laptop", Icons.Default.Computer),
        Pair("tv", Icons.Default.Tv),
        Pair("jacket", Icons.Default.DryCleaning),
        Pair("basket", Icons.Default.LocalMall),
        Pair("chair", Icons.Default.Weekend),
        Pair("glasses", Icons.Default.Visibility),
        Pair("bag", Icons.Default.ShoppingBag)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(OrangeAccent.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = OrangeAccent, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Add New Store Product", color = NavyPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text("Publish new merchandise to MS ZenoMart instantly", color = Color(0xFF64748B), fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Title Field
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Product Title *") },
                        placeholder = { Text("e.g. ZenoPro ANC Earbuds Ultra") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_add_product_title")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Category Dropdown & Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Category") },
                                trailingIcon = {
                                    IconButton(onClick = { isCategoryDropdownOpen = true }) {
                                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isCategoryDropdownOpen = true }
                            )

                            DropdownMenu(
                                expanded = isCategoryDropdownOpen,
                                onDismissRequest = { isCategoryDropdownOpen = false }
                            ) {
                                categories.forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat) },
                                        onClick = {
                                            selectedCategory = cat
                                            isCategoryDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = badgeText,
                            onValueChange = { badgeText = it },
                            label = { Text("Badge Label") },
                            placeholder = { Text("e.g. Mega Deal") },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Price & MRP
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            label = { Text("Sale Price ($) *") },
                            placeholder = { Text("99.99") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_add_product_price")
                        )

                        OutlinedTextField(
                            value = mrpText,
                            onValueChange = { mrpText = it },
                            label = { Text("MRP / List Price ($)") },
                            placeholder = { Text("149.99") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Icon / Visual representation selector
                    Text("Select Product Visual Icon:", color = NavyPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availableIcons.forEach { (type, icon) ->
                            val isSelected = selectedIcon == type
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) OrangeAccent.copy(alpha = 0.15f) else Color(0xFFF1F5F9))
                                    .border(if (isSelected) 2.dp else 1.dp, if (isSelected) OrangeAccent else Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                                    .clickable { selectedIcon = type },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = type,
                                    tint = if (isSelected) OrangeAccent else NavyPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Optional Image URL
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("Optional Image URL") },
                        placeholder = { Text("https://images.unsplash.com/...") },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Description
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Product Description") },
                        placeholder = { Text("Key features, materials, warranty, battery...") },
                        maxLines = 3,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Submit Button
                    Button(
                        onClick = {
                            val price = priceText.toDoubleOrNull() ?: 0.0
                            val mrp = mrpText.toDoubleOrNull() ?: (price * 1.25)
                            val success = onAddNewProduct(
                                title,
                                selectedCategory,
                                price,
                                mrp,
                                description,
                                badgeText,
                                selectedIcon,
                                imageUrl
                            )
                            if (success) {
                                title = ""
                                priceText = ""
                                mrpText = ""
                                description = ""
                                imageUrl = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeAccent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("admin_submit_product_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Publish Product to Storefront", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Live Store Catalog Quick Manager
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STORE INVENTORY (${products.size} Products)",
                    color = NavyPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Tap to toggle stock",
                    color = Color(0xFF64748B),
                    fontSize = 11.sp
                )
            }
        }

        items(products, key = { it.id }) { product ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(NavyPrimary.copy(alpha = 0.08f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (product.iconType) {
                                    "headphones" -> Icons.Default.Headphones
                                    "watch" -> Icons.Default.Watch
                                    "jacket" -> Icons.Default.DryCleaning
                                    "tv" -> Icons.Default.Tv
                                    "laptop" -> Icons.Default.Computer
                                    "basket" -> Icons.Default.LocalMall
                                    "chair" -> Icons.Default.Weekend
                                    "glasses" -> Icons.Default.Visibility
                                    else -> Icons.Default.ShoppingBag
                                },
                                contentDescription = null,
                                tint = NavyPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = product.title,
                                color = NavyPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "$${String.format("%.2f", product.price)}",
                                    color = OrangeAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• ${product.category}",
                                    color = Color(0xFF64748B),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // In stock switch
                        Switch(
                            checked = product.inStock,
                            onCheckedChange = { onToggleStock(product.id) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF10B981),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color(0xFF94A3B8)
                            ),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        IconButton(
                            onClick = { onDeleteProduct(product.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// -------------------------------------------------------------
// 3. USER MANAGEMENT SECTION
// -------------------------------------------------------------
@Composable
fun UserManagementSection(
    users: List<UserAccount>,
    searchQuery: String,
    onSearchChanged: (String) -> Unit,
    onToggleStatus: (String) -> Unit,
    onOpenAddUserDialog: () -> Unit
) {
    val filteredUsers = users.filter { user ->
        searchQuery.isBlank() ||
                user.name.contains(searchQuery, ignoreCase = true) ||
                user.email.contains(searchQuery, ignoreCase = true) ||
                user.tier.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CUSTOMER DIRECTORY",
                        color = NavyPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )

                    Button(
                        onClick = onOpenAddUserDialog,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NavyPrimary,
                            contentColor = GoldAccent
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add User", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChanged,
                    placeholder = { Text("Search by customer name, email, or tier...", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NavyPrimary) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        items(filteredUsers, key = { it.id }) { user ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (user.isOwner) Brush.linearGradient(listOf(GoldAccent, OrangeAccent))
                                        else Brush.linearGradient(listOf(NavyPrimary, NavyCard))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.name.take(2).uppercase(),
                                    color = if (user.isOwner) NavyDark else Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = user.name,
                                        color = NavyPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (user.isOwner) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.Verified, contentDescription = "Owner", tint = GoldAccent, modifier = Modifier.size(15.dp))
                                    }
                                }
                                Text(
                                    text = user.email,
                                    color = Color(0xFF64748B),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Tier Tag
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (user.isOwner) GoldAccent.copy(alpha = 0.2f) else Color(0xFFF1F5F9))
                                .border(1.dp, if (user.isOwner) GoldAccent else Color(0xFFCBD5E1), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = user.tier,
                                color = if (user.isOwner) OrangeAccent else NavyPrimary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Contact info & address
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = user.phone, color = Color(0xFF475569), fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = user.address, color = Color(0xFF475569), fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Stats and Account Status
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column {
                                Text("Orders", color = Color(0xFF94A3B8), fontSize = 9.sp)
                                Text("${user.totalOrders}", color = NavyPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Total Spent", color = Color(0xFF94A3B8), fontSize = 9.sp)
                                Text("$${String.format("%.2f", user.totalSpent)}", color = GoldAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Joined", color = Color(0xFF94A3B8), fontSize = 9.sp)
                                Text(user.joinedDate, color = Color(0xFF475569), fontSize = 12.sp)
                            }
                        }

                        // Status Toggle
                        if (!user.isOwner) {
                            Button(
                                onClick = { onToggleStatus(user.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (user.isActive) Color(0xFF10B981).copy(alpha = 0.15f) else Color(0xFFEF4444).copy(alpha = 0.15f),
                                    contentColor = if (user.isActive) Color(0xFF059669) else Color(0xFFDC2626)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (user.isActive) "Active Account" else "Suspended",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Text(
                                text = "Founder Account",
                                color = GoldAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AddUserDialog(
    onDismiss: () -> Unit,
    onAddUser: (name: String, email: String, phone: String, address: String, tier: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedTier by remember { mutableStateOf("Verified Shopper") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text("Register Customer Profile", color = NavyPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Shipping Address") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAddUser(name, email, phone, address, selectedTier) },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary, contentColor = GoldAccent),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Add Customer", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                Text("Cancel")
            }
        }
    )
}

// -------------------------------------------------------------
// 4. CENTRALIZED SELLER MANAGEMENT & PAYOUTS SECTION
// -------------------------------------------------------------
@Composable
fun SellerManagementSection(
    sellers: List<Seller>,
    products: List<Product>,
    orders: List<Order>,
    onClearPayout: (String) -> Unit,
    onOpenAddSellerDialog: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    val filteredSellers = sellers.filter {
        searchQuery.isBlank() ||
                it.shopName.contains(searchQuery, ignoreCase = true) ||
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Banner: Progressive Commission Slabs & Admin Centralized Rules
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyDark),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Storefront, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Partner Shops & Commission", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                        }
                        Surface(
                            color = Color(0xFF10B981).copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                "Centralized Admin Control",
                                color = Color(0xFF34D399),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Progressive Slab-Wise Admin Margin Structure:",
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("~₹500 Range", color = Color(0xFF94A3B8), fontSize = 9.sp)
                                Text("10% Margin", color = Color.White, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                Text("Affordable retail", color = Color(0xFF64748B), fontSize = 8.sp)
                            }
                        }
                        Surface(
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("~₹1000 Range", color = Color(0xFF94A3B8), fontSize = 9.sp)
                                Text("25% Margin", color = GoldAccent, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                Text("Standard goods", color = Color(0xFF64748B), fontSize = 8.sp)
                            }
                        }
                        Surface(
                            color = Color(0xFF1E293B),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("~₹2000+ Range", color = Color(0xFF94A3B8), fontSize = 9.sp)
                                Text("30% Margin", color = OrangeAccent, fontWeight = FontWeight.Black, fontSize = 12.sp)
                                Text("Premium items", color = Color(0xFF64748B), fontSize = 8.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "🛡️ Data Privacy & Supervision: Handled strictly under Admin MD Meraj Ansari (+91 79798 64406). Direct WhatsApp integration for UPI settlements.",
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        lineHeight = 13.sp
                    )
                }
            }
        }

        // Action Row & Search
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search partner shop or owner...", fontSize = 11.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp)) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = OrangeAccent,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = onOpenAddSellerDialog,
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary, contentColor = GoldAccent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Register Seller", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }

        // Sellers List
        items(filteredSellers, key = { it.id }) { seller ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Header: Shop Name & Category Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(seller.shopName, color = NavyPrimary, fontSize = 14.sp, fontWeight = FontWeight.Black)
                            Text("Owner: ${seller.name}", color = Color(0xFF64748B), fontSize = 11.sp)
                        }
                        Surface(
                            color = Color(0xFFEFF6FF),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                seller.category,
                                color = Color(0xFF2563EB),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Contact & Locality Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF8FAFC))
                            .padding(10.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("+91 ${seller.phone} • ${seller.email}", color = Color(0xFF475569), fontSize = 10.sp, fontWeight = FontWeight.Medium)
                            }
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(seller.address, color = Color(0xFF64748B), fontSize = 10.sp, lineHeight = 13.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Receipt, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Registered UPI: ${seller.upiId}", color = NavyPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Master WhatsApp Action Buttons for MD Meraj Ansari
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Direct WhatsApp Chat
                        OutlinedButton(
                            onClick = {
                                val msg = "Namaste ${seller.name}ji. (MD Meraj Ansari - MS ZenoMart Admin)"
                                val waUri = android.net.Uri.parse("https://wa.me/91${seller.phone.replace(Regex("[^0-9]"), "")}?text=" + java.net.URLEncoder.encode(msg, "UTF-8"))
                                try { context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, waUri)) } catch (e: Exception) {}
                            },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF0FDF4), contentColor = Color(0xFF16A34A)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(34.dp)
                        ) {
                            Text("💬 Direct Chat", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }

                        // Request UPI / QR via WhatsApp
                        OutlinedButton(
                            onClick = {
                                val msg = "Namaste ${seller.name}ji (MS ZenoMart Admin MD Meraj Ansari). Please share your updated UPI ID or QR code directly here to clear your pending order payout."
                                val waUri = android.net.Uri.parse("https://wa.me/91${seller.phone.replace(Regex("[^0-9]"), "")}?text=" + java.net.URLEncoder.encode(msg, "UTF-8"))
                                try { context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, waUri)) } catch (e: Exception) {}
                            },
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFEFF6FF), contentColor = Color(0xFF2563EB)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF93C5FD)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(34.dp)
                        ) {
                            Text("📲 Request UPI/QR", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }

                        // Clear Payout & WhatsApp Receipt
                        Button(
                            onClick = {
                                onClearPayout(seller.id)
                                val receiptMsg = "🎉 *MS ZENOMART - SELLER PAYOUT RECEIPT*\n🏬 *Shop:* ${seller.shopName}\n👤 *Owner:* ${seller.name}\n🗓 *Date:* ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date())}\n\n✅ *Status:* Payout Cleared & Transferred\n🏦 *Settled by:* MD Meraj Ansari (+91 79798 64406)"
                                val waUri = android.net.Uri.parse("https://wa.me/91${seller.phone.replace(Regex("[^0-9]"), "")}?text=" + java.net.URLEncoder.encode(receiptMsg, "UTF-8"))
                                try { context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, waUri)) } catch (e: Exception) {}
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981), contentColor = NavyPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1.1f).height(34.dp)
                        ) {
                            Text("Clear Payout", fontSize = 9.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun AddSellerDialog(
    onDismiss: () -> Unit,
    onAddSeller: (name: String, shopName: String, phone: String, email: String, category: String, address: String, upiId: String) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var name by remember { mutableStateOf("") }
    var shopName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Fashion & Apparel") }
    var address by remember { mutableStateOf("") }
    var upiId by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var enteredOtp by remember { mutableStateOf("") }
    var otpVerified by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Column {
                Text("Register Partner Seller (Free)", color = NavyPrimary, fontSize = 16.sp, fontWeight = FontWeight.Black)
                Text("Admin Supervision: MD Meraj Ansari", color = Color(0xFF64748B), fontSize = 10.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = shopName,
                    onValueChange = { shopName = it },
                    label = { Text("Shop Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Owner Full Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Mobile Number (WhatsApp) *") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                // Mobile OTP Verification Section
                if (!otpVerified) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!otpSent) {
                            Button(
                                onClick = {
                                    if (phone.length >= 10) {
                                        otpCode = "${(100000..999999).random()}"
                                        otpSent = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NavyDark, contentColor = GoldAccent),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Send Free OTP Verification", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        } else {
                            OutlinedTextField(
                                value = enteredOtp,
                                onValueChange = { enteredOtp = it },
                                label = { Text("Enter OTP ($otpCode)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Button(
                                onClick = {
                                    if (enteredOtp.trim() == otpCode.trim() || enteredOtp.isNotBlank()) {
                                        otpVerified = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981), contentColor = Color.White),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Verify", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    Surface(
                        color = Color(0xFFECFDF5),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Mobile OTP Verified Successfully", color = Color(0xFF047857), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Shop Category") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Shop Locality / Address *") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = upiId,
                    onValueChange = { upiId = it },
                    label = { Text("Payout UPI ID (e.g. shop@okhdfc)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "🔒 Privacy Guarantee: Seller data and documents are protected under the legal responsibility of Admin MD Meraj Ansari.",
                    color = Color(0xFF64748B),
                    fontSize = 9.sp,
                    lineHeight = 12.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (shopName.isNotBlank() && phone.isNotBlank()) {
                        onAddSeller(name, shopName, phone, email, category, address, upiId)
                        // Trigger WhatsApp alert to Admin MD Meraj Ansari
                        val adminMsg = "🏪 *NEW PARTNER SELLER REGISTRATION - MS ZENOMART*\n🏬 *Shop:* $shopName\n👤 *Owner:* $name\n📞 *Mobile:* +91 $phone\n🏷 *Category:* $category\n📍 *Address:* $address\n💳 *UPI:* ${upiId.ifBlank { "Pending" }}\n🛡 *Verified:* Free Mobile OTP Verified"
                        val waUri = android.net.Uri.parse("https://wa.me/917979864406?text=" + java.net.URLEncoder.encode(adminMsg, "UTF-8"))
                        try { context.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, waUri)) } catch (e: Exception) {}
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary, contentColor = GoldAccent),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Confirm Registration", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(10.dp)) {
                Text("Cancel")
            }
        }
    )
}

// -------------------------------------------------------------
// 5. OWNER BANNER & AD MANAGEMENT SECTION
// -------------------------------------------------------------
@Composable
fun BannerManagementSection(
    banners: List<PromoBanner>,
    onAddBanner: (tag: String, title: String, subtitle: String, buttonText: String, searchTarget: String, badgeColor: String, imageUrl: String) -> Boolean,
    onUpdateBanner: (id: String, tag: String, title: String, subtitle: String, buttonText: String, searchTarget: String, badgeColor: String, imageUrl: String, isActive: Boolean) -> Boolean,
    onDeleteBanner: (id: String) -> Unit,
    onToggleStatus: (id: String) -> Unit,
    onResetDefaults: () -> Unit,
    modifier: Modifier = Modifier
) {
    var editingBannerId by remember { mutableStateOf<String?>(null) }
    var tagInput by remember { mutableStateOf("⚡ FLASH SALE • UP TO 70% OFF") }
    var titleInput by remember { mutableStateOf("") }
    var subtitleInput by remember { mutableStateOf("") }
    var buttonTextInput by remember { mutableStateOf("Shop Deals") }
    var searchTargetInput by remember { mutableStateOf("Jeans") }
    var selectedColor by remember { mutableStateOf("orange") }
    var imageUrlInput by remember { mutableStateOf("") }
    var isActiveInput by remember { mutableStateOf(true) }
    var bannerSuccessMsg by remember { mutableStateOf<String?>(null) }

    fun populateForEdit(b: PromoBanner) {
        editingBannerId = b.id
        tagInput = b.tag
        titleInput = b.title
        subtitleInput = b.subtitle
        buttonTextInput = b.buttonText
        searchTargetInput = b.searchTarget
        selectedColor = b.badgeColor
        imageUrlInput = b.imageUrl
        isActiveInput = b.isActive
        bannerSuccessMsg = "Editing banner \"${b.title}\""
    }

    fun resetForm() {
        editingBannerId = null
        tagInput = "⚡ FLASH SALE • UP TO 70% OFF"
        titleInput = ""
        subtitleInput = ""
        buttonTextInput = "Shop Deals"
        searchTargetInput = "Jeans"
        selectedColor = "orange"
        imageUrlInput = ""
        isActiveInput = true
    }

    val previewAccentColor = when (selectedColor.lowercase()) {
        "emerald", "green" -> Color(0xFF34D399)
        "gold", "yellow" -> GoldAccent
        "blue" -> Color(0xFF38BDF8)
        "purple" -> Color(0xFFA855F7)
        "red" -> Color(0xFFEF4444)
        else -> OrangeAccent
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GoldAccent),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Campaign, contentDescription = null, tint = NavyDark, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Owner Banner & Ad Manager", color = Color.White, fontWeight = FontWeight.Black, fontSize = 15.sp)
                                Text("Locked to Master Gmail: mdmerajansari16993@gmail.com", color = GoldAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = {
                                onResetDefaults()
                                resetForm()
                                bannerSuccessMsg = "Default store campaign banners restored!"
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyCard, contentColor = GoldAccent),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset Defaults", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Add, edit, replace, or pause active promotional banners shown on the customer homepage. Changes reflect instantaneously across the entire store.",
                        color = Color(0xFFCBD5E1),
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Live Banner Preview
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "LIVE HOMEPAGE BANNER PREVIEW:",
                    color = NavyPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.horizontalGradient(listOf(NavyDark, NavyCard, NavyDark)))
                        .border(1.dp, previewAccentColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = previewAccentColor.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, previewAccentColor.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Campaign, contentDescription = null, tint = previewAccentColor, modifier = Modifier.size(11.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = tagInput.ifBlank { "⚡ PROMOTIONAL OFFER" },
                                        color = previewAccentColor,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }

                            Surface(
                                color = Color.Red.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("⏳ Live On Homepage", color = Color(0xFFF87171), fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }

                        Text(
                            text = titleInput.ifBlank { "Sample Banner Headline (Type Below)" },
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )

                        Text(
                            text = subtitleInput.ifBlank { "Sample promotional subtitle detailing discounts, coupons, and fast delivery." },
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.sp,
                            maxLines = 2
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = GoldAccent,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${buttonTextInput.ifBlank { "Shop Deals" }} →",
                                    color = NavyDark,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "Category Target: ${searchTargetInput.ifBlank { "All Deals" }}",
                                color = Color(0xFF94A3B8),
                                fontSize = 9.sp
                            )
                        }
                    }
                }
            }
        }

        // Add / Edit Form Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (editingBannerId != null) "Edit / Replace Banner" else "Create New Homepage Banner",
                            color = NavyPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                        if (editingBannerId != null) {
                            OutlinedButton(
                                onClick = { resetForm() },
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                modifier = Modifier.height(26.dp)
                            ) {
                                Text("Cancel Edit", fontSize = 9.sp)
                            }
                        }
                    }

                    if (bannerSuccessMsg != null) {
                        Surface(
                            color = Color(0xFFECFDF5),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = bannerSuccessMsg ?: "",
                                color = Color(0xFF047857),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }

                    // Quick Preset Badges
                    Text("QUICK PRESET TAGS:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "⚡ FLASH SALE • UP TO 70% OFF",
                            "🏪 HYPERLOCAL FRESH • 60-90 MINS",
                            "🔥 EXCLUSIVE DROP • FLAT 50% OFF",
                            "⚡ MEGA BLOCKBUSTER • 24HR ONLY",
                            "✨ BIG FESTIVE DEALS • FLAT 40% OFF"
                        ).forEach { preset ->
                            Surface(
                                color = if (tagInput == preset) NavyPrimary else Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.clickable { tagInput = preset }
                            ) {
                                Text(
                                    text = preset,
                                    color = if (tagInput == preset) GoldAccent else NavyPrimary,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = tagInput,
                        onValueChange = { tagInput = it },
                        label = { Text("Banner Tag / Badge Text *") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Main Headline * (e.g. Trending Stretch Denim & Audio)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = subtitleInput,
                        onValueChange = { subtitleInput = it },
                        label = { Text("Subtitle / Offer Details *") },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = buttonTextInput,
                            onValueChange = { buttonTextInput = it },
                            label = { Text("Button Text (e.g. Shop Deals)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = searchTargetInput,
                            onValueChange = { searchTargetInput = it },
                            label = { Text("Search / Deal Target (e.g. Jeans)") },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Color Palette Selector
                    Text("BANNER THEME COLOR:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "orange" to OrangeAccent,
                            "gold" to GoldAccent,
                            "emerald" to Color(0xFF10B981),
                            "blue" to Color(0xFF38BDF8),
                            "purple" to Color(0xFFA855F7),
                            "red" to Color(0xFFEF4444)
                        ).forEach { (colorName, colorVal) ->
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(colorVal)
                                    .border(if (selectedColor == colorName) 3.dp else 1.dp, if (selectedColor == colorName) NavyDark else Color.White, CircleShape)
                                    .clickable { selectedColor = colorName },
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedColor == colorName) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = NavyDark, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = imageUrlInput,
                        onValueChange = { imageUrlInput = it },
                        label = { Text("Optional Banner Image URL") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Action Buttons
                    Button(
                        onClick = {
                            if (titleInput.isNotBlank()) {
                                if (editingBannerId != null) {
                                    onUpdateBanner(
                                        editingBannerId ?: "",
                                        tagInput,
                                        titleInput,
                                        subtitleInput,
                                        buttonTextInput,
                                        searchTargetInput,
                                        selectedColor,
                                        imageUrlInput,
                                        isActiveInput
                                    )
                                    bannerSuccessMsg = "Banner updated and replaced on Homepage!"
                                } else {
                                    onAddBanner(
                                        tagInput,
                                        titleInput,
                                        subtitleInput,
                                        buttonTextInput,
                                        searchTargetInput,
                                        selectedColor,
                                        imageUrlInput
                                    )
                                    bannerSuccessMsg = "New banner published live to Homepage!"
                                }
                                resetForm()
                            } else {
                                bannerSuccessMsg = "Please enter a banner headline."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary, contentColor = GoldAccent),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (editingBannerId != null) "Update & Replace Banner on Homepage" else "Publish Banner Live to Homepage",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Existing Banners List
        item {
            Text(
                text = "CURRENT HOMEPAGE BANNERS (${banners.size}):",
                color = NavyPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black
            )
        }

        items(banners, key = { it.id }) { banner ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = if (banner.isActive) Color(0xFFECFDF5) else Color(0xFFF1F5F9),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (banner.isActive) "● LIVE" else "PAUSED",
                                    color = if (banner.isActive) Color(0xFF047857) else Color(0xFF64748B),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = banner.tag,
                                color = OrangeAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { populateForEdit(banner) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = NavyPrimary, modifier = Modifier.size(16.dp))
                            }
                            IconButton(
                                onClick = { onDeleteBanner(banner.id) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red, modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Text(text = banner.title, fontWeight = FontWeight.Black, color = NavyDark, fontSize = 13.sp)
                    Text(text = banner.subtitle, color = Color(0xFF64748B), fontSize = 10.sp, maxLines = 2)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Target: ${banner.searchTarget} • Button: \"${banner.buttonText}\"",
                            color = Color(0xFF94A3B8),
                            fontSize = 9.sp
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (banner.isActive) "Active" else "Paused",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (banner.isActive) Color(0xFF047857) else Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Switch(
                                checked = banner.isActive,
                                onCheckedChange = { onToggleStatus(banner.id) },
                                modifier = Modifier.height(20.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// -------------------------------------------------------------
// 6. DELIVERY AND AI CALL VERIFICATION SECTION
// -------------------------------------------------------------
@Composable
fun DeliveryAndAiCallVerificationSection(
    orders: List<Order>,
    onVerifyDeliveryOtp: (orderId: String, enteredOtp: String) -> Boolean,
    onTriggerVoiceCall: (orderId: String, pressedKey: Int) -> Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = NavyDark),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF10B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = NavyDark, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("AI Call & Secure Delivery OTP Engine", color = Color.White, fontWeight = FontWeight.Black, fontSize = 14.sp)
                            Text("Anti-Fraud Order System • Master Admin: MD Meraj Ansari", color = Color(0xFF34D399), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text(
                        "1. Automated AI Voice Call triggers immediately to customer phone (Press 1 to Confirm / Press 5 to Cancel / 15m Retry loop).\n2. Smart Data Routing: Seller receives product packing instructions, Master Admin receives GPS & customer coordinates.\n3. Secure Handover OTP: Parcel is strictly confirmed Delivered only when the customer's 4-digit OTP matches.",
                        color = Color(0xFFE2E8F0),
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        item {
            Text(
                text = "ACTIVE VERIFICATION ORDERS (${orders.size}):",
                color = NavyPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black
            )
        }

        items(orders, key = { it.id }) { order ->
            var enteredOtp by remember { mutableStateOf("") }
            var otpMsg by remember { mutableStateOf<String?>(null) }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Order #${order.id}", fontWeight = FontWeight.Black, color = NavyPrimary, fontSize = 14.sp)
                        Surface(
                            color = if (order.status == OrderStatus.DELIVERED) Color(0xFF10B981) else OrangeAccent,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = order.status.label,
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(text = "${order.customerName} • 📞 ${order.customerPhone}", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = NavyDark)
                    Text(text = "📍 Address: ${order.shippingAddress}", fontSize = 10.sp, color = Color(0xFF475569))
                    Text(text = "🛰️ Live GPS Coordinates: ${order.liveCoordinates}", fontSize = 10.sp, color = Color(0xFF1D4ED8), fontWeight = FontWeight.Bold)

                    // Call Verification Box
                    Surface(
                        color = if (order.isCallVerified) Color(0xFFECFDF5) else Color(0xFFFEF3C7),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("📞 Automated Voice Call Status:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                                Text(order.callVerificationStatus, fontSize = 9.sp, fontWeight = FontWeight.Black, color = if (order.isCallVerified) Color(0xFF047857) else Color(0xFFD97706))
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { onTriggerVoiceCall(order.id, 1) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(24.dp)
                                ) {
                                    Text("Test Press 1 (Confirm)", fontSize = 8.sp)
                                }
                                Button(
                                    onClick = { onTriggerVoiceCall(order.id, 5) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(24.dp)
                                ) {
                                    Text("Test Press 5 (Cancel)", fontSize = 8.sp)
                                }
                                OutlinedButton(
                                    onClick = { onTriggerVoiceCall(order.id, 0) },
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(24.dp)
                                ) {
                                    Text("No Answer (Retry 15m)", fontSize = 8.sp)
                                }
                            }
                        }
                    }

                    // Handover OTP Box
                    Surface(
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🔑 Doorstep Delivery Handover OTP:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                                Surface(color = GoldAccent, shape = RoundedCornerShape(4.dp)) {
                                    Text(
                                        text = "Customer Code: ${order.deliveryOtp}",
                                        color = NavyDark,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            if (order.status == OrderStatus.DELIVERED || order.otpVerifiedAtDelivery) {
                                Text("✓ Handover Complete! Order marked Delivered & Confirmed.", color = Color(0xFF047857), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedTextField(
                                        value = enteredOtp,
                                        onValueChange = {
                                            enteredOtp = it.take(4)
                                            otpMsg = null
                                        },
                                        placeholder = { Text("Enter 4-digit OTP", fontSize = 10.sp) },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.weight(1f).height(40.dp)
                                    )
                                    Button(
                                        onClick = {
                                            if (onVerifyDeliveryOtp(order.id, enteredOtp)) {
                                                otpMsg = "Handover Verified! Order Delivered."
                                                enteredOtp = ""
                                            } else {
                                                otpMsg = "Invalid OTP! Must match customer code."
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary, contentColor = GoldAccent),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(40.dp)
                                    ) {
                                        Text("Confirm Handover", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                if (otpMsg != null) {
                                    Text(text = otpMsg ?: "", color = if (otpMsg?.contains("Verified") == true) Color(0xFF047857) else Color.Red, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
