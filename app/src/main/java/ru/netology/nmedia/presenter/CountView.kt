package ru.netology.nmedia.presenter

object CountView {
     fun convert(value: Long): String {
        return when (value) {
            in 0..999 -> value.toString()
            in 1_000..9999 -> if (value%1_000<100) String.format("%dK", value/1_000) else String.format("%.1fK",value/1_000.0)
            in 10_000..999_999 -> String.format("%dK",value/1_000)
            in 1_000_000..1_000_000_000 -> if (value%1_000_000<100_000) String.format("%dM",value/1_000_000) else String.format("%.1fM",value/1_000_000.0)
            else -> String.format("%dB",value/1_000_000_000)
        }
    }
}
