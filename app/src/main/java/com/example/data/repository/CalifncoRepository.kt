package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object CalifncoRepository {

    // Logged in User Session (null means user must register/login first)
    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    // Current User Role
    private val _currentRole = MutableStateFlow(UserRole.CUSTOMER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    // Dark Mode Toggle
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun setRole(role: UserRole) {
        _currentRole.value = role
        addNotification(
            title = "Switched to ${role.name.lowercase().replaceFirstChar { it.uppercase() }} Mode",
            message = when (role) {
                UserRole.CUSTOMER -> "Browsing retail store catalog with direct home delivery & warranty."
                UserRole.DEALER -> "Wholesale B2B pricing unlocked with bulk MOQs and tax invoicing."
                UserRole.ADMIN -> "Management portal: supervise orders, dealer approvals, and service desk."
            },
            type = "SYSTEM"
        )
    }

    // 50+ Product Catalog
    val products: List<Product> = listOf(
        // === PURIFIERS (12) ===
        Product(
            id = "pur-01",
            name = "Califnco AquaShield Alkaline RO+UV+MTDS",
            category = ProductCategory.PURIFIERS,
            subCategory = "Alkaline RO",
            retailPrice = 14999.0,
            wholesalePrice = 9800.0,
            moq = 5,
            rating = 4.8,
            reviewCount = 342,
            stockCount = 120,
            description = "Multi-stage RO purification with alkaline mineralizer cartridge delivering healthy pH 8.5+ antioxidant drinking water.",
            keyFeatures = listOf("10-Stage Purification", "Copper & Zinc Infusion", "Smart LED TDS Indicator", "8 Litre Food-Grade Tank"),
            specifications = mapOf("Filtration Capacity" to "15 L/hr", "Storage Tank" to "8 Litres", "Membrane" to "Califnco 80 GPD", "Power" to "24V DC 36W"),
            warranty = "1 Year Complete Onsite Warranty",
            imageUrl = "https://images.unsplash.com/photo-1548839140-29a749e1bc4e?w=800&q=80",
            isBestseller = true
        ),
        Product(
            id = "pur-02",
            name = "Califnco PureFlow Copper Max RO",
            category = ProductCategory.PURIFIERS,
            subCategory = "Copper RO",
            retailPrice = 16499.0,
            wholesalePrice = 10750.0,
            moq = 5,
            rating = 4.9,
            reviewCount = 215,
            stockCount = 85,
            description = "Active copper infusion technology that enriches purified water with natural immunity-boosting copper micro-nutrients.",
            keyFeatures = listOf("Real Copper Infuser", "TDS Controller", "Zero Water Wastage Recovery", "Stainless Steel Tank"),
            specifications = mapOf("Capacity" to "18 L/hr", "Tank" to "7 Litres SS304", "Recovery" to "Up to 60%", "Warranty" to "1 Year"),
            warranty = "1 Year Comprehensive",
            imageUrl = "https://images.unsplash.com/photo-1585771724684-38269d6639fd?w=800&q=80",
            isBestseller = true
        ),
        Product(
            id = "pur-03",
            name = "Califnco SlimFit Under-Sink RO",
            category = ProductCategory.PURIFIERS,
            subCategory = "Under Sink RO",
            retailPrice = 18999.0,
            wholesalePrice = 12500.0,
            moq = 3,
            rating = 4.7,
            reviewCount = 180,
            stockCount = 45,
            description = "Compact concealed under-counter RO system with sleek brushed stainless steel gooseneck deck faucet.",
            keyFeatures = listOf("Space-Saving Design", "High-Flow Hydro Booster", "Separate Stainless Faucet", "Quick-Twist Filters"),
            specifications = mapOf("Flow Rate" to "20 L/hr", "Faucet" to "SS 304 Lead-Free", "Storage" to "10L Pressure Tank"),
            warranty = "2 Years Califnco Warranty",
            imageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800&q=80"
        ),
        Product(
            id = "pur-04",
            name = "Califnco HydroSmart IoT Smart Purifier",
            category = ProductCategory.PURIFIERS,
            subCategory = "IoT Smart RO",
            retailPrice = 21999.0,
            wholesalePrice = 14200.0,
            moq = 3,
            rating = 4.9,
            reviewCount = 94,
            stockCount = 38,
            description = "Wi-Fi enabled smart purifier with real-time TDS telemetry, live filter health tracker, and automated service booking.",
            keyFeatures = listOf("App Connected Telemetry", "Real-Time Filter Health", "Auto-Flush Membrane", "UV-in-Tank Sterilization"),
            specifications = mapOf("Connectivity" to "Wi-Fi & Bluetooth", "Purification" to "RO+UV+UF+TDS", "Tank" to "9 Litres"),
            warranty = "2 Years Smart Care",
            imageUrl = "https://images.unsplash.com/photo-1527061011665-3652c757a4d4?w=800&q=80",
            isNewArrival = true
        ),
        Product(
            id = "pur-05",
            name = "Califnco EcoPure Gravity UF Purifier",
            category = ProductCategory.PURIFIERS,
            subCategory = "Gravity UF",
            retailPrice = 3499.0,
            wholesalePrice = 2100.0,
            moq = 10,
            rating = 4.4,
            reviewCount = 512,
            stockCount = 200,
            description = "Non-electric gravity-fed hollow fiber ultrafiltration water purifier. Perfect for low TDS municipal tap water.",
            keyFeatures = listOf("Zero Electricity Needed", "0.01 Micron UF Membrane", "Bacteriostatic Carbon", "20L Storage"),
            specifications = mapOf("Capacity" to "20 Litres", "Filtration" to "UF + Carbon", "Flow" to "3 L/hr"),
            warranty = "6 Months Warranty",
            imageUrl = "https://images.unsplash.com/photo-1542013936693-884638332954?w=800&q=80"
        ),
        Product(
            id = "pur-06",
            name = "Califnco Commercial 50 LPH RO Plant",
            category = ProductCategory.PURIFIERS,
            subCategory = "Commercial RO",
            retailPrice = 38999.0,
            wholesalePrice = 26500.0,
            moq = 2,
            rating = 4.8,
            reviewCount = 67,
            stockCount = 25,
            description = "Heavy-duty 50 Litres per hour commercial RO plant for offices, schools, cafes, and healthcare clinics.",
            keyFeatures = listOf("Dual 300 GPD Booster Pumps", "Dual 3012 RO Membranes", "Stainless Steel Skid Frame", "Low Maintenance"),
            specifications = mapOf("Flow" to "50 LPH", "Pumps" to "2x 300 GPD", "Frame" to "SS Skid", "TDS Range" to "Up to 3000 PPM"),
            warranty = "1 Year Industrial Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80"
        ),
        Product(
            id = "pur-07",
            name = "Califnco Commercial 100 LPH Skid RO",
            category = ProductCategory.PURIFIERS,
            subCategory = "Commercial RO",
            retailPrice = 64999.0,
            wholesalePrice = 45000.0,
            moq = 1,
            rating = 4.9,
            reviewCount = 38,
            stockCount = 15,
            description = "High capacity 100 LPH industrial grade RO system with raw water booster and dual flow rotameters.",
            keyFeatures = listOf("100 LPH Capacity", "SS High Pressure Pump", "Pressure Gauge Monitoring", "Heavy Duty CIP Ready"),
            specifications = mapOf("Output" to "100 LPH", "Inlet TDS" to "Up to 3500 PPM", "Power" to "220V 1-Phase"),
            warranty = "1 Year Industrial Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092335397-9583fe92d232?w=800&q=80"
        ),
        Product(
            id = "pur-08",
            name = "Califnco Crystal UV+UF Tap Purifier",
            category = ProductCategory.PURIFIERS,
            subCategory = "UV+UF",
            retailPrice = 7999.0,
            wholesalePrice = 5200.0,
            moq = 5,
            rating = 4.5,
            reviewCount = 140,
            stockCount = 90,
            description = "Instant high-flow UV disinfection with ultra-fine UF barrier for cities with soft TDS water.",
            keyFeatures = listOf("High Intensity 11W UV Lamp", "Super-fast 2L/min Flow", "Filter Life Indicator", "Compact Wall Mount"),
            specifications = mapOf("UV Power" to "11W Philips", "UF Pore" to "0.01 Micron", "Flow" to "2 L/min"),
            warranty = "1 Year Onsite",
            imageUrl = "https://images.unsplash.com/photo-1518837695005-2083093ee35b?w=800&q=80"
        ),
        Product(
            id = "pur-09",
            name = "Califnco ThermoSip Hot & Cold Built-In RO",
            category = ProductCategory.PURIFIERS,
            subCategory = "Hot & Cold RO",
            retailPrice = 27999.0,
            wholesalePrice = 18900.0,
            moq = 2,
            rating = 4.9,
            reviewCount = 112,
            stockCount = 30,
            description = "Integrated RO purifier with instant boiling hot (90°C), chilled (10°C), and ambient water dispenser taps.",
            keyFeatures = listOf("Instant Hot & Chilled Water", "Child Safety Lock", "Stainless Steel Hot/Cold Tanks", "Multi-stage RO"),
            specifications = mapOf("Hot Tank" to "1.5L (90°C)", "Cold Tank" to "3L (10°C)", "RO Speed" to "15 L/hr"),
            warranty = "1 Year Comprehensive",
            imageUrl = "https://images.unsplash.com/photo-1544816155-12df9643f363?w=800&q=80",
            isBestseller = true
        ),
        Product(
            id = "pur-10",
            name = "Califnco UltraPure 8-Stage Mineralizer RO",
            category = ProductCategory.PURIFIERS,
            subCategory = "Mineral RO",
            retailPrice = 13499.0,
            wholesalePrice = 8900.0,
            moq = 5,
            rating = 4.6,
            reviewCount = 198,
            stockCount = 110,
            description = "Balanced essential mineral infusion (Calcium, Magnesium, Potassium) for sweet tasting, pure water.",
            keyFeatures = listOf("Bio-Ceramic Mineral Ball", "75 GPD Vontron RO", "Low Power Consumption", "Transparent Water Level"),
            specifications = mapOf("Capacity" to "12 L/hr", "Tank" to "8.5 Litres", "Max TDS" to "2000 PPM"),
            warranty = "1 Year Onsite",
            imageUrl = "https://images.unsplash.com/photo-1517646287270-a5a9ca602e5c?w=800&q=80"
        ),
        Product(
            id = "pur-11",
            name = "Califnco Compact Tabletop Mini RO",
            category = ProductCategory.PURIFIERS,
            subCategory = "Tabletop RO",
            retailPrice = 11999.0,
            wholesalePrice = 7800.0,
            moq = 5,
            rating = 4.5,
            reviewCount = 82,
            stockCount = 65,
            description = "Plug-and-play desktop RO purifier ideal for modern studio apartments and single families.",
            keyFeatures = listOf("Zero Installation Required", "Removable 5L Tank", "Touch Dispense Button", "Self-Diagnostic Beep"),
            specifications = mapOf("Tank" to "5 Litres", "Dimensions" to "28x22x38 cm", "Weight" to "6.5 kg"),
            warranty = "1 Year Replacement",
            imageUrl = "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=800&q=80"
        ),
        Product(
            id = "pur-12",
            name = "Califnco Solar-Powered Rural RO Unit",
            category = ProductCategory.PURIFIERS,
            subCategory = "Solar RO",
            retailPrice = 32999.0,
            wholesalePrice = 22000.0,
            moq = 2,
            rating = 4.8,
            reviewCount = 44,
            stockCount = 20,
            description = "DC-direct high-efficiency solar water purifier engineered for farmhouses and off-grid rural communities.",
            keyFeatures = listOf("Direct 24V DC Solar Compatible", "High Pressure Diaphragm", "Heavy Mud Pre-filter", "Low Wattage"),
            specifications = mapOf("Power" to "24V DC 45W", "Recovery" to "50%", "Capacity" to "25 L/hr"),
            warranty = "2 Years Califnco Rural Care",
            imageUrl = "https://images.unsplash.com/photo-1509391365360-2e959784a276?w=800&q=80"
        ),

        // === DISPENSERS (8) ===
        Product(
            id = "dsp-01",
            name = "Califnco Glacier Touchless Water Dispenser",
            category = ProductCategory.DISPENSERS,
            subCategory = "Touchless",
            retailPrice = 13999.0,
            wholesalePrice = 9100.0,
            moq = 4,
            rating = 4.8,
            reviewCount = 153,
            stockCount = 50,
            description = "Hygienic infrared sensor touchless water dispenser delivering ice cold, hot, and ambient water hands-free.",
            keyFeatures = listOf("Touchless Sensor Activation", "High-efficiency Compressor", "Stainless Steel Water Path", "LED Nightlight"),
            specifications = mapOf("Cooling" to "3 L/hr at 10°C", "Heating" to "5 L/hr at 90°C", "Compressor" to "Danfoss Compatible"),
            warranty = "1 Year Compressor Warranty",
            imageUrl = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=800&q=80",
            isBestseller = true
        ),
        Product(
            id = "dsp-02",
            name = "Califnco Bottom-Load Elegance Dispenser",
            category = ProductCategory.DISPENSERS,
            subCategory = "Bottom Loading",
            retailPrice = 15499.0,
            wholesalePrice = 10200.0,
            moq = 3,
            rating = 4.7,
            reviewCount = 98,
            stockCount = 40,
            description = "Effortless bottle replacement with hidden bottom cabinet. No lifting of heavy 20L water cans required.",
            keyFeatures = listOf("Easy Bottom Can Loading", "Empty Bottle Alert", "Hot Water Child Lock", "Ultra Silent Cooling"),
            specifications = mapOf("Bottle Type" to "Standard 20L Can", "Tanks" to "SS304 Hygienic", "Power" to "500W Heat / 85W Cool"),
            warranty = "1 Year Complete Warranty",
            imageUrl = "https://images.unsplash.com/photo-1585771724684-38269d6639fd?w=800&q=80"
        ),
        Product(
            id = "dsp-03",
            name = "Califnco Countertop Instant Hot Dispenser",
            category = ProductCategory.DISPENSERS,
            subCategory = "Countertop",
            retailPrice = 8999.0,
            wholesalePrice = 5900.0,
            moq = 6,
            rating = 4.6,
            reviewCount = 114,
            stockCount = 75,
            description = "3-second instant flash heating dispenser with digital temperature selector (45°C, 75°C, 85°C, 100°C) for tea and coffee.",
            keyFeatures = listOf("3s Flash Boiling", "Adjustable Temp 45-100°C", "3 Litre Detachable Tank", "Descaling Reminder"),
            specifications = mapOf("Heating Element" to "Quartz Tube 2200W", "Tank" to "3.0L BPA-Free", "Weight" to "2.8 kg"),
            warranty = "1 Year Warranty",
            imageUrl = "https://images.unsplash.com/photo-1544816155-12df9643f363?w=800&q=80",
            isNewArrival = true
        ),
        Product(
            id = "dsp-04",
            name = "Califnco Commercial 3-Tap Water Cooler",
            category = ProductCategory.DISPENSERS,
            subCategory = "Commercial Cooler",
            retailPrice = 28999.0,
            wholesalePrice = 19800.0,
            moq = 2,
            rating = 4.8,
            reviewCount = 42,
            stockCount = 20,
            description = "Industrial full stainless-steel water cooler with 40-litre chilled storage tank and 3 heavy brass push taps.",
            keyFeatures = listOf("Full SS 304 Body", "40 Litre Cooling Tank", "Embraco Heavy Compressor", "Drainage Waste Tray"),
            specifications = mapOf("Cooling Speed" to "40 L/hr", "Tank" to "40L SS304", "Dimensions" to "45x45x125 cm"),
            warranty = "1 Year Commercial Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80"
        ),
        Product(
            id = "dsp-05",
            name = "Califnco Tabletop Cold Mini Dispenser",
            category = ProductCategory.DISPENSERS,
            subCategory = "Countertop",
            retailPrice = 6499.0,
            wholesalePrice = 4200.0,
            moq = 8,
            rating = 4.3,
            reviewCount = 76,
            stockCount = 60,
            description = "Compact electronic thermoelectric cooling and heating dispenser for bedroom and home office desks.",
            keyFeatures = listOf("Thermoelectric Silent Tech", "Lightweight Portable", "Dual Faucet", "Low Power Draw 65W"),
            specifications = mapOf("Type" to "Thermoelectric", "Cooling" to "0.7 L/hr", "Heating" to "5 L/hr"),
            warranty = "6 Months Warranty",
            imageUrl = "https://images.unsplash.com/photo-1542013936693-884638332954?w=800&q=80"
        ),
        Product(
            id = "dsp-06",
            name = "Califnco Direct-Piping Office HydroStation",
            category = ProductCategory.DISPENSERS,
            subCategory = "Bottleless",
            retailPrice = 24999.0,
            wholesalePrice = 16500.0,
            moq = 2,
            rating = 4.9,
            reviewCount = 33,
            stockCount = 18,
            description = "Bottleless direct plumbing cooler with built-in 4-stage ultrafiltration, ozone auto-sanitization, and digital display.",
            keyFeatures = listOf("Direct Waterline Connected", "Self-Cleaning Ozone Cycle", "Zero Bottle Stacking", "Chilled & Boiling"),
            specifications = mapOf("Purifier" to "Internal 4-Stage UF", "Cooling" to "5 L/hr", "Heating" to "7 L/hr"),
            warranty = "2 Years Corporate Warranty",
            imageUrl = "https://images.unsplash.com/photo-1527061011665-3652c757a4d4?w=800&q=80"
        ),
        Product(
            id = "dsp-07",
            name = "Califnco Wall-Mount Bubbler Drinking Fountain",
            category = ProductCategory.DISPENSERS,
            subCategory = "Drinking Fountain",
            retailPrice = 19499.0,
            wholesalePrice = 13200.0,
            moq = 3,
            rating = 4.6,
            reviewCount = 28,
            stockCount = 15,
            description = "Heavy duty anti-vandalism stainless steel public drinking bubbler with laminar stream projector and bottle filler.",
            keyFeatures = listOf("Hands-Free Sensor Bottle Filler", "Heavy Gauge SS304", "Anti-Microbial Bubbler Hood", "Deep Basin"),
            specifications = mapOf("Mounting" to "Wall Hung", "Material" to "SS 304 Heavy Gauge", "Certification" to "ADA Compliant"),
            warranty = "2 Years Warranty",
            imageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800&q=80"
        ),
        Product(
            id = "dsp-08",
            name = "Califnco Smart Sparkling Water Maker Dispenser",
            category = ProductCategory.DISPENSERS,
            subCategory = "Sparkling",
            retailPrice = 31999.0,
            wholesalePrice = 21500.0,
            moq = 2,
            rating = 4.9,
            reviewCount = 47,
            stockCount = 12,
            description = "Luxury 4-in-1 dispenser providing chilled sparkling soda water, chilled still, hot, and ambient water.",
            keyFeatures = listOf("CO2 Carbonation Chamber", "Custom Fizz Level Selector", "Medical Grade Internal Valves", "Touchscreen UI"),
            specifications = mapOf("Fizz" to "3 Carbonation Levels", "CO2 Cylinder" to "Standard 60L", "Cooling" to "4 L/hr"),
            warranty = "2 Years Warranty",
            imageUrl = "https://images.unsplash.com/photo-1518837695005-2083093ee35b?w=800&q=80",
            isNewArrival = true
        ),

        // === PUMPS (10) ===
        Product(
            id = "pmp-01",
            name = "Califnco Kemflo 75 GPD Booster Pump",
            category = ProductCategory.PUMPS,
            subCategory = "75 GPD",
            retailPrice = 2299.0,
            wholesalePrice = 1450.0,
            moq = 10,
            rating = 4.7,
            reviewCount = 420,
            stockCount = 250,
            description = "Original high-performance 75 GPD diaphragm booster pump with pure copper winding and low vibration rubber feet.",
            keyFeatures = listOf("Pure Copper Coil", "125 PSI Max Pressure", "Low Noise Under 45 dB", "24V DC Standard"),
            specifications = mapOf("Voltage" to "24V DC", "Current" to "1.2A", "Working Flow" to ">= 0.8 LPM at 70 PSI"),
            warranty = "1 Year Replacement Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80",
            isBestseller = true
        ),
        Product(
            id = "pmp-02",
            name = "Califnco Grand Forest 100 GPD Booster Pump",
            category = ProductCategory.PUMPS,
            subCategory = "100 GPD",
            retailPrice = 2599.0,
            wholesalePrice = 1650.0,
            moq = 10,
            rating = 4.8,
            reviewCount = 310,
            stockCount = 180,
            description = "High flow 100 GPD booster pump engineered for RO systems in high TDS areas requiring higher membrane driving pressure.",
            keyFeatures = listOf("135 PSI Working Cutoff", "Anti-Vibration Mounts", "Thermal Overload Protection", "Leak-Proof Viton Seal"),
            specifications = mapOf("Voltage" to "24V DC", "Flow" to "1.1 LPM at 80 PSI", "Duty Cycle" to "Continuous"),
            warranty = "1 Year Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092335397-9583fe92d232?w=800&q=80",
            isBestseller = true
        ),
        Product(
            id = "pmp-03",
            name = "Califnco SuperSilent 150 GPD Diaphragm Pump",
            category = ProductCategory.PUMPS,
            subCategory = "150 GPD",
            retailPrice = 3199.0,
            wholesalePrice = 2050.0,
            moq = 8,
            rating = 4.9,
            reviewCount = 189,
            stockCount = 95,
            description = "Acoustically insulated diaphragm pump engineered with balanced planetary gears for near-silent operation.",
            keyFeatures = listOf("Super Silent < 38 dB", "Double Ball Bearing", "140 PSI High Head", "Corrosion Resistant Head"),
            specifications = mapOf("Flow Rate" to "1.6 LPM at 80 PSI", "Voltage" to "36V DC / 24V DC", "Power" to "48W"),
            warranty = "18 Months Replacement",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80"
        ),
        Product(
            id = "pmp-04",
            name = "Califnco Commercial 300 GPD Heavy Booster Pump",
            category = ProductCategory.PUMPS,
            subCategory = "Commercial Pump",
            retailPrice = 5499.0,
            wholesalePrice = 3600.0,
            moq = 4,
            rating = 4.8,
            reviewCount = 95,
            stockCount = 55,
            description = "High throughput 300 GPD booster pump tailored for 50 LPH commercial RO setups and industrial beverage systems.",
            keyFeatures = listOf("300 GPD Capacity", "36V DC High Output", "Heavy Duty Aluminum Housing", "High Flow Rate 2.8 LPM"),
            specifications = mapOf("Voltage" to "36V DC", "Current" to "2.5A", "Open Flow" to "3.5 LPM", "Pressure" to "150 PSI"),
            warranty = "1 Year Industrial Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092335397-9583fe92d232?w=800&q=80"
        ),
        Product(
            id = "pmp-05",
            name = "Califnco 24V DC Inline Transfer Pump",
            category = ProductCategory.PUMPS,
            subCategory = "Transfer Pump",
            retailPrice = 1799.0,
            wholesalePrice = 1150.0,
            moq = 10,
            rating = 4.5,
            reviewCount = 88,
            stockCount = 120,
            description = "Compact self-priming transfer pump for transferring water from low-level storage tanks to RO machines.",
            keyFeatures = listOf("Self-Priming up to 2m", "Dry-Run Resistant", "Inline 3/8-inch Ports", "Low Power 20W"),
            specifications = mapOf("Voltage" to "24V DC", "Self-Prime" to "2 Meters", "Flow" to "3.0 LPM Free Flow"),
            warranty = "6 Months Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80"
        ),
        Product(
            id = "pmp-06",
            name = "Califnco Self-Priming Suction RO Booster Pump",
            category = ProductCategory.PUMPS,
            subCategory = "Self-Priming",
            retailPrice = 2899.0,
            wholesalePrice = 1850.0,
            moq = 8,
            rating = 4.6,
            reviewCount = 135,
            stockCount = 80,
            description = "Designed for areas with zero tap pressure. Capable of lifting water directly from an open bucket or underground sump.",
            keyFeatures = listOf("Negative Suction Head 2.5m", "Built-in Bypass Valve", "Food Grade Diaphragm", "Heavy Base"),
            specifications = mapOf("Suction Lift" to "2.5 Metres", "Pressure" to "120 PSI", "Operating Voltage" to "24V DC"),
            warranty = "1 Year Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092335397-9583fe92d232?w=800&q=80"
        ),
        Product(
            id = "pmp-07",
            name = "Califnco 36V DC High-Head RO Booster Pump",
            category = ProductCategory.PUMPS,
            subCategory = "36V Booster",
            retailPrice = 3499.0,
            wholesalePrice = 2250.0,
            moq = 6,
            rating = 4.7,
            reviewCount = 62,
            stockCount = 45,
            description = "36V high-pressure pump for multi-membrane RO arrays and high resistance mineralizing systems.",
            keyFeatures = listOf("36V High Torque Motor", "160 PSI Max Head", "Anti-Leak Bypass Valve", "Industrial Copper Wire"),
            specifications = mapOf("Voltage" to "36V DC", "Max Pressure" to "160 PSI", "Inlet/Outlet" to "3/8-inch Female"),
            warranty = "1 Year Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80"
        ),
        Product(
            id = "pmp-08",
            name = "Califnco 400 GPD Multi-Array Commercial Pump",
            category = ProductCategory.PUMPS,
            subCategory = "Commercial Pump",
            retailPrice = 7499.0,
            wholesalePrice = 4900.0,
            moq = 2,
            rating = 4.9,
            reviewCount = 31,
            stockCount = 20,
            description = "Heavy commercial pump handling 400 GPD membranes for 100 LPH plants and high-speed water bottling units.",
            keyFeatures = listOf("Ultra High Output 400 GPD", "Heavy Heat Dissipation Fins", "Vibration Isolating Base", "CE Certified"),
            specifications = mapOf("Current" to "3.2A at 36V", "Open Flow" to "4.2 LPM", "Weight" to "3.8 kg"),
            warranty = "1 Year Comprehensive",
            imageUrl = "https://images.unsplash.com/photo-1581092335397-9583fe92d232?w=800&q=80"
        ),
        Product(
            id = "pmp-09",
            name = "Califnco Mini Submersible RO Cooler Pump",
            category = ProductCategory.PUMPS,
            subCategory = "Submersible",
            retailPrice = 899.0,
            wholesalePrice = 520.0,
            moq = 15,
            rating = 4.3,
            reviewCount = 210,
            stockCount = 300,
            description = "Waterproof mini submersible pump for water dispensers, ice makers, and cooler circulation.",
            keyFeatures = listOf("IPX8 Fully Submersible", "Silent Mag-Drive Impeller", "Suction Cup Feet", "Low 12V Power"),
            specifications = mapOf("Voltage" to "12V DC", "Flow" to "350 L/hr", "Head" to "2.2 Meters"),
            warranty = "6 Months Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80"
        ),
        Product(
            id = "pmp-10",
            name = "Califnco Float-Assisted Sump Booster Pump",
            category = ProductCategory.PUMPS,
            subCategory = "Auto Booster",
            retailPrice = 2999.0,
            wholesalePrice = 1950.0,
            moq = 8,
            rating = 4.6,
            reviewCount = 74,
            stockCount = 65,
            description = "Equipped with automatic magnetic micro-switch float that shuts off instantly when reservoir is dry.",
            keyFeatures = listOf("Dry Run Cutoff Sensor", "High-Flow Diaphragm", "Brass Port Inserts", "Overvoltage Guard"),
            specifications = mapOf("Pressure" to "130 PSI", "Voltage" to "24V DC", "Cutoff" to "Auto Micro-switch"),
            warranty = "1 Year Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092335397-9583fe92d232?w=800&q=80"
        ),

        // === FILTERS & CARTRIDGES (12) ===
        Product(
            id = "flt-01",
            name = "Califnco 5-Micron Spun Sediment Pre-Filter",
            category = ProductCategory.FILTERS,
            subCategory = "Sediment Filter",
            retailPrice = 199.0,
            wholesalePrice = 95.0,
            moq = 20,
            rating = 4.8,
            reviewCount = 1240,
            stockCount = 1500,
            description = "100% pure polypropylene melt-blown spun filter removing sand, silt, rust, and suspended dirt particles.",
            keyFeatures = listOf("5 Micron Filtration", "Gradient Density Matrix", "Universal 10-Inch Fit", "NSF Certified Food Grade"),
            specifications = mapOf("Length" to "10 Inch", "Outer Diameter" to "2.5 Inch", "Micron Rating" to "5 Micron", "Life" to "3-6 Months"),
            warranty = "Replacement on Manufacturing Defect",
            imageUrl = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=800&q=80",
            isBestseller = true
        ),
        Product(
            id = "flt-02",
            name = "Califnco Coconut Shell Carbon Block CTO Filter",
            category = ProductCategory.FILTERS,
            subCategory = "Carbon Filter",
            retailPrice = 399.0,
            wholesalePrice = 210.0,
            moq = 20,
            rating = 4.7,
            reviewCount = 890,
            stockCount = 900,
            description = "High iodine active coconut shell activated carbon block absorbing chlorine, VOCs, foul taste, and odors.",
            keyFeatures = listOf("1100+ Iodine Value", "High Chlorine Removal", "Dust-Free Extrusion", "Protects RO Membrane"),
            specifications = mapOf("Iodine Value" to "1100 mg/g", "Size" to "10 Inch x 2.5 Inch", "Capacity" to "6000 Litres"),
            warranty = "Replacement on Defect",
            imageUrl = "https://images.unsplash.com/photo-1548839140-29a749e1bc4e?w=800&q=80",
            isBestseller = true
        ),
        Product(
            id = "flt-03",
            name = "Califnco Dow Filmtec 75 GPD RO Membrane",
            category = ProductCategory.FILTERS,
            subCategory = "RO Membrane",
            retailPrice = 1899.0,
            wholesalePrice = 1150.0,
            moq = 10,
            rating = 4.9,
            reviewCount = 780,
            stockCount = 450,
            description = "Genuine thin-film composite (TFC) RO membrane with 97% salt rejection down to 0.0001 microns.",
            keyFeatures = listOf("97% Salt & Heavy Metal Rejection", "0.0001 Micron Precision", "Up to 2500 PPM TDS", "Long Life 24 Months"),
            specifications = mapOf("Permeate Flow" to "75 GPD (12 L/hr)", "Rejection Rate" to "97%", "Dimensions" to "1.81\" x 11.75\""),
            warranty = "1 Year Limited Membrane Warranty",
            imageUrl = "https://images.unsplash.com/photo-1585771724684-38269d6639fd?w=800&q=80",
            isBestseller = true
        ),
        Product(
            id = "flt-04",
            name = "Califnco 100 GPD CSM High-Flow Membrane",
            category = ProductCategory.FILTERS,
            subCategory = "RO Membrane",
            retailPrice = 2299.0,
            wholesalePrice = 1420.0,
            moq = 10,
            rating = 4.8,
            reviewCount = 350,
            stockCount = 320,
            description = "High flow 100 GPD polyamide membrane designed for fast tank refilling and high inlet water TDS up to 3000 PPM.",
            keyFeatures = listOf("100 GPD Fast Tank Fill", "Handles up to 3000 PPM", "Dense Spiral Wound Layers", "Anti-Fouling Sheet"),
            specifications = mapOf("Output" to "16 Litres/hr", "Salt Rejection" to "96.5%", "Operating Pressure" to "60-125 PSI"),
            warranty = "1 Year Warranty",
            imageUrl = "https://images.unsplash.com/photo-1585771724684-38269d6639fd?w=800&q=80"
        ),
        Product(
            id = "flt-05",
            name = "Califnco Alkaline Mineralizer pH+ Cartridge",
            category = ProductCategory.FILTERS,
            subCategory = "Alkaline Filter",
            retailPrice = 899.0,
            wholesalePrice = 490.0,
            moq = 15,
            rating = 4.8,
            reviewCount = 520,
            stockCount = 600,
            description = "Replenishes essential calcium, magnesium, sodium, and potassium while raising water pH to healthy alkaline 8.0-9.0.",
            keyFeatures = listOf("Raises pH to 8.0 - 9.0", "Negative ORP Antioxidant", "Micro-Clustered Hydration", "Quick Push-Fit Ports"),
            specifications = mapOf("Life" to "4000 Litres", "Inlet/Outlet" to "1/4\" Push-Fit", "Minerals" to "Ca, Mg, K, Na"),
            warranty = "Quality Guaranteed",
            imageUrl = "https://images.unsplash.com/photo-1548839140-29a749e1bc4e?w=800&q=80",
            isBestseller = true
        ),
        Product(
            id = "flt-06",
            name = "Califnco Copper & Zinc Bio-Cartridge",
            category = ProductCategory.FILTERS,
            subCategory = "Copper Filter",
            retailPrice = 999.0,
            wholesalePrice = 580.0,
            moq = 15,
            rating = 4.7,
            reviewCount = 310,
            stockCount = 400,
            description = "Ayurvedic copper balls and antimicrobial zinc matrix providing active antimicrobial protection and immunity booster.",
            keyFeatures = listOf("99.9% Pure Copper Granules", "Antimicrobial Zinc Media", "Sweetened Water Taste", "Inline 1/4\" Fit"),
            specifications = mapOf("Fitting" to "1/4\" Quick Connect", "Service Life" to "12 Months", "Housing" to "Food-grade Virgin PP"),
            warranty = "Quality Assurance",
            imageUrl = "https://images.unsplash.com/photo-1548839140-29a749e1bc4e?w=800&q=80"
        ),
        Product(
            id = "flt-07",
            name = "Califnco 11W Philips UV Lamp & Quartz Sleeve Kit",
            category = ProductCategory.FILTERS,
            subCategory = "UV Lamp",
            retailPrice = 1199.0,
            wholesalePrice = 690.0,
            moq = 12,
            rating = 4.8,
            reviewCount = 275,
            stockCount = 280,
            description = "Original 11-watt 253.7nm germicidal UV tube with high transmission fused silica quartz sleeve.",
            keyFeatures = listOf("Original Philips 11W Tube", "99.99% Pathogen Inactivation", "High Transmission Quartz", "4-Pin Single Ended"),
            specifications = mapOf("Wavelength" to "254 nm UV-C", "Power" to "11W", "Rated Life" to "9000 Hours"),
            warranty = "6 Months Replacement",
            imageUrl = "https://images.unsplash.com/photo-1518837695005-2083093ee35b?w=800&q=80"
        ),
        Product(
            id = "flt-08",
            name = "Califnco 0.01 Micron UF Hollow Fiber Membrane",
            category = ProductCategory.FILTERS,
            subCategory = "UF Filter",
            retailPrice = 699.0,
            wholesalePrice = 380.0,
            moq = 15,
            rating = 4.6,
            reviewCount = 188,
            stockCount = 350,
            description = "Ultrafiltration hollow fiber bundle that blocks cysts, bacteria, and colloids without stripping natural minerals.",
            keyFeatures = listOf("0.01 Micron Pore Size", "Preserves Natural Minerals", "Zero Water Wastage", "Backwashable"),
            specifications = mapOf("Pore Size" to "0.01 Micron", "Material" to "Hydrophilic PVDF", "Max Pressure" to "60 PSI"),
            warranty = "Replacement on Defect",
            imageUrl = "https://images.unsplash.com/photo-1585771724684-38269d6639fd?w=800&q=80"
        ),
        Product(
            id = "flt-09",
            name = "Califnco Anti-Scalant Siliphos Dosing Balls",
            category = ProductCategory.FILTERS,
            subCategory = "Anti-Scalant",
            retailPrice = 449.0,
            wholesalePrice = 230.0,
            moq = 20,
            rating = 4.7,
            reviewCount = 160,
            stockCount = 500,
            description = "Slow-dissolving polyphosphate balls that prevent calcium and magnesium scaling on the sensitive RO membrane surface.",
            keyFeatures = listOf("Prevents Membrane Scaling", "Extends Membrane Lifespan 2x", "Pre-Filter Housing Insert", "Non-Toxic"),
            specifications = mapOf("Quantity" to "500g Container", "Composition" to "Sodium Polyphosphate", "Food Grade" to "Yes"),
            warranty = "Shelf Life 3 Years",
            imageUrl = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=800&q=80"
        ),
        Product(
            id = "flt-10",
            name = "Califnco Post Silver Nano Carbon Polisher",
            category = ProductCategory.FILTERS,
            subCategory = "Post Carbon",
            retailPrice = 499.0,
            wholesalePrice = 260.0,
            moq = 20,
            rating = 4.8,
            reviewCount = 410,
            stockCount = 650,
            description = "Silver-impregnated post carbon cartridge installed right before dispensing tap to prevent secondary tank contamination.",
            keyFeatures = listOf("Silver Nano Antibacterial", "Crisp Natural Mountain Taste", "Quick Connect Ports", "Polishes Final Water"),
            specifications = mapOf("Media" to "Silver Impregnated Carbon", "Size" to "Inline 10\"", "Flow" to "2.5 L/min"),
            warranty = "Replacement on Defect",
            imageUrl = "https://images.unsplash.com/photo-1548839140-29a749e1bc4e?w=800&q=80"
        ),
        Product(
            id = "flt-11",
            name = "Califnco Heavy Metal & Iron Scavenger Cartridge",
            category = ProductCategory.FILTERS,
            subCategory = "Specialty Filter",
            retailPrice = 1299.0,
            wholesalePrice = 750.0,
            moq = 10,
            rating = 4.7,
            reviewCount = 142,
            stockCount = 200,
            description = "Engineered for borewell water with yellow tint and metallic iron odor. Traps dissolved iron, manganese, and lead.",
            keyFeatures = listOf("Dissolved Iron Removal", "Eliminates Metallic Aftertaste", "Borewell Water Specialist", "10-inch Standard"),
            specifications = mapOf("Media" to "Catalytic Birm & Manganese Dioxide", "Max Iron Inlet" to "5 PPM", "Life" to "6-9 Months"),
            warranty = "1 Year Warranty",
            imageUrl = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=800&q=80"
        ),
        Product(
            id = "flt-12",
            name = "Califnco 10-Inch Jumbo Pre-Filter Housing Bowl",
            category = ProductCategory.FILTERS,
            subCategory = "Housing",
            retailPrice = 799.0,
            wholesalePrice = 420.0,
            moq = 15,
            rating = 4.6,
            reviewCount = 330,
            stockCount = 420,
            description = "Transparent high-burst pressure food-grade external pre-filter housing bowl with brass threaded 3/8-inch ports.",
            keyFeatures = listOf("Transparent Heavy Wall", "Brass Thread Inserts", "O-Ring Leak Seal", "Includes Wrench & Clamp"),
            specifications = mapOf("Burst Pressure" to "300 PSI", "Material" to "Virgin Polycarbonate", "Port" to "3/8\" or 1/4\" FPT"),
            warranty = "2 Years Housing Warranty",
            imageUrl = "https://images.unsplash.com/photo-1548839140-29a749e1bc4e?w=800&q=80"
        ),

        // === SPARES & ACCESSORIES (15) ===
        Product(
            id = "spr-01",
            name = "Califnco Digital TDS & Temperature Meter",
            category = ProductCategory.SPARES,
            subCategory = "Meters & Testers",
            retailPrice = 499.0,
            wholesalePrice = 240.0,
            moq = 25,
            rating = 4.8,
            reviewCount = 980,
            stockCount = 800,
            description = "High precision digital water testing meter with titanium alloy probe, backlit LCD, and auto-temp compensation.",
            keyFeatures = listOf("Titanium Alloy Sensor Probe", "Range 0-9990 PPM", "Backlit Dual Color LCD", "Auto Hold Function"),
            specifications = mapOf("Range" to "0-9990 PPM", "Accuracy" to "±2%", "Battery" to "2x 1.5V Included"),
            warranty = "1 Year Replacement",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80",
            isBestseller = true
        ),
        Product(
            id = "spr-02",
            name = "Califnco Auto-Cut Solenoid Valve 24V DC",
            category = ProductCategory.SPARES,
            subCategory = "Valves",
            retailPrice = 349.0,
            wholesalePrice = 180.0,
            moq = 25,
            rating = 4.7,
            reviewCount = 450,
            stockCount = 600,
            description = "Normally closed 24V electromagnetic water inlet valve that cuts off raw water flow when purifier stops.",
            keyFeatures = listOf("Pure Copper Coil", "Quick Push Ports 1/4\"", "Zero Pressure Drop", "Long Life 100k Cycles"),
            specifications = mapOf("Voltage" to "24V DC", "Connection" to "1/4\" Push-Fit", "Working Pressure" to "0-120 PSI"),
            warranty = "1 Year Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092335397-9583fe92d232?w=800&q=80"
        ),
        Product(
            id = "spr-03",
            name = "Califnco Low Pressure Switch (LPS)",
            category = ProductCategory.SPARES,
            subCategory = "Switches",
            retailPrice = 189.0,
            wholesalePrice = 85.0,
            moq = 30,
            rating = 4.6,
            reviewCount = 280,
            stockCount = 750,
            description = "Safety switch that halts the booster pump when inlet tap water supply is shut, preventing pump dry burn.",
            keyFeatures = listOf("Dry Run Prevention", "Instant Electrical Cutoff", "1/4\" Push Connector", "Heavy Contact Points"),
            specifications = mapOf("Actuation Pressure" to "0.03 MPa", "Rating" to "125V/250V 5A", "Fitting" to "1/4\" Tube"),
            warranty = "1 Year Replacement",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80"
        ),
        Product(
            id = "spr-04",
            name = "Califnco High Pressure Switch (HPS)",
            category = ProductCategory.SPARES,
            subCategory = "Switches",
            retailPrice = 199.0,
            wholesalePrice = 90.0,
            moq = 30,
            rating = 4.7,
            reviewCount = 310,
            stockCount = 700,
            description = "Tank full auto cutoff switch that shuts off the RO machine as soon as the clean water storage tank fills up.",
            keyFeatures = listOf("Tank Full Auto Cutoff", "Stainless Steel Spring", "Prevents Water Overflow", "1/4\" Push Fit"),
            specifications = mapOf("Cutoff Pressure" to "0.25 MPa", "Ports" to "1/4\" Quick Connect", "Switch" to "Microswitch SPDT"),
            warranty = "1 Year Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092335397-9583fe92d232?w=800&q=80"
        ),
        Product(
            id = "spr-05",
            name = "Califnco 4-Way Auto Shutoff Mechanical Valve",
            category = ProductCategory.SPARES,
            subCategory = "Valves",
            retailPrice = 249.0,
            wholesalePrice = 120.0,
            moq = 30,
            rating = 4.5,
            reviewCount = 215,
            stockCount = 550,
            description = "Zero-electricity differential pressure valve that cuts off feed water when backpressure reaches 60%.",
            keyFeatures = listOf("Zero Electricity Cutoff", "Durable Silicone Diaphragm", "4x 1/4\" Push Connectors", "Compact Fit"),
            specifications = mapOf("Ratio" to "1:2 Shutoff", "Ports" to "4x 1/4\" Push", "Material" to "POM Engineering Plastic"),
            warranty = "6 Months Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092335397-9583fe92d232?w=800&q=80"
        ),
        Product(
            id = "spr-06",
            name = "Califnco Heavy Duty Brass Diverter Valve with Lever",
            category = ProductCategory.SPARES,
            subCategory = "Fittings",
            retailPrice = 399.0,
            wholesalePrice = 195.0,
            moq = 25,
            rating = 4.8,
            reviewCount = 410,
            stockCount = 480,
            description = "Solid chrome-plated brass diverter tap connector with quarter-turn handle to connect purifier to kitchen sink faucets.",
            keyFeatures = listOf("Forged Solid Brass", "Mirror Chrome Plating", "Leak-Proof Ceramic Core", "Smooth Diverter Lever"),
            specifications = mapOf("Threads" to "1/2\" Female x 1/2\" Male", "Tube Barb" to "1/4\" Tube", "Material" to "Solid Brass"),
            warranty = "3 Years Leak-Proof Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80"
        ),
        Product(
            id = "spr-07",
            name = "Califnco Food-Grade RO PE Tubing 1/4-inch 20m Roll",
            category = ProductCategory.SPARES,
            subCategory = "Tubing & Pipes",
            retailPrice = 349.0,
            wholesalePrice = 175.0,
            moq = 20,
            rating = 4.8,
            reviewCount = 560,
            stockCount = 650,
            description = "NSF-51 certified virgin polyethylene blue & white tubing for potable water purifiers. Kink and crack resistant.",
            keyFeatures = listOf("100% Virgin Food-Grade PE", "Burst Pressure 250 PSI", "Uniform Outer Diameter", "Kink-Resistant"),
            specifications = mapOf("Length" to "20 Metres", "Outer Diameter" to "6.35 mm (1/4\")", "Temperature" to "Up to 50°C"),
            warranty = "Quality Assurance",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80"
        ),
        Product(
            id = "spr-08",
            name = "Califnco High-Flow 3/8-inch Tubing 10m Roll",
            category = ProductCategory.SPARES,
            subCategory = "Tubing & Pipes",
            retailPrice = 299.0,
            wholesalePrice = 150.0,
            moq = 20,
            rating = 4.7,
            reviewCount = 190,
            stockCount = 420,
            description = "Thick-walled 3/8-inch tubing for commercial RO feed lines, under-sink tanks, and booster pump inlets.",
            keyFeatures = listOf("High Throughput 3/8\"", "Pressure Tested 300 PSI", "UV Degradation Resistant", "White Clean Finish"),
            specifications = mapOf("Length" to "10 Metres", "OD" to "9.52 mm (3/8\")", "Material" to "LLDPE Food-Grade"),
            warranty = "Quality Assurance",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80"
        ),
        Product(
            id = "spr-09",
            name = "Califnco Quick-Connect Push Elbow & Tee Kit (Pack of 10)",
            category = ProductCategory.SPARES,
            subCategory = "Fittings",
            retailPrice = 249.0,
            wholesalePrice = 110.0,
            moq = 30,
            rating = 4.7,
            reviewCount = 380,
            stockCount = 900,
            description = "Assorted pack of 10 leak-free quick-push fittings with stainless steel collet locking teeth and double EPDM O-rings.",
            keyFeatures = listOf("Stainless Steel Grip Teeth", "Dual EPDM O-Rings", "Includes Locking Blue Clips", "Tool-Free Push Install"),
            specifications = mapOf("Pack Contents" to "4x Elbows, 3x Tees, 3x Straight Connectors", "Fit" to "1/4\" Tube", "Pressure" to "Up to 150 PSI"),
            warranty = "1 Year Replacement",
            imageUrl = "https://images.unsplash.com/photo-1581092335397-9583fe92d232?w=800&q=80"
        ),
        Product(
            id = "spr-10",
            name = "Califnco Stainless Steel Gooseneck Designer Faucet",
            category = ProductCategory.SPARES,
            subCategory = "Faucets",
            retailPrice = 1299.0,
            wholesalePrice = 720.0,
            moq = 12,
            rating = 4.9,
            reviewCount = 240,
            stockCount = 180,
            description = "Lead-free SUS304 brushed stainless steel 360-degree swivel gooseneck faucet with ceramic disc valve.",
            keyFeatures = listOf("100% Lead-Free SUS304", "360° Smooth Swivel Spout", "Brushed Satin Finish", "Countertop Deck Mount"),
            specifications = mapOf("Height" to "28 cm", "Hole Diameter" to "12-28 mm", "Stem Length" to "70 mm", "Connection" to "1/4\" Tube"),
            warranty = "3 Years Finish & Leak Warranty",
            imageUrl = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=800&q=80",
            isBestseller = true
        ),
        Product(
            id = "spr-11",
            name = "Califnco 12L Antibacterial Pressurized Storage Tank",
            category = ProductCategory.SPARES,
            subCategory = "Storage Tanks",
            retailPrice = 2499.0,
            wholesalePrice = 1450.0,
            moq = 6,
            rating = 4.8,
            reviewCount = 195,
            stockCount = 130,
            description = "Post-cured butyl diaphragm pressure storage tank with stainless steel water connection and virgin polypropylene liner.",
            keyFeatures = listOf("Antibacterial Food Liner", "Virgin Butyl Diaphragm", "Pre-charged 7 PSI Bladder", "Includes Tank Ball Valve"),
            specifications = mapOf("Gross Volume" to "12 Litres (3.2 Gal)", "Working Pressure" to "50 PSI", "Dimensions" to "28x28x38 cm"),
            warranty = "2 Years Tank Warranty",
            imageUrl = "https://images.unsplash.com/photo-1548839140-29a749e1bc4e?w=800&q=80"
        ),
        Product(
            id = "spr-12",
            name = "Califnco Constant Voltage 24V 2.5A SMPS Power Supply",
            category = ProductCategory.SPARES,
            subCategory = "Electrical",
            retailPrice = 699.0,
            wholesalePrice = 380.0,
            moq = 15,
            rating = 4.7,
            reviewCount = 310,
            stockCount = 350,
            description = "High efficiency switch-mode power adapter powering RO booster pumps and solenoid valves with surge and short-circuit protection.",
            keyFeatures = listOf("Surge Protection up to 4kV", "Pure 24V 2.5A Output", "Flame-Retardant ABS Shell", "Overheat Cutoff"),
            specifications = mapOf("Input" to "140V-280V AC 50Hz", "Output" to "24V DC 2.5A", "Efficiency" to "> 85%"),
            warranty = "1 Year Replacement Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80"
        ),
        Product(
            id = "spr-13",
            name = "Califnco Electronic UV Choke Ballast 11W",
            category = ProductCategory.SPARES,
            subCategory = "Electrical",
            retailPrice = 449.0,
            wholesalePrice = 240.0,
            moq = 20,
            rating = 4.6,
            reviewCount = 185,
            stockCount = 400,
            description = "High-frequency electronic ballast with LED audio buzzer alert that rings if the UV sterilization lamp fuses.",
            keyFeatures = listOf("Lamp Failure Audio Buzzer", "Dual LED Health Indicator", "Moisture Sealed Potted Circuit", "11W Spec"),
            specifications = mapOf("Voltage" to "220V AC", "Lamp Power" to "11W / 16W", "Alarm" to "Audible Beep + Red LED"),
            warranty = "1 Year Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092335397-9583fe92d232?w=800&q=80"
        ),
        Product(
            id = "spr-14",
            name = "Califnco Water Hardness Titration Drop Test Kit",
            category = ProductCategory.SPARES,
            subCategory = "Meters & Testers",
            retailPrice = 399.0,
            wholesalePrice = 190.0,
            moq = 20,
            rating = 4.5,
            reviewCount = 140,
            stockCount = 300,
            description = "Professional chemical drop test kit measuring exact water hardness in PPM/Grains to determine softener or RO requirement.",
            keyFeatures = listOf("Over 100 Tests Per Bottle", "Instant Color Change Endpoint", "Includes Calibrated Vial", "Step-by-step Guide"),
            specifications = mapOf("Sensitivity" to "1 Drop = 25 PPM CaCO3", "Bottles" to "Reagent A & B", "Shelf Life" to "2 Years"),
            warranty = "Quality Assurance",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=800&q=80"
        ),
        Product(
            id = "spr-15",
            name = "Califnco Smart Leakage Sensor Auto-Shutoff Valve",
            category = ProductCategory.SPARES,
            subCategory = "Safety",
            retailPrice = 999.0,
            wholesalePrice = 540.0,
            moq = 15,
            rating = 4.9,
            reviewCount = 110,
            stockCount = 160,
            description = "Battery-powered floor sensor valve that detects moisture drops under the sink and slams the incoming water line shut in 1 second.",
            keyFeatures = listOf("Instant 1-Second Emergency Cutoff", "Loud 85dB Warning Siren", "Protects Kitchen Cabinets", "Zero Plumbing Tools"),
            specifications = mapOf("Response Time" to "< 1s", "Power" to "9V Battery Included", "Valve Size" to "1/4\" Push-In"),
            warranty = "2 Years Warranty",
            imageUrl = "https://images.unsplash.com/photo-1581092335397-9583fe92d232?w=800&q=80",
            isNewArrival = true
        )
    )

    // Reactive Cart State
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    fun addToCart(product: Product, qty: Int = 1) {
        val currentList = _cart.value.toMutableList()
        val index = currentList.indexOfFirst { it.product.id == product.id }
        if (index != -1) {
            val existing = currentList[index]
            currentList[index] = existing.copy(quantity = existing.quantity + qty)
        } else {
            val finalQty = if (_currentRole.value == UserRole.DEALER && qty < product.moq) product.moq else qty
            currentList.add(CartItem(product = product, quantity = finalQty))
        }
        _cart.value = currentList
    }

    fun updateQuantity(productId: String, newQty: Int) {
        if (newQty <= 0) {
            removeFromCart(productId)
            return
        }
        _cart.value = _cart.value.map {
            if (it.product.id == productId) it.copy(quantity = newQty) else it
        }
    }

    fun removeFromCart(productId: String) {
        _cart.value = _cart.value.filterNot { it.product.id == productId }
    }

    fun clearCart() {
        _cart.value = emptyList()
    }

    // Orders State
    private val _orders = MutableStateFlow<List<Order>>(
        listOf(
            Order(
                id = "ORD-2026-9812",
                userRole = UserRole.CUSTOMER,
                customerName = "Piyush Nagbansi",
                customerPhone = "+91 98765 43210",
                items = listOf(
                    CartItem(products[0], 1)
                ),
                subtotal = 14999.0,
                discount = 500.0,
                gst = 2609.82,
                totalAmount = 17108.82,
                shippingAddress = "Flat 402, Green Valley Apartments, Andheri West, Mumbai, MH - 400053",
                paymentMethod = "Razorpay (UPI - Google Pay)",
                razorpayPaymentId = "pay_Nxi98Kz710a",
                status = OrderStatus.DISPATCHED,
                createdAt = System.currentTimeMillis() - 86400000L * 2
            ),
            Order(
                id = "ORD-2026-8744",
                userRole = UserRole.DEALER,
                customerName = "AquaTech Solutions Ltd (Dealer)",
                customerPhone = "+91 98220 11223",
                items = listOf(
                    CartItem(products[20], 10), // 10 Kemflo pumps
                    CartItem(products[30], 20)  // 20 Sediment filters
                ),
                subtotal = 16400.0,
                discount = 1200.0,
                gst = 2736.0,
                totalAmount = 17936.0,
                shippingAddress = "Warehouse #4B, MIDC Industrial Area, Pune, MH - 411019",
                paymentMethod = "Razorpay B2B Credit (NetBanking)",
                razorpayPaymentId = "pay_B2BMx761p0",
                status = OrderStatus.CONFIRMED,
                createdAt = System.currentTimeMillis() - 86400000L * 4,
                isDealerOrder = true,
                gstin = "27AAACA9921M1Z4"
            )
        )
    )
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    fun placeOrder(
        customerName: String,
        customerPhone: String,
        address: String,
        paymentMethod: String,
        razorpayPaymentId: String,
        discount: Double = 0.0,
        gstin: String? = null
    ): Order {
        val currentItems = _cart.value
        val isDealer = _currentRole.value == UserRole.DEALER
        val subtotal = currentItems.sumOf {
            val unitPrice = if (isDealer) it.product.wholesalePrice else it.product.retailPrice
            unitPrice * it.quantity
        }
        val taxable = (subtotal - discount).coerceAtLeast(0.0)
        val gst = taxable * 0.18
        val total = taxable + gst

        val newOrder = Order(
            id = "ORD-2026-${(1000..9999).random()}",
            userRole = _currentRole.value,
            customerName = customerName,
            customerPhone = customerPhone,
            items = currentItems,
            subtotal = subtotal,
            discount = discount,
            gst = gst,
            totalAmount = total,
            shippingAddress = address,
            paymentMethod = paymentMethod,
            razorpayPaymentId = razorpayPaymentId,
            status = OrderStatus.PLACED,
            createdAt = System.currentTimeMillis(),
            isDealerOrder = isDealer,
            gstin = gstin
        )

        _orders.value = listOf(newOrder) + _orders.value
        clearCart()

        // Trigger SMS & Email simulation
        dispatchEmailNotification(
            to = "$customerPhone (SMS & EmailJS)",
            subject = "Califnco Order Confirmation #${newOrder.id}",
            body = "Thank you! Your payment of ₹${"%,.2f".format(total)} via $paymentMethod was verified. Razorpay ID: $razorpayPaymentId. Delivery tracking active."
        )

        addNotification(
            title = "Order #${newOrder.id} Placed Successfully",
            message = "We've received your order of ${currentItems.size} items. Razorpay ID: $razorpayPaymentId",
            type = "ORDER"
        )

        return newOrder
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        _orders.value = _orders.value.map {
            if (it.id == orderId) it.copy(status = newStatus) else it
        }
        addNotification(
            title = "Order $orderId Updated",
            message = "Status changed to: ${newStatus.label}",
            type = "ORDER"
        )
    }

    // Service Desk State
    private val _serviceBookings = MutableStateFlow<List<ServiceBooking>>(
        listOf(
            ServiceBooking(
                id = "SRV-8821",
                customerName = "Dr. Suresh Sharma",
                customerPhone = "+91 94220 88712",
                address = "12B, Palm Grove Villa, Jubilee Hills, Hyderabad - 500033",
                purifierModel = "Califnco AquaShield Alkaline RO",
                serviceType = ServiceType.PERIODIC_MAINTENANCE,
                preferredDate = "25 Oct 2026",
                preferredSlot = "10:00 AM - 01:00 PM",
                assignedTechnician = "Rajesh Verma (Senior Tech #42)",
                status = ServiceStatus.TECHNICIAN_ASSIGNED,
                notes = "TDS value increased from 80 to 240 PPM. Filter replacement check required."
            ),
            ServiceBooking(
                id = "SRV-8790",
                customerName = "Pooja Patel",
                customerPhone = "+91 98980 34120",
                address = "Flat 101, Nilamber Heights, Vasna, Vadodara - 390007",
                purifierModel = "Califnco PureFlow Copper Max",
                serviceType = ServiceType.NEW_INSTALLATION,
                preferredDate = "26 Oct 2026",
                preferredSlot = "02:00 PM - 05:00 PM",
                assignedTechnician = null,
                status = ServiceStatus.SCHEDULED,
                notes = "New home installation. Marble countertop drilling needed."
            )
        )
    )
    val serviceBookings: StateFlow<List<ServiceBooking>> = _serviceBookings.asStateFlow()

    fun bookService(
        customerName: String,
        customerPhone: String,
        address: String,
        purifierModel: String,
        serviceType: ServiceType,
        preferredDate: String,
        preferredSlot: String,
        notes: String
    ): ServiceBooking {
        val newBooking = ServiceBooking(
            id = "SRV-${(1000..9999).random()}",
            customerName = customerName,
            customerPhone = customerPhone,
            address = address,
            purifierModel = purifierModel,
            serviceType = serviceType,
            preferredDate = preferredDate,
            preferredSlot = preferredSlot,
            notes = notes,
            status = ServiceStatus.PENDING,
            createdAt = System.currentTimeMillis()
        )
        _serviceBookings.value = listOf(newBooking) + _serviceBookings.value

        dispatchEmailNotification(
            to = customerPhone,
            subject = "Service Ticket #${newBooking.id} Booked",
            body = "Your ${serviceType.title} service request has been logged. A certified technician will visit on $preferredDate between $preferredSlot."
        )

        addNotification(
            title = "Service Ticket #${newBooking.id} Raised",
            message = "Scheduled for $preferredDate ($preferredSlot). Califnco service team notified.",
            type = "SERVICE"
        )

        return newBooking
    }

    fun updateServiceStatus(bookingId: String, status: ServiceStatus, techName: String? = null) {
        _serviceBookings.value = _serviceBookings.value.map {
            if (it.id == bookingId) {
                it.copy(
                    status = status,
                    assignedTechnician = techName ?: it.assignedTechnician
                )
            } else it
        }
        addNotification(
            title = "Service Ticket $bookingId Updated",
            message = "New status: ${status.label}" + (if (!techName.isNullOrBlank()) " | Tech: $techName" else ""),
            type = "SERVICE"
        )
    }

    // Dealer Management State
    private val _dealerApplications = MutableStateFlow<List<DealerApplication>>(
        listOf(
            DealerApplication(
                id = "DLR-101",
                businessName = "Shree Jal Enterprises",
                contactPerson = "Ramesh Gupta",
                phone = "+91 97654 32109",
                email = "shreejal@gmail.com",
                gstin = "07AAAAA0000A1Z5",
                city = "New Delhi",
                expectedVolume = "₹5,00,000 / month",
                status = DealerApprovalStatus.APPROVED,
                creditLimit = 150000.0,
                tier = "Tier 1 Wholesaler"
            ),
            DealerApplication(
                id = "DLR-102",
                businessName = "Apex Water Solutions",
                contactPerson = "Vikas Singhal",
                phone = "+91 91234 56789",
                email = "apexwaters@yahoo.com",
                gstin = "29BBBBB1111B1Z2",
                city = "Bengaluru",
                expectedVolume = "₹2,50,000 / month",
                status = DealerApprovalStatus.PENDING,
                creditLimit = 75000.0,
                tier = "Tier 2 Partner"
            )
        )
    )
    val dealerApplications: StateFlow<List<DealerApplication>> = _dealerApplications.asStateFlow()

    fun updateDealerStatus(dealerId: String, status: DealerApprovalStatus) {
        _dealerApplications.value = _dealerApplications.value.map {
            if (it.id == dealerId) it.copy(status = status) else it
        }
        addNotification(
            title = "Dealer Application $dealerId",
            message = "Status updated to: ${status.name}",
            type = "DEALER"
        )
    }

    fun submitDealerApplication(
        businessName: String,
        contactPerson: String,
        phone: String,
        email: String,
        gstin: String,
        city: String,
        volume: String
    ): DealerApplication {
        val newApp = DealerApplication(
            id = "DLR-${(200..999).random()}",
            businessName = businessName,
            contactPerson = contactPerson,
            phone = phone,
            email = email,
            gstin = gstin,
            city = city,
            expectedVolume = volume,
            status = DealerApprovalStatus.PENDING
        )
        _dealerApplications.value = listOf(newApp) + _dealerApplications.value
        addNotification(
            title = "Dealer Application Submitted",
            message = "Application ${newApp.id} for $businessName is under Admin review.",
            type = "DEALER"
        )
        return newApp
    }

    // Notifications State
    private val _notifications = MutableStateFlow<List<NotificationItem>>(
        listOf(
            NotificationItem(
                id = "notif-01",
                title = "Welcome to Califnco!",
                message = "Explore over 50+ premium water purifiers, spares, and accessories with doorstep service.",
                type = "PROMO"
            ),
            NotificationItem(
                id = "notif-02",
                title = "Wholesale Pricing Unlocked",
                message = "Dealers and distributors can purchase bulk units at up to 40% wholesale discount.",
                type = "DEALER"
            ),
            NotificationItem(
                id = "notif-03",
                title = "TDS Health Monitor Alert",
                message = "Schedule your seasonal membrane flush and pre-filter replacement today.",
                type = "ALERT"
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    fun addNotification(title: String, message: String, type: String) {
        val newNotif = NotificationItem(
            id = "notif-${UUID.randomUUID().toString().take(6)}",
            title = title,
            message = message,
            type = type,
            timestamp = System.currentTimeMillis()
        )
        _notifications.value = listOf(newNotif) + _notifications.value
    }

    // Email & SMS Dispatcher Logs (Simulating EmailJS & TextBee)
    data class DispatchLog(
        val to: String,
        val subject: String,
        val body: String,
        val channel: String, // "EmailJS (Email)" or "TextBee (SMS)"
        val timestamp: Long = System.currentTimeMillis()
    )

    private val _dispatchLogs = MutableStateFlow<List<DispatchLog>>(emptyList())
    val dispatchLogs: StateFlow<List<DispatchLog>> = _dispatchLogs.asStateFlow()

    private fun dispatchEmailNotification(to: String, subject: String, body: String) {
        val log = DispatchLog(
            to = to,
            subject = subject,
            body = body,
            channel = "EmailJS & TextBee"
        )
        _dispatchLogs.value = listOf(log) + _dispatchLogs.value
    }

    // AI Social Automation Hub State (Mandatory Assignment Requirement)
    private val _aiPosts = MutableStateFlow<List<AiSocialPost>>(
        listOf(
            AiSocialPost(
                id = "post-01",
                productId = "pur-01",
                productName = "Califnco AquaShield Alkaline RO+UV+MTDS",
                caption = "💧 Transform your hydration with the all-new Califnco AquaShield Alkaline RO! Featuring 10-stage purification and natural copper-alkaline balance (pH 8.5+) for crystal clear, sweet water. Say goodbye to harmful TDS and heavy metals. Order online today or contact your local dealer! ✨",
                hashtags = "#CalifncoRO #CleanWater #AlkalineWater #WaterPurifier #PureWaterPureLife #HealthyLiving #HomeAppliances #SmartLiving",
                imageUrl = "https://images.unsplash.com/photo-1548839140-29a749e1bc4e?w=800&q=80",
                targetPlatform = "Instagram",
                scheduledTime = "Daily 09:00 AM IST",
                isPublished = true,
                publicHostingUrl = "https://res.cloudinary.com/califnco/image/upload/v1729600000/aquashield_promo.jpg"
            ),
            AiSocialPost(
                id = "post-02",
                productId = "dsp-01",
                productName = "Califnco Glacier Touchless Water Dispenser",
                caption = "❄️ Hands-free hygiene meets instant cooling! The Califnco Glacier Touchless Water Dispenser dispenses chilled and hot water without touching a single button. Designed for modern offices and conscious homes. 🏢✨",
                hashtags = "#Califnco #TouchlessDispenser #OfficeStyle #PureHydration #WaterCooler #SmartTech",
                imageUrl = "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=800&q=80",
                targetPlatform = "WhatsApp Status",
                scheduledTime = "Daily 11:30 AM IST",
                isPublished = true,
                publicHostingUrl = "https://res.cloudinary.com/califnco/image/upload/v1729600000/glacier_waha.jpg"
            )
        )
    )
    val aiPosts: StateFlow<List<AiSocialPost>> = _aiPosts.asStateFlow()

    fun generateDailyAiPost(product: Product): AiSocialPost {
        val newPost = AiSocialPost(
            id = "post-${UUID.randomUUID().toString().take(6)}",
            productId = product.id,
            productName = product.name,
            caption = "🚀 Spotlight on ${product.name}! Engineered with ${product.keyFeatures.joinToString(", ")}. Ensures 100% pure taste and certified health protection. Retail: ₹${"%,.0f".format(product.retailPrice)} | Dealer Wholesale: ₹${"%,.0f".format(product.wholesalePrice)}. Visit Califnco to grab yours!",
            hashtags = "#Califnco #${product.category.name.lowercase().replaceFirstChar { it.uppercase() }} #PureWater #WaterTech #EcoFriendly #MadeInIndia",
            imageUrl = product.imageUrl,
            targetPlatform = "Instagram",
            scheduledTime = "Automated Cron (Daily Scheduled)",
            isPublished = false,
            publicHostingUrl = "https://res.cloudinary.com/califnco/image/upload/v1729600000/${product.id}.jpg"
        )
        _aiPosts.value = listOf(newPost) + _aiPosts.value
        addNotification(
            title = "AI Automation Post Generated",
            message = "New daily campaign created for ${product.name}. Ready for Meta Graph API / WAHA dispatch.",
            type = "SYSTEM"
        )
        return newPost
    }

    fun publishSocialPost(postId: String) {
        _aiPosts.value = _aiPosts.value.map {
            if (it.id == postId) it.copy(isPublished = true) else it
        }
        addNotification(
            title = "Social Post Published",
            message = "Dispatched via Meta Graph API & Cloudinary public asset CDN.",
            type = "SYSTEM"
        )
    }

    fun broadcastWhatsAppStatus(product: Product): String {
        addNotification(
            title = "WAHA WhatsApp Status Broadcasted",
            message = "Product story for '${product.name}' pushed to WhatsApp Status via WAHA Docker webhook.",
            type = "SYSTEM"
        )
        return "WAHA WhatsApp Status HTTP 200 OK: Broadcasted to WhatsApp Status via port 3000."
    }

    // Referral Program (+15 Bonus Points)
    val referralCode = "CALIF-PIYUSH26"
    private val _referralCount = MutableStateFlow(8)
    val referralCount: StateFlow<Int> = _referralCount.asStateFlow()
    private val _referralRewards = MutableStateFlow(4000.0) // ₹4,000 earned
    val referralRewards: StateFlow<Double> = _referralRewards.asStateFlow()

    fun applyReferralBonus() {
        _referralCount.value += 1
        _referralRewards.value += 500.0
        addNotification(
            title = "Referral Reward Credited!",
            message = "A friend used your code $referralCode! ₹500 credited to your Califnco wallet.",
            type = "PROMO"
        )
    }

    // === USER REGISTRATION & AUTHENTICATION (GATEWAY FIRST) ===
    fun registerCustomer(
        name: String,
        email: String,
        phone: String,
        city: String,
        referralInput: String? = null
    ): UserProfile {
        val bonus = if (!referralInput.isNullOrBlank() && referralInput.trim().equals(referralCode, ignoreCase = true)) 500.0 else 250.0
        val profile = UserProfile(
            id = "USR-${(1000..9999).random()}",
            name = name,
            email = email,
            phone = phone,
            role = UserRole.CUSTOMER,
            city = city.ifBlank { "Mumbai" },
            walletBalance = bonus
        )
        _currentUser.value = profile
        setRole(UserRole.CUSTOMER)
        addNotification(
            title = "Welcome to Califnco, $name!",
            message = "Account created successfully. ₹${"%,.0f".format(bonus)} welcome bonus added to your wallet.",
            type = "PROMO"
        )
        return profile
    }

    fun registerDealer(
        businessName: String,
        contactPerson: String,
        email: String,
        phone: String,
        gstin: String,
        city: String
    ): UserProfile {
        val profile = UserProfile(
            id = "DLR-${(1000..9999).random()}",
            name = contactPerson,
            email = email,
            phone = phone,
            role = UserRole.DEALER,
            city = city.ifBlank { "Pune" },
            businessName = businessName,
            gstin = gstin,
            walletBalance = 1000.0
        )
        _currentUser.value = profile
        setRole(UserRole.DEALER)
        submitDealerApplication(businessName, contactPerson, phone, email, gstin, city, "₹5,00,000 / month")
        addNotification(
            title = "Wholesale Dealer Account Active",
            message = "Welcome, $businessName! Wholesale discounts (30-40%) and GST invoice billing unlocked.",
            type = "DEALER"
        )
        return profile
    }

    fun login(emailOrPhone: String, role: UserRole): UserProfile {
        val isDealer = role == UserRole.DEALER
        val profile = UserProfile(
            id = if (isDealer) "DLR-9921" else "USR-4412",
            name = if (isDealer) "AquaTech Dealer (Ramesh)" else (if (role == UserRole.ADMIN) "Califnco Admin" else "Piyush Nagbansi"),
            email = if (emailOrPhone.contains("@")) emailOrPhone else "piyushnagbansi500@gmail.com",
            phone = if (!emailOrPhone.contains("@") && emailOrPhone.isNotBlank()) emailOrPhone else "+91 98765 43210",
            role = role,
            city = "Mumbai",
            businessName = if (isDealer) "AquaTech Solutions Ltd" else null,
            gstin = if (isDealer) "27AAACA9921M1Z4" else null,
            walletBalance = if (isDealer) 2500.0 else 500.0
        )
        _currentUser.value = profile
        setRole(role)
        addNotification(
            title = "Logged in as ${profile.name}",
            message = "Session restored in ${role.name.lowercase().replaceFirstChar { it.uppercase() }} mode.",
            type = "SYSTEM"
        )
        return profile
    }

    fun loginWithGoogle(role: UserRole): UserProfile {
        val profile = UserProfile(
            id = "GOOGLE-${(1000..9999).random()}",
            name = "Piyush Nagbansi",
            email = "piyushnagbansi500@gmail.com",
            phone = "+91 98765 43210",
            role = role,
            city = "Mumbai",
            walletBalance = 500.0
        )
        _currentUser.value = profile
        setRole(role)
        addNotification(
            title = "Signed in with Google",
            message = "Google ID verified. Welcome back, ${profile.name}!",
            type = "SYSTEM"
        )
        return profile
    }

    fun logout() {
        _currentUser.value = null
        clearCart()
        addNotification(
            title = "Logged Out",
            message = "You have safely logged out of Califnco.",
            type = "SYSTEM"
        )
    }

    // === TDS WATER QUALITY & FILTER LIFE ANALYZER ===
    fun analyzeTds(ppm: Int): TdsAnalysisResult {
        return when {
            ppm < 150 -> TdsAnalysisResult(
                tdsPpm = ppm,
                ratingColor = "Good",
                hardnessCategory = "Soft / Municipal Tap Water (0-150 PPM)",
                healthImplication = "Water has low dissolved minerals. Ideal mineral balance.",
                recommendedTech = "Califnco UV + UF Ultrafiltration (No RO needed - saves 100% water!)",
                estimatedMembraneLifeMonths = 36,
                suitablePurifierIds = listOf("pur-05", "pur-08")
            )
            ppm in 150..400 -> TdsAnalysisResult(
                tdsPpm = ppm,
                ratingColor = "Moderate",
                hardnessCategory = "Moderate Hardness (150-400 PPM)",
                healthImplication = "Safe, but contains calcium/magnesium scaling salts & pipe chlorine.",
                recommendedTech = "Califnco PureFlow RO + MTDS Controller",
                estimatedMembraneLifeMonths = 24,
                suitablePurifierIds = listOf("pur-01", "pur-02", "pur-10")
            )
            ppm in 401..900 -> TdsAnalysisResult(
                tdsPpm = ppm,
                ratingColor = "High",
                hardnessCategory = "High Hardness / Borewell & Tanker Mix (400-900 PPM)",
                healthImplication = "High salt content, harsh on kidneys and causes heavy scaling on appliances.",
                recommendedTech = "Califnco AquaShield Alkaline RO + UV + TDS Mineralizer",
                estimatedMembraneLifeMonths = 18,
                suitablePurifierIds = listOf("pur-01", "pur-02", "pur-04")
            )
            else -> TdsAnalysisResult(
                tdsPpm = ppm,
                ratingColor = "Critical",
                hardnessCategory = "Critical High TDS Borewell (900-3000+ PPM)",
                healthImplication = "Heavy hardness, dissolved heavy metals, and brackish salinity.",
                recommendedTech = "Califnco Commercial / 100 GPD Heavy-Duty TFC Multi-Stage RO",
                estimatedMembraneLifeMonths = 12,
                suitablePurifierIds = listOf("pur-03", "pur-04", "pur-06", "pur-07")
            )
        }
    }

    // === WATER PURIFIER COMPARISON FEATURE ===
    private val _compareList = MutableStateFlow<List<Product>>(emptyList())
    val compareList: StateFlow<List<Product>> = _compareList.asStateFlow()

    fun toggleCompare(product: Product): Boolean {
        val current = _compareList.value.toMutableList()
        val exists = current.any { it.id == product.id }
        if (exists) {
            current.removeAll { it.id == product.id }
            _compareList.value = current
            return false
        } else {
            if (current.size >= 3) {
                current.removeAt(0) // Keep maximum 3 products
            }
            current.add(product)
            _compareList.value = current
            return true
        }
    }

    fun clearCompare() {
        _compareList.value = emptyList()
    }

    // === ANNUAL MAINTENANCE CONTRACT (AMC) SUBSCRIPTIONS ===
    val amcPlans: List<AmcSubscription> = listOf(
        AmcSubscription(
            id = "amc-silver",
            planName = "Califnco Silver Care AMC",
            annualCost = 1999.0,
            filterReplacements = "2 Spun Sediment + 1 Carbon CTO",
            freeVisits = "3 Free Technician Visits",
            features = listOf(
                "3 Preventive Maintenance visits/year",
                "2 Free Sediment Filters replaced",
                "1 Free Activated Carbon Cartridge",
                "TDS & Water Quality check on every visit",
                "Zero labor charges for breakdown repairs"
            ),
            expiryDate = "1 Year Protection"
        ),
        AmcSubscription(
            id = "amc-gold",
            planName = "Califnco Gold Shield Total AMC",
            annualCost = 3499.0,
            filterReplacements = "Full Filter Kit + RO Membrane Coverage",
            freeVisits = "Unlimited Service Calls",
            features = listOf(
                "Unlimited Free Breakdown Visits",
                "Complete Filter Replacement Kit (Sediment, Carbon, Post-Silver)",
                "RO Membrane Replacement coverage (100% Free)",
                "Booster Pump & SMPS electrical breakdown covered",
                "Priority 4-hour technician dispatch"
            ),
            isActive = true,
            expiryDate = "Active until 24 Oct 2027"
        )
    )

    private val _activeSubscription = MutableStateFlow<AmcSubscription?>(amcPlans[1])
    val activeSubscription: StateFlow<AmcSubscription?> = _activeSubscription.asStateFlow()

    fun subscribeAmc(plan: AmcSubscription) {
        _activeSubscription.value = plan.copy(isActive = true, expiryDate = "Active for 12 Months")
        addNotification(
            title = "AMC Activated: ${plan.planName}",
            message = "Your purifier is now covered with doorstep filter replacement & priority maintenance.",
            type = "SERVICE"
        )
    }

    // === PRODUCT REVIEWS & RATINGS (SCREENSHOT 4) ===
    private val _reviews = MutableStateFlow<List<ProductReview>>(
        listOf(
            ProductReview(
                id = "rev-101",
                productId = "pur-01",
                authorName = "Rohan Sharma",
                rating = 5,
                dateStr = "2 days ago",
                comment = "Water taste is exceptionally sweet and crisp. The TDS display shows 65 PPM reduced from 850 PPM municipal water. Doorstep technician installation was completed within 3 hours.",
                isVerified = true,
                helpfulCount = 28
            ),
            ProductReview(
                id = "rev-102",
                productId = "pur-01",
                authorName = "Ananya Deshmukh",
                rating = 5,
                dateStr = "1 week ago",
                comment = "Best water purifier we've owned! The copper and alkaline cartridges make a visible difference in digestion. Compact wall-mount looks very sleek in our modern modular kitchen.",
                isVerified = true,
                helpfulCount = 14
            ),
            ProductReview(
                id = "rev-103",
                productId = "pur-01",
                authorName = "Vikram Malhotra",
                rating = 4,
                dateStr = "2 weeks ago",
                comment = "Excellent filtration speed and quiet booster pump. Installation was smooth. Tank capacity of 8 liters is ample for a family of four.",
                isVerified = true,
                helpfulCount = 9
            ),
            ProductReview(
                id = "rev-104",
                productId = "pur-02",
                authorName = "Kavita Rao",
                rating = 5,
                dateStr = "3 days ago",
                comment = "The pure copper chamber gives a natural soothing taste. Zero water leakage and the stainless steel tank is so much more hygienic than plastic tanks.",
                isVerified = true,
                helpfulCount = 19
            ),
            ProductReview(
                id = "rev-105",
                productId = "pur-04",
                authorName = "Aditya Sen",
                rating = 5,
                dateStr = "4 days ago",
                comment = "The IoT telemetry is unbelievable! I can monitor the exact filter health and incoming vs outgoing TDS directly on my phone. Highly recommend!",
                isVerified = true,
                helpfulCount = 12
            )
        )
    )
    val reviews: StateFlow<List<ProductReview>> = _reviews.asStateFlow()

    fun getReviewsForProduct(productId: String): List<ProductReview> {
        val list = _reviews.value.filter { it.productId == productId }
        return if (list.isNotEmpty()) list else listOf(
            ProductReview(
                id = "rev-gen-1",
                productId = productId,
                authorName = "Piyush Nagbansi",
                rating = 5,
                dateStr = "Recently",
                comment = "Authentic Califnco certified quality. Robust packaging, quick delivery, and hassle-free operation.",
                isVerified = true,
                helpfulCount = 7
            )
        )
    }

    fun addReview(productId: String, authorName: String, rating: Int, comment: String) {
        val newReview = ProductReview(
            id = "rev-${UUID.randomUUID().toString().take(6)}",
            productId = productId,
            authorName = authorName.ifBlank { _currentUser.value?.name ?: "Customer" },
            rating = rating,
            dateStr = "Just now",
            comment = comment,
            isVerified = true,
            helpfulCount = 1
        )
        _reviews.value = listOf(newReview) + _reviews.value
        addNotification(
            title = "Review Submitted",
            message = "Thank you for reviewing! 50 Califnco coins credited for your verified rating.",
            type = "PROMO"
        )
    }

    // === FILTER REPLACEMENT REMINDER TRACKER ===
    private val _filterSchedule = MutableStateFlow(
        FilterReplacementSchedule(
            purifierName = "Califnco AquaShield Alkaline RO",
            installedDate = "15 Jan 2026",
            daysRemaining = 38,
            healthPercentage = 74,
            nextFilterType = "Pre-Sediment + Activated Carbon CTO Kit",
            partProductId = "flt-01"
        )
    )
    val filterSchedule: StateFlow<FilterReplacementSchedule> = _filterSchedule.asStateFlow()

    // === SMART PRODUCT FINDER WIZARD ===
    fun recommendPurifiers(answers: SmartFinderAnswers): List<Product> {
        val purifiers = products.filter { it.category == ProductCategory.PURIFIERS }
        return when {
            answers.waterSource.contains("Municipal", ignoreCase = true) -> {
                purifiers.filter { it.subCategory.contains("UF") || it.subCategory.contains("Alkaline") || it.retailPrice < 16000 }
                    .take(3)
            }
            answers.waterSource.contains("Borewell", ignoreCase = true) -> {
                purifiers.filter { it.subCategory.contains("Copper") || it.subCategory.contains("IoT") || it.retailPrice >= 16000 }
                    .take(3)
            }
            else -> {
                purifiers.sortedByDescending { it.rating }.take(3)
            }
        }
    }

    // === INVENTORY & STOCK MANAGEMENT ===
    private val _stockOverrides = MutableStateFlow<Map<String, Int>>(
        mapOf(
            "pur-04" to 8,  // Low stock warning (< 15)
            "flt-03" to 5,  // Low stock warning (< 15)
            "pmp-02" to 11  // Low stock warning (< 15)
        )
    )
    val stockOverrides: StateFlow<Map<String, Int>> = _stockOverrides.asStateFlow()

    fun getStock(product: Product): Int {
        return _stockOverrides.value[product.id] ?: product.stockCount
    }

    fun restockProduct(productId: String, addedUnits: Int) {
        val current = _stockOverrides.value.toMutableMap()
        val currentStock = current[productId] ?: (products.find { it.id == productId }?.stockCount ?: 40)
        current[productId] = currentStock + addedUnits
        _stockOverrides.value = current
        addNotification(
            title = "Inventory Restocked",
            message = "Product $productId replenished with +$addedUnits units. Stock level now: ${current[productId]}.",
            type = "ALERT"
        )
    }
}

