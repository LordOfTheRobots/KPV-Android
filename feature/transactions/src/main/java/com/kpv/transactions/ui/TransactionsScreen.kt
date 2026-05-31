package com.kpv.transactions.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.domain.core.model.Transaction
import com.kpv.bankcardsmanagement.domain.core.model.TransactionType
import com.kpv.transactions.TransactionFilter
import com.kpv.transactions.TransactionsViewModel
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val today = remember { LocalDate.now() }
    val fmt = remember { NumberFormat.getCurrencyInstance(Locale("ru", "RU")) }

    val filteredTransactions = remember(state.transactions, state.filter) {
        when (state.filter) {
            TransactionFilter.INCOME -> state.transactions.filter { it.type == TransactionType.INCOME }
            TransactionFilter.EXPENSE -> state.transactions.filter { it.type == TransactionType.OUTCOME }
            else -> state.transactions
        }
    }

    val groupedTransactions = remember(filteredTransactions) {
        filteredTransactions
            .groupBy { tx -> tx.date.substring(0, minOf(tx.date.length, 10)) }
            .toSortedMap(compareByDescending { it })
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = StringsObject.transactionsTitle.asString(),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryGlassCard(
                label = StringsObject.monthlyIncome.asString(),
                value = state.monthlyIncome,
                icon = Icons.Default.ArrowUpward,
                gradientColors = listOf(Color(0xFF2E7D32), Color(0xFF4CAF50))
            )
            SummaryGlassCard(
                label = StringsObject.monthlyExpense.asString(),
                value = state.monthlyExpenses,
                icon = Icons.Default.ArrowDownward,
                gradientColors = listOf(Color(0xFFC62828), Color(0xFFE53935))
            )
        }

        Spacer(Modifier.height(16.dp))

        FilterRow(
            selectedFilter = state.filter,
            onFilterChange = viewModel::setFilter
        )

        Spacer(Modifier.height(16.dp))

        when {
            state.isLoading -> LoadingPlaceholder()
            state.error != null -> ErrorPlaceholder(message = state.error!!.asString(), onRetry = viewModel::loadTransactions)
            groupedTransactions.isEmpty() -> EmptyTransactionsPlaceholder()
            else -> TransactionsList(groupedTransactions, fmt, today)
        }
    }
}

@Composable
private fun SummaryGlassCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(100.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(brush = Brush.verticalGradient(colors = gradientColors), shape = RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = Color.White.copy(alpha = 0.25f), shape = RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.9f), maxLines = 1)
            }
            Text(
                text = value.ifBlank { "0,00 ₽" },
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold, fontSize = 20.sp),
                color = Color.White,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun FilterRow(
    selectedFilter: TransactionFilter,
    onFilterChange: (TransactionFilter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FilterChipGlass(
            selected = selectedFilter == TransactionFilter.ALL,
            onClick = { onFilterChange(TransactionFilter.ALL) },
            label = StringsObject.filterAll.asString()
        )
        FilterChipGlass(
            selected = selectedFilter == TransactionFilter.INCOME,
            onClick = { onFilterChange(TransactionFilter.INCOME) },
            label = StringsObject.filterIncome.asString(),
            accentColor = Color(0xFF4CAF50)
        )
        FilterChipGlass(
            selected = selectedFilter == TransactionFilter.EXPENSE,
            onClick = { onFilterChange(TransactionFilter.EXPENSE) },
            label = StringsObject.filterExpense.asString(),
            accentColor = Color(0xFFE53935)
        )
    }
}

@Composable
private fun FilterChipGlass(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    accentColor: Color = Color(0xFF1976D2)
) {
    val containerColor = if (selected) accentColor.copy(alpha = 0.25f) else Color.Transparent
    val borderColor = if (selected) accentColor.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.2f)
    val textColor = if (selected) Color.White else Color(0xFFB3E5FC)

    Surface(
        modifier = Modifier
            .fillMaxWidth(0.32f)
            .height(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .background(containerColor, shape = RoundedCornerShape(10.dp))
            .border(1.dp, borderColor, shape = RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        color = Color.Transparent
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = label, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium), color = textColor, maxLines = 1)
        }
    }
}

@Composable
private fun TransactionsList(
    grouped: Map<String, List<Transaction>>,
    fmt: NumberFormat,
    today: LocalDate,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        grouped.forEach { (date, txs) ->
            item(key = "header_$date") {
                DateSectionHeader(dateStr = date, today = today)
            }
            items(txs, key = { it.id }) { tx ->
                GlassTransactionItem(tx = tx, fmt = fmt)
            }
        }
    }
}

@Composable
private fun DateSectionHeader(dateStr: String, today: LocalDate) {
    val displayDate = remember(dateStr, today) {
        runCatching {
            val date = LocalDate.parse(dateStr)
            date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("ru")))
        }.getOrDefault(dateStr)
    }

    Text(
        text = displayDate,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = Color(0xFFB3E5FC),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun GlassTransactionItem(tx: Transaction, fmt: NumberFormat) {
    val (icon, amountColor, amountPrefix) = when (tx.type) {
        TransactionType.INCOME -> Triple(Icons.Default.ArrowUpward, Color(0xFF4CAF50), "+")
        TransactionType.OUTCOME -> Triple(Icons.Default.ArrowDownward, Color(0xFFE53935), "−")
        else -> Triple(Icons.Default.ArrowDownward, Color.Gray, "")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(amountColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = amountColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Text(
                        text = tx.description,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = Color.White,
                        maxLines = 1
                    )
                    Text(
                        text = extractTime(tx.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFB3E5FC).copy(alpha = 0.7f)
                    )
                }
            }

            Text(
                text = "$amountPrefix ${fmt.format(tx.amount)}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = amountColor,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
private fun LoadingPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize().padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
    }
}

@Composable
private fun ErrorPlaceholder(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("⚠️", fontSize = 48.sp)
            Spacer(Modifier.height(12.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFFFEB3B),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF0D47A1)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(StringsObject.retry.asString())
            }
        }
    }
}

@Composable
private fun EmptyTransactionsPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize().padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("📋", fontSize = 64.sp)
            Spacer(Modifier.height(16.dp))
            Text(
                text = StringsObject.noTransactions.asString(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = StringsObject.noTransactionsHint.asString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFB3E5FC),
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun extractTime(dateTime: String): String {
    return if (dateTime.length >= 16) {
        dateTime.substring(11, 16)
    } else {
        dateTime
    }
}