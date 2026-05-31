package com.kpv.bankcardsmanagement.domain.core.model

enum class CardStatus(
    val code: Int,
    val label: String,
    val color: String,
    val isAvailable: Boolean
) {
    ACTIVE(1, "Активна", "green", true),
    BLOCKED(2, "Заблокирована", "red", false),
    EXPIRED(3, "Истек срок", "gray", false),
    PENDING(4, "На рассмотрении", "yellow", true);

    companion object {
        fun fromCode(code: Int): CardStatus =
            values().find { it.code == code }
                ?: throw IllegalArgumentException("Неизвестный статус карты: $code")
    }
}