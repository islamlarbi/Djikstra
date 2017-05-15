package djikstra;

import djikstra.core.Algorithm;
import djikstra.core.AlgorithmSuccess;
import djikstra.filehandler.FileExtractor;
import djikstra.filehandler.GraphRawData;
import djikstra.graph.GraphManager;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    GraphRawData graphRawData;
    GraphManager graphManager;

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        long startTime = System.nanoTime();

        Main main = new Main();

        /*Reading from file*/
        FileExtractor fileExtractor = new FileExtractor("big.txt");
        main.graphRawData = fileExtractor.readFile();

        long startTimeMid = System.nanoTime();

        System.out.println("Execution time (read file): " + (startTimeMid - startTime) / 1000000 + "ms");

        /*Generating a graph by creating nodes and edges objects*/
        main.graphManager = new GraphManager(main.graphRawData.getNumberOfNodes());
        main.graphManager.generateNodes();

        for (float[] edges : main.graphRawData.getEdges()) {
            main.graphManager.addEdge((int) edges[0], (int) edges[1], edges[2]);
        }

        /*__________________HERE IS THE MAGIC________________*/
        /*
            Here I go multithreading.
            I create a pool of threads
         */
        ExecutorService executor = Executors.newWorkStealingPool();

        /*queries is an array of 2 elements
        * First one is starting node
        * Second one is the node that we need to find a path to*/
        for (int[] queries : main.graphRawData.getQueries()) {
            /*
                Here I tell the first free thread to execute the algorithm
             */
            executor.execute(() -> {
                Algorithm algorithm = new Algorithm(
                        main.graphRawData.getNumberOfNodes(),
                        main.graphManager.getNodes().get(queries[0] - 1),
                        main.graphManager.getNodes().get(queries[1] - 1));

                AlgorithmSuccess algorithmSuccess = algorithm.calculateShortestPath();
                Collections.reverse(algorithmSuccess.backtracePath);
                logger.info("From {} to {} found the shortest distance {} with path {}",
                        algorithmSuccess.fromNode.getValue(),
                        algorithmSuccess.toNode.getValue(),
                        algorithmSuccess.distance,
                        algorithmSuccess.backtracePath);
            });
        }
        /*I tell the thread pool to stop collecting any new requests for executions*/
        executor.shutdown();

        /*I'm waiting for all the algorithms to finish*/
        executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.MILLISECONDS);

        long endTime = System.nanoTime();

        System.out.println("Execution time (computation): " + (endTime - startTimeMid) / 1000000 + "ms");
        System.out.println("Execution time (total): " + (endTime - startTime) / 1000000 + "ms");

    }
}
