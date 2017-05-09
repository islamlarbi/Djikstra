package djikstra.core;

import djikstra.graph.Edge;
import djikstra.graph.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Algorithm {
    private static Logger logger = LoggerFactory.getLogger(Algorithm.class);

    private Node startNode;
    private Node endNode;

    private boolean alreadyCalculated = false;

    private PriorityQueue<NodeWrapper> priorityQueue;
    private NodeWrapper[] wrappedNodes;
    private NodeWrapper[] alreadyBeenToNodes;

    public Algorithm(int queueSize, Node startNode, Node endNode) {
        // TODO: 09.05.2017 Float.compare() might be a bottleneck
        priorityQueue = new PriorityQueue<>(queueSize, (o1, o2) -> {
            return Float.compare(o1.getDistance(), o2.getDistance());
        });

        this.startNode = startNode;
        this.endNode = endNode;

        this.wrappedNodes = new NodeWrapper[queueSize];
        this.alreadyBeenToNodes = new NodeWrapper[queueSize];
    }

    private NodeWrapper wrapNode(Node node) {
        return new NodeWrapper(node);
    }

    private NodeWrapper wrapStartNode(Node node) {
        NodeWrapper nodeWrapper = wrapNode(node);
        nodeWrapper.setDistance(0);

        return nodeWrapper;
    }

    public List<NodeWrapper> calculateShortestPath() {
        if (alreadyCalculated) {
            throw new AlgorithmException("The shortest path was already calculated");
        }
        alreadyCalculated = true;

        NodeWrapper startNodeWrapper = wrapStartNode(startNode);
        wrappedNodes[startNode.getValue() - 1] = startNodeWrapper;
        priorityQueue.add(startNodeWrapper);

        NodeWrapper finalNode = this.startAlgorithm();
        if (finalNode != null) {
            logger.debug("__________________");
            logger.debug("SUCCESS!!!");
            logger.debug("{}", finalNode);
            logger.debug("{}", backtraceNodePath(finalNode));
        } else {
            logger.debug("__________________");
            logger.debug("FAILED!!!");
        }

        return new ArrayList<>();
    }

    private NodeWrapper startAlgorithm() {
        NodeWrapper focusNode = null;

        while (!priorityQueue.isEmpty()) {
//        for (int i = 0; i < 10; i++) {
            focusNode = priorityQueue.poll();

            NodeWrapper foundEndNode = calculateNodesFrom(focusNode);

            if (foundEndNode != null) {
                return foundEndNode;
            }

            alreadyBeenToNodes[focusNode.getCoreNode().getValue() - 1] = focusNode;
        }

        return focusNode;
    }

    private NodeWrapper calculateNodesFrom(NodeWrapper focusNode) {
        logger.debug("___________");
        logger.debug("Focus node: {}", focusNode);
        for (Edge edge : focusNode.getCoreNode().getEdges()) {
            Node otherNode = edge.getOtherNode(focusNode.getCoreNode());
            logger.debug("Child node: {}", otherNode);

            /*Here i check if the connecting node is the node that was previously analyzed
            * If yes I skip calculating this node*/
            if (alreadyBeenToNodes[otherNode.getValue() -1] != null) {
                logger.debug("Already been to this node. Skipping...");
                continue;
            }

            /*If otherNode is not wrapped then I add it to the wrapped nodes pool*/
            if (wrappedNodes[otherNode.getValue() - 1] == null) {
                wrappedNodes[otherNode.getValue() - 1] = wrapNode(otherNode);
                logger.debug("Adding to a priority queue");
                priorityQueue.add(wrappedNodes[otherNode.getValue() - 1]);
            }

            NodeWrapper otherNodeWrapped = wrappedNodes[otherNode.getValue() - 1];
            float distanceToOtherNode = (float) focusNode.getDistance() + edge.getWeight();

            if (otherNodeWrapped.getDistance() > distanceToOtherNode) {
                otherNodeWrapped.setDistance(distanceToOtherNode);
                otherNodeWrapped.setPreviousNode(focusNode);
            }

            if (otherNodeWrapped.getCoreNode() == endNode) {
                return otherNodeWrapped;
            }
        }

        return null;
    }

    private List<Integer> backtraceNodePath(NodeWrapper nodeWrapper) {
        List<Integer> result = new LinkedList<>();

        NodeWrapper currentNode = nodeWrapper;
        while (true) {
            result.add(currentNode.getCoreNode().getValue());

            if (currentNode.getPreviousNode() == null) {
                break;
            }
            currentNode = currentNode.getPreviousNode();
        }

        return result;
    }

}
