package com.destructo.sushi.util

object Pkce {
    private const val VERIFIER_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-._~"

    fun generate(length: Int=128): String{
        return (1..length)
            .map { VERIFIER_STRING.random() }
            .joinToString("")
    }
}

