package com.kpv.bankcardsmanagement.core.common.page

inline fun <T, reified E : Throwable> Result<T>.mapFailure(
    crossinline transform: (Throwable) -> E
): Result<T> = fold(
    onSuccess = { Result.success(it) },
    onFailure = { Result.failure(transform(it)) }
)