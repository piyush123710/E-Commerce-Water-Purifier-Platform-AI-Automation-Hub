package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.model.Product
import com.example.data.model.ProductReview
import com.example.ui.theme.*

/**
 * Reviews & Ratings section matching Screenshot 4:
 * - 4.8 Overall rating score with stars and total count
 * - 5-tier rating distribution bar breakdown (5★ to 1★)
 * - "Write a Review" button
 * - Customer review cards with verified badges and comments
 */
@Composable
fun ReviewsAndRatingsSection(
    product: Product,
    reviews: List<ProductReview>,
    onWriteReviewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("reviews_ratings_section")
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ratings & Customer Reviews",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Button(
                onClick = onWriteReviewClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = OceanBluePrimary
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("open_write_review_btn")
            ) {
                Icon(Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Write Review", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Ratings Breakdown Card (Matching Screenshot 4)
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Left: Score & Stars
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(100.dp)
                ) {
                    Text(
                        text = "%.1f".format(product.rating),
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < product.rating.toInt()) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = null,
                                tint = WarningAmber,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Text(
                        text = "${product.reviewCount} Reviews",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Divider
                Box(
                    modifier = Modifier
                        .height(90.dp)
                        .width(1.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                )

                // Right: 5-Star Distribution Bars
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    RatingDistributionBar(star = 5, percentage = 0.78f, label = "78%")
                    RatingDistributionBar(star = 4, percentage = 0.16f, label = "16%")
                    RatingDistributionBar(star = 3, percentage = 0.04f, label = "4%")
                    RatingDistributionBar(star = 2, percentage = 0.01f, label = "1%")
                    RatingDistributionBar(star = 1, percentage = 0.01f, label = "1%")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reviews List
        Text(
            text = "Verified Buyer Reviews (${reviews.size})",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        reviews.forEach { review ->
            CustomerReviewCard(review = review)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun RatingDistributionBar(
    star: Int,
    percentage: Float,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.width(32.dp)
        ) {
            Text(text = "$star", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            Icon(Icons.Filled.Star, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(10.dp))
        }

        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = if (star >= 4) SuccessGreen else if (star == 3) WarningAmber else ErrorRed,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )

        Text(
            text = label,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(26.dp)
        )
    }
}

@Composable
fun CustomerReviewCard(
    review: ProductReview,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: User avatar, Name, Verified Badge, Date
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
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(OceanBlueLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.authorName.take(1).uppercase(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = OceanBluePrimary
                        )
                    }

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = review.authorName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (review.isVerified) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFDCFCE7)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                                    ) {
                                        Icon(Icons.Default.Verified, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(10.dp))
                                        Text("Verified", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF15803D))
                                    }
                                }
                            }
                        }

                        Text(
                            text = review.dateStr,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Star Rating
                Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                    repeat(5) { i ->
                        Icon(
                            imageVector = if (i < review.rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = null,
                            tint = WarningAmber,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Review Comment
            Text(
                text = review.comment,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Helpful vote footer
            var helpfulCount by remember { mutableStateOf(review.helpfulCount) }
            var hasVoted by remember { mutableStateOf(false) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.clickable {
                    if (!hasVoted) {
                        helpfulCount += 1
                        hasVoted = true
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.ThumbUp,
                    contentDescription = "Helpful",
                    tint = if (hasVoted) OceanBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Helpful ($helpfulCount)",
                    fontSize = 11.sp,
                    color = if (hasVoted) OceanBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = if (hasVoted) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

/**
 * Write a Review Dialog (Matching Screenshot 4):
 * - Product Preview
 * - Interactive 1-5 Star Selector
 * - Photo upload placeholder toggle
 * - Multiline feedback text field
 * - Submit review button
 */
@Composable
fun WriteReviewDialog(
    product: Product,
    onSubmit: (rating: Int, comment: String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedRating by remember { mutableStateOf(5) }
    var reviewText by remember { mutableStateOf("") }
    var attachedPhotoCount by remember { mutableStateOf(1) } // Simulated sample attached photo

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(16.dp)
                .testTag("write_review_dialog")
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Write a Review",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Product mini card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "Verified Califnco Customer Purchase",
                            fontSize = 10.sp,
                            color = SuccessGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Star Rating Picker
                Text(
                    text = "Overall Rating",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    (1..5).forEach { star ->
                        IconButton(
                            onClick = { selectedRating = star },
                            modifier = Modifier.size(44.dp)
                        ) {
                            Icon(
                                imageVector = if (star <= selectedRating) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = "$star Stars",
                                tint = if (star <= selectedRating) WarningAmber else Color.LightGray,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                }

                Text(
                    text = when (selectedRating) {
                        5 -> "⭐⭐⭐⭐⭐ Outstanding Quality!"
                        4 -> "⭐⭐⭐⭐ Good & Satisfactory"
                        3 -> "⭐⭐⭐ Average Performance"
                        2 -> "⭐⭐ Needs Improvement"
                        else -> "⭐ Poor Experience"
                    },
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OceanBluePrimary,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Review Text Input
                OutlinedTextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    label = { Text("Share your experience (Water taste, TDS reduction, installation)", fontSize = 12.sp) },
                    placeholder = { Text("e.g. Water tastes sweet and crystal clear. TDS reduced from 800 to 55 PPM.", fontSize = 11.sp) },
                    minLines = 4,
                    maxLines = 6,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("review_comment_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Photos Attachment Pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, tint = OceanBluePrimary, modifier = Modifier.size(18.dp))
                        Text("Add Photos / TDS Meter Test", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    Switch(
                        checked = attachedPhotoCount > 0,
                        onCheckedChange = { attachedPhotoCount = if (it) 1 else 0 }
                    )
                }

                if (attachedPhotoCount > 0) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = OceanBlueLight,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "📸 1 Purifier Setup Photo Attached",
                            fontSize = 11.sp,
                            color = OceanBlueDark,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Submit Button
                Button(
                    onClick = {
                        val comment = if (reviewText.isBlank()) "Excellent purifier with delicious sweet water taste and quiet operation!" else reviewText
                        onSubmit(selectedRating, comment)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OceanBluePrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_review_btn")
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit Verified Review (+50 Coins)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
