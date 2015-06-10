
import org.junit.Test
import org.hanuna.Graph
import kotlin.test.assertEquals

class GraphTest {
    Test fun edgeTest() {
        val graph = Graph(2, 5)
        assertEquals(false, graph[0, 4])
        graph[0, 4] = true
        assertEquals(true, graph[0, 4])
        graph[0, 4] = true
        assertEquals(true, graph[0, 4])
        graph[0, 4] = false
        assertEquals(false, graph[0, 4])
    }

    Test fun edgeCountTest() {
        val graph = Graph(5, 2)

        assertEquals(0, graph.getClientDegree(4))
        assertEquals(0, graph.getFragmentDegree(1))
        graph[4, 1] = true
        assertEquals(1, graph.getClientDegree(4))
        assertEquals(1, graph.getFragmentDegree(1))
        graph[4, 1] = true
        assertEquals(1, graph.getClientDegree(4))
        assertEquals(1, graph.getFragmentDegree(1))
        graph[4, 1] = false
        assertEquals(0, graph.getClientDegree(4))
        assertEquals(0, graph.getFragmentDegree(1))
        graph[4, 1] = false
        assertEquals(0, graph.getClientDegree(4))
        assertEquals(0, graph.getFragmentDegree(1))
    }
}