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
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Tv
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
import com.example.model.UserAccount
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
    onBackToStore: () -> Unit,
    onAddNewProduct: (title: String, category: String, price: Double, mrp: Double, description: String, badge: String, iconType: String, imageUrl: String) -> Boolean,
    onDeleteProduct: (Int) -> Unit,
    onToggleProductStock: (Int) -> Unit,
    onUpdateOrderStatus: (orderId: String, newStatus: OrderStatus) -> Unit,
    onToggleUserStatus: (userId: String) -> Unit,
    onAddNewUser: (name: String, email: String, phone: String, address: String, tier: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var adminSubTab by remember { mutableIntStateOf(0) } // 0: Orders, 1: Add Product, 2: Users
    var orderStatusFilter by remember { mutableStateOf("ALL") }
    var orderSearchQuery by remember { mutableStateOf("") }
    var userSearchQuery by remember { mutableStateOf("") }
    var showAddUserDialog by remember { mutableStateOf(false) }

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
                }

                // Sub Tabs (Orders, Add Product, Users)
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
                    onUpdateStatus = onUpdateOrderStatus
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
    onUpdateStatus: (orderId: String, newStatus: OrderStatus) -> Unit
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
                OrderCard(order = order, onUpdateStatus = onUpdateStatus)
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
    onUpdateStatus: (String, OrderStatus) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showStatusMenu by remember { mutableStateOf(false) }

    val statusColor = when (order.status) {
        OrderStatus.PENDING -> Color(0xFFF59E0B)
        OrderStatus.PROCESSING -> OrangeAccent
        OrderStatus.SHIPPED -> Color(0xFF3B82F6)
        OrderStatus.DELIVERED -> Color(0xFF10B981)
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
                        Text("🗺️ Navigate to Customer Location (Google Maps)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
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
                    order.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
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
