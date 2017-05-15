package djikstra.filehandler;

import io.reactivex.Observable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileExtractor {
    private String filePath;

    public FileExtractor(String filePath) {
        this.filePath = "src/main/resources/" + filePath;
    }

    public GraphRawData readFile() throws FileNotFoundException {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath() + " does not exist");
        }

        Scanner scanner = new Scanner(file);

        GraphRawData data = new GraphRawDataExtractor(scanner).extractData();

        scanner.close();

        return data;
    }

    private static class GraphRawDataExtractor {
        private GraphRawData graphRawData = new GraphRawData();
        private Scanner scanner;
        private String currentLine;

        public GraphRawDataExtractor(Scanner scanner) {
            this.scanner = scanner;
        }

        public GraphRawData extractData() {
            currentLine = scanner.nextLine();

            extractHeader();
            graphRawData.setEdges(new float[graphRawData.getNumberOfEdges()][3]);
            graphRawData.setQueries(new int[graphRawData.getQueryNumber()][2]);

            currentLine = scanner.nextLine();

            /*
                Getting every line with edge data from file
             */
            for (int index = 0; index < graphRawData.getNumberOfEdges(); index++) {
                extractEdge(index);
                currentLine = scanner.nextLine();
            }

            /*
                Getting every line with query data from file
             */
            for (int index = 0; index < graphRawData.getQueryNumber(); index++) {
                extractQuery(index);

                if (index >= graphRawData.getQueryNumber() - 1) {
                    continue;
                }

                currentLine = scanner.nextLine();
            }

            return graphRawData;
        }

        public void extractHeader() {
            String[] rawNumbers = currentLine.split(" ");

            if (rawNumbers.length != 3) {
                throw new DataExctractingException("extractHeader() - currentLine: " + currentLine + " does not have 3 numbers");
            }

            graphRawData.setNumberOfNodes(Integer.parseInt(rawNumbers[0]));
            graphRawData.setNumberOfEdges(Integer.parseInt(rawNumbers[1]));
            graphRawData.setQueryNumber(Integer.parseInt(rawNumbers[2]));
        }

        public void extractEdge(int index) {
            String[] rawNumbers = currentLine.split(" ");

            if (rawNumbers.length != 3) {
                throw new DataExctractingException("extractEdge() - currentLine: " + currentLine + " does not have 3 numbers");
            }

            float[] arr = graphRawData.getEdges()[index];

            arr[0] = Integer.parseInt(rawNumbers[0]);
            arr[1] = Integer.parseInt(rawNumbers[1]);
            arr[2] = Float.parseFloat(rawNumbers[2]);
        }

        public void extractQuery(int index) {
            String[] rawNumbers = currentLine.split(" ");

            if (rawNumbers.length != 2) {
                throw new DataExctractingException("extractQuery() - currentLine: " + currentLine + " does not have 3 numbers");
            }

            int[] arr = graphRawData.getQueries()[index];

            arr[0] = Integer.parseInt(rawNumbers[0]);
            arr[1] = Integer.parseInt(rawNumbers[1]);
        }
    }
}
