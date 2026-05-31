package com.kpv.transfer.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.transfer.TransfersViewModel
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransfersScreen(viewModel: TransfersViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val fmt = remember { NumberFormat.getCurrencyInstance(Locale("ru", "RU")) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = StringsObject.transferTitle.asString(),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = state.dropdownExpanded,
            onExpandedChange = { if (!state.isCardsLoading && !state.isLoading) viewModel.toggleDropdown(it) }
        ) {
            OutlinedTextField(
                value = state.selectedCard?.let { "**** ${it.cardMask}" } ?: "",
                onValueChange = {},
                readOnly = true,
                enabled = !state.isLoading,
                label = { Text(StringsObject.selectSourceCard.asString(), color = Color(0xFFB3E5FC)) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.dropdownExpanded)
                },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                colors = glassTextFieldColors()
            )
            ExposedDropdownMenu(
                expanded = state.dropdownExpanded,
                onDismissRequest = { viewModel.toggleDropdown(false) },
                containerColor = Color(0xFF0D47A1).copy(alpha = 0.95f)
            ) {
                if (state.availableCards.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text(StringsObject.noActiveCards.asString(), color = Color.White) },
                        onClick = {}
                    )
                } else {
                    state.availableCards.forEach { card ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(card.cardMask, color = Color.White, fontWeight = FontWeight.Medium)
                                    Text(
                                        text = "${StringsObject.cardBalanceFmt.asString()} ${fmt.format(card.balance)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFB3E5FC)
                                    )
                                }
                            },
                            onClick = { viewModel.selectCard(card) }
                        )
                    }
                }
            }
        }

        OutlinedTextField(
            value = state.toCardNumber,
            onValueChange = { viewModel.updateToCard(it) },
            label = { Text(StringsObject.toCardLabel.asString(), color = Color(0xFFB3E5FC)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !state.isLoading,
            colors = glassTextFieldColors()
        )

        OutlinedTextField(
            value = state.amount,
            onValueChange = { viewModel.updateAmount(it.filter { c -> c.isDigit() || c == '.' || c == ',' }) },
            label = { Text(StringsObject.amountLabel.asString(), color = Color(0xFFB3E5FC)) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !state.isLoading,
            colors = glassTextFieldColors()
        )

        OutlinedTextField(
            value = state.description,
            onValueChange = { viewModel.updateDescription(it) },
            label = { Text(StringsObject.descriptionLabel.asString(), color = Color(0xFFB3E5FC)) },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            enabled = !state.isLoading,
            colors = glassTextFieldColors()
        )

        state.error?.let { err ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF000000).copy(alpha = 0.3f))
                    .border(1.dp, Color(0xFFFFEB3B).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = err.asString(),
                    color = Color(0xFFFFEB3B),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
            }
        }

        if (state.success) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF4CAF50).copy(alpha = 0.2f))
                    .border(1.dp, Color(0xFF4CAF50).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = StringsObject.transferSuccess.asString(),
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                    TextButton(onClick = { viewModel.consumeResult() }) {
                        Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Button(
            onClick = { viewModel.sendTransfer() },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            enabled = !state.isLoading &&
                    state.selectedCard != null &&
                    state.toCardNumber.isNotBlank() &&
                    state.amount.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF0D47A1),
                disabledContainerColor = Color.White.copy(alpha = 0.3f),
                disabledContentColor = Color(0xFF0D47A1).copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(14.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp, color = Color(0xFF0D47A1))
            } else {
                Text(StringsObject.sendButton.asString(), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun glassTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White.copy(alpha = 0.12f),
    unfocusedContainerColor = Color.White.copy(alpha = 0.08f),
    disabledContainerColor = Color.White.copy(alpha = 0.05f),
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    disabledTextColor = Color.White.copy(alpha = 0.4f),
    focusedBorderColor = Color.White.copy(alpha = 0.5f),
    unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
    disabledBorderColor = Color.White.copy(alpha = 0.1f),
    cursorColor = Color.White,
    focusedLabelColor = Color(0xFFB3E5FC),
    unfocusedLabelColor = Color(0xFFB3E5FC).copy(alpha = 0.7f),
    disabledLabelColor = Color.White.copy(alpha = 0.3f)
)