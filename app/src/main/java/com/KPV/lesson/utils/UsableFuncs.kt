package com.kpv.lesson.utils

fun CreateURL(
    node: String,
    params: List<String>
): String{
    var url = node
    if (params.isNotEmpty()) {
        url += "?" + params.joinToString("&")
    }
    return url
}