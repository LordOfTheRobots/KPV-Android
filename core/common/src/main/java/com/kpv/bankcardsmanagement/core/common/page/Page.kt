package com.kpv.bankcardsmanagement.core.common.page

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable;

@Serializable
data class PageResponse<T>(
    @SerialName("content") val content: List<T>,
    @SerialName("pageable") val pageable: Pageable,
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("totalElements") val totalElements: Int,
    @SerialName("last") val isLast: Boolean,
    @SerialName("first") val isFirst: Boolean,
    @SerialName("numberOfElements") val numberOfElements: Int,
    @SerialName("empty") val isEmpty: Boolean
)

@Serializable
data class Pageable(
    @SerialName("pageNumber") val pageNumber: Int,
    @SerialName("pageSize") val pageSize: Int,
    @SerialName("sort") val sort: Sort? = null
)

@Serializable
data class Sort(
    @SerialName("sorted") val isSorted: Boolean,
    @SerialName("unsorted") val isUnsorted: Boolean,
    @SerialName("empty") val isEmpty: Boolean
)