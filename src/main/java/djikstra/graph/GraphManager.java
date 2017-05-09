package djikstra.graph;

import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

/*
    This class stores all the nodes in a graph
 */
public class GraphManager {
    private List<Node> nodes;
    private int nodeSize;

    public GraphManager(int nodeSize) {
        this.nodeSize = nodeSize;
        this.nodes = new ArrayList<>(nodeSize);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    /*
        This method generates all the nodes from 1 to x
     */
    public void generateNodes() {
        Observable.range(1, nodeSize)
                .blockingSubscribe(index -> nodes.add(new Node(index)));
    }

    /*
        This method adds an edge connecting two nodes.
     */
    public void addEdge(int firstNodeValue, int secondNodeValue, float weight) {
        Edge edge = new Edge(weight);

        /*I subtract 1, because node values are from 1 to x
         * and indexes are from 0 to x-1 */
        Node firstNode = nodes.get(firstNodeValue - 1);
        Node secondNode = nodes.get(secondNodeValue - 1);

        /*I for sure check if the node has the same value as its index*/
        if (firstNode.getValue() != firstNodeValue) {
            throw new GraphException("First node value: " + firstNode.getValue() + " is not equal param value: " + firstNodeValue);
        }
        if (secondNode.getValue() != secondNodeValue) {
            throw new GraphException("Second node value: " + secondNode.getValue() + " is not equal param value: " + secondNodeValue);
        }

        firstNode.addEdge(edge);
        secondNode.addEdge(edge);
        edge.setFirstNode(firstNode);
        edge.setSecondNode(secondNode);
    }
}
