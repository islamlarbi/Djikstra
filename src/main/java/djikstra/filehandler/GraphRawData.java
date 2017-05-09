package djikstra.filehandler;

/*
    This class stores raw data from the text file
 */
public class GraphRawData {
    private int numberOfNodes;
    private int numberOfEdges;
    private int queryNumber;

    private float[][] edges;
    private int[][] queries;

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    public void setNumberOfEdges(int numberOfEdges) {
        this.numberOfEdges = numberOfEdges;
    }

    public int getQueryNumber() {
        return queryNumber;
    }

    public void setQueryNumber(int queryNumber) {
        this.queryNumber = queryNumber;
    }

    public float[][] getEdges() {
        return edges;
    }

    public void setEdges(float[][] edges) {
        this.edges = edges;
    }

    public int[][] getQueries() {
        return queries;
    }

    public void setQueries(int[][] queries) {
        this.queries = queries;
    }
}
