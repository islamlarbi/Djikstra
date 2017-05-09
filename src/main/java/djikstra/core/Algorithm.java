package djikstra.core;

import djikstra.graph.Edge;
import djikstra.graph.Node;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Algorithm {
    private Node startNode;
    private Node endNode;

    private boolean alreadyCalculated = false;

    private PriorityQueue<NodeWrapper> priorityQueue;
    private NodeWrapper[] wrappedNodes;

    public Algorithm(int queueSize, Node startNode, Node endNode) {
        // TODO: 09.05.2017 Float.compare() might be a bottleneck
        priorityQueue = new PriorityQueue<>(queueSize, (o1, o2) -> Float.compare(o1.getDistance(), o2.getDistance()));

        this.startNode = startNode;
        this.endNode = endNode;

        this.wrappedNodes = new NodeWrapper[queueSize];
    }

    private NodeWrapper wrapNode(Node node) {
        return new NodeWrapper(node);
    }

    public List<NodeWrapper> calculateShortestPath() {
        if (alreadyCalculated) {
            throw new AlgorithmException("The shortest path was already calculated");
        }
        alreadyCalculated = true;



        Observable.fromIterable(startNode.getEdges())
                .map(edge -> edge.getOtherNode(startNode))
                .doOnNext(node -> {
                    /*If node is already wrapped*/
                    if (wrappedNodes[node.getValue() -1] == null) {
                        wrappedNodes[node.getValue() -1] = wrapNode(node);
                    }
                })
                .map(node -> wrappedNodes[node.getValue() -1])
                .subscribe(node -> {
                    System.out.println(node);
                });

        return new ArrayList<>();
    }

}
