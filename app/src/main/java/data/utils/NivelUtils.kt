package com.example.habitos.data.utils

fun calcularNivel(puntos: Int): String = when {
    puntos >= 250 -> "Diamond"
    puntos >= 150 -> "Platinum"
    puntos >= 100 -> "Gold"
    puntos >= 50 -> "Silver"
    else -> "Bronze"
}