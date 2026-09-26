package com.example.model

enum class OrderStatus(val label: String) {
    PENDING("Pending Verification"),
    PROCESSING("Processing & Packing"),
    SHIPPED("Dispatched / In Transit"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled by Customer")
}

data class OrderItem(
    val productTitle: String,
    val quantity: Int,
    val unitPrice: Double,
    val category: String = ""
)

data class Order(
    val id: String,
    val customerName: String,
    val customerEmail: String,
    val customerPhone: String,
    val shippingAddress: String,
    val items: List<OrderItem>,
    val subtotal: Double,
    val discount: Double,
    val tax: Double,
    val shippingFee: Double,
    val totalAmount: Double,
    val paymentMethod: String = "UPI Instant Pay",
    val transactionRef: String = "",
    val timestamp: String,
    val status: OrderStatus = OrderStatus.PROCESSING,
    val deliveryOtp: String = "4892",
    val isCallVerified: Boolean = true,
    val callVerificationStatus: String = "Confirmed (Key 1 Pressed)", // "Confirmed (Key 1 Pressed)", "Calling Customer...", "No Answer - Retry in 15 mins", "Cancelled (Key 5 Pressed)"
    val sellerNotified: Boolean = true,
    val ownerNotified: Boolean = true,
    val liveCoordinates: String = "24.4826° N, 86.6978° E (Deoghar)",
    val otpVerifiedAtDelivery: Boolean = false
) {
    val date: String get() = timestamp
    val deliveryAddress: String get() = shippingAddress
    val sellerDispatchedData: String get() = items.joinToString(", ") { "${it.quantity}x ${it.productTitle}" }
    val adminDispatchedData: String get() = "$customerName ($customerPhone), $shippingAddress | GPS: $liveCoordinates"
}

data class AiOrderChatMessage(
    val id: String,
    val sender: String, // "user" or "ai"
    val text: String,
    val timestamp: String,
    val orderId: String = ""
) {
    val isFromCustomer: Boolean get() = sender == "user"
}

data class UserAccount(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val totalOrders: Int,
    val totalSpent: Double,
    val tier: String, // "Store Owner & Admin", "VIP Platinum", "Gold Member", "Verified Shopper"
    val isActive: Boolean = true,
    val joinedDate: String,
    val isOwner: Boolean = false
)

data class Seller(
    val id: String,
    val name: String,
    val shopName: String,
    val phone: String,
    val email: String,
    val category: String,
    val address: String,
    val upiId: String,
    val verified: Boolean = true,
    val registeredDate: String = "2026-09-15",
    val status: String = "ACTIVE"
)

data class CommissionSlab(
    val rate: Int,
    val slabName: String,
    val adminMargin: Double,
    val sellerPayout: Double
)

fun calculateAdminCommission(price: Double): CommissionSlab {
    val p = maxOf(0.0, price)
    val rate = when {
        p >= 2000.0 -> 30
        p >= 800.0 -> 25
        else -> 10
    }
    val adminMargin = (p * rate) / 100.0
    val sellerPayout = maxOf(0.0, p - adminMargin)
    val slabName = when (rate) {
        30 -> "Tier 3 (₹2000+ @ 30%)"
        25 -> "Tier 2 (~₹1000 @ 25%)"
        else -> "Tier 1 (~₹500 @ 10%)"
    }
    return CommissionSlab(rate, slabName, adminMargin, sellerPayout)
}

data class PromoBanner(
    val id: String,
    val tag: String,
    val title: String,
    val subtitle: String,
    val buttonText: String = "Shop Deals",
    val searchTarget: String = "Deals",
    val badgeColor: String = "orange", // "orange", "gold", "blue", "red", "emerald", "purple"
    val imageUrl: String = "",
    val isActive: Boolean = true,
    val displayOrder: Int = 0,
    val isOwnerSponsored: Boolean = true
)
