package indigo.utils

import kotlin.math.PI

enum class Suites(val suit: String) {
    HEARTS("♥"),
    PIKES("♠"),
    CLUBS("♣"),
    DIAMONDS("♦");

    companion object {
        fun getSuite(suite: String?): Suites {
            return when(suite) {
                HEARTS.suit -> HEARTS
                PIKES.suit -> PIKES
                CLUBS.suit -> CLUBS
                else -> DIAMONDS
            }
        }
    }
}