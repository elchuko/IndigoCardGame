package indigo

class Deck {
    // reset, shuffle and get

    private val goldenDeck = mutableListOf<Card>()
    private lateinit var usableDeck: MutableList<Card>
    var size = 0
        get() {
            return usableDeck.size
        }

    init{
        val ranks = mutableListOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val suits = mutableListOf("♠", "♥", "♦",  "♣")
        for (rank in ranks) {
            for (suite in suits) {
                goldenDeck.add(Card(rank + suite))
            }
        }
        reset()
        shuffle()
    }

    fun get(qty: Int): Pair<MutableList<Card>, String> {
        if (qty > usableDeck.size && qty <= 52) {
            return mutableListOf<Card>() to "The remaining cards are insufficient to meet the request."
        } else if (qty <= 0 || qty > 52) {
            return mutableListOf<Card>() to "Invalid number of cards."
        }
        var cards: MutableList<Card> = usableDeck.subList(0, qty).toMutableList()

        repeat(qty) {
            usableDeck.removeAt(0)
        }
        return cards to "Good"
    }

    fun reset(): String {
        usableDeck = goldenDeck.toMutableList()
        return "Card deck is reset."
    }

    fun shuffle(): String {
        usableDeck.shuffle()
        return "Card deck is shuffled."
    }

    companion object {
        const val MAX_SIZE = 52
    }

}