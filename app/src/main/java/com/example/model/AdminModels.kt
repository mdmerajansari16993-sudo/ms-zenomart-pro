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
