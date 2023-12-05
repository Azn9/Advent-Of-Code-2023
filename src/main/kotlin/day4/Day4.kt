package day4

import java.io.File
import java.lang.Math.pow
import kotlin.math.pow

fun main() {
    val file = File("input/day4.txt")

    // ===== Part 1 ======
    val res1 = file.readLines()
        .asSequence()
        .map { line ->
            val usableData = line.split(Regex(": "), 2)[1]

            val winingNumbers = usableData.split("|")[0].split(" ").mapNotNull { it.toIntOrNull() }
            val ourNumbers = usableData.split("|")[1].split(" ").mapNotNull { it.toIntOrNull() }
            val okCount = ourNumbers.count { winingNumbers.contains(it) }

            if (okCount == 0) {
                0.0
            } else {
                2.0.pow(okCount - 1)
            }
        }.sum()

    println("Part 1: $res1")


    // ===== Part 2 =====
    val originalCards = mutableMapOf<Int, Pair<List<Int>, List<Int>>>()

    file.readLines()
        .asSequence()
        .forEach { line ->
            val cardInfo = line.split(Regex(": "), 2)[0]
            val usableData = line.split(Regex(": "), 2)[1]

            val cardId = cardInfo.split(" ").last().toInt()

            val winingNumbers = usableData.split("|")[0].split(" ").mapNotNull { it.toIntOrNull() }
            val ourNumbers = usableData.split("|")[1].split(" ").mapNotNull { it.toIntOrNull() }

            originalCards[cardId] = Pair(winingNumbers, ourNumbers)
        }

    val toProcessCards = mutableListOf<Int>()
    toProcessCards.addAll(originalCards.keys)

    val allCardCount = mutableMapOf<Int, Int>()

    do {
        val currentCardId = toProcessCards.removeFirst()
        allCardCount.compute(currentCardId) { _, v -> v?.plus(1) ?: 1 }

        val currentCard = originalCards[currentCardId] ?: continue

        val winingNumbers = currentCard.second.count { currentCard.first.contains(it) }

        if (winingNumbers == 0) {
            continue
        }

        val newCards = ((currentCardId + 1)..(currentCardId + winingNumbers)).toList()

        println("Card $currentCardId: won $winingNumbers cards: $newCards")

        toProcessCards.addAll(newCards)
    } while (toProcessCards.isNotEmpty())

    val res2 = allCardCount.values.sum()
    println("Part 2: $res2")
}
