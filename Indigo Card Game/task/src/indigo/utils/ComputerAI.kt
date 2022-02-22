package indigo.utils

import indigo.Card
import kotlin.random.Random


object ComputerAI {

    private fun throwRandom(cards: MutableList<Card?>): Card?  = cards[Random.nextInt(0, cards.size)]


    fun determineCandidates(cardOnTable: Card?, cardsOnHand: MutableList<Card?>): MutableList<Card?> {
        var candidates: MutableList<Card?> = mutableListOf()

        cardsOnHand.forEach {
            if (it?.rank == cardOnTable?.rank || it?.suit == cardOnTable?.suit) {
                var notNull = it ?: Card("")
                if (notNull.toString() != "") candidates.add(notNull)
            }
        }
        return candidates
    }

    fun determineCandidateToThrow(candidates: MutableList<Card?>): Card? {
        when (candidates.size) {
            1 -> return candidates.last()
            2 -> return throwRandom(candidates)
            else -> return determineCardToThrow(candidates)
        }
    }

    fun determineCardToThrow(candidates: MutableList<Card?>): Card? {
        val candidatesSize = candidates.size
        val (ranks, suites) = countRanksAndSuites(candidates)
        val repeatedSuites: Boolean = suites.size < candidatesSize
        val repeatedRanks: Boolean = ranks.size < candidatesSize
        var newCandidates = when {
            repeatedSuites -> getRepeatedSuites(candidates)
            repeatedRanks -> getRepeatedRanks(candidates)
            else -> candidates
        }

        return throwRandom(newCandidates as MutableList<Card?>)
    }

    private fun getRepeatedSuites(cards: MutableList<Card?>): MutableList<Card?> {
        var hearts: Int = 0
        var pikes: Int = 0
        var clubs: Int = 0
        var diamonds: Int = 0
        cards.forEach {
            var suite = Suites.getSuite(it?.suit)
            when (suite) {
                Suites.HEARTS -> hearts++
                Suites.DIAMONDS -> diamonds++
                Suites.CLUBS -> clubs++
                Suites.PIKES -> pikes++
            }
        }

        var countedSuites = mutableListOf(
            hearts to Suites.HEARTS,
            pikes to Suites.PIKES,
            clubs to Suites.CLUBS,
            diamonds to Suites.DIAMONDS
        )

        var availableCards = mutableListOf<Card?>()

        countedSuites.forEach {
            var (num, word) = it
            if (num > 1) { // if the suites num is bigger than 1
                cards.forEach { card -> // iterate through the cards and add the ones with the same suite
                    if (card?.suit == word.suit) availableCards.add(card)
                }
            }
        }
        return availableCards
    }

    private fun getRepeatedRanks(cards: MutableList<Card?>): MutableList<Card?> {
        var ace = 0
        var two = 0
        var three = 0
        var four = 0
        var five = 0
        var six = 0
        var seven = 0
        var eight = 0
        var nine = 0
        var ten = 0
        var jack = 0
        var queen = 0
        var king = 0

        cards.forEach {
            var rank = Ranks.getRank(it?.rank)
            when (rank) {
                Ranks.A -> ace++
                Ranks.TWO -> two++
                Ranks.THREE -> three++
                Ranks.FOUR -> four++
                Ranks.FIVE -> five++
                Ranks.SIX -> six++
                Ranks.SEVEN -> seven++
                Ranks.EIGHT -> eight++
                Ranks.NINE -> nine++
                Ranks.TEN -> ten++
                Ranks.J -> jack++
                Ranks.Q -> queen++
                Ranks.K -> king++
            }
        }

        var countedRanks = mutableListOf(
            ace to Ranks.A,
            two to Ranks.TWO,
            three to Ranks.THREE,
            four to Ranks.FOUR,
            five to Ranks.FIVE,
            six to Ranks.SIX,
            seven to Ranks.SEVEN,
            eight to Ranks.EIGHT,
            nine to Ranks.NINE,
            ten to Ranks.TEN,
            jack to Ranks.J,
            queen to Ranks.Q,
            king to Ranks.K
        )

        var availableCards = mutableListOf<Card?>()

        countedRanks.forEach {
            var (num, word) = it
            if (num > 1) { // if the ranks num is bigger than 1
                cards.forEach { card -> // iterate through the cards and add the ones with the same rank
                    if (card?.rank == word.rank) availableCards.add(card)
                }
            }
        }
        return availableCards
    }

    private fun countRanksAndSuites(cards: MutableList<Card?>): Pair<MutableSet<Ranks>, MutableSet<Suites>> {

        var setOfSuites = mutableSetOf<Suites>()
        var setOfRanks = mutableSetOf<Ranks>()

        cards.forEach {
            var suite = Suites.getSuite(it?.suit)
            setOfSuites.add(suite)

            var rank = Ranks.getRank(it?.rank)
            setOfRanks.add(rank)
        }
        return setOfRanks to setOfSuites
    }
}