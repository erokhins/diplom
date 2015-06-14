package org.hanuna

import asString
import tryOrReport

fun main(args: Array<String>) {
    val q1 = QLimits(0.0, 0.52, 26, 200, 5000)
//    println(q1.runForModel(Models.MODEL1).results.joinToString())
    q1.run12()
}

fun QLimits.run12() {
    println(this)
    print("QValue :")
    println(count.indices.map { start + it * delta }.asString())
    print("Model 1:")
    println(runNForModel(Models.MODEL1).result.asString())
    print("Model 2:")
    println(runNForModel(Models.MODEL2).result.asString())
//    print("Model 3:")
//    println(runNForModel(Models.MODEL3).result.asString())
}

data class QLimits(val start: Double, val end: Double, val count: Int, val clients: Int, val fragments: Int,
                   val delta: Double = (end - start) / (count.toDouble()))

data class MResult(val qLimits: QLimits) {
    val results = IntArray(qLimits.count)
}

data class MidResult(val qLimits: QLimits) {
    val result = DoubleArray(qLimits.count)
}

fun List<MResult>.mid(): MidResult {
    val res = MidResult(first().qLimits)

    for (i in res.result.indices) {
        val degree = sumBy { it.results[i] }.toDouble() / (size().toDouble())
        res.result[i] = degree / (res.qLimits.clients * res.qLimits.fragments).toDouble()
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
            if (graph.G() >= q() * graph.clients) {
                mResult.results[currentIndex] = graph.edgesCount
                currentIndex++
            }
            m.step(graph)
        }
    }

    return mResult
}

fun QLimits.runNForModel(m: Model, steps: Int = 10) = steps.indices.map { runForModel(m) }.mid()

