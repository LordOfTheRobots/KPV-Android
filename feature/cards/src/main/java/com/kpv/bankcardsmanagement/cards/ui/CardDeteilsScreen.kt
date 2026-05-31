package com.kpv.bankcardsmanagement.cards.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.domain.core.model.Card
import com.kpv.bankcardsmanagement.domain.core.model.DailySpending
import com.kpv.bankcardsmanagement.domain.core.model.Transaction
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.kpv.bankcardsmanagement.domain.core.model.TransactionType

@Composable
fun CardDetailsScreen(
    card: Card,
    transactions: List<Transaction>,
    weeklySpending: List<DailySpending>,
    isLoading: Boolean,
    onClose: () -> Unit,
    onBlock: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.ArrowBack, contentDescription = StringsObject.close.asString(), tint = Color.White)
                }
                Text(
                    text = StringsObject.details.asString(),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
        }

        item { CardInfoGlass(card) }

        item { BlockButton(onBlock, isLoading) }

        item { SectionTitle(StringsObject.weeklySpending.asString()) }
        item { SpendingChartStyled(weeklySpending) }

        item { SectionTitle(StringsObject.transactionsLabel.asString()) }
        if (transactions.isEmpty()) {
            item { EmptyPlaceholder() }
        } else {
            items(transactions, key = { it.id }) { tx -> TransactionItem(tx) }
        }
    }
}

@Composable
private fun CardInfoGlass(card: Card) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                )
            )
            .border(1.dp, Color.White.copy(0.2f), RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = card.cardMask,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge.copy(letterSpacing = 2.sp, fontWeight = FontWeight.Medium)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Действует до: ${card.expireDate}",
                color = Color(0xFFB3E5FC),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = StringsObject.cardBalance.asString(),
                color = Color(0xFFB3E5FC),
                style = MaterialTheme.typography.labelSmall
            )
            Text(
                text = formatBalance(card.balance),
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(Modifier.height(12.dp))
            StatusBadge(label = card.condition.status.label, colorHex = card.condition.status.color)
            card.condition.comment?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = Color(0xFFE3F2FD).copy(0.8f), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
@Composable
private fun BlockButton(onBlock: () -> Unit, isLoading: Boolean) {
    Button(
        onClick = onBlock,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE53935).copy(0.12f),
            contentColor = Color(0xFFE53935),
            disabledContainerColor = Color.Gray.copy(0.1f),
            disabledContentColor = Color.Gray.copy(0.5f)
        ),
        border = BorderStroke(1.dp, Color(0xFFE53935).copy(0.3f)),
        shape = RoundedCornerShape(14.dp)
    ) {
        if (isLoading) CircularProgressIndicator(Modifier.size(20.dp), color = Color(0xFFE53935), strokeWidth = 2.dp)
        else Text(StringsObject.blockCard.asString(), fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun SpendingChartStyled(spending: List<DailySpending>) {
    if (spending.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().height(140.dp), contentAlignment = Alignment.Center) {
            Text(StringsObject.noTransactions.asString(), color = Color(0xFFB3E5FC))
        }
        return
    }

    val maxVal = spending.maxOfOrNull { it.amount } ?: 0.0
    val formatter = DateTimeFormatter.ofPattern("dd.MM", Locale("ru"))

    Row(
        modifier = Modifier.fillMaxWidth().height(140.dp).padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        spending.forEach { day ->
            val heightRatio = if (maxVal > 0) (day.amount / maxVal).toFloat() else 0f
            val barHeight = (90.dp * heightRatio).coerceAtLeast(6.dp)

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(barHeight)
                        .background(Color(0xFF42A5F5).copy(0.85f), shape = RoundedCornerShape(4.dp))
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = if (day.date.length >= 5) day.date.substring(0, 5) else day.date,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFB3E5FC),
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun TransactionItem(tx: Transaction) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(0.08f))
            .border(1.dp, Color.White.copy(0.15f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(tx.description, color = Color.White, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                Text(tx.date, color = Color(0xFFB3E5FC).copy(0.8f), style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = formatBalance(tx.amount),
                color = if (tx.type == TransactionType.INCOME) Color(0xFF4CAF50) else Color(0xFFE53935),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
}

@Composable
private fun EmptyPlaceholder() {
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
        Text(StringsObject.noTransactions.asString(), color = Color(0xFFB3E5FC), style = MaterialTheme.typography.bodyMedium)
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
        color = statusColor.copy(0.2f),
        modifier = Modifier.border(1.dp, statusColor.copy(0.4f), RoundedCornerShape(8.dp))
    ) {
        Text(label, color = Color.White, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
    }
}

private fun formatBalance(value: Double): String {
    val fmt = NumberFormat.getCurrencyInstance(Locale("ru", "RU"))
    fmt.minimumFractionDigits = 2
    return fmt.format(value)
}