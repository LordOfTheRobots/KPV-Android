package com.kpv.bankcardsmanagement.cards.validator

import java.util.Calendar

object CardValidators {

    fun isLuhnValid(number: String): Boolean {
        val digits = number.filter { it.isDigit() }
        if (digits.length < 13 || digits.length > 19) return false

        var sum = 0
        var alternate = false
        for (i in digits.length - 1 downTo 0) {
            var n = digits[i].digitToInt()
            if (alternate) {
                n *= 2
                if (n > 9) n -= 9
            }
            sum += n
            alternate = !alternate
        }
        return sum % 10 == 0
    }

    fun formatCardNumber(input: String): String {
        val digits = input.filter { it.isDigit() }.take(16)
        return digits.chunked(4).joinToString(" ")
    }

    fun formatExpiryDate(input: String, previous: String): String {
        val digits = input.filter { it.isDigit() }.take(4)

        if (digits.length >= 1) {
            val firstDigit = digits[0].digitToInt()
            if (firstDigit > 1 && digits.length == 1) return previous
        }
        if (digits.length >= 2) {
            val month = digits.substring(0, 2).toIntOrNull() ?: return previous
            if (month > 12 || month == 0) return previous
        }

        return when {
            digits.length <= 2 -> digits
            else -> "${digits.substring(0, 2)}/${digits.substring(2)}"
        }
    }

    fun isExpiryValid(expiry: String): Boolean {
        if (expiry.length != 5 || !expiry.contains("/")) return false
        val parts = expiry.split("/")
        if (parts.size != 2 || parts[0].length != 2 || parts[1].length != 2) return false

        val month = parts[0].toIntOrNull() ?: return false
        val year = parts[1].toIntOrNull() ?: return false
        if (month !in 1..12) return false

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR) % 100
        val currentMonth = calendar.get(Calendar.MONTH) + 1

        return when {
            year > currentYear -> true
            year == currentYear -> month >= currentMonth
            else -> false
        }
    }
}