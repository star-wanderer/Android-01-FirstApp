package ru.netology.nmedia.error

object ErrorCodes {

    fun getDescription(errorCode: Int): String {
        return when (errorCode) {
            0x0 -> "Unknown error"
            in 300..399 -> "Your code was forwarded"
            in 400..499 -> "Client side error"
            in 500..599 -> "Server side error"
            else -> {"Highly likely success"}
        }
    }
}