package com.example.model

enum class OrderStatus(val label: String) {
    PENDING("Pending Verification"),
    PROCESSING("Processing & Packing"),
    SHIPPED("Dispatched / In Transit"),
    DELIVERED("Delivered")
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
    val status: OrderStatus = OrderStatus.PROCESSING
)

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
