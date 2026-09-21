package com.example.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Product(
    val id: Int,
    val title: String,
    val category: String,
    val price: Double,
    val mrp: Double,
    val rating: Double,
    val reviewsCount: Int,
    val description: String,
    val badge: String = "",
    val iconType: String = "gadget",
    val imageUrl: String = "",
    val inStock: Boolean = true
) {
    val discountPercent: Int
        get() = (((mrp - price) / mrp) * 100).toInt()
}

data class CartItem(
    val product: Product,
    var quantity: Int
)

data class CategoryItem(
    val id: String,
    val name: String,
    val subtitle: String,
    val iconName: String
)
