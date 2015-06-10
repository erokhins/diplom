package org.hanuna

import java.util.*

class Graph(val clients: Int, val fragments: Int) {
    private val edges = BitSet(clients * fragments)
    private val fragmentsDegree = IntArray(fragments)
    private val clientsDegree = IntArray(clients)

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
        fragmentsDegree[fragment] += change
        clientsDegree[client] += change
    }

    fun getClientDegree(client: Int) = clientsDegree[client]
    fun getFragmentDegree(fragment: Int) = fragmentsDegree[fragment]
}

trait Model {
    fun step(graph: Graph)
}