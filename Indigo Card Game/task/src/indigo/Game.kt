package indigo

import indigo.utils.ComputerAI

class Game(val numberOfPlayers: Int = 2) {
    private var deck: Deck = Deck()
    private var cardsOnTable :  MutableList<Card?> = mutableListOf<Card?>()
    private var message: String = ""
    private lateinit var currentPlayer: User
    private lateinit var lastWinner: User
    private var status: GameStatus = GameStatus.STOP
    private lateinit var humanUser: HumanUser
    private lateinit var computer: Computer
    private val cpAI = ComputerAI

    init{
        println("Indigo Card Game")
        var (cards, currentMessage) = deck.get(4)
        cardsOnTable = cards.toMutableList()
        message = currentMessage
        var answer = ""
        while (answer != "yes" && answer != "no") {
            println("Play first?")
            answer = readLine()!!
        }
        setUsers(answer)
        if (message == "Good") {
            println("Initial cards on the table: ${ cardsOnTable.joinToString(" ") { it?.value ?: "" } }")
        } else {
            throw Exception()
        }
        play()
    }

    private fun setUsers(playFirst: String) {
        computer = Computer(ai = cpAI)
        humanUser = HumanUser("Player")
        giveCardsToUser(computer)
        giveCardsToUser(humanUser)
        computer.nextPlayer = humanUser
        humanUser.nextPlayer = computer
        if (playFirst == "yes") {
            currentPlayer = humanUser
        } else {
            currentPlayer = computer
        }

        currentPlayer.first = true
    }

    private fun play() {
        status = GameStatus.PLAYING
        while(status == GameStatus.PLAYING) {

            printTurnMessage()

            if (humanUser.wonCards.size + computer.wonCards.size + cardsOnTable.size == Deck.MAX_SIZE) {
                endGame()
                break
            }
            if (currentPlayer.userDeck.size == 0) giveCardsToUser(currentPlayer)
            var play = currentPlayer.playTurn()

            getTurnWinner(play)

            currentPlayer = currentPlayer.nextPlayer
        }
        println("Game Over")
    }

    private fun printTurnMessage() {
        if (cardsOnTable.size == 0) {
            println("\nNo cards on the table")
        } else {
            println("\n${ cardsOnTable.size } cards on the table, and the top card is ${ cardsOnTable.last() }")
        }
    }

    private fun getTurnWinner(play: Card?){
        if (cardsOnTable.size == 0) {
            cardsOnTable.add(play)
        } else if (play?.rank == cardsOnTable.last()?.rank || play?.suit == cardsOnTable.last()?.suit) {
            cardsOnTable.add(play)
            currentPlayer.winCards(cardsOnTable)
            cardsOnTable.clear()
            lastWinner = currentPlayer
            println("${currentPlayer.name} wins cards")
            printScore()
        } else {
            cardsOnTable.add(play)
        }
    }

    private fun printScore() {
        println("Score: ${humanUser.name} ${humanUser.score} - ${computer.name} ${computer.score}")
        println("Cards: ${humanUser.name} ${humanUser.wonCards.size} - ${computer.name} ${computer.wonCards.size}")
    }

    fun endGame(endedByUser: Boolean = false) {
        if(!endedByUser){
            giveRemainsToLastWinner(lastWinner)
            var winner: User = when {
                humanUser.wonCards.size > computer.wonCards.size -> humanUser
                computer.wonCards.size > humanUser.wonCards.size -> computer
                humanUser.first -> humanUser
                else -> computer
            }
            winner.score += 3
            printScore()
        }
        status = GameStatus.STOP
    }

    private fun giveRemainsToLastWinner(lw: User) {
        lw.winCards(cardsOnTable)
        cardsOnTable.clear()
    }

    fun giveCardsToUser(user: User) {
        if (deck.size == 0) { endGame()
            return
        }
        val (newDeck, message) = deck.get(6)
        if (message == "Good") {
            user.renewDeck(newDeck)
        } else {
            println(message)
        }

    }

    enum class GameStatus(val status: String) {
        PLAYING("Playing"),
        STOP("Stop")
    }

    inner class HumanUser(override val name: String) : User() {
        var cardToPlay: String = ""
        var lastCard = 0

        private fun isInCardRange(value: Int): Boolean {
            return value !in 0..(lastCard)
        }

        override fun playTurn(): Card? {
            print("Cards in hand:")
            userDeck.forEachIndexed { index, card -> print(" ${ index + 1 })${ card?.value }") }
            lastCard = userDeck.indexOf(userDeck.last()) + 1
            while (true) {
                println("\nChoose a card to play (1-$lastCard):")
                cardToPlay = readLine()!!

                try {
                    if (isInCardRange(cardToPlay.toInt() - 1)) {
                        continue
                    }
                    return placeCard(cardToPlay.toInt() - 1)

                } catch (e: Exception) {
                    if (cardToPlay == "exit") {
                        endGame(true)
                        return null
                    }
                }
            }
        }

        override fun placeCard(cardToPlay: Int): Card? {
            return userDeck.removeAt(cardToPlay)
        }
    }

    inner class Computer(override val name: String = "Computer", val ai: ComputerAI) : User() {

        override fun playTurn(): Card? {
            println(userDeck.joinToString(" "))
            var candidates = if (cardsOnTable.size == 0) {
                userDeck
            } else {
                ai.determineCandidates(cardsOnTable.last(), userDeck)
            }
            var card = if (candidates.size == 0) {
                ai.determineCardToThrow(userDeck)
            } else {
                ai.determineCandidateToThrow(candidates)
            }

            return placeCard(getCardNumber(card))
        }

        override fun placeCard(cardToPlay: Int): Card? {
            var card = userDeck.removeAt(cardToPlay)
            println("Computer plays $card")
            return card
        }

        fun getCardNumber(card: Card?): Int {
            var index = 0
            userDeck.forEachIndexed { ind, it ->
                if (card == it) {
                    index = ind
                }
            }
            return index
        }
    }
}