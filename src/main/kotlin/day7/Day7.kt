package day7

import java.io.File

fun main() {
    val file = File("input/day7.txt")
    val lines = file.readLines()

    val cardsOrder = listOf("A", "K", "Q", "J", "T", "10", "9", "8", "7", "6", "5", "4", "3", "2")

    data class Hand(val cards: String, val bid: Int)

    val handsPerRank = mutableMapOf<Int, List<Hand>>()

    lines.forEach { line ->
        val hand = line.split(" ")[0]
        val bid = line.split(" ")[1]

        // Parse hand
        val cardCountPerType = mutableMapOf<Char, Int>()

        hand.forEach { card ->
            cardCountPerType[card] = cardCountPerType.getOrDefault(card, 0) + 1
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

        handsPerRank[handRank] = handsPerRank.getOrDefault(handRank, mutableListOf()) + Hand(hand, bid.toInt())
    }

    // Sort hands with same rank
    handsPerRank.forEach { (rank, hands) ->
        handsPerRank[rank] = hands.sortedWith { hand1, hand2 ->
            for (i in 0..<5) {
                val card1 = hand1.cards[i]
                val card2 = hand2.cards[i]

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