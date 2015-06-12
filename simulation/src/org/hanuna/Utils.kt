

import random
import java.util.Random

val random = Random()

// including borders
fun random(range: IntRange) = range.start + random.nextInt(range.end - range.start + 1)

inline fun notMoreN(n: Int = 100, f:() -> Unit) {
    for(i in 1..n) {
        f()
    }
    throw IllegalStateException("more 100")
}

inline fun <T> tryOrReport(f: () -> T): T? {
    try {
        return f()
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return null
}

fun DoubleArray.asString() = map { java.lang.String.format( "%.3f", it ) }.joinToString(", ", "[", "]")
fun List<Double>.asString() = map { java.lang.String.format( "%.3f", it ) }.joinToString(", ", "[", "]")