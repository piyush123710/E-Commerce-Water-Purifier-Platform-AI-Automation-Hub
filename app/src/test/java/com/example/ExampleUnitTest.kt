package com.example

import com.example.data.model.ProductCategory
import com.example.data.model.ServiceType
import com.example.data.model.UserRole
import com.example.data.repository.CalifncoRepository
import org.junit.Assert.*
import org.junit.Test

class CalifncoAppUnitTest {

    @Test
    fun testCatalogHasOverFiftyProducts() {
        val products = CalifncoRepository.products
        assertTrue("Product catalog must contain 50+ items", products.size >= 50)
    }

    @Test
    fun testAllCategoriesAreRepresented() {
        val categories = CalifncoRepository.products.map { it.category }.toSet()
        assertTrue(categories.contains(ProductCategory.PURIFIERS))
        assertTrue(categories.contains(ProductCategory.DISPENSERS))
        assertTrue(categories.contains(ProductCategory.PUMPS))
        assertTrue(categories.contains(ProductCategory.FILTERS))
        assertTrue(categories.contains(ProductCategory.SPARES))
    }

    @Test
    fun testWholesalePriceIsLowerThanRetail() {
        CalifncoRepository.products.forEach { prod ->
            assertTrue(
                "Wholesale price (${prod.wholesalePrice}) must be less than retail price (${prod.retailPrice}) for ${prod.name}",
                prod.wholesalePrice < prod.retailPrice
            )
        }
    }

    @Test
    fun testUserRegistrationAndGatekeeper() {
        val customer = CalifncoRepository.registerCustomer(
            name = "Piyush Nagbansi",
            email = "piyush@example.com",
            phone = "+91 98765 43210",
            city = "Mumbai",
            referralInput = "CALIF-PIYUSH26"
        )
        assertNotNull(customer)
        assertEquals("Piyush Nagbansi", customer.name)
        assertEquals(UserRole.CUSTOMER, customer.role)
        assertEquals(500.0, customer.walletBalance, 0.01)
        assertNotNull(CalifncoRepository.currentUser.value)

        val dealer = CalifncoRepository.registerDealer(
            businessName = "Apex Water Systems",
            contactPerson = "Amit Verma",
            email = "apex@dealer.com",
            phone = "+91 91234 56789",
            gstin = "27AAACA9921M1Z4",
            city = "Pune"
        )
        assertNotNull(dealer)
        assertEquals(UserRole.DEALER, dealer.role)
        assertEquals("Apex Water Systems", dealer.businessName)

        // Test Logout
        CalifncoRepository.logout()
        assertNull(CalifncoRepository.currentUser.value)
    }

    @Test
    fun testTdsAnalyzerLogic() {
        val softWater = CalifncoRepository.analyzeTds(120)
        assertEquals("Good", softWater.ratingColor)
        assertTrue(softWater.recommendedTech.contains("UF"))

        val hardWater = CalifncoRepository.analyzeTds(750)
        assertEquals("High", hardWater.ratingColor)
        assertTrue(hardWater.recommendedTech.contains("Alkaline RO"))

        val criticalWater = CalifncoRepository.analyzeTds(1500)
        assertEquals("Critical", criticalWater.ratingColor)
        assertEquals(12, criticalWater.estimatedMembraneLifeMonths)
    }

    @Test
    fun testProductComparisonFeature() {
        val p1 = CalifncoRepository.products[0]
        val p2 = CalifncoRepository.products[1]

        CalifncoRepository.clearCompare()
        assertTrue(CalifncoRepository.compareList.value.isEmpty())

        CalifncoRepository.toggleCompare(p1)
        assertEquals(1, CalifncoRepository.compareList.value.size)

        CalifncoRepository.toggleCompare(p2)
        assertEquals(2, CalifncoRepository.compareList.value.size)

        CalifncoRepository.toggleCompare(p1)
        assertEquals(1, CalifncoRepository.compareList.value.size)
    }

    @Test
    fun testCartAndOrderPlacement() {
        val product = CalifncoRepository.products.first()
        CalifncoRepository.addToCart(product, 2)

        val order = CalifncoRepository.placeOrder(
            customerName = "Piyush Nagbansi",
            customerPhone = "+91 98765 43210",
            address = "Mumbai, MH",
            paymentMethod = "Razorpay (UPI)",
            razorpayPaymentId = "pay_test_unit123"
        )

        assertNotNull(order)
        assertTrue(order.id.startsWith("ORD-2026-"))
        assertEquals("pay_test_unit123", order.razorpayPaymentId)
        assertTrue(order.totalAmount > 0.0)
    }

    @Test
    fun testServiceBookingCreation() {
        val booking = CalifncoRepository.bookService(
            customerName = "Test User",
            customerPhone = "+91 99999 88888",
            address = "Pune",
            purifierModel = "Califnco AquaShield Alkaline RO",
            serviceType = ServiceType.PERIODIC_MAINTENANCE,
            preferredDate = "25 Oct 2026",
            preferredSlot = "10:00 AM - 01:00 PM",
            notes = "Routine filter check"
        )

        assertNotNull(booking)
        assertTrue(booking.id.startsWith("SRV-"))
    }

    @Test
    fun testAiSocialPostGeneration() {
        val product = CalifncoRepository.products.first()
        val post = CalifncoRepository.generateDailyAiPost(product)

        assertNotNull(post)
        assertTrue(post.caption.contains(product.name))
        assertTrue(post.hashtags.contains("#Califnco"))
    }
}
