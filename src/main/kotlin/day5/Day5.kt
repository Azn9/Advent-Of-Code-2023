/*
 * NOTE: This solution is NOT OPTIMAL for the part 2
 * (It takes ~10 minutes to run)
 */
package day5

import java.io.File
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread

fun main() {
    val file = File("input/day5.txt")
    val input = file.readLines()

    var currentData = ""

    //var seeds = listOf<Long>()
    var seeds = listOf<LongRange>()

    data class Range(val src: Long, val dest: Long, val length: Long)

    val stsMap = mutableListOf<Range>()
    val stfMap = mutableListOf<Range>()
    val ftwMap = mutableListOf<Range>()
    val wtlMap = mutableListOf<Range>()
    val lttMap = mutableListOf<Range>()
    val tthMap = mutableListOf<Range>()
    val htlMap = mutableListOf<Range>()

    input.forEach { line ->
        when {
            line.startsWith("seeds:") -> {
                // ===== Part 1 =====
                /*seeds = line.substringAfter(":")
                    .split(" ")
                    .filter { it.isNotEmpty() }
                    .map { it.toLong() }*/

                // ===== Part 2 =====
                seeds = line.substringAfter(":")
                    .split(" ")
                    .filter { it.isNotEmpty() }
                    .zipWithNext { a, b -> Pair(a.toLong(), b.toLong()) }
                    .filterIndexed { index, _ -> index % 2 == 0 }
                    .apply {
                        println(this)
                    }
                    .map {
                        (it.first..<(it.first + it.second))
                    }
            }
            line == "seed-to-soil map:" -> currentData = "sts"
            line == "soil-to-fertilizer map:" -> currentData = "stf"
            line == "fertilizer-to-water map:" -> currentData = "ftw"
            line == "water-to-light map:" -> currentData = "wtl"
            line == "light-to-temperature map:" -> currentData = "ltt"
            line == "temperature-to-humidity map:" -> currentData = "tth"
            line == "humidity-to-location map:" -> currentData = "htl"
            line.isNotEmpty() -> {
                val data = line.split(" ")
                val destRangeStart = data[0].toLong()
                val srcRangeStart = data[1].toLong()
                val rangeLength = data[2].toLong()
                val range = Range(srcRangeStart, destRangeStart, rangeLength)

                when (currentData) {
                    "sts" -> stsMap.add(range)
                    "stf" -> stfMap.add(range)
                    "ftw" -> ftwMap.add(range)
                    "wtl" -> wtlMap.add(range)
                    "ltt" -> lttMap.add(range)
                    "tth" -> tthMap.add(range)
                    "htl" -> htlMap.add(range)
                }
            }
        }
    }

    val minValue = AtomicLong(Long.MAX_VALUE)

    println(seeds.size)

    val threads = mutableListOf<Thread>()

    seeds.forEachIndexed { index, range ->
        val th = thread(true) {
            var localMinValue = Long.MAX_VALUE

            range.forEach { seed ->
                val soil = stsMap.mapNotNull {
                    if ((it.src..<(it.src + it.length)).contains(seed)) {
                        val diff = seed - it.src
                        return@mapNotNull it.dest + diff
                    }

                    null
                }.firstOrNull() ?: seed
                val fertilizer = stfMap.mapNotNull {
                    if ((it.src..<(it.src + it.length)).contains(soil)) {
                        val diff = soil - it.src
                        return@mapNotNull it.dest + diff
                    }

                    null
                }.firstOrNull() ?: soil
                val water = ftwMap.mapNotNull {
                    if ((it.src..<(it.src + it.length)).contains(fertilizer)) {
                        val diff = fertilizer - it.src
                        return@mapNotNull it.dest + diff
                    }

                    null
                }.firstOrNull() ?: fertilizer
                val light = wtlMap.mapNotNull {
                    if ((it.src..<(it.src + it.length)).contains(water)) {
                        val diff = water - it.src
                        return@mapNotNull it.dest + diff
                    }

                    null
                }.firstOrNull() ?: water
                val temperature = lttMap.mapNotNull {
                    if ((it.src..<(it.src + it.length)).contains(light)) {
                        val diff = light - it.src
                        return@mapNotNull it.dest + diff
                    }

                    null
                }.firstOrNull() ?: light
                val humidity = tthMap.mapNotNull {
                    if ((it.src..<(it.src + it.length)).contains(temperature)) {
                        val diff = temperature - it.src
                        return@mapNotNull it.dest + diff
                    }

                    null
                }.firstOrNull() ?: temperature
                val location = htlMap.mapNotNull {
                    if ((it.src..<(it.src + it.length)).contains(humidity)) {
                        val diff = humidity - it.src
                        return@mapNotNull it.dest + diff
                    }

                    null
                }.firstOrNull() ?: humidity

                if (location < localMinValue) {
                    localMinValue = location
                }
            }

            println("Thread ${Thread.currentThread().name} finished with $localMinValue")
            if (localMinValue < minValue.get()) {
                minValue.set(localMinValue)
            }
        }
        th.name = "Thread $index"
        th.priority = Thread.MAX_PRIORITY
        threads.add(th)
    }

    threads.forEach { it.join() }

    println(minValue)
}
