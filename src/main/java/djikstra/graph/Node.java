package djikstra.graph;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int value;
    private List<Edge> edges = new ArrayList<>();

    public Node(int value) {
        this.value = value;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                '}';
    }
}
