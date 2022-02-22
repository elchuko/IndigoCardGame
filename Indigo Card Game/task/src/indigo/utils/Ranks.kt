package indigo.utils

enum class Ranks(val rank: String) {
    A("A"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    J("J"),
    Q("Q"),
    K("K");

    companion object {
        fun getRank(rank: String?): Ranks {
            return when (rank) {
                A.rank -> A
                TWO.rank -> TWO
                THREE.rank -> THREE
                FOUR.rank -> FOUR
                FIVE.rank -> FIVE
                SIX.rank -> SIX
                SEVEN.rank -> SEVEN
                EIGHT.rank -> EIGHT
                NINE.rank -> NINE
                TEN.rank -> TEN
                J.rank -> J
                Q.rank -> Q
                else -> K
            }
        }
    }


}