package org.hanuna

import tryOrReport

fun main(args: Array<String>) {

}


data class QLimits(val start: Double, val end: Double, val count: Int, val clients: Int, val fragments: Int) {
    val delta: Double get() = (end - start) / (count.toDouble())
}

data class MResult(val qLimits: QLimits) {
    val results = IntArray(qLimits.count)
}

data class MidResult(val qLimits: QLimits) {
    val result = DoubleArray(qLimits.count)
}

fun List<MResult>.mid(): MidResult {
    val res = MidResult(first().qLimits)

    for (i in res.result.indices) {
        res.result[i] = sumBy { it.results[i] }.toDouble() / (size().toDouble())
    }

    return res
}

fun QLimits.runForModel(m: Model): MResult {
    val graph = Graph(clients, fragments)
    val mResult = MResult(this)

    var currentIndex = 0
    fun q() = currentIndex * delta + start

    while (currentIndex < count) {
        tryOrReport {
            if (graph.G() >= q()) {
                mResult.results[currentIndex] = graph.edgesCount
                currentIndex++
            }
            m.step(graph)
        }
    }

    return mResult
}

fun QLimits.runNForModel(m: Model, steps: Int = 50) = steps.indices.map { runForModel(m) }.mid()

