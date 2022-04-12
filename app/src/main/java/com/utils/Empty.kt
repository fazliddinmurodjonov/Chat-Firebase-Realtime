package com.utils

object Empty {
    fun empty(text: String): Boolean {
        var bool = false
        for (c in text) {
            if (c != ' ') {
                bool = true
                break
            }
        }
        return bool
    }

    fun space(text: String): Boolean {
        return text != " "
    }
}