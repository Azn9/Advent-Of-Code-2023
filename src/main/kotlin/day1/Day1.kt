package day1

import java.io.File

fun main() {
    val file = File("input/day1.txt")

    // ===== Part 1 =====
    val res1 = file.readLines()
        .asSequence()
        .map { line ->
            val iterator = line.asSequence().filter { it.isDigit() }

            (iterator.first() + "" + iterator.last()).toInt()
        }.sum()
    print(res1)

    // ===== Part 2 =====
    val res2 = file.readLines()
        .asSequence()
        .map { line ->
            line.replace("one", "o1e")
                .replace("two", "t2o")
                .replace("three", "t3e")
                .replace("four", "f4r")
                .replace("five", "f5e")
                .replace("six", "s6x")
                .replace("seven", "s7n")
                .replace("eight", "e8t")
                .replace("nine", "n9e")
        }
        .map { line ->
            val iterator = line.asSequence().filter { it.isDigit() }

            (iterator.first() + "" + iterator.last()).toInt()
        }.sum()
    print(res2)
}