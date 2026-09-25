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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DryCleaning
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Weekend
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CategoryItem
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.SlateBorder
import kotlinx.coroutines.delay

/**
 * TopStickyNavBar mimics the signature Amazon/Flipkart mobile header:
 * 1. Delivery Location bar with pin icon
 * 2. Brand bar with Logo, Wishlist, Cart, and Profile
 * 3. Prominent, clean search bar with integrated Category dropdown and clear icon
 * 4. Trending search keyword pills
 */
@Composable
fun TopStickyNavBar(
    searchQuery: String,
    onSearchChanged: (String) -> Unit,
    selectedCategory: String,
    onCategoryChanged: (String) -> Unit,
    cartCount: Int,
    wishlistCount: Int,
    cartSubtotal: Double,
    onCartClicked: () -> Unit,
    onWishlistClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isCategoryDropdownOpen by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("top_navigation_bar"),
        color = NavyPrimary,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            
            // 1. Amazon / Flipkart Delivery Location Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDark)
                    .padding(horizontal = 14.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = OrangeAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Deliver to: ",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                    Text(
                        text = "MD Meraj Ansari - Deoghar 814112 ▾",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Express Delivery Pill
                Surface(
                    color = Color(0xFF10B981).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = Color(0xFF34D399),
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "60-90 Min Express",
                            color = Color(0xFF34D399),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 2. Main Brand Bar: Brand Logo, Profile, Wishlist, Cart
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand Logo
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onCategoryChanged("all"); onSearchChanged("") }
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(GoldLight, OrangeAccent, NavyDark)
                                )
                            )
                            .border(1.dp, GoldAccent.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Z",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "MS ",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = "ZenoMart",
                                color = GoldAccent,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                color = OrangeAccent,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "PLUS",
                                    color = Color.White,
                                    fontSize = 7.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Text(
                            text = "HYPERLOCAL DROPSHIPPING",
                            color = Color(0xFF94A3B8),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                // Action Icons (Profile, Wishlist, Cart)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Profile Icon
                    IconButton(
                        onClick = onProfileClicked,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NavyCard)
                            .testTag("user_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Profile",
                            tint = GoldLight,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    // Wishlist Icon with Counter Badge
                    IconButton(
                        onClick = onWishlistClicked,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NavyCard)
                            .testTag("wishlist_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (wishlistCount > 0) {
                                    Badge(
                                        containerColor = OrangeAccent,
                                        contentColor = Color.White
                                    ) {
                                        Text(wishlistCount.toString(), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (wishlistCount > 0) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Wishlist",
                                tint = if (wishlistCount > 0) OrangeAccent else Color(0xFFE2E8F0),
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }

                    // Cart Button with Counter Badge & Rupee Subtotal
                    Button(
                        onClick = onCartClicked,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrangeAccent,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 9.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("cart_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (cartCount > 0) {
                                    Badge(
                                        containerColor = NavyDark,
                                        contentColor = GoldAccent
                                    ) {
                                        Text(cartCount.toString(), fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                tint = Color.White,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = if (cartSubtotal > 0) "₹${cartSubtotal.toInt()}" else "Cart",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 3. Prominent Amazon / Flipkart Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChanged,
                    placeholder = {
                        Text(
                            text = "Search jeans, electronics, groceries, ghee...",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        // Category Dropdown inside Search Bar
                        Box {
                            Row(
                                modifier = Modifier
                                    .clickable { isCategoryDropdownOpen = true }
                                    .padding(start = 10.dp, end = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (selectedCategory.equals("all", true)) "All" else selectedCategory,
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Select category",
                                    tint = GoldAccent,
                                    modifier = Modifier.size(14.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = isCategoryDropdownOpen,
                                onDismissRequest = { isCategoryDropdownOpen = false },
                                modifier = Modifier.background(NavyCard)
                            ) {
                                listOf("All", "Fashion", "Electronics", "Groceries", "Gadgets", "Home").forEach { cat ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = if (cat == "All") "All Categories" else cat,
                                                color = if (selectedCategory.equals(cat, true)) GoldAccent else Color.White,
                                                fontWeight = if (selectedCategory.equals(cat, true)) FontWeight.Bold else FontWeight.Normal,
                                                fontSize = 13.sp
                                            )
                                        },
                                        onClick = {
                                            onCategoryChanged(if (cat == "All") "all" else cat)
                                            isCategoryDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchChanged("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = GoldAccent,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NavyCard,
                        unfocusedContainerColor = NavyDark,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldAccent,
                        unfocusedBorderColor = NavyBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("global_search_input")
                )
            }

            // 4. Amazon & Flipkart Trending Keywords Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(start = 14.dp, end = 14.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Trending:",
                    color = GoldAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                listOf("Jeans", "Headphones", "Ghee", "T-Shirt", "Sneakers", "Wallet").forEach { tag ->
                    Surface(
                        color = NavyDark,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                        modifier = Modifier.clickable { onSearchChanged(tag) }
                    ) {
                        Text(
                            text = tag,
                            color = Color(0xFFE2E8F0),
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Top-tier Hero Banner Carousel (Amazon Great Indian Festival / Flipkart Big Billion Days style)
 */
@Composable
fun HeroBannerCarousel(
    onShopNowClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeSlide by remember { mutableIntStateOf(0) }

    // Auto rotate slides every 4.5 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(4500)
            activeSlide = (activeSlide + 1) % 3
        }
    }

    val slides = listOf(
        Triple(
            "⚡ FLASH SALE • UP TO 70% OFF",
            "Trending Stretch Denim & Audio Gadgets",
            "Shop ANC noise-cancelling headphones, AMOLED smartwatches, and premium stretch denim jeans."
        ),
        Triple(
            "🏪 HYPERLOCAL FRESH • 60-90 MINS",
            "Pure A2 Cow Ghee & Daily Grocery Mart",
            "Direct neighborhood store sourcing with zero delay. Free delivery on orders over ₹1000!"
        ),
        Triple(
            "🔥 EXCLUSIVE DROP • FLAT 50% OFF",
            "Signature Genuine Leather & Couture",
            "Handcrafted cowhide bifold wallets with gift box, titanium aviators, and artisan wear."
        )
    )

    val current = slides[activeSlide]

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(NavyDark, NavyCard, NavyDark)
                )
            )
            .border(1.dp, GoldAccent.copy(alpha = 0.35f), RoundedCornerShape(18.dp))
            .padding(16.dp)
            .testTag("hero_banner_carousel")
    ) {
        Column {
            // Tag Pill & Countdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(OrangeAccent.copy(alpha = 0.2f))
                        .border(1.dp, OrangeAccent.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Flash",
                        tint = OrangeAccent,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = current.first,
                        color = OrangeAccent,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }

                // Countdown Pill
                Surface(
                    color = Color.Red.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = Color(0xFFF87171),
                            modifier = Modifier.size(11.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Ends Tonight",
                            color = Color(0xFFF87171),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Title
            Text(
                text = current.second,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 22.sp,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle
            Text(
                text = current.third,
                color = Color(0xFFCBD5E1),
                fontSize = 11.sp,
                lineHeight = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Shop button and Carousel dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onShopNowClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldAccent,
                        contentColor = NavyDark
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Shop Deals",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Go",
                        tint = NavyDark,
                        modifier = Modifier.size(13.dp)
                    )
                }

                // Controls: Previous / Next & Indicator dots
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { activeSlide = if (activeSlide == 0) 2 else activeSlide - 1 },
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Prev",
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    // Dots
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(3) { index ->
                            Box(
                                modifier = Modifier
                                    .size(width = if (activeSlide == index) 16.dp else 5.dp, height = 5.dp)
                                    .clip(CircleShape)
                                    .background(if (activeSlide == index) GoldAccent else Color(0xFF475569))
                            )
                        }
                    }

                    IconButton(
                        onClick = { activeSlide = (activeSlide + 1) % 3 },
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Amazon / Flipkart style Category Circular Avatar Grid
 */
@Composable
fun CategoryQuickGrid(
    categories: List<CategoryItem>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("category_grid_section")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "EXPLORE CATEGORIES",
                    color = OrangeAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Popular Departments",
                    color = NavyPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
            }
            if (selectedCategory != "all") {
                Text(
                    text = "Clear Filter",
                    color = GoldAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onCategorySelected("all") }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Horizontal Category Cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = category.id.equals(selectedCategory, ignoreCase = true)
                CategoryPillCard(
                    category = category,
                    isSelected = isSelected,
                    onClick = { onCategorySelected(category.id) }
                )
            }
        }
    }
}

@Composable
fun CategoryPillCard(
    category: CategoryItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) GoldAccent else Color(0xFFE2E8F0),
        label = "border"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) GoldAccent.copy(alpha = 0.12f) else Color.White,
        label = "bg"
    )

    val icon: ImageVector = when (category.iconName) {
        "fashion" -> Icons.Default.DryCleaning
        "electronics" -> Icons.Default.Computer
        "groceries" -> Icons.Default.LocalMall
        "gadgets" -> Icons.Default.Headphones
        "home" -> Icons.Default.Weekend
        else -> Icons.Default.ShoppingBag
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 4.dp)
            .testTag("category_card_${category.id}")
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) Brush.linearGradient(listOf(GoldAccent, OrangeAccent))
                    else Brush.linearGradient(listOf(NavyPrimary, NavyCard))
                )
                .border(1.5.dp, if (isSelected) OrangeAccent else SlateBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = category.name,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = category.name,
            color = if (isSelected) OrangeAccent else NavyPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = category.subtitle,
            color = Color(0xFF64748B),
            fontSize = 8.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Signature Amazon / Flipkart "Blockbuster Deals & Deal of the Day" Section
 */
@Composable
fun BlockbusterDealsSection(
    onDealClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .testTag("blockbuster_deals_section")
    ) {
        // Section Header with Countdown timer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "DEAL OF THE DAY",
                        color = Color.Red,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Limited quantities • Grab before price reset",
                        color = Color(0xFF64748B),
                        fontSize = 9.sp
                    )
                }
            }

            Surface(
                color = Color.Red.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "⏳ 03h 45m left",
                    color = Color.Red,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Horizontal Deals Cards Row (Amazon/Flipkart style)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Deal 1: Levi's Leather Wallet
            DealItemCard(
                title = "Levi's Leather Bifold Wallet",
                category = "Fashion",
                dealPrice = 250,
                mrp = 899,
                discountPercent = 72,
                claimedPercent = 0.78f,
                icon = Icons.Default.ShoppingBag,
                onClick = { onDealClicked("Wallet") }
            )

            // Deal 2: Bass ANC Headphones
            DealItemCard(
                title = "ZenoStudio ANC Headphones",
                category = "Gadgets",
                dealPrice = 1499,
                mrp = 2999,
                discountPercent = 50,
                claimedPercent = 0.85f,
                icon = Icons.Default.Headphones,
                onClick = { onDealClicked("Headphones") }
            )

            // Deal 3: Pure Desi Cow Ghee
            DealItemCard(
                title = "Pure A2 Cow Ghee (1L)",
                category = "Groceries",
                dealPrice = 749,
                mrp = 1100,
                discountPercent = 32,
                claimedPercent = 0.64f,
                icon = Icons.Default.LocalMall,
                onClick = { onDealClicked("Ghee") }
            )

            // Deal 4: Slim-Fit Stretch Jeans
            DealItemCard(
                title = "Apex Stretch Denim Jeans",
                category = "Fashion",
                dealPrice = 999,
                mrp = 1999,
                discountPercent = 50,
                claimedPercent = 0.91f,
                icon = Icons.Default.DryCleaning,
                onClick = { onDealClicked("Jeans") }
            )
        }
    }
}

@Composable
fun DealItemCard(
    title: String,
    category: String,
    dealPrice: Int,
    mrp: Int,
    discountPercent: Int,
    claimedPercent: Float,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .width(160.dp)
            .border(1.dp, SlateBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Icon & Discount Tag
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = NavyPrimary,
                    modifier = Modifier.size(36.dp)
                )

                // Discount Pill
                Surface(
                    color = Color.Red,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                ) {
                    Text(
                        text = "$discountPercent% OFF",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                color = NavyDark,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Pricing
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "₹$dealPrice",
                    color = NavyPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "₹$mrp",
                    color = Color(0xFF94A3B8),
                    fontSize = 9.sp,
                    textDecoration = TextDecoration.LineThrough
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Claimed Progress Bar (Amazon style)
            LinearProgressIndicator(
                progress = { claimedPercent },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = OrangeAccent,
                trackColor = Color(0xFFE2E8F0),
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${(claimedPercent * 100).toInt()}% claimed",
                color = OrangeAccent,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Top-tier E-commerce Bottom Navigation Shortcuts Bar (Amazon & Flipkart style)
 */
@Composable
fun EcommerceBottomNavigationBar(
    selectedTab: Int,
    cartCount: Int,
    ordersCount: Int,
    onHomeClicked: () -> Unit,
    onCategoriesClicked: () -> Unit,
    onDealsClicked: () -> Unit,
    onOrdersClicked: () -> Unit,
    onCartClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .testTag("bottom_navigation_bar"),
        containerColor = NavyDark,
        windowInsets = WindowInsets.navigationBars
    ) {
        // 1. Home
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = onHomeClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(20.dp)
                )
            },
            label = { Text("Home", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GoldAccent,
                selectedTextColor = GoldAccent,
                unselectedIconColor = Color(0xFF94A3B8),
                unselectedTextColor = Color(0xFF94A3B8),
                indicatorColor = NavyCard
            )
        )

        // 2. Categories
        NavigationBarItem(
            selected = false,
            onClick = onCategoriesClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.LocalMall,
                    contentDescription = "Categories",
                    modifier = Modifier.size(20.dp)
                )
            },
            label = { Text("Categories", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GoldAccent,
                selectedTextColor = GoldAccent,
                unselectedIconColor = Color(0xFF94A3B8),
                unselectedTextColor = Color(0xFF94A3B8),
                indicatorColor = NavyCard
            )
        )

        // 3. Deals / Flash Sale
        NavigationBarItem(
            selected = false,
            onClick = onDealsClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Deals",
                    tint = OrangeAccent,
                    modifier = Modifier.size(20.dp)
                )
            },
            label = { Text("Deals", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = OrangeAccent,
                selectedTextColor = OrangeAccent,
                unselectedIconColor = OrangeAccent,
                unselectedTextColor = OrangeAccent,
                indicatorColor = NavyCard
            )
        )

        // 4. Orders Tracker
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = onOrdersClicked,
            icon = {
                BadgedBox(
                    badge = {
                        if (ordersCount > 0) {
                            Badge(containerColor = OrangeAccent, contentColor = Color.White) {
                                Text("$ordersCount", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalShipping,
                        contentDescription = "Orders",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            label = { Text("Orders", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GoldAccent,
                selectedTextColor = GoldAccent,
                unselectedIconColor = Color(0xFF94A3B8),
                unselectedTextColor = Color(0xFF94A3B8),
                indicatorColor = NavyCard
            )
        )

        // 5. Cart
        NavigationBarItem(
            selected = false,
            onClick = onCartClicked,
            icon = {
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge(containerColor = OrangeAccent, contentColor = Color.White) {
                                Text("$cartCount", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            label = { Text("Cart", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GoldAccent,
                selectedTextColor = GoldAccent,
                unselectedIconColor = Color(0xFF94A3B8),
                unselectedTextColor = Color(0xFF94A3B8),
                indicatorColor = NavyCard
            )
        )

        // 6. Account
        NavigationBarItem(
            selected = false,
            onClick = onProfileClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Account",
                    modifier = Modifier.size(20.dp)
                )
            },
            label = { Text("Account", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = GoldAccent,
                selectedTextColor = GoldAccent,
                unselectedIconColor = Color(0xFF94A3B8),
                unselectedTextColor = Color(0xFF94A3B8),
                indicatorColor = NavyCard
            )
        )
    }
}
