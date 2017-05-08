package djikstra.graph;

public class Edge {
    private float weight;
    private Node firstNode;
    private Node secondNode;

    public Edge(float weight) {
        this.weight = weight;
    }

    public void setFirstNode(Node firstNode) {
        this.firstNode = firstNode;
    }

    public void setSecondNode(Node secondNode) {
        this.secondNode = secondNode;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Node getOtherNode(Node node) {
        if (firstNode == node) {
            return secondNode;
        } else if (secondNode == node) {
            return firstNode;
        } else {
            throw new IllegalArgumentException(node.toString() + " is neither firstNode or secondNode");
        }
    }
}
