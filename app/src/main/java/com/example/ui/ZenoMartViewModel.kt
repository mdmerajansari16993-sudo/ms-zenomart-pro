package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AiOrderChatMessage
import com.example.model.CartItem
import com.example.model.CategoryItem
import com.example.model.Order
import com.example.model.OrderItem
import com.example.model.OrderStatus
import com.example.model.Product
import com.example.model.PromoBanner
import com.example.model.Seller
import com.example.model.UserAccount
import com.example.model.calculateAdminCommission
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
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
        ),
        Product(
            id = 23,
            title = "Levi's Men's Heritage Genuine Leather Bifold Wallet with Red Tab & Gift Box",
            category = "Fashion",
            price = 250.0,
            mrp = 899.0,
            rating = 4.9,
            reviewsCount = 94,
            badge = "Levi's Original",
            description = "100% handcrafted top-grain cowhide leather with iconic red tab, 6 card slots, dual currency divider, RFID protection, and collector's gift tin box.",
            iconType = "wallet",
            imageUrl = "assets/products/levis_wallet_closed.jpg",
            images = listOf(
                "assets/products/levis_wallet_closed.jpg",
                "assets/products/wallet_interior_open.jpg",
                "assets/products/brown_wallet_gift_pack.jpg"
            )
        ),
        Product(
            id = 24,
            title = "Reebok Sport Pro Textured Genuine Leather Men's Bi-Fold Wallet",
            category = "Fashion",
            price = 250.0,
            mrp = 799.0,
            rating = 4.8,
            reviewsCount = 67,
            badge = "Reebok Sport",
            description = "Textured water-resistant genuine leather with Reebok vector crest, 8 card sleeves, double bill divider, and gift box.",
            iconType = "wallet",
            imageUrl = "assets/products/black_luxury_wallet.jpg",
            images = listOf(
                "assets/products/black_luxury_wallet.jpg",
                "assets/products/wallet_interior_open.jpg"
            )
        ),
        Product(
            id = 25,
            title = "United Colors of Benetton (UCB) Classic Cognac Tan Genuine Leather Wallet",
            category = "Fashion",
            price = 250.0,
            mrp = 899.0,
            rating = 4.9,
            reviewsCount = 73,
            badge = "UCB Italian",
            description = "Italian cognac tan full-grain leather wallet with debossed Benetton crest, 6 card slots, coin pocket, and presentation box.",
            iconType = "wallet",
            imageUrl = "assets/products/tan_wallet_open_view.jpg",
            images = listOf(
                "assets/products/tan_wallet_open_view.jpg",
                "assets/products/wallet_interior_open.jpg"
            )
        ),
        Product(
            id = 26,
            title = "Calvin Klein (CK) Luxury Matte Saffiano Leather Men's Slim Wallet",
            category = "Fashion",
            price = 250.0,
            mrp = 999.0,
            rating = 5.0,
            reviewsCount = 112,
            badge = "CK Luxury",
            description = "Ultra-slim profile scratch-resistant Saffiano leather with gunmetal CK plaque, RFID blocking lining, and hard-shell casing.",
            iconType = "wallet",
            imageUrl = "assets/products/black_luxury_wallet.jpg",
            images = listOf(
                "assets/products/black_luxury_wallet.jpg",
                "assets/products/wallet_interior_open.jpg"
            )
        ),
        Product(
            id = 27,
            title = "Versace Medusa Couture Edition Premium Grained Leather Men's Wallet",
            category = "Fashion",
            price = 250.0,
            mrp = 999.0,
            rating = 5.0,
            reviewsCount = 88,
            badge = "Versace Couture",
            description = "Grained calfskin leather with polished gold-tone Medusa emblem, 8 card slots, 2 cash sleeves, and luxury magnetic gift box.",
            iconType = "wallet",
            imageUrl = "assets/products/black_luxury_wallet.jpg",
            images = listOf(
                "assets/products/black_luxury_wallet.jpg",
                "assets/products/wallet_interior_open.jpg"
            )
        ),
        Product(
            id = 28,
            title = "Emporio Armani Classic Eagle Crest Bi-Fold Men's Leather Wallet",
            category = "Fashion",
            price = 250.0,
            mrp = 999.0,
            rating = 4.9,
            reviewsCount = 95,
            badge = "Armani Premium",
            description = "Espresso brown vegetable-tanned genuine leather with Armani eagle crest insignia, twin currency partitions, and gift box.",
            iconType = "wallet",
            imageUrl = "assets/products/levis_wallet_closed.jpg",
            images = listOf(
                "assets/products/levis_wallet_closed.jpg",
                "assets/products/wallet_interior_open.jpg"
            )
        ),
        Product(
            id = 29,
            title = "Diesel Rugged Raw-Finish Vintage Tan Brown Genuine Leather Bifold Wallet",
            category = "Fashion",
            price = 250.0,
            mrp = 899.0,
            rating = 4.8,
            reviewsCount = 79,
            badge = "Diesel Rugged",
            description = "Vintage oil-waxed full-grain leather wallet with raw-edge detailing, stamped Diesel crest, 7 card slots, and metal tin box.",
            iconType = "wallet",
            imageUrl = "assets/products/tan_wallet_open_view.jpg",
            images = listOf(
                "assets/products/tan_wallet_open_view.jpg",
                "assets/products/wallet_interior_open.jpg"
            )
        ),
        Product(
            id = 30,
            title = "Allen Solly London Executive Dual-Tone Genuine Leather Men's Wallet",
            category = "Fashion",
            price = 250.0,
            mrp = 799.0,
            rating = 4.8,
            reviewsCount = 62,
            badge = "Allen Solly",
            description = "British tailoring smooth leather with contrast stitching, Allen Solly stag emblem, 6 card slots, and presentation box.",
            iconType = "wallet",
            imageUrl = "assets/products/levis_wallet_closed.jpg",
            images = listOf(
                "assets/products/levis_wallet_closed.jpg",
                "assets/products/wallet_interior_open.jpg"
            )
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

    private val initialAiChatMessages = listOf(
        AiOrderChatMessage(
            id = "welcome_1",
            sender = "ai",
            text = "Namaste! I am your MS ZenoMart AI Order & Delivery Assistant. Ask me anything about your order status, arrival time, live GPS tracking, or your 4-digit Secure Delivery OTP.",
            timestamp = "Active"
        )
    )
    private val _aiChatMessages = MutableStateFlow<List<AiOrderChatMessage>>(initialAiChatMessages)
    val aiChatMessages: StateFlow<List<AiOrderChatMessage>> = _aiChatMessages.asStateFlow()

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

    // Multi-Vendor Partner Sellers for MD Meraj Ansari's Centralized Management
    private val initialSellers = listOf(
        Seller(
            id = "seller_1",
            name = "Rajesh Kumar Sharma",
            shopName = "Apex Denim & Casuals",
            phone = "9835123456",
            email = "apexdenim@zenomart.local",
            category = "Fashion & Jeans",
            address = "Shop #14, MG Road Market, Deoghar, PIN: 814112",
            upiId = "apexdenim@okhdfcbank",
            verified = true,
            registeredDate = "2026-09-01"
        ),
        Seller(
            id = "seller_2",
            name = "Sunil Verma",
            shopName = "City Organics",
            phone = "9431987654",
            email = "cityorganics@zenomart.local",
            category = "Groceries & Essentials",
            address = "Plot 22, Sector-4, Raghunathpur, Deoghar, PIN: 814112",
            upiId = "sunilorganics@paytm",
            verified = true,
            registeredDate = "2026-09-05"
        ),
        Seller(
            id = "seller_3",
            name = "Amitabh Roy",
            shopName = "Royal Sweets & Dairy",
            phone = "9123456780",
            email = "royalsweets@zenomart.local",
            category = "Sweets & Bakery",
            address = "Station Road Near Ghormora, Deoghar, PIN: 814112",
            upiId = "royalsweets@axl",
            verified = true,
            registeredDate = "2026-09-10"
        ),
        Seller(
            id = "seller_4",
            name = "Manoj Kumar Gupta",
            shopName = "Apex Tech Bazaar",
            phone = "9876501234",
            email = "apextech@zenomart.local",
            category = "Electronics & Gadgets",
            address = "Apex Tech Bazaar, MG Road, Deoghar, PIN: 814112",
            upiId = "apextech@upi",
            verified = true,
            registeredDate = "2026-09-12"
        ),
        Seller(
            id = "seller_5",
            name = "Priya Kumari",
            shopName = "City Trends Boutique",
            phone = "9798012345",
            email = "priya.trends@zenomart.local",
            category = "Fashion & Jeans",
            address = "City Trends Boutique, Plaza-2, Deoghar, PIN: 814112",
            upiId = "priya.trends@upi",
            verified = true,
            registeredDate = "2026-09-15"
        )
    )

    private val _sellers = MutableStateFlow<List<Seller>>(initialSellers)
    val sellers: StateFlow<List<Seller>> = _sellers.asStateFlow()

    // Owner Promotional Banner & Ad Management System (Owner: MD Meraj Ansari)
    private val initialBanners = listOf(
        PromoBanner(
            id = "banner_1",
            tag = "⚡ FLASH SALE • UP TO 70% OFF",
            title = "Trending Stretch Denim & Audio Gadgets",
            subtitle = "Shop ANC noise-cancelling headphones, AMOLED smartwatches, and premium stretch denim jeans.",
            buttonText = "Shop Deals",
            searchTarget = "Jeans",
            badgeColor = "orange",
            imageUrl = "https://images.unsplash.com/photo-1542272604-787c3835535d?w=800&q=80",
            isActive = true,
            displayOrder = 1
        ),
        PromoBanner(
            id = "banner_2",
            tag = "🏪 HYPERLOCAL FRESH • 60-90 MINS",
            title = "Pure A2 Cow Ghee & Daily Grocery Mart",
            subtitle = "Direct neighborhood store sourcing with zero delay. Free delivery on orders over ₹1000!",
            buttonText = "Order Groceries",
            searchTarget = "Groceries",
            badgeColor = "emerald",
            imageUrl = "https://images.unsplash.com/photo-1588964895597-cfccd6e2dbf9?w=800&q=80",
            isActive = true,
            displayOrder = 2
        ),
        PromoBanner(
            id = "banner_3",
            tag = "🔥 EXCLUSIVE DROP • FLAT 50% OFF",
            title = "Signature Genuine Leather & Couture",
            subtitle = "Handcrafted cowhide bifold wallets with gift box, titanium aviators, and artisan wear.",
            buttonText = "Shop Leather",
            searchTarget = "Wallet",
            badgeColor = "gold",
            imageUrl = "https://images.unsplash.com/photo-1627123424574-724758594e93?w=800&q=80",
            isActive = true,
            displayOrder = 3
        ),
        PromoBanner(
            id = "banner_4",
            tag = "⚡ MEGA BLOCKBUSTER • 24HR ONLY",
            title = "4K OLED Smart TVs & Gaming Beast Laptops",
            subtitle = "Massive exchange bonuses and zero delivery delay on verified high-tech store catalog.",
            buttonText = "Explore Electronics",
            searchTarget = "Electronics",
            badgeColor = "blue",
            imageUrl = "https://images.unsplash.com/photo-1593305841991-05c297ba4575?w=800&q=80",
            isActive = true,
            displayOrder = 4
        )
    )

    private val _banners = MutableStateFlow<List<PromoBanner>>(initialBanners)
    val banners: StateFlow<List<PromoBanner>> = _banners.asStateFlow()

    val activeBanners: StateFlow<List<PromoBanner>> = _banners.map { list ->
        list.filter { it.isActive }.sortedBy { it.displayOrder }
    }.stateIn(viewModelScope, SharingStarted.Lazily, initialBanners)

    // Dynamic Deal Products for Homepage: Combines high discounts & approved partner seller products automatically
    val dealProducts: StateFlow<List<Product>> = _products.map { list ->
        list.filter { product ->
            product.discountPercent >= 25 ||
            product.badge.isNotBlank() ||
            product.category.equals("Fashion", ignoreCase = true) ||
            product.category.equals("Gadgets", ignoreCase = true) ||
            product.iconType == "wallet"
        }.take(8)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

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
            status = OrderStatus.PROCESSING,
            deliveryOtp = (1000..9999).random().toString(),
            isCallVerified = true,
            callVerificationStatus = "Confirmed (Key 1 Pressed)",
            sellerNotified = true,
            ownerNotified = true,
            liveCoordinates = "24.4826° N, 86.6978° E (Deoghar)",
            otpVerifiedAtDelivery = false
        )
        _orders.value = listOf(newOrder) + _orders.value
        clearCart()
        _lastMessage.value = "Order #$orderId placed! Automated voice call confirmed (Key 1). Delivery OTP is ${newOrder.deliveryOtp}."
        return newOrder
    }

    // --- Automated AI Voice Call Verification & Delivery OTP ---

    fun verifyOrderViaCall(orderId: String, pressedKey: Int): Boolean {
        var success = false
        _orders.value = _orders.value.map { order ->
            if (order.id == orderId) {
                if (pressedKey == 1) {
                    success = true
                    order.copy(
                        isCallVerified = true,
                        callVerificationStatus = "Confirmed (Key 1 Pressed)",
                        sellerNotified = true,
                        ownerNotified = true
                    )
                } else if (pressedKey == 5) {
                    order.copy(
                        isCallVerified = false,
                        callVerificationStatus = "Cancelled by Customer (Key 5 Pressed)"
                    )
                } else {
                    order.copy(
                        isCallVerified = false,
                        callVerificationStatus = "No Answer - Auto-retry in 15 mins"
                    )
                }
            } else order
        }
        if (pressedKey == 1) {
            _lastMessage.value = "AI Call Verified! Dispatched packing details to Seller and GPS coordinates to Delivery."
        } else if (pressedKey == 5) {
            _lastMessage.value = "Order cancelled via customer automated voice call."
        } else {
            _lastMessage.value = "Customer did not answer call. Automated retry scheduled in 15 minutes."
        }
        return success
    }

    fun verifyDeliveryOtp(orderId: String, enteredOtp: String): Boolean {
        val order = _orders.value.find { it.id == orderId } ?: return false
        if (order.deliveryOtp.trim() == enteredOtp.trim()) {
            _orders.value = _orders.value.map {
                if (it.id == orderId) {
                    it.copy(
                        status = OrderStatus.DELIVERED,
                        otpVerifiedAtDelivery = true
                    )
                } else it
            }
            _lastMessage.value = "Delivery OTP Verified successfully! Order #$orderId marked Delivered & Confirmed."
            return true
        } else {
            _lastMessage.value = "Invalid OTP! Handover blocked. Expected OTP from customer app."
            return false
        }
    }

    fun sendAiOrderChatMessage(orderId: String, question: String) {
        val q = question.trim()
        if (q.isBlank()) return
        val userMsg = AiOrderChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            sender = "user",
            text = q,
            timestamp = "Just now",
            orderId = orderId
        )
        val order = _orders.value.find { it.id == orderId } ?: _orders.value.firstOrNull()
        val botReply = when {
            q.contains("where", ignoreCase = true) || q.contains("status", ignoreCase = true) || q.contains("track", ignoreCase = true) -> {
                if (order != null) {
                    "Order #${order.id} is currently ${order.status.label}. Delivery OTP is ${order.deliveryOtp}. Live Coordinates: ${order.liveCoordinates}. Express courier is en route."
                } else {
                    "Your order is currently processing at the local hub. Estimated arrival is 60-90 minutes."
                }
            }
            q.contains("when", ignoreCase = true) || q.contains("arrive", ignoreCase = true) || q.contains("time", ignoreCase = true) -> {
                "Your package will arrive in approximately 60 to 90 minutes. Our local delivery agent is on their way."
            }
            q.contains("otp", ignoreCase = true) || q.contains("code", ignoreCase = true) -> {
                if (order != null) {
                    "Your Secure Delivery OTP is ${order.deliveryOtp}. Please share this 4-digit OTP with the delivery agent only when they reach your doorstep."
                } else {
                    "Your 4-digit Secure Delivery OTP is generated when your order is placed and is required upon parcel handover."
                }
            }
            q.contains("call", ignoreCase = true) || q.contains("verify", ignoreCase = true) -> {
                if (order != null) {
                    "Voice Verification Status: ${order.callVerificationStatus}. If unconfirmed, the automated system auto-retries every 15 minutes."
                } else {
                    "Our automated system initiates a voice call where pressing 1 confirms your order."
                }
            }
            else -> {
                "Hello! I am your MS ZenoMart Order Assistant. Your items are sourced from verified neighborhood partner shops. You can ask me for delivery ETA, your Secure Delivery OTP, or tracking details."
            }
        }
        val aiMsg = AiOrderChatMessage(
            id = "msg_${System.currentTimeMillis() + 1}",
            sender = "ai",
            text = botReply,
            timestamp = "Just now",
            orderId = orderId
        )
        _aiChatMessages.value = _aiChatMessages.value + listOf(userMsg, aiMsg)
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

    // --- Multi-Vendor Partner Seller Management for MD Meraj Ansari ---

    fun registerSeller(
        name: String,
        shopName: String,
        phone: String,
        email: String,
        category: String,
        address: String,
        upiId: String
    ): Seller {
        val newSeller = Seller(
            id = "seller_${System.currentTimeMillis()}",
            name = name.trim(),
            shopName = shopName.trim(),
            phone = phone.trim(),
            email = email.trim(),
            category = category.ifBlank { "Fashion & Essentials" },
            address = address.trim(),
            upiId = upiId.trim().ifBlank { "Not Set" },
            verified = true,
            registeredDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        )
        _sellers.value = listOf(newSeller) + _sellers.value
        _lastMessage.value = "Partner shop \"${newSeller.shopName}\" registered successfully!"
        return newSeller
    }

    fun clearSellerPayout(sellerId: String) {
        val seller = _sellers.value.find { it.id == sellerId }
        _lastMessage.value = "Payout settled for ${seller?.shopName ?: "Seller"}."
    }

    // --- Owner Promotional Banner & Ad Management System ---

    fun addBanner(
        tag: String,
        title: String,
        subtitle: String,
        buttonText: String = "Shop Deals",
        searchTarget: String = "Deals",
        badgeColor: String = "orange",
        imageUrl: String = ""
    ): Boolean {
        if (title.isBlank()) {
            _lastMessage.value = "Banner title cannot be empty."
            return false
        }
        val newBanner = PromoBanner(
            id = "banner_${System.currentTimeMillis()}",
            tag = if (tag.isBlank()) "⚡ EXCLUSIVE PROMO" else tag.trim(),
            title = title.trim(),
            subtitle = subtitle.trim(),
            buttonText = if (buttonText.isBlank()) "Shop Deals" else buttonText.trim(),
            searchTarget = if (searchTarget.isBlank()) "Deals" else searchTarget.trim(),
            badgeColor = badgeColor.ifBlank { "orange" },
            imageUrl = imageUrl.trim(),
            isActive = true,
            displayOrder = (_banners.value.maxOfOrNull { it.displayOrder } ?: 0) + 1
        )
        _banners.value = listOf(newBanner) + _banners.value
        _lastMessage.value = "Promotional banner \"${newBanner.title}\" published to Homepage!"
        return true
    }

    fun updateBanner(
        id: String,
        tag: String,
        title: String,
        subtitle: String,
        buttonText: String,
        searchTarget: String,
        badgeColor: String,
        imageUrl: String,
        isActive: Boolean = true
    ): Boolean {
        if (title.isBlank()) {
            _lastMessage.value = "Banner title cannot be empty."
            return false
        }
        val current = _banners.value
        val updated = current.map { b ->
            if (b.id == id) {
                b.copy(
                    tag = tag.trim(),
                    title = title.trim(),
                    subtitle = subtitle.trim(),
                    buttonText = buttonText.trim(),
                    searchTarget = searchTarget.trim(),
                    badgeColor = badgeColor,
                    imageUrl = imageUrl.trim(),
                    isActive = isActive
                )
            } else b
        }
        _banners.value = updated
        _lastMessage.value = "Banner updated successfully!"
        return true
    }

    fun deleteBanner(id: String) {
        val current = _banners.value.filter { it.id != id }
        _banners.value = current
        _lastMessage.value = "Promotional banner removed from Homepage."
    }

    fun toggleBannerActive(id: String) {
        val updated = _banners.value.map { b ->
            if (b.id == id) b.copy(isActive = !b.isActive) else b
        }
        _banners.value = updated
        _lastMessage.value = "Banner status updated."
    }

    fun toggleBannerStatus(id: String) = toggleBannerActive(id)

    fun addNewBanner(
        tag: String,
        title: String,
        subtitle: String,
        buttonText: String = "Shop Deals",
        searchTarget: String = "Deals",
        badgeColor: String = "orange",
        imageUrl: String = ""
    ): Boolean = addBanner(tag, title, subtitle, buttonText, searchTarget, badgeColor, imageUrl)

    fun triggerAutomatedVoiceCall(orderId: String, pressedKey: Int): Boolean = verifyOrderViaCall(orderId, pressedKey)

    fun resetBannersToDefault() {
        _banners.value = initialBanners
        _lastMessage.value = "Homepage banners restored to default campaign."
    }

    fun clearMessage() {
        _lastMessage.value = null
    }
}
