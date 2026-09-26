package com.example.ui.components

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneCallback
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiOrderChatMessage
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangeAccent
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateLight
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * Customer Orders & AI Assistant Modal Sheet
 * Features:
 * 1. AI Voice Call Verification with automated retry loop & Press 1 (Confirm) / Press 5 (Cancel) triggers
 * 2. Smart Data Routing to Seller & Master Admin (MD Meraj Ansari)
 * 3. Secure Delivery OTP Handover Verification
 * 4. AI Order Chat Assistant with instant TextToSpeech Speaker Voice-Out
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerOrderAiAssistantSheet(
    orders: List<Order>,
    aiChatMessages: List<AiOrderChatMessage>,
    onDismiss: () -> Unit,
    onSendMessage: (orderId: String, text: String) -> Unit,
    onTriggerVoiceCall: (orderId: String, key: Int) -> Boolean,
    onVerifyDeliveryOtp: (orderId: String, otp: String) -> Boolean,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // TextToSpeech Engine
    var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }
    var isSpeaking by remember { mutableStateOf(false) }
    var currentlySpeakingText by remember { mutableStateOf<String?>(null) }

    DisposableEffect(context) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Initialize default locale
            }
        }
        tts.language = Locale.US
        ttsEngine = tts

        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun speakText(text: String) {
        ttsEngine?.let { engine ->
            if (isSpeaking && currentlySpeakingText == text) {
                engine.stop()
                isSpeaking = false
                currentlySpeakingText = null
            } else {
                engine.stop()
                engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ZenoMartTts_${System.currentTimeMillis()}")
                isSpeaking = true
                currentlySpeakingText = text
            }
        }
    }

    var selectedOrderId by remember(orders) {
        mutableStateOf(orders.firstOrNull()?.id ?: "")
    }

    val currentOrder = orders.find { it.id == selectedOrderId } ?: orders.firstOrNull()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = NavyDark,
        contentColor = Color.White,
        modifier = modifier
            .fillMaxHeight(0.94f)
            .testTag("customer_orders_ai_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(OrangeAccent.copy(alpha = 0.2f))
                            .border(1.dp, OrangeAccent, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Assistant,
                            contentDescription = "AI Assistant",
                            tint = OrangeAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Order Tracking & AI Assistant",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "AI Voice Verification • Speaker Voice-out • OTP Handover",
                            color = Color(0xFF94A3B8),
                            fontSize = 10.sp
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }

            if (orders.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No Orders Placed Yet",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Place an order from the cart to track automated AI voice calls and chat with the assistant.",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }
                }
            } else {
                // Horizontal Order Selector Chips
                if (orders.size > 1) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(orders) { order ->
                            val isSelected = order.id == currentOrder?.id
                            Surface(
                                color = if (isSelected) OrangeAccent else NavyPrimary,
                                shape = RoundedCornerShape(10.dp),
                                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                                modifier = Modifier.clickable { selectedOrderId = order.id }
                            ) {
                                Text(
                                    text = "#${order.id}",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                currentOrder?.let { order ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Card 1: Order Details & Status Badge
                        item {
                            Surface(
                                color = NavyPrimary,
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
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
                                                text = "ORDER #${order.id}",
                                                color = GoldAccent,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                            Text(
                                                text = order.date,
                                                color = Color(0xFF94A3B8),
                                                fontSize = 10.sp
                                            )
                                        }

                                        Surface(
                                            color = when (order.status) {
                                                OrderStatus.DELIVERED -> Color(0xFF10B981).copy(alpha = 0.2f)
                                                OrderStatus.CANCELLED -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                                else -> OrangeAccent.copy(alpha = 0.2f)
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            border = androidx.compose.foundation.BorderStroke(
                                                1.dp,
                                                when (order.status) {
                                                    OrderStatus.DELIVERED -> Color(0xFF10B981)
                                                    OrderStatus.CANCELLED -> Color(0xFFEF4444)
                                                    else -> OrangeAccent
                                                }
                                            )
                                        ) {
                                            Text(
                                                text = order.status.name.replace("_", " "),
                                                color = when (order.status) {
                                                    OrderStatus.DELIVERED -> Color(0xFF34D399)
                                                    OrderStatus.CANCELLED -> Color(0xFFF87171)
                                                    else -> OrangeAccent
                                                },
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${order.items.size} item(s) • Total Paid: $${String.format("%.2f", order.totalAmount)}",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Delivery to: ${order.customerName}, ${order.deliveryAddress}",
                                        color = Color(0xFFCBD5E1),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }

                        // Card 2: Automated AI Voice Call Verification System
                        item {
                            Surface(
                                color = NavyPrimary,
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3B82F6).copy(alpha = 0.6f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.PhoneCallback,
                                                contentDescription = null,
                                                tint = Color(0xFF60A5FA),
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "AI Voice Call Verification",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }

                                        Surface(
                                            color = if (order.callVerificationStatus.contains("Confirmed", ignoreCase = true)) {
                                                Color(0xFF10B981).copy(alpha = 0.2f)
                                            } else {
                                                Color(0xFFF59E0B).copy(alpha = 0.2f)
                                            },
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = order.callVerificationStatus,
                                                color = if (order.callVerificationStatus.contains("Confirmed", ignoreCase = true)) {
                                                    Color(0xFF34D399)
                                                } else {
                                                    Color(0xFFFBBF24)
                                                },
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Automated voice call dispatched to customer's phone (${order.customerPhone}). Press 1 to confirm, Press 5 to cancel. If unconfirmed, system auto-retries every 15 minutes.",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 10.sp,
                                        lineHeight = 14.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Interactive call trigger buttons
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Button(
                                            onClick = { onTriggerVoiceCall(order.id, 1) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF10B981),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(34.dp)
                                                .testTag("voice_call_confirm_key1")
                                        ) {
                                            Text("📞 Press 1 (Confirm)", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = { onTriggerVoiceCall(order.id, 5) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFFEF4444),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(34.dp)
                                                .testTag("voice_call_cancel_key5")
                                        ) {
                                            Text("❌ Press 5 (Cancel)", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Button(
                                            onClick = { onTriggerVoiceCall(order.id, 0) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF334155),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(34.dp)
                                                .testTag("voice_call_retry_15m")
                                        ) {
                                            Text("⏳ 15m Retry", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }

                        // Card 3: Smart Data Routing upon Confirmation
                        item {
                            Surface(
                                color = NavyPrimary,
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "📡 Smart Data Routing (Upon Confirmation)",
                                        color = GoldAccent,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Seller Route
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Icon(
                                            Icons.Default.Storefront,
                                            contentDescription = null,
                                            tint = Color(0xFF34D399),
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column {
                                            Text("To Registered Seller:", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            Text(order.sellerDispatchedData, color = Color(0xFF94A3B8), fontSize = 9.sp)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Master Admin / Delivery Route
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Icon(
                                            Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = OrangeAccent,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Column {
                                            Text("To Master Admin (MD Meraj Ansari) & Delivery:", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            Text(order.adminDispatchedData, color = Color(0xFF94A3B8), fontSize = 9.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Card 4: Secure Delivery OTP Verification
                        item {
                            Surface(
                                color = NavyPrimary,
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.Security,
                                                contentDescription = null,
                                                tint = GoldAccent,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Secure Delivery Handover OTP",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }

                                        Surface(
                                            color = if (order.otpVerifiedAtDelivery) Color(0xFF10B981).copy(alpha = 0.2f) else OrangeAccent.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = if (order.otpVerifiedAtDelivery) "VERIFIED ✅" else "PENDING HANDOVER",
                                                color = if (order.otpVerifiedAtDelivery) Color(0xFF34D399) else OrangeAccent,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Black,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // OTP Display Box
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(NavyDark)
                                            .border(1.dp, GoldAccent.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                            .padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("Your 4-Digit Delivery OTP", color = Color(0xFF94A3B8), fontSize = 10.sp)
                                            Text(
                                                text = order.deliveryOtp,
                                                color = GoldAccent,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Black,
                                                letterSpacing = 4.sp
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text("Handover Rule", color = Color(0xFF94A3B8), fontSize = 9.sp)
                                            Text("Give to agent at doorstep", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    // Handover simulation for delivery agent / owner
                                    if (!order.otpVerifiedAtDelivery && order.status != OrderStatus.CANCELLED) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        var testOtpInput by remember { mutableStateOf("") }
                                        var otpError by remember { mutableStateOf<String?>(null) }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            OutlinedTextField(
                                                value = testOtpInput,
                                                onValueChange = {
                                                    testOtpInput = it.take(4)
                                                    otpError = null
                                                },
                                                placeholder = { Text("Agent enter OTP", fontSize = 10.sp, color = Color(0xFF94A3B8)) },
                                                singleLine = true,
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(48.dp)
                                                    .testTag("delivery_otp_agent_input"),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedTextColor = Color.White,
                                                    unfocusedTextColor = Color.White,
                                                    focusedBorderColor = GoldAccent,
                                                    unfocusedBorderColor = SlateBorder
                                                )
                                            )

                                            Button(
                                                onClick = {
                                                    val success = onVerifyDeliveryOtp(order.id, testOtpInput)
                                                    if (!success) {
                                                        otpError = "Invalid OTP. Expected ${order.deliveryOtp}"
                                                    } else {
                                                        testOtpInput = ""
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = Color(0xFF10B981),
                                                    contentColor = Color.White
                                                ),
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier
                                                    .height(48.dp)
                                                    .testTag("verify_delivery_otp_button")
                                            ) {
                                                Text("Confirm Handover", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }

                                        otpError?.let { err ->
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(err, color = Color(0xFFEF4444), fontSize = 10.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Card 5: AI Order Chat Assistant with Instant Speaker Voice-Out
                        item {
                            Surface(
                                color = NavyPrimary,
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, OrangeAccent.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.Assistant,
                                                contentDescription = null,
                                                tint = OrangeAccent,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "AI Order Chat Assistant",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }

                                        Surface(
                                            color = OrangeAccent.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = "🔊 Voice-out Ready",
                                                color = OrangeAccent,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Ask questions in real-time. Tap the speaker icon on any message to hear the AI speak the answer aloud in audio!",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 10.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Quick prompt chips
                                    val quickQuestions = listOf(
                                        "Where is my order?",
                                        "When will it arrive?",
                                        "What is my Delivery OTP?",
                                        "Explain voice verification"
                                    )

                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        items(quickQuestions) { question ->
                                            Surface(
                                                color = NavyDark,
                                                shape = RoundedCornerShape(12.dp),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                                                modifier = Modifier.clickable {
                                                    onSendMessage(order.id, question)
                                                }
                                            ) {
                                                Text(
                                                    text = question,
                                                    color = Color(0xFFE2E8F0),
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Chat Messages Box
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(NavyDark)
                                            .border(1.dp, SlateBorder, RoundedCornerShape(10.dp))
                                            .padding(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        aiChatMessages.forEach { msg ->
                                            val isAi = !msg.isFromCustomer
                                            val speakingThis = isSpeaking && currentlySpeakingText == msg.text

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = if (isAi) Arrangement.Start else Arrangement.End
                                            ) {
                                                Surface(
                                                    color = if (isAi) NavyPrimary else Color(0xFF1E3A8A),
                                                    shape = RoundedCornerShape(10.dp),
                                                    border = if (speakingThis) androidx.compose.foundation.BorderStroke(1.5.dp, GoldAccent) else null,
                                                    modifier = Modifier.widthIn(max = 280.dp)
                                                ) {
                                                    Column(modifier = Modifier.padding(8.dp)) {
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.SpaceBetween,
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Text(
                                                                text = if (isAi) "🤖 AI Order Assistant" else "👤 You",
                                                                color = if (isAi) GoldAccent else Color(0xFF93C5FD),
                                                                fontSize = 9.sp,
                                                                fontWeight = FontWeight.Black
                                                            )

                                                            if (isAi) {
                                                                // Speaker Audio Voice-out Button
                                                                IconButton(
                                                                    onClick = { speakText(msg.text) },
                                                                    modifier = Modifier
                                                                        .size(24.dp)
                                                                        .testTag("speaker_voice_out_button")
                                                                ) {
                                                                    Icon(
                                                                        imageVector = if (speakingThis) Icons.Default.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                                                                        contentDescription = "Read Aloud",
                                                                        tint = if (speakingThis) Color(0xFFEF4444) else GoldAccent,
                                                                        modifier = Modifier.size(14.dp)
                                                                    )
                                                                }
                                                            }
                                                        }

                                                        Spacer(modifier = Modifier.height(2.dp))
                                                        Text(
                                                            text = msg.text,
                                                            color = Color.White,
                                                            fontSize = 11.sp,
                                                            lineHeight = 15.sp
                                                        )

                                                        if (speakingThis) {
                                                            Spacer(modifier = Modifier.height(4.dp))
                                                            Text(
                                                                text = "🔊 Speaking aloud now...",
                                                                color = GoldAccent,
                                                                fontSize = 8.sp,
                                                                fontWeight = FontWeight.Bold
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Chat Input Box
                                    var customInput by remember { mutableStateOf("") }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = customInput,
                                            onValueChange = { customInput = it },
                                            placeholder = { Text("Ask where is my order...", fontSize = 11.sp, color = Color(0xFF94A3B8)) },
                                            singleLine = true,
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(46.dp)
                                                .testTag("ai_chat_input"),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White,
                                                focusedBorderColor = OrangeAccent,
                                                unfocusedBorderColor = SlateBorder
                                            )
                                        )

                                        IconButton(
                                            onClick = {
                                                if (customInput.isNotBlank()) {
                                                    onSendMessage(order.id, customInput.trim())
                                                    customInput = ""
                                                }
                                            },
                                            modifier = Modifier
                                                .size(46.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(OrangeAccent)
                                                .testTag("ai_chat_send_button")
                                        ) {
                                            Icon(
                                                Icons.AutoMirrored.Filled.Send,
                                                contentDescription = "Send",
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Bottom Spacer
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}
