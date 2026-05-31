package com.kpv.bankcardsmanagement.cards.ui

import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kpv.bankcardsmanagement.cards.validator.CardValidators
import com.kpv.bankcardsmanagement.core.ui.theme.StringsObject
import com.kpv.bankcardsmanagement.domain.core.model.CardInput
import com.kpv.bankcardsmanagement.feature.cards.viewmodel.CardsViewModel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun AddCardDialog(viewModel: CardsViewModel, onDismiss: () -> Unit) {
    var numberField by remember { mutableStateOf(TextFieldValue("")) }
    var expireField by remember { mutableStateOf(TextFieldValue("")) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    val digits = numberField.text.filter { it.isDigit() }

    val isNumberComplete = digits.length in 13..19
    val isLuhnValid = CardValidators.isLuhnValid(digits)
    val isExpireValid = CardValidators.isExpiryValid(expireField.text)
    val canSubmit = isNumberComplete && isExpireValid && !state.isAdding

    val showNumberError = digits.length >= 16 && !isLuhnValid
    val showExpireError = expireField.text.length == 5 && !isExpireValid

    Dialog(onDismissRequest = { if (!state.isAdding) onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0D47A1).copy(alpha = 0.95f),
                                Color(0xFF1976D2).copy(alpha = 0.95f)
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = StringsObject.addCard.asString(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )

                    Spacer(Modifier.height(24.dp))

                    GlassTextField(
                        value = numberField,
                        onValueChange = { newValue ->
                            numberField = formatCardNumberWithCursor(newValue, numberField)
                        },
                        label = StringsObject.cardNumberHint.asString(),
                        isError = showNumberError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    if (showNumberError) {
                        Text(
                            text = StringsObject.errorInvalidCardNumber.asString(),
                            color = Color(0xFFFFEB3B),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    GlassTextField(
                        value = expireField,
                        onValueChange = { newValue ->
                            expireField = formatExpiryWithCursor(newValue, expireField)
                        },
                        label = StringsObject.expireHint.asString(),
                        isError = showExpireError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )

                    if (showExpireError) {
                        Text(
                            text = StringsObject.errorInvalidDate.asString(),
                            color = Color(0xFFFFEB3B),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                        )
                    }

                    state.error?.let { error ->
                        Spacer(Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF000000).copy(alpha = 0.3f))
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = error.asString(),
                                color = Color(0xFFFFEB3B),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            enabled = !state.isAdding,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = Color.White.copy(alpha = if (state.isAdding) 0.3f else 0.5f)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(StringsObject.cancel.asString())
                        }

                        Button(
                            onClick = {
                                viewModel.addCard(CardInput(digits, expireField.text))
                            },
                            enabled = canSubmit,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF0D47A1),
                                disabledContainerColor = Color.White.copy(alpha = 0.3f),
                                disabledContentColor = Color(0xFF0D47A1).copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            if (state.isAdding) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color(0xFF0D47A1),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = StringsObject.add.asString(),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatCardNumberWithCursor(
    newValue: TextFieldValue,
    oldValue: TextFieldValue
): TextFieldValue {
    val isDeletion = newValue.text.length < oldValue.text.length
    val oldCursorPos = oldValue.selection.start

    var adjustedText = newValue.text
    var adjustedCursorPos = newValue.selection.start

    if (isDeletion && oldCursorPos > 0 && oldValue.text[oldCursorPos - 1] == ' ') {
        val deleteIndex = newValue.selection.start - 1
        if (deleteIndex >= 0) {
            adjustedText = newValue.text.removeRange(deleteIndex, deleteIndex + 1)
            adjustedCursorPos = deleteIndex
        }
    }

    val digitsBeforeCursor = adjustedText.take(adjustedCursorPos).count { it.isDigit() }

    val rawDigits = adjustedText.filter { it.isDigit() }.take(16)
    val formatted = rawDigits.chunked(4).joinToString(" ")

    var newCursorPos = 0
    var digitCount = 0
    for (i in 0..formatted.length) {
        if (digitCount == digitsBeforeCursor) {
            newCursorPos = i
        }
        if (i < formatted.length && formatted[i].isDigit()) {
            if (digitCount == digitsBeforeCursor) break
            digitCount++
        }
    }

    return TextFieldValue(
        text = formatted,
        selection = TextRange(newCursorPos)
    )
}

private fun formatExpiryWithCursor(
    newValue: TextFieldValue,
    oldValue: TextFieldValue
): TextFieldValue {
    val isDeletion = newValue.text.length < oldValue.text.length
    val oldCursorPos = oldValue.selection.start

    var adjustedText = newValue.text
    var adjustedCursorPos = newValue.selection.start

   if (isDeletion && oldCursorPos > 0 && oldValue.text[oldCursorPos - 1] == '/') {
        val deleteIndex = newValue.selection.start - 1
        if (deleteIndex >= 0) {
            adjustedText = newValue.text.removeRange(deleteIndex, deleteIndex + 1)
            adjustedCursorPos = deleteIndex
        }
    }

    val digitsBeforeCursor = adjustedText.take(adjustedCursorPos).count { it.isDigit() }
    val rawDigits = adjustedText.filter { it.isDigit() }.take(4)

    if (rawDigits.isNotEmpty() && rawDigits[0].digitToInt() > 1) {
        return oldValue
    }
    if (rawDigits.length >= 2) {
        val month = rawDigits.substring(0, 2).toIntOrNull() ?: return oldValue
        if (month > 12 || month == 0) return oldValue
    }

    val formatted = when {
        rawDigits.length > 2 -> "${rawDigits.substring(0, 2)}/${rawDigits.substring(2)}"
        rawDigits.length == 2 && !isDeletion -> "${rawDigits}/"
        else -> rawDigits
    }

    var newCursorPos = 0
    var digitCount = 0
    for (i in 0..formatted.length) {
        if (digitCount == digitsBeforeCursor) {
            newCursorPos = i
        }
        if (i < formatted.length && formatted[i].isDigit()) {
            if (digitCount == digitsBeforeCursor) break
            digitCount++
        }
    }

    return TextFieldValue(
        text = formatted,
        selection = TextRange(newCursorPos)
    )
}

@Composable
private fun GlassTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    isError: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = if (isError) Color(0xFFFFEB3B) else Color(0xFFB3E5FC)
            )
        },
        trailingIcon = trailingIcon,
        isError = isError,
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(alpha = 0.12f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.08f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = if (isError) Color(0xFFFFEB3B) else Color.White.copy(alpha = 0.6f),
            unfocusedBorderColor = if (isError) Color(0xFFFFEB3B).copy(alpha = 0.7f) else Color.White.copy(alpha = 0.25f),
            cursorColor = Color.White,
            errorBorderColor = Color(0xFFFFEB3B),
            errorCursorColor = Color(0xFFFFEB3B),
            errorLabelColor = Color(0xFFFFEB3B)
        )
    )
}

@Composable
private fun CardTypeIcon(text: String) {
    if (text.isNotEmpty()) {
        Box(
            modifier = Modifier
                .padding(end = 12.dp)
                .size(44.dp, 28.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color(0xFF0D47A1),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing
                ),
                maxLines = 1
            )
        }
    }
}