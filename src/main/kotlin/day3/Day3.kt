package day3

import java.io.File

fun main() {
    val file = File("input/day3.txt")
    val res = file.readLines()

    val grid = Array(res.size) { Array(res[0].length) { ' ' } }
    val digits = mutableListOf<Pair<Int, Int>>()
    val symbols = mutableListOf<Pair<Int, Int>>()
    val gears = mutableListOf<Pair<Int, Int>>()

    for (i in res.indices) {
        val line = res[i]

        for (j in line.indices) {
            val char = line[j]

            grid[i][j] = char

            if (char.isDigit()) {
                digits.add(Pair(i, j))
            } else if (char != '.') {
                if (char == '*') {
                    gears.add(Pair(i, j))
                }

                symbols.add(Pair(i, j))
            }
        }
    }

    data class DigitCluster(val coords: Set<Pair<Int, Int>>)

    val digitClusters = mutableListOf<DigitCluster>()
    val lookedCoords = mutableListOf<Pair<Int, Int>>()

    digits.forEach { coords ->
        if (lookedCoords.contains(coords)) return@forEach

        val cluster = mutableSetOf<Pair<Int, Int>>()
        val toLook = mutableListOf<Pair<Int, Int>>()

        toLook.add(coords)

        while (toLook.isNotEmpty()) {
            val coord = toLook.removeAt(0)

            if (lookedCoords.contains(coord)) continue
            lookedCoords.add(coord)

            cluster.add(coord)

            val x = coord.first
            val y = coord.second

            if (y > 0 && grid[x][y - 1].isDigit()) toLook.add(Pair(x, y - 1))
            if (y < grid[0].size - 1 && grid[x][y + 1].isDigit()) toLook.add(Pair(x, y + 1))
        }

        digitClusters.add(DigitCluster(cluster))
    }

    println("Found clusters: $digitClusters")

    val digitClustersWithSymbols = mutableListOf<DigitCluster>()

    digitClusters.forEach { cluster ->
        val toLookCoords = mutableSetOf<Pair<Int, Int>>()

        cluster.coords.forEach { coord ->
            if (coord.first > 0) toLookCoords.add(Pair(coord.first - 1, coord.second))
            if (coord.first < grid.size - 1) toLookCoords.add(Pair(coord.first + 1, coord.second))
            if (coord.second > 0) toLookCoords.add(Pair(coord.first, coord.second - 1))
            if (coord.second < grid[0].size - 1) toLookCoords.add(Pair(coord.first, coord.second + 1))
            if (coord.first > 0 && coord.second > 0) toLookCoords.add(Pair(coord.first - 1, coord.second - 1))
            if (coord.first > 0 && coord.second < grid[0].size - 1) toLookCoords.add(
                Pair(
                    coord.first - 1,
                    coord.second + 1
                )
            )
            if (coord.first < grid.size - 1 && coord.second > 0) toLookCoords.add(
                Pair(
                    coord.first + 1,
                    coord.second - 1
                )
            )
            if (coord.first < grid.size - 1 && coord.second < grid[0].size - 1) toLookCoords.add(
                Pair(
                    coord.first + 1,
                    coord.second + 1
                )
            )
        }

        if (toLookCoords.any { coord -> symbols.contains(coord) }) {
            digitClustersWithSymbols.add(cluster)
        }
    }

    println("Found clusters with symbols: $digitClustersWithSymbols")

    // ==== PART 1 ====
    val res1 = digitClustersWithSymbols.sumOf { cluster ->
        cluster.coords.sortedBy { it.first }.map { coord ->
            val x = coord.first
            val y = coord.second

            grid[x][y]
        }.joinToString("").toInt()
    }

    println(res1)

    // ==== PART 2 ====

    var res2 = 0
    gears.forEach { gear ->
        val x = gear.first
        val y = gear.second

        val coords = mutableListOf<Pair<Int, Int>>()

        if (x > 0) coords.add(Pair(x - 1, y))
        if (x < grid.size - 1) coords.add(Pair(x + 1, y))
        if (y > 0) coords.add(Pair(x, y - 1))
        if (y < grid[0].size - 1) coords.add(Pair(x, y + 1))
        if (x > 0 && y > 0) coords.add(Pair(x - 1, y - 1))
        if (x > 0 && y < grid[0].size - 1) coords.add(Pair(x - 1, y + 1))
        if (x < grid.size - 1 && y > 0) coords.add(Pair(x + 1, y - 1))
        if (x < grid.size - 1 && y < grid[0].size - 1) coords.add(Pair(x + 1, y + 1))

        val digitClustersWithSymbolsWithGear = digitClustersWithSymbols.filter { cluster ->
            cluster.coords.any { coords.contains(it) }
        }

        if (digitClustersWithSymbolsWithGear.size == 2) {
            val cluster1 = digitClustersWithSymbolsWithGear[0]
            val cluster2 = digitClustersWithSymbolsWithGear[1]

            val value1 = cluster1.coords
                .sortedBy { it.first }
                .map { coord -> grid[coord.first][coord.second] }
                .joinToString("")
                .toInt()
            val value2 = cluster2.coords
                .sortedBy { it.first }
                .map { coord -> grid[coord.first][coord.second] }
                .joinToString("")
                .toInt()

            res2 += value1 * value2
        }
    }

    println(res2)
}
