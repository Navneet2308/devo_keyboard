package com.example.keyboard_app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform