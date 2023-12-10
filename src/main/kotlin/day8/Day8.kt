package day8

import java.io.File

fun main() {
    val input = File("input/day8.txt").readLines()

    val instructions = input[0]
    val data = input.subList(1, input.size)

    data class Node(var name: String, var left: Node? = null, var right: Node? = null) {
        override fun equals(other: Any?): Boolean {
            if (other !is Node) return false
            return name == other.name
        }
    }

    val allNodes = mutableMapOf<String, Node>()

    data.forEach { line ->
        val name = line.split(Regex(" "), 2)[0]
        allNodes[name] = Node(name)
    }

    data.forEach { line ->
        if (!line.contains("(")) return@forEach

        val name = line.split(Regex(" "), 2)[0]
        val data = line.split("(")[1]
            .split(")")[0]
        val left = data.split(", ")[0]
        val right = data.split(", ")[1]

        allNodes[name]?.left = allNodes[left]
        allNodes[name]?.right = allNodes[right]
    }
/*
    val startNode = allNodes["AAA"]
    val endNode = allNodes["ZZZ"]

    var moves = 0
    var currentNode = startNode
    do {
        instructions.forEach { c ->
            if (currentNode == endNode) {
                println(moves)
                return
            }

            println("Current node: ${currentNode?.name} - $c")

            currentNode = if (c == 'L') {
                currentNode?.left
            } else {
                currentNode?.right
            }

            println("New node: ${currentNode?.name}")

            moves++
        }
    } while (currentNode != endNode)

    println(moves)*/

    var moves = 0
    var currentNodes = allNodes.values.filter { it.name.endsWith("A") }.toMutableList()

    println("Starting with ${currentNodes.size} nodes")

    do {
        println(currentNodes.map { it.name })

        instructions.forEach { c ->
            currentNodes.forEachIndexed { i, node ->
                if (c == 'L') {
                    currentNodes[i] = node.left!!
                } else {
                    currentNodes[i] = node.right!!
                }
            }

            moves++
        }
    } while (currentNodes.any { !it.name.endsWith("Z") })
    println(currentNodes.map { it.name })

    println(moves)
}