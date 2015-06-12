package org.hanuna

import notMoreN
import random
import java.util.*

class Graph(val clients: Int, val fragments: Int) {
    private val edges = BitSet(clients * fragments)
    val fragmentsDegree = IntArray(fragments)
    val clientsDegree = IntArray(clients)
    var edgesCount: Int = 0
        private set

    private fun edgeIndex(client: Int, fragment: Int): Int {
        if (client !in 0..clients - 1) throw IllegalStateException("Client count out of range: $client !in [0, $clients)")
        if (fragment !in 0..fragments - 1) throw IllegalStateException("Fragment count out of range: $fragment !in [0, $fragments)")
        return client * fragments + fragment
    }

    fun get(client: Int, fragment: Int): Boolean = edges[edgeIndex(client, fragment)]

    fun set(client: Int, fragment: Int, value: Boolean) {
        val prevValue = edges[edgeIndex(client, fragment)]
        edges[edgeIndex(client, fragment)] = value

        val change = when {
            (prevValue == true) and (value == false) -> -1
            (prevValue == false) and (value == true) -> 1
            else -> 0
        }
        edgesCount += change
        fragmentsDegree[fragment] += change
        clientsDegree[client] += change
    }

    fun getClientDegree(client: Int) = clientsDegree[client]
    fun getFragmentDegree(fragment: Int) = fragmentsDegree[fragment]
}

fun Graph.G() = fragmentsDegree.min()!!

fun Graph.W() = fragmentsDegree.max()!!

trait Model {
    fun step(graph: Graph)
}


enum class Models: Model {
    MODEL3_FIRST : Models() {
        override fun step(graph: Graph) = notMoreN {
            val client = random(0..graph.clients - 1)

            var fragmentDegree = graph.clients + 100
            var fragmentIndex = -1
            for (fragment in 0..graph.fragments - 1) {
                if (!graph[client, fragment] && fragmentDegree > graph.getFragmentDegree(fragment)) {
                    fragmentDegree = graph.getFragmentDegree(fragment)
                    fragmentIndex = fragment
                }
            }
            if (fragmentIndex != -1) {
                graph[client, fragmentIndex] = true
            }
            return
        }
    }

    MODEL3 : Models() {
        override fun step(graph: Graph) = notMoreN {
            val client = random(0..graph.clients - 1)

            val minDegree = (0..graph.fragments - 1).sequence().map {
                if (graph[client, it]) Int.MAX_VALUE
                else graph.getFragmentDegree(it)
            }.min<Int>()
            if (minDegree != Int.MAX_VALUE) {
                val minEdges = (0..graph.fragments - 1).filter { !graph[client, it] && (graph.getFragmentDegree(it) == minDegree) }
                assert(minEdges.isNotEmpty())
                val fragment = minEdges[random(0..minEdges.size() - 1)]
                graph[client, fragment] = true
                return
            }
        }
    }

    MODEL1: Models() {
        override fun step(graph: Graph) = notMoreN(1000) {
            val client = random(graph.clients.indices)
            val fragment = random(graph.fragments.indices)

            if (!graph[client, fragment]) {
                graph[client, fragment] = true
                return
            }
        }
    }

    MODEL2: Models() {
        override fun step(graph: Graph) = notMoreN(1000) {
            val client = random(graph.clients.indices)
            if (graph.clientsDegree[client] < graph.fragments) {
                notMoreN() {
                    val fragment = random(graph.fragments.indices)
                    if (!graph[client, fragment]) {
                        graph[client, fragment] = true
                        return
                    }
                }
            }
        }
    }
}