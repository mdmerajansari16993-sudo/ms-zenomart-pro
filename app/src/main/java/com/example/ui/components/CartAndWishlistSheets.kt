package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.model.CartItem
import com.example.model.Product
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.SlateBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartBottomSheet(
    sheetState: SheetState,
    cartItems: List<CartItem>,
    promoDiscountPercent: Int,
    onQuantityChange: (productId: Int, delta: Int) -> Unit,
    onRemoveItem: (productId: Int) -> Unit,
    onApplyPromo: (String) -> Boolean,
    onCheckout: () -> Unit,
    onDismiss: () -> Unit
) {
    var promoCodeInput by remember { mutableStateOf("") }
    var promoMessage by remember { mutableStateOf<String?>(null) }

    val subtotal = cartItems.sumOf { it.product.price * it.quantity }
    val discount = subtotal * (promoDiscountPercent / 100.0)
    val tax = if (subtotal > 0) (subtotal - discount) * 0.05 else 0.0
    val shipping = if (subtotal >= 50.0 || subtotal == 0.0) 0.0 else 9.99
    val finalTotal = (subtotal - discount + tax + (if (subtotal > 0) shipping else 0.0)).coerceAtLeast(0.0)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NavyDark,
        modifier = Modifier.testTag("cart_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Cart Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(OrangeAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = OrangeAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Shopping Cart",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${cartItems.sumOf { it.quantity }} items in your bag",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF94A3B8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Free Shipping Progress Bar ($50)
            val shippingProgress = (subtotal / 50.0).toFloat().coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(NavyCard)
                    .padding(10.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (subtotal >= 50.0) "Unlocked FREE Express Shipping!" else "Add $${String.format("%.2f", (50.0 - subtotal).coerceAtLeast(0.0))} for FREE Shipping",
                            color = if (subtotal >= 50.0) Color(0xFF10B981) else Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${(shippingProgress * 100).toInt()}%",
                            color = GoldAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { shippingProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = GoldAccent,
                        trackColor = NavyDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cart Items List
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Your cart is empty. Add items from featured deals!",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartItems, key = { it.product.id }) { item ->
                        CartItemRow(
                            item = item,
                            onQuantityChange = { delta -> onQuantityChange(item.product.id, delta) },
                            onRemove = { onRemoveItem(item.product.id) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Promo Code Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = promoCodeInput,
                    onValueChange = { promoCodeInput = it },
                    placeholder = { Text("Coupon code (e.g. ZENO20)", fontSize = 11.sp, color = Color(0xFF94A3B8)) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = NavyCard,
                        unfocusedContainerColor = NavyCard,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GoldAccent,
                        unfocusedBorderColor = NavyBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        val success = onApplyPromo(promoCodeInput)
                        promoMessage = if (success) "20% off applied!" else "Invalid code."
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyCard, contentColor = GoldAccent),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(44.dp)
                ) {
                    Text("Apply", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (promoMessage != null) {
                Text(
                    text = promoMessage!!,
                    color = if (promoDiscountPercent > 0) Color(0xFF10B981) else Color(0xFFF87171),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Pricing Breakdown
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NavyCard)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Subtotal", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    Text("$${String.format("%.2f", subtotal)}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                if (promoDiscountPercent > 0) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Discount (20%)", color = Color(0xFF10B981), fontSize = 11.sp)
                        Text("-$${String.format("%.2f", discount)}", color = Color(0xFF10B981), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Estimated Tax (5%)", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    Text("$${String.format("%.2f", tax)}", color = Color.White, fontSize = 11.sp)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Shipping", color = Color(0xFF94A3B8), fontSize = 11.sp)
                    Text(if (shipping == 0.0) "FREE" else "$${String.format("%.2f", shipping)}", color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(NavyBorder)
                        .padding(vertical = 2.dp)
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Amount", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("$${String.format("%.2f", finalTotal)}", color = GoldAccent, fontSize = 15.sp, fontWeight = FontWeight.Black)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Checkout Button
            Button(
                onClick = onCheckout,
                enabled = cartItems.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OrangeAccent,
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF475569)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("proceed_checkout_button")
            ) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Proceed to UPI Checkout ($${String.format("%.2f", finalTotal)})", fontWeight = FontWeight.Black, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(NavyCard)
            .border(1.dp, NavyBorder, RoundedCornerShape(10.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product.title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$${item.product.price} each",
                color = GoldAccent,
                fontSize = 10.sp
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            IconButton(
                onClick = { onQuantityChange(-1) },
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(NavyDark)
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Minus", tint = Color.White, modifier = Modifier.size(14.dp))
            }
            Text(
                text = "${item.quantity}",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            IconButton(
                onClick = { onQuantityChange(1) },
                modifier = Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(NavyDark)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Plus", tint = Color.White, modifier = Modifier.size(14.dp))
            }
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(26.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistBottomSheet(
    sheetState: SheetState,
    savedProducts: List<Product>,
    onAddToCart: (Product) -> Unit,
    onRemoveWishlist: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NavyDark,
        modifier = Modifier.testTag("wishlist_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Saved Wishlist (${savedProducts.size})",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF94A3B8))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (savedProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No saved items in wishlist yet. Tap the heart icon on any product!",
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(savedProducts, key = { it.id }) { product ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(NavyCard)
                                .border(1.dp, NavyBorder, RoundedCornerShape(10.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = product.title,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "$${product.price}",
                                    color = GoldAccent,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = { onAddToCart(product) },
                                    colors = ButtonDefaults.buttonColors(containerColor = OrangeAccent),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("Add", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(onClick = { onRemoveWishlist(product.id) }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Remove", tint = Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ProfileDialog(
    onDismiss: () -> Unit,
    onOpenAdmin: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NavyDark,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(GoldAccent, OrangeAccent))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = "User", tint = NavyDark, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("MD Meraj Ansari", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.CheckCircle, contentDescription = "Verified Owner", tint = GoldAccent, modifier = Modifier.size(16.dp))
                    }
                    Text("mdmerajansari16993@gmail.com", color = Color(0xFF94A3B8), fontSize = 11.sp)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(NavyCard)
                        .border(1.dp, GoldAccent.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Store Owner & Founder", color = GoldAccent, fontSize = 12.sp, fontWeight = FontWeight.Black)
                            Text("Full administrative & inventory privileges", color = Color(0xFF94A3B8), fontSize = 9.sp)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(GoldAccent.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text("EXECUTIVE", color = GoldLight, fontSize = 9.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }

                Text("• Store Platform: MS ZenoMart E-Commerce", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                Text("• Active Incoming Orders: Instant Live Tracking", color = Color(0xFFCBD5E1), fontSize = 11.sp)
                Text("• Store Inventory: Manage stock, prices, and catalog", color = Color(0xFFCBD5E1), fontSize = 11.sp)

                Spacer(modifier = Modifier.height(4.dp))

                // Launch Admin Button
                Button(
                    onClick = {
                        onDismiss()
                        onOpenAdmin()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeAccent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("dialog_open_admin_button")
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Open Admin Dashboard Panel", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Close", color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium, fontSize = 12.sp)
            }
        }
    )
}

@Composable
fun SimulatedQrCode(
    modifier: Modifier = Modifier,
    sizeDp: Int = 180
) {
    val matrixSize = 25
    val grid = remember {
        val g = Array(matrixSize) { BooleanArray(matrixSize) }
        // 1. Top-left 7x7 finder
        for (r in 0 until 7) {
            for (c in 0 until 7) {
                if (r == 0 || r == 6 || c == 0 || c == 6 || (r in 2..4 && c in 2..4)) {
                    g[r][c] = true
                }
            }
        }
        // 2. Top-right 7x7 finder
        for (r in 0 until 7) {
            for (c in (matrixSize - 7) until matrixSize) {
                val relC = c - (matrixSize - 7)
                if (r == 0 || r == 6 || relC == 0 || relC == 6 || (r in 2..4 && relC in 2..4)) {
                    g[r][c] = true
                }
            }
        }
        // 3. Bottom-left 7x7 finder
        for (r in (matrixSize - 7) until matrixSize) {
            val relR = r - (matrixSize - 7)
            for (c in 0 until 7) {
                if (relR == 0 || relR == 6 || c == 0 || c == 6 || (relR in 2..4 && c in 2..4)) {
                    g[r][c] = true
                }
            }
        }
        // 4. Timing patterns
        for (i in 7 until matrixSize - 7) {
            if (i % 2 == 0) {
                g[6][i] = true
                g[i][6] = true
            }
        }
        // 5. Data modules
        val rnd = java.util.Random(1337L)
        val centerStart = (matrixSize / 2) - 2
        val centerEnd = (matrixSize / 2) + 2
        for (r in 0 until matrixSize) {
            for (c in 0 until matrixSize) {
                val inTopLeft = r < 8 && c < 8
                val inTopRight = r < 8 && c >= matrixSize - 8
                val inBottomLeft = r >= matrixSize - 8 && c < 8
                val inTiming = r == 6 || c == 6
                val inCenterLogo = r in centerStart..centerEnd && c in centerStart..centerEnd
                if (!inTopLeft && !inTopRight && !inBottomLeft && !inTiming && !inCenterLogo) {
                    g[r][c] = rnd.nextBoolean()
                }
            }
        }
        g
    }

    Box(
        modifier = modifier
            .size(sizeDp.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cellSize = size.width / matrixSize
            val dotColor = Color(0xFF0A192F)

            for (r in 0 until matrixSize) {
                for (c in 0 until matrixSize) {
                    if (grid[r][c]) {
                        drawRoundRect(
                            color = dotColor,
                            topLeft = Offset(c * cellSize, r * cellSize),
                            size = Size(cellSize * 0.90f, cellSize * 0.90f),
                            cornerRadius = CornerRadius(cellSize * 0.2f, cellSize * 0.2f)
                        )
                    }
                }
            }

            // Scanner corner brackets in OrangeAccent
            val bracketLen = cellSize * 2.8f
            val bracketStroke = 3.dp.toPx()
            val bracketColor = Color(0xFFFF6B00)

            // Top-Left
            drawLine(bracketColor, Offset(0f, 0f), Offset(bracketLen, 0f), strokeWidth = bracketStroke, cap = StrokeCap.Round)
            drawLine(bracketColor, Offset(0f, 0f), Offset(0f, bracketLen), strokeWidth = bracketStroke, cap = StrokeCap.Round)

            // Top-Right
            drawLine(bracketColor, Offset(size.width, 0f), Offset(size.width - bracketLen, 0f), strokeWidth = bracketStroke, cap = StrokeCap.Round)
            drawLine(bracketColor, Offset(size.width, 0f), Offset(size.width, bracketLen), strokeWidth = bracketStroke, cap = StrokeCap.Round)

            // Bottom-Left
            drawLine(bracketColor, Offset(0f, size.height), Offset(bracketLen, size.height), strokeWidth = bracketStroke, cap = StrokeCap.Round)
            drawLine(bracketColor, Offset(0f, size.height), Offset(0f, size.height - bracketLen), strokeWidth = bracketStroke, cap = StrokeCap.Round)

            // Bottom-Right
            drawLine(bracketColor, Offset(size.width, size.height), Offset(size.width - bracketLen, size.height), strokeWidth = bracketStroke, cap = StrokeCap.Round)
            drawLine(bracketColor, Offset(size.width, size.height), Offset(size.width, size.height - bracketLen), strokeWidth = bracketStroke, cap = StrokeCap.Round)
        }

        // Center badge with "UPI" and lightning bolt
        Box(
            modifier = Modifier
                .size((sizeDp * 0.24f).dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
                .border(1.5.dp, Color(0xFF0A192F), RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "UPI",
                    color = Color(0xFF0072CE),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "PAY",
                    color = Color(0xFFFF6B00),
                    fontSize = 7.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
fun PaymentQrDialog(
    totalAmount: Double,
    orderNumber: String,
    onPaymentSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var isCopied by remember { mutableStateOf(false) }
    var isVerifying by remember { mutableStateOf(false) }
    var secondsRemaining by remember { mutableIntStateOf(300) } // 5 minutes timer

    // Countdown Timer
    LaunchedEffect(Unit) {
        while (secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining--
        }
    }

    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)
    val upiId = "zenomart@icici"

    AlertDialog(
        onDismissRequest = {
            if (!isVerifying) onDismiss()
        },
        containerColor = NavyDark,
        shape = RoundedCornerShape(20.dp),
        title = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GoldAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Secure",
                                tint = GoldAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "UPI Express Checkout",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "256-Bit SSL Encrypted & Verified",
                                color = Color(0xFF10B981),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        enabled = !isVerifying,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFF94A3B8))
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Total Amount Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(NavyCard)
                        .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Payable", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            Text(
                                text = "$${String.format("%.2f", totalAmount)}",
                                color = GoldAccent,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Order Ref", color = Color(0xFF94A3B8), fontSize = 10.sp)
                            Text(
                                text = "#$orderNumber",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Simulated QR Code
                SimulatedQrCode(sizeDp = 175)

                Spacer(modifier = Modifier.height(8.dp))

                // Scan Instruction & Live Timer
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Scan with any UPI App • Expires in ",
                        color = Color(0xFFCBD5E1),
                        fontSize = 11.sp
                    )
                    Text(
                        text = formattedTime,
                        color = OrangeAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // UPI ID with 1-click Copy
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF0F1E36))
                        .border(1.dp, SlateBorder.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .clickable {
                            clipboardManager.setText(AnnotatedString(upiId))
                            isCopied = true
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("VPA / UPI ID", color = Color(0xFF64748B), fontSize = 9.sp)
                            Text(upiId, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isCopied) Color(0xFF10B981).copy(alpha = 0.2f) else NavyCard)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (isCopied) "Copied!" else "Copy",
                                color = if (isCopied) Color(0xFF10B981) else GoldAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Supported UPI Apps Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val apps = listOf(
                        "GPay" to Color(0xFF4285F4),
                        "PhonePe" to Color(0xFF5F259F),
                        "Paytm" to Color(0xFF00B9F1),
                        "BHIM" to Color(0xFFFF6B00),
                        "CRED" to Color(0xFFFBBF24)
                    )
                    apps.forEach { (name, color) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(color.copy(alpha = 0.15f))
                                .border(0.5.dp, color.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(name, color = color, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action: Simulate Payment Success
                Button(
                    onClick = {
                        isVerifying = true
                    },
                    enabled = !isVerifying,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF10B981),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("simulate_payment_button")
                ) {
                    if (isVerifying) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Verifying UPI Payment...", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.Check, contentDescription = "Pay", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Simulate Successful Payment", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (isVerifying) {
                    LaunchedEffect(Unit) {
                        delay(900L)
                        onPaymentSuccess()
                    }
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
fun OrderSuccessDialog(
    orderNumber: String,
    totalAmount: Double,
    onDismiss: () -> Unit,
    onViewInAdmin: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NavyDark,
        shape = RoundedCornerShape(20.dp),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981).copy(alpha = 0.2f))
                        .border(2.dp, Color(0xFF10B981), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Payment Verified!",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Order Placed Successfully",
                    color = Color(0xFF10B981),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(NavyCard)
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Order Reference", color = Color(0xFF94A3B8), fontSize = 11.sp)
                            Text("#$orderNumber", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Paid", color = Color(0xFF94A3B8), fontSize = 11.sp)
                            Text("$${String.format("%.2f", totalAmount)}", color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Payment Mode", color = Color(0xFF94A3B8), fontSize = 11.sp)
                            Text("UPI Instant Pay", color = Color(0xFF10B981), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Delivery Estimate", color = Color(0xFF94A3B8), fontSize = 11.sp)
                            Text("2-3 Business Days", color = Color.White, fontSize = 11.sp)
                        }
                    }
                }

                Text(
                    text = "Thank you for shopping at MS ZenoMart! The incoming order has been registered in the system and is now ready for fulfillment.",
                    color = Color(0xFFCBD5E1),
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        onDismiss()
                        onViewInAdmin()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OrangeAccent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .testTag("view_in_admin_button")
                ) {
                    Icon(Icons.Default.ReceiptLong, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("View in Owner Orders Panel", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldAccent,
                    contentColor = NavyDark
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .testTag("continue_shopping_button")
            ) {
                Text("Continue Shopping", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    )
}
