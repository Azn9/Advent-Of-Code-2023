package day9

import java.io.File

fun main() {
    val input = File("input/day9.txt").readLines()

    input.sumOf { line ->
        var list = line.split(" ").map { it.toLong() }
        val lastNumbers = mutableListOf(list.last())
        val firstNumbers = mutableListOf(list.first())

        do {
            list = list.zipWithNext().map { pair -> pair.second - pair.first }
            lastNumbers += list.last()
            firstNumbers += list.first()
        } while (list.any { it != 0L })

        var currentNumber = firstNumbers.last()
        while (firstNumbers.isNotEmpty()) {
            currentNumber = currentNumber * -1 + firstNumbers.removeLast()
        }

        currentNumber
    }.let { println("Part 1: $it") }
}