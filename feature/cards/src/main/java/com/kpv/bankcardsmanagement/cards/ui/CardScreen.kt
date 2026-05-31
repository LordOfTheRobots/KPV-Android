package com.kpv.bankcardsmanagement.cards.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.domain.core.model.Card
import com.kpv.bankcardsmanagement.feature.cards.viewmodel.CardsViewModel

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign

@Composable
fun CardsScreen(viewModel: CardsViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        TotalBalanceCard(balance = state.totalBalance)

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = StringsObject.myCards.asString(),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
                    .clickable { viewModel.toggleAddDialog(true) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = StringsObject.addCard.asString(),
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        when {
            state.isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }

            state.error != null -> GlassErrorCard(message = state.error!!.asString())

            state.selectedCard != null -> CardDetailsScreen(
                card = state.selectedCard!!,
                transactions = state.transactions,
                weeklySpending = state.weeklySpending,
                isLoading = state.isActionLoading,
                onClose = { viewModel.closeCardDetails() },
                onBlock = { viewModel.blockCard(state.selectedCard!!.cardId) }
            )

            state.cards.isEmpty() -> EmptyCardsPlaceholder()

            else -> LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(state.cards, key = { it.cardId }) { card ->
                    BankCardItem(
                        card = card,
                        onClick = { viewModel.openCardDetails(card.cardId) }
                    )
                }
            }
        }
    }

    if (state.showAddDialog) {
        AddCardDialog(viewModel, onDismiss = { viewModel.toggleAddDialog(false) })
    }
}

@Composable
fun TotalBalanceCard(balance: Double) {
    val displayBalance = "${balance} ₽"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF0A2A5E), Color(0xFF0D47A1), Color(0xFF1565C0))
                )
            )
            .border(1.dp, Color.White.copy(0.2f), RoundedCornerShape(24.dp))
            .padding(24.dp)
    ) {
        Column {
            Text(StringsObject.totalBalance.asString(), style = MaterialTheme.typography.bodyMedium, color = Color(0xFFB3E5FC))
            Spacer(Modifier.height(8.dp))
            Text(
                text = displayBalance,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold, letterSpacing = (-1).sp),
                color = Color.White
            )
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row {
                    Box(modifier = Modifier.size(28.dp).background(Color(0xFFEB001B).copy(0.8f), shape = CircleShape))
                    Box(modifier = Modifier.size(28.dp).offset(x = (-10).dp).background(Color(0xFFF79E1B).copy(0.8f), shape = CircleShape))
                }
                Text(StringsObject.cardPremium.asString(), color = Color(0xFFB3E5FC), style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 2.sp))
            }
        }
    }
}

@Composable
private fun BankCardItem(card: Card, onClick: () -> Unit) {
    val gradientColors = getCardGradient(card.condition.status.color)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusBadge(
                    label = card.condition.status.label,
                    colorHex = card.condition.status.color
                )
                Text(
                    text = StringsObject.cardBankName.asString(),
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp
                    )
                )
            }

            Text(
                text = card.cardMask,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = StringsObject.cardValidThru.asString(),
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = card.expireDate,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = StringsObject.cardBalance.asString(),
                        color = Color.White.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = card.balance.toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(label: String, colorHex: String) {
    val statusColor = when (colorHex) {
        "green" -> Color(0xFF4CAF50)
        "red" -> Color(0xFFE53935)
        "yellow" -> Color(0xFFFFC107)
        "gray" -> Color(0xFF9E9E9E)
        else -> Color.Gray
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = statusColor.copy(alpha = 0.25f),
        modifier = Modifier.border(
            width = 1.dp,
            color = statusColor.copy(alpha = 0.5f),
            shape = RoundedCornerShape(8.dp)
        )
    ) {
        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            ),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

private fun getCardGradient(statusColor: String): List<Color> = when (statusColor) {
    "green" -> listOf(
        Color(0xFF0A2A5E),
        Color(0xFF1565C0),
        Color(0xFF1976D2)
    )
    "red" -> listOf(
        Color(0xFF4A0E0E),
        Color(0xFFB71C1C),
        Color(0xFFE53935)
    )
    "yellow" -> listOf(
        Color(0xFF3E2A00),
        Color(0xFFE65100),
        Color(0xFFFF9800)
    )
    "gray" -> listOf(
        Color(0xFF263238),
        Color(0xFF455A64),
        Color(0xFF607D8B)
    )
    else -> listOf(
        Color(0xFF0D47A1),
        Color(0xFF1565C0),
        Color(0xFF1976D2)
    )
}

@Composable
private fun EmptyCardsPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "💳",
                fontSize = 64.sp
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = StringsObject.noCards.asString(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = StringsObject.addCardHint.asString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFB3E5FC)
            )
        }
    }
}

@Composable
private fun GlassErrorCard(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF000000).copy(alpha = 0.3f))
            .border(
                width = 1.dp,
                color = Color(0xFFFFEB3B).copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = message,
            color = Color(0xFFFFEB3B),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )
    }
}