package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceBooking
import com.example.data.model.ServiceStatus
import com.example.data.model.ServiceType
import com.example.ui.theme.*

@Composable
fun ServiceDeskScreen(
    bookings: List<ServiceBooking>,
    onBookService: (name: String, phone: String, address: String, model: String, type: ServiceType, date: String, slot: String, notes: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Book Service, 1: My Tickets

    var name by remember { mutableStateOf("Piyush Nagbansi") }
    var phone by remember { mutableStateOf("+91 98765 43210") }
    var address by remember { mutableStateOf("Flat 402, Green Valley Apartments, Andheri West, Mumbai - 400053") }
    var model by remember { mutableStateOf("Califnco AquaShield Alkaline RO") }
    var selectedServiceType by remember { mutableStateOf(ServiceType.PERIODIC_MAINTENANCE) }
    var selectedDate by remember { mutableStateOf("26 Oct 2026") }
    var selectedSlot by remember { mutableStateOf("10:00 AM - 01:00 PM") }
    var notes by remember { mutableStateOf("") }
    var bookingSuccessMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .testTag("service_desk_screen")
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Doorstep Service & Maintenance",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Book certified Califnco technicians for filter changes, AMC, or repair.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Book New Service", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Service Tickets (${bookings.size})", fontSize = 13.sp, fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (selectedTab == 0) {
            // Service Booking Form
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    bookingSuccessMessage?.let { msg ->
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen)
                                Text(msg, color = Color(0xFF15803D), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }

                item {
                    Text("Select Service Required", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    ServiceType.values().forEach { st ->
                        val isSelected = selectedServiceType == st
                        Surface(
                            onClick = { selectedServiceType = st },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) OceanBlueLight else MaterialTheme.colorScheme.surface,
                            border = if (isSelected) ButtonDefaults.outlinedButtonBorder else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = when (st) {
                                        ServiceType.NEW_INSTALLATION -> Icons.Default.Handyman
                                        ServiceType.FILTER_REPLACEMENT -> Icons.Default.ChangeCircle
                                        ServiceType.PERIODIC_MAINTENANCE -> Icons.Default.CalendarMonth
                                        ServiceType.REPAIR_LEAK -> Icons.Default.Warning
                                        ServiceType.WATER_QUALITY_TEST -> Icons.Default.Science
                                    },
                                    contentDescription = null,
                                    tint = if (isSelected) OceanBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = st.title,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.weight(1f)
                                )
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedServiceType = st },
                                    colors = RadioButtonDefaults.colors(selectedColor = OceanBluePrimary)
                                )
                            }
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = model,
                        onValueChange = { model = it },
                        label = { Text("Purifier Brand & Model", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.WaterDrop, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Your Name", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            label = { Text("Contact Phone", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Visit Address", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Text("Preferred Date & Time Slot", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("10:00 AM - 01:00 PM", "02:00 PM - 05:00 PM", "05:00 PM - 08:00 PM").forEach { slot ->
                            val isSel = selectedSlot == slot
                            FilterChip(
                                selected = isSel,
                                onClick = { selectedSlot = slot },
                                label = { Text(slot.take(8), fontSize = 11.sp) }
                            )
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Issue Symptoms / Special Instructions (Optional)", fontSize = 12.sp) },
                        placeholder = { Text("e.g., TDS reading high, water taste bitter, minor pipe drip...", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = {
                            onBookService(name, phone, address, model, selectedServiceType, selectedDate, selectedSlot, notes)
                            bookingSuccessMessage = "Ticket Raised Successfully! Technician will arrive on $selectedDate ($selectedSlot)."
                            notes = ""
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OceanBluePrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("book_service_btn")
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Confirm Doorstep Booking", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        } else {
            // Service Tickets List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(bookings, key = { it.id }) { ticket ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = ticket.id,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = ticket.serviceType.title,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (ticket.status) {
                                        ServiceStatus.PENDING -> Color(0xFFFEF3C7)
                                        ServiceStatus.SCHEDULED -> Color(0xFFE0E7FF)
                                        ServiceStatus.TECHNICIAN_ASSIGNED -> Color(0xFFEDE9FE)
                                        ServiceStatus.COMPLETED -> Color(0xFFDCFCE7)
                                    }
                                ) {
                                    Text(
                                        text = ticket.status.label,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (ticket.status) {
                                            ServiceStatus.PENDING -> Color(0xFF92400E)
                                            ServiceStatus.SCHEDULED -> Color(0xFF3730A3)
                                            ServiceStatus.TECHNICIAN_ASSIGNED -> PurpleAccent
                                            ServiceStatus.COMPLETED -> Color(0xFF15803D)
                                        },
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            Text(
                                text = "Purifier: ${ticket.purifierModel}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Slot: ${ticket.preferredDate} (${ticket.preferredSlot})",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Address: ${ticket.address}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            ticket.assignedTechnician?.let { tech ->
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Default.Engineering, contentDescription = null, tint = OceanBluePrimary, modifier = Modifier.size(16.dp))
                                        Text(
                                            text = "Technician: $tech",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
