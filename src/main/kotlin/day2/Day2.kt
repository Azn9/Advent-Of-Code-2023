package day2

import java.io.File
import kotlin.math.max

fun main() {
    val file = File("input/day2.txt")

    // ===== Part 1 =====
    val maxRed = 12
    val maxGreen = 13
    val maxBlue = 14

    val res1 = file.readLines()
        .asSequence()
        .map {
            val gameId = it.split(Regex(" "), 2)[1].split(":")[0].toInt()
            val datas = it.split(Regex(" "), 2)[1].split(";")

            var maxShowedRed = 0
            var maxShowedGreen = 0
            var maxShowedBlue = 0

            datas.forEach { data ->
                val otherDatas = data.split(",")

                otherDatas.forEach { od ->
                    val amount = od.split(" ")[1].toInt()
                    val color = od.split(" ")[2]

                    when (color) {
                        "red" -> maxShowedRed = max(maxShowedRed, amount)
                        "green" -> maxShowedGreen = max(maxShowedGreen, amount)
                        "blue" -> maxShowedBlue = max(maxShowedBlue, amount)
                    }
                }
            }

            if (maxShowedRed > maxRed || maxShowedBlue > maxBlue || maxShowedGreen > maxGreen)
                return@map 0

            gameId
        }.sum()

    println(res1)

    // ===== Part 2 =====
    val res2 = file.readLines()
        .asSequence()
        .map {
            val datas = it.split(Regex(" "), 2)[1].split(";")

            var maxShowedRed = 0
            var maxShowedGreen = 0
            var maxShowedBlue = 0

            datas.forEach { data ->
                val otherDatas = data.split(",")

                otherDatas.forEach { od ->
                    val amount = od.split(" ")[1].toInt()
                    val color = od.split(" ")[2]

                    when (color) {
                        "red" -> maxShowedRed = max(maxShowedRed, amount)
                        "green" -> maxShowedGreen = max(maxShowedGreen, amount)
                        "blue" -> maxShowedBlue = max(maxShowedBlue, amount)
                    }
                }
            }

            return@map maxShowedRed * maxShowedBlue * maxShowedGreen
        }.sum()
    println(res2)
}