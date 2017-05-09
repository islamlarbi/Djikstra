package djikstra.core;

import djikstra.graph.Node;

public class NodeWrapper {
    private Node coreNode;
    private NodeWrapper previousNode = null;
    private float distance = Float.MAX_VALUE;

    public NodeWrapper(Node coreNode) {
        this.coreNode = coreNode;
    }

    public Node getCoreNode() {
        return coreNode;
    }

    public NodeWrapper getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(NodeWrapper previousNode) {
        this.previousNode = previousNode;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "NodeWrapper{" +
                "coreNode=" + coreNode +
                ", previousNode=" + (previousNode != null?previousNode.getCoreNode():null) +
                ", distance=" + distance +
                '}';
    }
}
