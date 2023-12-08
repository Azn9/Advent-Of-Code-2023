package day7

import java.io.File

fun main() {
    val file = File("input/day7.txt")
    val lines = file.readLines()

    val cardsOrder = listOf("A", "K", "Q", "T", "10", "9", "8", "7", "6", "5", "4", "3", "2", "J")

    data class Hand(val cards: String, val bid: Int, val originalCardCountPerType: MutableMap<Char, Int>)

    val handsPerRank = mutableMapOf<Int, List<Hand>>()

    lines.forEach { line ->
        val hand = line.split(" ")[0]
        val bid = line.split(" ")[1]

        // Parse hand
        val cardCountPerType = mutableMapOf<Char, Int>()

        hand.forEach { card ->
            cardCountPerType[card] = cardCountPerType.getOrDefault(card, 0) + 1
        }

        val originalCardCountPerType = cardCountPerType.toMutableMap()

        if (hand.contains("J")) {
            val ccptWithoutJ = cardCountPerType.filter { it.key != 'J' }

            if (ccptWithoutJ.isEmpty()) {
                cardCountPerType.clear()
                cardCountPerType['A'] = 5
            } else {
                val highestCard = ccptWithoutJ.maxBy { it.value }
                val jCount = cardCountPerType['J']!!
                cardCountPerType.remove('J')
                cardCountPerType[highestCard.key] = highestCard.value + jCount
            }
        }

        // Rank hand
        val handRank = when (cardCountPerType.size) {
            1 -> 7 // Five of a kind
            2 -> {
                if (cardCountPerType.any { it.value == 4 }) {
                    6 // Four of a kind
                } else {
                    5 // Full house
                }
            }
            3 -> {
                if (cardCountPerType.any { it.value == 3 }) {
                    4 // Three of a kind
                } else {
                    3 // Two pairs
                }
            }
            4 -> 2 // One pair
            else -> 1 // High card
        }

        handsPerRank[handRank] = handsPerRank.getOrDefault(handRank, mutableListOf()) + Hand(hand, bid.toInt(), originalCardCountPerType)
    }

    // Sort hands with same rank
    handsPerRank.forEach { (rank, hands) ->
        handsPerRank[rank] = hands.sortedWith { hand1, hand2 ->
            for (i in 0..<5) {
                var card1 = hand1.cards[i]
                var card2 = hand2.cards[i]

                /*
                if (card1 == 'J' && card2 != 'J') {
                    val ccptWithoutJ = hand1.originalCardCountPerType.filter { it.key != 'J' }

                    if (ccptWithoutJ.isEmpty()) {
                        if (card2 == 'A')
                            card1 = 'J'
                        else
                            card1 = 'A'
                    } else
                        card1 = hand1.originalCardCountPerType.filter { it.key != 'J' }.maxBy { it.value }.key
                }
                if (card2 == 'J' && card1 != 'J') {
                    val ccptWithoutJ = hand2.originalCardCountPerType.filter { it.key != 'J' }

                    if (ccptWithoutJ.isEmpty()) {
                        if (card1 == 'A')
                            card2 = 'J'
                        else
                            card2 = 'A'
                    } else
                        card2 = hand2.originalCardCountPerType.filter { it.key != 'J' }.maxBy { it.value }.key
                }

                 */

                val card1Index = cardsOrder.indexOf(card1.toString())
                val card2Index = cardsOrder.indexOf(card2.toString())

                if (card1Index != card2Index) { // if card1index < card2index, card1 is better
                    return@sortedWith card1Index - card2Index
                }
            }

            return@sortedWith 0
        }
    }

    val sortedHands = mutableListOf<Hand>()
    handsPerRank.keys.sortedDescending().forEach { rank ->
        sortedHands.addAll(handsPerRank[rank]!!)
    }

    println(sortedHands)

    var res = 0
    sortedHands.forEachIndexed { index, hand ->
        res += hand.bid * (sortedHands.size - index)
    }

    println(res)
}