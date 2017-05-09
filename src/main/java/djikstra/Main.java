package djikstra;

import djikstra.core.Algorithm;
import djikstra.filehandler.FileExtractor;
import djikstra.filehandler.GraphRawData;
import djikstra.graph.GraphManager;
import io.reactivex.Observable;

import java.io.FileNotFoundException;

public class Main {
    GraphRawData graphRawData;
    GraphManager graphManager;

    public static void main(String[] args) throws FileNotFoundException {
        Main main = new Main();
//        long startTime = System.nanoTime();
        FileExtractor fileExtractor = new FileExtractor("computerphile.txt");
        main.graphRawData = fileExtractor.readFile();

//        long startTimeMid = System.nanoTime();

//        System.out.println("Execution time (read file): " + (startTimeMid - startTime) / 1000000 + "ms");

        main.graphManager = new GraphManager(main.graphRawData.getNumberOfNodes());
        main.graphManager.generateNodes();

        Observable.fromArray(main.graphRawData.getEdges())
                .blockingSubscribe(edges -> {
                    main.graphManager.addEdge((int) edges[0], (int) edges[1], edges[2]);
                });

        Observable.fromArray(main.graphRawData.getQueries())
                .blockingSubscribe(queries -> {
                    Algorithm algorithm = new Algorithm(
                            main.graphRawData.getNumberOfNodes(),
                            main.graphManager.getNodes().get(queries[0] -1),
                            main.graphManager.getNodes().get(queries[1] -1));

                    algorithm.calculateShortestPath();
                });

//        long endTime = System.nanoTime();

//        System.out.println("Execution time (instantiation): " + (endTime - startTimeMid) / 1000000 + "ms");
//        System.out.println("Execution time (total): " + (endTime - startTime) / 1000000 + "ms");

    }
}
