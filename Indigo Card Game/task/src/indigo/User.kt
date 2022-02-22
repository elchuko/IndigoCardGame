package indigo

abstract class User {

    abstract val name: String

    lateinit var nextPlayer: User

    var userDeck: MutableList<Card?> = mutableListOf()

    var wonCards: MutableList<Card?> = mutableListOf()

    var first: Boolean = false

    var score: Int = 0

    abstract fun placeCard(cardToPlay: Int): Card?

    abstract fun playTurn(): Card?

    fun calculateScore(cardsWon: MutableList<Card?>): Int {
        for(card in cardsWon) {
            score += card?.numValue ?: 0
        }
        return score
    }

    fun winCards(cardsWon: MutableList<Card?>) {
        calculateScore(cardsWon)
        wonCards.addAll(cardsWon)
    }

    fun renewDeck(newDeck: MutableList<Card>) {
        userDeck = newDeck.toMutableList()
    }
}