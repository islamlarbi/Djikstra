package djikstra;

import djikstra.filehandler.FileExtractor;
import djikstra.filehandler.GraphRawData;
import djikstra.graph.GraphManager;
import io.reactivex.Observable;

import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.nanoTime();
        FileExtractor fileExtractor = new FileExtractor("big.txt");
        GraphRawData graphRawData = fileExtractor.readFile();

        long startTimeMid = System.nanoTime();

        System.out.println("Execution time (read file): " + (startTimeMid - startTime) / 1000000 + "ms");

        GraphManager graphManager = new GraphManager(graphRawData.getNumberOfNodes());
        graphManager.generateNodes();

        Observable.fromArray(graphRawData.getEdges())
                .blockingSubscribe(edges -> {
                    graphManager.addEdge((int) edges[0], (int) edges[1], edges[2]);
                });

        long endTime = System.nanoTime();

        System.out.println("Execution time (instantiation): " + (endTime - startTimeMid) / 1000000 + "ms");
        System.out.println("Execution time (total): " + (endTime - startTime) / 1000000 + "ms");

    }
}
