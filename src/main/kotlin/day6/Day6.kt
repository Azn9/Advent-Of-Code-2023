package day6

import java.io.File

fun main() {
    val file = File("input/day6.txt")
    val data = file.readLines()

    val times = data[0].split(Regex(":"), 2)[1].split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
    val distances = data[1].split(Regex(":"), 2)[1].split(" ").filter { it.isNotEmpty() }.map { it.toLong() }

    var res = 1

    times.mapIndexed { index, time ->
        (0..<time).count { it * time - it > distances[index] }
    }.forEach { res *= it }

    println(res)
}