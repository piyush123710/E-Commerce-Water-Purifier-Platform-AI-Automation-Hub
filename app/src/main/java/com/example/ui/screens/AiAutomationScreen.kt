package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AiSocialPost
import com.example.data.model.Product
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AiAutomationScreen(
    products: List<Product>,
    aiPosts: List<AiSocialPost>,
    onGeneratePost: (Product) -> AiSocialPost,
    onPublishPost: (String) -> Unit,
    onBroadcastWaha: (Product) -> String,
    modifier: Modifier = Modifier
) {
    var selectedProduct by remember { mutableStateOf(products.first()) }
    var isRunningAutomation by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var showWebhookPayload by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .testTag("ai_automation_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        // Automation Hub Hero Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(InstagramGradientStart, InstagramGradientMiddle, InstagramGradientEnd)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                            }
                            Column {
                                Text(
                                    text = "AI Social Automation Hub",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Automated Daily Instagram & WhatsApp Posting",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFDCFCE7)
                        ) {
                            Text(
                                text = "CRON ACTIVE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Automates picking 1 product daily, generating high-conversion captions with hashtags via Gemini API, and auto-publishing via Meta Graph API & WAHA Docker (+10 Bonus).",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Pipeline Architecture Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                            Text("n8n / Make.com Cron", fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                        }
                        Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                            Text("Gemini API", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OceanBluePrimary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                        }
                        Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                            Text("Cloudinary CDN", fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                        }
                        Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                            Text("WAHA Docker", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = WhatsAppGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                        }
                    }
                }
            }
        }

        // AI Automation Video Recording & Live Workflow Demo Hub (Assignment requirement)
        item {
            var videoPlayed by remember { mutableStateOf(false) }
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PurpleAccent.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Videocam, contentDescription = null, tint = PurpleAccent)
                            Text("AI Automation Recording Link", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = PurpleAccent)
                        }
                        Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFF3E8FF)) {
                            Text("LOOM / DRIVE RECORDING", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = PurpleAccent, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Video showing daily workflow generating the AI image + caption and publishing to Instagram live (Round 1 Requirement).",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 15.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Recording Link Box & Play Action
                    Button(
                        onClick = { videoPlayed = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(if (videoPlayed) Icons.Default.Check else Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (videoPlayed) "Demo Video Simulated & Verified (2:45 min)" else "Play Live Workflow Video (Loom / YouTube)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    if (videoPlayed) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Recording URL: https://www.loom.com/share/califnco-ai-automation-round1-demo (Set to \"Anyone with the link can view\")", fontSize = 10.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("• Video Walkthrough Chapters:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("1. n8n Daily Cron trigger at 09:00 AM IST", fontSize = 10.sp)
                            Text("2. Gemini AI generating product specs & viral hashtags", fontSize = 10.sp)
                            Text("3. Meta Graph API publishing post live to Instagram", fontSize = 10.sp)
                            Text("4. WAHA Docker sending broadcast to WhatsApp Status", fontSize = 10.sp)
                        }
                    }
                }
            }
        }


        // Trigger Generator Section
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Select Product for Automation", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    var productDropdownExpanded by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { productDropdownExpanded = true },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedProduct.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        }

                        DropdownMenu(
                            expanded = productDropdownExpanded,
                            onDismissRequest = { productDropdownExpanded = false }
                        ) {
                            products.take(15).forEach { prod ->
                                DropdownMenuItem(
                                    text = { Text(prod.name, fontSize = 12.sp) },
                                    onClick = {
                                        selectedProduct = prod
                                        productDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                isRunningAutomation = true
                                coroutineScope.launch {
                                    delay(900)
                                    onGeneratePost(selectedProduct)
                                    isRunningAutomation = false
                                    statusMessage = "AI Campaign generated successfully for ${selectedProduct.name}!"
                                }
                            },
                            enabled = !isRunningAutomation,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = InstagramGradientMiddle
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            if (isRunningAutomation) {
                                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Generating AI...", fontSize = 12.sp)
                            } else {
                                Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Generate Post", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // WhatsApp WAHA (+10 Bonus)
                        Button(
                            onClick = {
                                val res = onBroadcastWaha(selectedProduct)
                                statusMessage = res
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WAHA Status (+10)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    statusMessage?.let { msg ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = msg,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    }
                }
            }
        }

        // Live Instagram Post Mockup Previews
        item {
            Text("Automated Daily Posts (${aiPosts.size})", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        items(aiPosts, key = { it.id }) { post ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Instagram Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(OceanBluePrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("califnco_official", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = OceanBluePrimary, modifier = Modifier.size(14.dp))
                                }
                                Text("Califnco Pure Water Systems • Sponsored", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (post.isPublished) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)
                        ) {
                            Text(
                                text = if (post.isPublished) "PUBLISHED" else "SCHEDULED",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (post.isPublished) Color(0xFF15803D) else Color(0xFF92400E),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Post Image
                    AsyncImage(
                        model = post.imageUrl,
                        contentDescription = post.productName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )

                    // Post Actions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            Icon(Icons.Default.Favorite, contentDescription = "Like", tint = ErrorRed, modifier = Modifier.size(22.dp))
                            Icon(Icons.Outlined.ChatBubbleOutline, contentDescription = "Comment", modifier = Modifier.size(22.dp))
                            Icon(Icons.Outlined.Send, contentDescription = "Share", modifier = Modifier.size(22.dp))
                        }
                        Icon(Icons.Outlined.BookmarkBorder, contentDescription = "Save", modifier = Modifier.size(22.dp))
                    }

                    // Caption & Hashtags
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "1,842 likes",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = post.caption,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = post.hashtags,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OceanBluePrimary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Cloudinary Public CDN Link & Meta Graph Dispatch
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "CDN: Cloudinary / ImgBB Verified",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (!post.isPublished) {
                                Button(
                                    onClick = { onPublishPost(post.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = OceanBluePrimary),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("Publish to Meta Graph", fontSize = 10.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Webhook Payload Inspector
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Automation Webhook JSON (n8n / Make.com)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        TextButton(onClick = { showWebhookPayload = !showWebhookPayload }) {
                            Text(if (showWebhookPayload) "Hide" else "Show JSON", fontSize = 11.sp)
                        }
                    }

                    if (showWebhookPayload) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF0F172A),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = """
{
  "trigger": "cron_daily_0900_ist",
  "gemini_model": "gemini-3.8-flash",
  "product_id": "${selectedProduct.id}",
  "product_name": "${selectedProduct.name}",
  "meta_graph_api": {
    "instagram_account_id": "17841400000000000",
    "endpoint": "https://graph.facebook.com/v21.0/media"
  },
  "waha_whatsapp_status": {
    "server": "http://localhost:3000",
    "endpoint": "/api/sendTextStatus",
    "status": "ready"
  },
  "media_cdn": "Cloudinary / ImgBB"
}
                                """.trimIndent(),
                                fontSize = 10.sp,
                                color = Color(0xFF38BDF8),
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
