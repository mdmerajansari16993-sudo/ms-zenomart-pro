package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.CartItem
import com.example.model.CategoryItem
import com.example.model.Order
import com.example.model.OrderItem
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.UserAccount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ZenoMartViewModel : ViewModel() {

    val sampleCategories = listOf(
        CategoryItem("all", "All Items", "8 Products", "all"),
        CategoryItem("Fashion", "Fashion", "Apparel & Wear", "fashion"),
        CategoryItem("Electronics", "Electronics", "Laptops & TV", "electronics"),
        CategoryItem("Groceries", "Groceries", "Organic Foods", "groceries"),
        CategoryItem("Gadgets", "Gadgets", "Audio & Wearables", "gadgets"),
        CategoryItem("Home", "Home", "Decor & Living", "home")
    )

    val sampleProducts = listOf(
        Product(
            id = 1,
            title = "ZenoStudio ANC Wireless Headphones",
            category = "Gadgets",
            price = 149.99,
            mrp = 299.99,
            rating = 4.9,
            reviewsCount = 1420,
            badge = "Top Seller",
            description = "Active Noise Cancellation with 40-hour ultra battery life and 3D spatial audio.",
            iconType = "headphones"
        ),
        Product(
            id = 2,
            title = "Titan Apex Series 9 Smartwatch",
            category = "Gadgets",
            price = 89.99,
            mrp = 179.99,
            rating = 4.8,
            reviewsCount = 890,
            badge = "Mega Deal",
            description = "AMOLED Always-On Display with SpO2, heart rate, and dual-band GPS.",
            iconType = "watch"
        ),
        Product(
            id = 3,
            title = "Signature Italian Leather Bomber Jacket",
            category = "Fashion",
            price = 189.00,
            mrp = 320.00,
            rating = 4.7,
            reviewsCount = 430,
            badge = "Trending",
            description = "Hand-stitched genuine grain lambskin leather with luxury satin lining.",
            iconType = "jacket"
        ),
        Product(
            id = 4,
            title = "ZenoUltra 65-Inch 4K OLED HDR Smart TV",
            category = "Electronics",
            price = 799.00,
            mrp = 1299.00,
            rating = 4.9,
            reviewsCount = 650,
            badge = "Mega Sale",
            description = "True 120Hz Dolby Vision OLED cinema experience with AI voice assistant.",
            iconType = "tv"
        ),
        Product(
            id = 5,
            title = "Pro Predator 16X M3 Gaming Laptop",
            category = "Electronics",
            price = 1149.00,
            mrp = 1599.00,
            rating = 5.0,
            reviewsCount = 310,
            badge = "Top Rated",
            description = "RTX 4070, 32GB DDR5 RAM, 1TB NVMe Gen4 SSD, and liquid metal vapor cooling.",
            iconType = "laptop"
        ),
        Product(
            id = 6,
            title = "Farm-Fresh Gourmet Organic Fruit Basket",
            category = "Groceries",
            price = 34.50,
            mrp = 49.00,
            rating = 4.8,
            reviewsCount = 1120,
            badge = "Certified Organic",
            description = "100% pesticide-free handpicked avocados, organic blueberries, and exotic apples.",
            iconType = "basket"
        ),
        Product(
            id = 7,
            title = "ErgoPosture Orthopedic Executive Mesh Chair",
            category = "Home",
            price = 219.00,
            mrp = 380.00,
            rating = 4.9,
            reviewsCount = 780,
            badge = "Best for Work",
            description = "Adaptive lumbar contour, 4D adjustable armrests, and high-elastic breathable mesh.",
            iconType = "chair"
        ),
        Product(
            id = 8,
            title = "Milano Aviator Polarized UV400 Sunglasses",
            category = "Fashion",
            price = 45.00,
            mrp = 95.00,
            rating = 4.6,
            reviewsCount = 520,
            badge = "Summer Pick",
            description = "Ultra-lightweight titanium alloy frame with anti-glare scratch-resistant lenses.",
            iconType = "glasses"
        )
    )

    private val _products = MutableStateFlow<List<Product>>(sampleProducts)
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("all")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _sortOption = MutableStateFlow("featured")
    val sortOption: StateFlow<String> = _sortOption.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _wishlistIds = MutableStateFlow<Set<Int>>(emptySet())
    val wishlistIds: StateFlow<Set<Int>> = _wishlistIds.asStateFlow()

    private val _promoDiscountPercent = MutableStateFlow(0)
    val promoDiscountPercent: StateFlow<Int> = _promoDiscountPercent.asStateFlow()

    private val _lastMessage = MutableStateFlow<String?>(null)
    val lastMessage: StateFlow<String?> = _lastMessage.asStateFlow()

    // Initial realistic incoming customer orders
    private val initialOrders = listOf(
        Order(
            id = "ZM-892140",
            customerName = "Sarah Jenkins",
            customerEmail = "sarah.j@techvision.io",
            customerPhone = "+1 (555) 441-2091",
            shippingAddress = "45 Market St, Suite 300, San Francisco, CA",
            items = listOf(
                OrderItem("ZenoStudio ANC Wireless Headphones", 1, 149.99, "Gadgets"),
                OrderItem("Titan Apex Series 9 Smartwatch", 1, 89.99, "Gadgets")
            ),
            subtotal = 239.98,
            discount = 0.0,
            tax = 11.99,
            shippingFee = 0.0,
            totalAmount = 251.97,
            paymentMethod = "UPI Instant Pay",
            transactionRef = "UPI-ZM-892140-5412",
            timestamp = "15 mins ago",
            status = OrderStatus.PROCESSING
        ),
        Order(
            id = "ZM-891902",
            customerName = "Rahul Verma",
            customerEmail = "rahul.verma@codelabs.in",
            customerPhone = "+91 98765 43210",
            shippingAddress = "12 Indiranagar 100ft Rd, Bangalore 560038",
            items = listOf(
                OrderItem("Pro Predator 16X M3 Gaming Laptop", 1, 1149.00, "Electronics")
            ),
            subtotal = 1149.00,
            discount = 229.80,
            tax = 45.96,
            shippingFee = 0.0,
            totalAmount = 965.16,
            paymentMethod = "Credit Card",
            transactionRef = "CC-AUTH-991204",
            timestamp = "2 hours ago",
            status = OrderStatus.SHIPPED
        ),
        Order(
            id = "ZM-890451",
            customerName = "Priya Sharma",
            customerEmail = "priya.fashion@vogue.org",
            customerPhone = "+91 98111 22334",
            shippingAddress = "Flat 402, Bandra West, Mumbai 400050",
            items = listOf(
                OrderItem("Signature Italian Leather Bomber Jacket", 1, 189.00, "Fashion"),
                OrderItem("Milano Aviator Polarized Sunglasses", 1, 45.00, "Fashion")
            ),
            subtotal = 234.00,
            discount = 46.80,
            tax = 9.36,
            shippingFee = 0.0,
            totalAmount = 196.56,
            paymentMethod = "UPI Instant Pay",
            transactionRef = "UPI-ZM-890451-9031",
            timestamp = "Yesterday",
            status = OrderStatus.DELIVERED
        ),
        Order(
            id = "ZM-889210",
            customerName = "David Chen",
            customerEmail = "david.chen@apexglobal.net",
            customerPhone = "+1 (555) 902-8841",
            shippingAddress = "88 Pine St, Floor 14, Seattle, WA",
            items = listOf(
                OrderItem("ZenoUltra 65-Inch 4K OLED HDR Smart TV", 1, 799.00, "Electronics")
            ),
            subtotal = 799.00,
            discount = 0.0,
            tax = 39.95,
            shippingFee = 0.0,
            totalAmount = 838.95,
            paymentMethod = "Net Banking",
            transactionRef = "NB-REF-77321",
            timestamp = "2 days ago",
            status = OrderStatus.DELIVERED
        )
    )

    private val _orders = MutableStateFlow<List<Order>>(initialOrders)
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Registered user base for MD Meraj Ansari's store management
    private val initialUsers = listOf(
        UserAccount(
            id = "USR-001",
            name = "MD Meraj Ansari",
            email = "mdmerajansari16993@gmail.com",
            phone = "+1 (555) 789-0142",
            address = "ZenoMart HQ, Tech Boulevard, Executive Tower",
            totalOrders = 18,
            totalSpent = 5420.00,
            tier = "Store Owner & Admin",
            isActive = true,
            joinedDate = "Founder",
            isOwner = true
        ),
        UserAccount(
            id = "USR-002",
            name = "Sarah Jenkins",
            email = "sarah.j@techvision.io",
            phone = "+1 (555) 441-2091",
            address = "45 Market St, Suite 300, San Francisco, CA",
            totalOrders = 8,
            totalSpent = 1420.50,
            tier = "VIP Platinum",
            isActive = true,
            joinedDate = "Jan 2024"
        ),
        UserAccount(
            id = "USR-003",
            name = "Rahul Verma",
            email = "rahul.verma@codelabs.in",
            phone = "+91 98765 43210",
            address = "12 Indiranagar 100ft Rd, Bangalore 560038",
            totalOrders = 5,
            totalSpent = 2180.00,
            tier = "Gold Member",
            isActive = true,
            joinedDate = "Mar 2024"
        ),
        UserAccount(
            id = "USR-004",
            name = "Priya Sharma",
            email = "priya.fashion@vogue.org",
            phone = "+91 98111 22334",
            address = "Flat 402, Bandra West, Mumbai 400050",
            totalOrders = 11,
            totalSpent = 1890.00,
            tier = "VIP Platinum",
            isActive = true,
            joinedDate = "Nov 2023"
        ),
        UserAccount(
            id = "USR-005",
            name = "David Chen",
            email = "david.chen@apexglobal.net",
            phone = "+1 (555) 902-8841",
            address = "88 Pine St, Floor 14, Seattle, WA",
            totalOrders = 3,
            totalSpent = 950.00,
            tier = "Verified Shopper",
            isActive = true,
            joinedDate = "May 2024"
        )
    )

    private val _users = MutableStateFlow<List<UserAccount>>(initialUsers)
    val users: StateFlow<List<UserAccount>> = _users.asStateFlow()

    val filteredProducts: StateFlow<List<Product>> = combine(
        _searchQuery,
        _selectedCategory,
        _sortOption,
        _products
    ) { query, category, sort, allProducts ->
        var list = allProducts.filter { product ->
            val matchesCategory = category.equals("all", ignoreCase = true) ||
                    product.category.equals(category, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                    product.title.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }

        list = when (sort) {
            "price-low" -> list.sortedBy { it.price }
            "price-high" -> list.sortedByDescending { it.price }
            "rating" -> list.sortedByDescending { it.rating }
            else -> list
        }
        list
    }.stateIn(viewModelScope, SharingStarted.Lazily, sampleProducts)

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
    }

    fun onSortChanged(newSort: String) {
        _sortOption.value = newSort
    }

    fun addToCart(product: Product) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            current[index] = current[index].copy(quantity = current[index].quantity + 1)
        } else {
            current.add(CartItem(product, 1))
        }
        _cartItems.value = current
        _lastMessage.value = "Added \"${product.title}\" to cart!"
    }

    fun updateCartQuantity(productId: Int, delta: Int) {
        val current = _cartItems.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == productId }
        if (index >= 0) {
            val newQty = current[index].quantity + delta
            if (newQty <= 0) {
                current.removeAt(index)
            } else {
                current[index] = current[index].copy(quantity = newQty)
            }
            _cartItems.value = current
        }
    }

    fun removeFromCart(productId: Int) {
        val current = _cartItems.value.toMutableList()
        current.removeAll { it.product.id == productId }
        _cartItems.value = current
        _lastMessage.value = "Item removed from cart."
    }

    fun toggleWishlist(productId: Int) {
        val current = _wishlistIds.value.toMutableSet()
        if (current.contains(productId)) {
            current.remove(productId)
            _lastMessage.value = "Removed from wishlist."
        } else {
            current.add(productId)
            _lastMessage.value = "Saved to wishlist!"
        }
        _wishlistIds.value = current
    }

    fun applyPromoCode(code: String): Boolean {
        return if (code.trim().equals("ZENO20", ignoreCase = true)) {
            _promoDiscountPercent.value = 20
            _lastMessage.value = "Coupon ZENO20 applied! 20% discount."
            true
        } else {
            _lastMessage.value = "Invalid coupon code. Try ZENO20."
            false
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _promoDiscountPercent.value = 0
    }

    fun recordOrder(
        orderId: String,
        customerName: String = "MD Meraj Ansari",
        customerEmail: String = "mdmerajansari16993@gmail.com",
        customerPhone: String = "+1 (555) 789-0142",
        shippingAddress: String = "MS ZenoMart Headquarters, Executive Suite 4B",
        items: List<CartItem>,
        subtotal: Double,
        discount: Double,
        tax: Double,
        shippingFee: Double,
        totalAmount: Double,
        paymentMethod: String = "UPI Instant Pay",
        transactionRef: String = "UPI-TXN-" + (100000..999999).random()
    ): Order {
        val orderItems = items.map {
            OrderItem(
                productTitle = it.product.title,
                quantity = it.quantity,
                unitPrice = it.product.price,
                category = it.product.category
            )
        }
        val newOrder = Order(
            id = orderId,
            customerName = customerName,
            customerEmail = customerEmail,
            customerPhone = customerPhone,
            shippingAddress = shippingAddress,
            items = orderItems,
            subtotal = subtotal,
            discount = discount,
            tax = tax,
            shippingFee = shippingFee,
            totalAmount = totalAmount,
            paymentMethod = paymentMethod,
            transactionRef = transactionRef,
            timestamp = "Just Now",
            status = OrderStatus.PROCESSING
        )
        _orders.value = listOf(newOrder) + _orders.value
        clearCart()
        _lastMessage.value = "Order #$orderId recorded and placed successfully!"
        return newOrder
    }

    // --- Admin Dashboard Actions for MD Meraj Ansari ---

    fun addNewProduct(
        title: String,
        category: String,
        price: Double,
        mrp: Double,
        description: String,
        badge: String = "New Arrival",
        iconType: String = "gadget",
        imageUrl: String = ""
    ): Boolean {
        if (title.isBlank() || price <= 0.0) {
            _lastMessage.value = "Please provide a valid product title and price."
            return false
        }
        val currentList = _products.value
        val nextId = (currentList.maxOfOrNull { it.id } ?: 0) + 1
        val newProduct = Product(
            id = nextId,
            title = title.trim(),
            category = if (category.isBlank()) "Gadgets" else category.trim(),
            price = price,
            mrp = if (mrp >= price) mrp else price * 1.3,
            rating = 5.0,
            reviewsCount = 1,
            description = if (description.isBlank()) "Newly added official MS ZenoMart premium item." else description.trim(),
            badge = if (badge.isBlank()) "New Arrival" else badge.trim(),
            iconType = iconType,
            imageUrl = imageUrl.trim(),
            inStock = true
        )
        _products.value = listOf(newProduct) + currentList
        _lastMessage.value = "Product \"${newProduct.title}\" added to store catalog!"
        return true
    }

    fun deleteProduct(productId: Int) {
        val current = _products.value.toMutableList()
        val removed = current.removeAll { it.id == productId }
        if (removed) {
            _products.value = current
            // Also clean from cart and wishlist if present
            _cartItems.value = _cartItems.value.filter { it.product.id != productId }
            _wishlistIds.value = _wishlistIds.value.filter { it != productId }.toSet()
            _lastMessage.value = "Product removed from catalog."
        }
    }

    fun toggleProductStock(productId: Int) {
        val current = _products.value.map {
            if (it.id == productId) it.copy(inStock = !it.inStock) else it
        }
        _products.value = current
        _lastMessage.value = "Product inventory status updated."
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        val updated = _orders.value.map { order ->
            if (order.id == orderId) order.copy(status = newStatus) else order
        }
        _orders.value = updated
        _lastMessage.value = "Order #$orderId updated to ${newStatus.label}."
    }

    fun toggleUserStatus(userId: String) {
        val updated = _users.value.map { user ->
            if (user.id == userId) {
                if (user.isOwner) {
                    // Owner cannot be suspended
                    user
                } else {
                    user.copy(isActive = !user.isActive)
                }
            } else user
        }
        _users.value = updated
        _lastMessage.value = "User account status updated."
    }

    fun addNewUser(name: String, email: String, phone: String, address: String, tier: String) {
        if (name.isBlank() || email.isBlank()) {
            _lastMessage.value = "Name and Email are required."
            return
        }
        val nextId = "USR-00" + (_users.value.size + 1)
        val newUser = UserAccount(
            id = nextId,
            name = name.trim(),
            email = email.trim(),
            phone = if (phone.isBlank()) "+1 (555) 000-0000" else phone.trim(),
            address = if (address.isBlank()) "Standard Shipping Address" else address.trim(),
            totalOrders = 0,
            totalSpent = 0.0,
            tier = if (tier.isBlank()) "Verified Shopper" else tier,
            isActive = true,
            joinedDate = "Today"
        )
        _users.value = _users.value + newUser
        _lastMessage.value = "User \"${newUser.name}\" registered successfully!"
    }

    fun clearMessage() {
        _lastMessage.value = null
    }
}
