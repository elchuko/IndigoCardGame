package indigo

import indigo.utils.PointedRanks

data class Card(val value: String) {

    val rank: String
        get() {
            return value.slice(0..value.length - 2 )
        }

    val suit: String
        get() {
            return value.slice(value.length - 1..value.length - 1 )
        }

    val numValue: Int
        get() {
            return PointedRanks.valueOf(getValueString(rank)).value
        }

    override fun toString(): String {
        return "$value"
    }

    private fun getValueString(rank: String): String {
        return when (rank) {
            "2" -> "TWO"
            "3" -> "THREE"
            "4" -> "FOUR"
            "5" -> "FIVE"
            "6" -> "SIX"
            "7" -> "SEVEN"
            "8" -> "EIGHT"
            "9" -> "NINE"
            "10" -> "TEN"
            else -> rank
        }
    }

}