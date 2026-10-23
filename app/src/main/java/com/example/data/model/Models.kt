package com.example.data.model

enum class UserRole {
    CUSTOMER,
    DEALER,
    ADMIN
}

enum class ProductCategory(val displayName: String) {
    ALL("All Products"),
    PURIFIERS("Water Purifiers"),
    DISPENSERS("Dispensers"),
    PUMPS("Booster Pumps"),
    FILTERS("Filters & Cartridges"),
    SPARES("Spares & Parts")
}

data class Product(
    val id: String,
    val name: String,
    val category: ProductCategory,
    val subCategory: String,
    val retailPrice: Double,
    val wholesalePrice: Double,
    val moq: Int = 1, // Minimum order quantity for dealers
    val rating: Double,
    val reviewCount: Int,
    val inStock: Boolean = true,
    val stockCount: Int = 45,
    val description: String,
    val keyFeatures: List<String>,
    val specifications: Map<String, String>,
    val warranty: String,
    val imageUrl: String,
    val isBestseller: Boolean = false,
    val isNewArrival: Boolean = false
)

data class CartItem(
    val product: Product,
    val quantity: Int
)

enum class OrderStatus(val label: String) {
    PLACED("Order Placed"),
    CONFIRMED("Confirmed"),
    PACKED("Packed & Ready"),
    DISPATCHED("Shipped"),
    OUT_FOR_DELIVERY("Out for Delivery"),
    INSTALLED("Delivered & Installed"),
    CANCELLED("Cancelled")
}

data class Order(
    val id: String,
    val userRole: UserRole,
    val customerName: String,
    val customerPhone: String,
    val items: List<CartItem>,
    val subtotal: Double,
    val discount: Double,
    val gst: Double,
    val totalAmount: Double,
    val shippingAddress: String,
    val paymentMethod: String, // "Razorpay UPI", "Razorpay Card", "NetBanking", "COD"
    val razorpayPaymentId: String?,
    val status: OrderStatus,
    val createdAt: Long = System.currentTimeMillis(),
    val isDealerOrder: Boolean = false,
    val gstin: String? = null
)

enum class ServiceType(val title: String) {
    NEW_INSTALLATION("Free New Installation"),
    FILTER_REPLACEMENT("Filter & Membrane Replacement"),
    PERIODIC_MAINTENANCE("Annual Maintenance (AMC)"),
    REPAIR_LEAK("Emergency Leakage / No Water Repair"),
    WATER_QUALITY_TEST("TDS & Water Quality Check")
}

enum class ServiceStatus(val label: String) {
    PENDING("Ticket Raised"),
    SCHEDULED("Scheduled"),
    TECHNICIAN_ASSIGNED("Technician En Route"),
    COMPLETED("Service Completed")
}

data class ServiceBooking(
    val id: String,
    val customerName: String,
    val customerPhone: String,
    val address: String,
    val purifierModel: String,
    val serviceType: ServiceType,
    val preferredDate: String,
    val preferredSlot: String, // e.g. "10:00 AM - 01:00 PM"
    val assignedTechnician: String? = null,
    val status: ServiceStatus = ServiceStatus.PENDING,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class DealerApprovalStatus {
    PENDING,
    APPROVED,
    REJECTED
}

data class DealerApplication(
    val id: String,
    val businessName: String,
    val contactPerson: String,
    val phone: String,
    val email: String,
    val gstin: String,
    val city: String,
    val expectedVolume: String,
    val status: DealerApprovalStatus = DealerApprovalStatus.PENDING,
    val creditLimit: Double = 50000.0,
    val tier: String = "Tier 1 Wholesaler"
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: String, // "ORDER", "SERVICE", "DEALER", "ALERT"
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

data class AiSocialPost(
    val id: String,
    val productId: String,
    val productName: String,
    val caption: String,
    val hashtags: String,
    val imageUrl: String,
    val targetPlatform: String, // "Instagram", "WhatsApp Status"
    val scheduledTime: String,
    val isPublished: Boolean = false,
    val publicHostingUrl: String
)

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: UserRole,
    val city: String = "Mumbai",
    val businessName: String? = null,
    val gstin: String? = null,
    val walletBalance: Double = 500.0,
    val registeredAt: Long = System.currentTimeMillis()
)

data class TdsAnalysisResult(
    val tdsPpm: Int,
    val ratingColor: String, // "Good", "Moderate", "High", "Critical"
    val hardnessCategory: String,
    val healthImplication: String,
    val recommendedTech: String,
    val estimatedMembraneLifeMonths: Int,
    val suitablePurifierIds: List<String>
)

data class AmcSubscription(
    val id: String,
    val planName: String,
    val annualCost: Double,
    val filterReplacements: String,
    val freeVisits: String,
    val features: List<String>,
    val isActive: Boolean = false,
    val expiryDate: String
)

data class ProductReview(
    val id: String,
    val productId: String,
    val authorName: String,
    val rating: Int,
    val dateStr: String,
    val comment: String,
    val isVerified: Boolean = true,
    val helpfulCount: Int = 12
)

data class FilterReplacementSchedule(
    val purifierName: String,
    val installedDate: String,
    val daysRemaining: Int,
    val healthPercentage: Int,
    val nextFilterType: String,
    val partProductId: String
)

data class SmartFinderAnswers(
    val waterSource: String = "Municipal / Tap Water",
    val familySize: String = "3 - 5 Members",
    val budgetTier: String = "Mid-Range (₹10,000 - ₹18,000)"
)

