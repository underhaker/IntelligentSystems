import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class KNN {
    private final static double TRAINING_PERCENTAGE = 0.2;
    private final static String DATA_SET = "resources/iris.data.txt";
    //
//    private List<Record> trainingRecords;
//    private List<Record> testingRecords;
    private List<Integer> usedIndexes;
    private int testingSize;

    public static void main(String[] args) {
        KNN knn = new KNN();
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
//        int k = 3;
        knn.solveKnn(k);
    }

    public void solveKnn(int k) {
        double prediction;
        List<Record> records = readFromFile(DATA_SET);
        usedIndexes = new ArrayList<>();
        List<Record> testingRecords = getTestingRecords(records);
        List<Record> trainingRecords = getTrainingRecords(records);
        for (Record testingRecord : testingRecords) {
            List<Record> neighbours = findKNearestNeighbours(trainingRecords, testingRecord, k);
            String className = classify(neighbours);
            testingRecord.setPredictedClassName(className);
        }
        prediction = getPrediction(testingRecords);
        System.out.println(prediction * 100 + "%");
    }

    private String classify(List<Record> neighbours) {
        HashMap<String, Record> map = new HashMap<>();
        double normalizedDistance;
        for (Record neighbour : neighbours) {
            if (!map.containsKey(neighbour.getClassName())) {
                normalizedDistance = 1 / neighbour.getDistance();
                neighbour.setCountSize(neighbour.getCountSize() + 1);
                neighbour.setDistance(normalizedDistance);
                map.put(neighbour.getClassName(), neighbour);
            } else {
                Record record = map.get(neighbour.getClassName());
                double distance = record.getDistance();
                normalizedDistance = 1 / neighbour.getDistance();
                distance += normalizedDistance;
                record.setDistance(distance);
                record.setCountSize(record.getCountSize() + 1);
                map.put(neighbour.getClassName(), record);
            }
        }

        double maxValue = 0;
        int frequencyCounter = 0;
        Set<String> classNameSet = map.keySet();
        Iterator<String> iterator = classNameSet.iterator();
        String predictedClassName = null;
        while (iterator.hasNext()) {
            String className = iterator.next();
            Record record = map.get(className);
            double distance = record.getDistance();
            if (distance > maxValue && record.getCountSize() > frequencyCounter) {
                maxValue = distance;
                frequencyCounter = record.getCountSize();
                predictedClassName = className;
            }
        }
        return predictedClassName;
    }

    private List<Record> findKNearestNeighbours(List<Record> trainingRecords, Record testingRecord, int k) {
        List<Record> neighbours = new ArrayList<>();
        Record trainingRecord;
        for (int index = 0; index < k; index++) {
            trainingRecord = trainingRecords.get(index);
            trainingRecord.setDistance(getDistance(trainingRecord, testingRecord));
            neighbours.add(trainingRecord);
        }
        for (int index = k; index < trainingRecords.size(); index++) {
            trainingRecord = trainingRecords.get(index);
            trainingRecord.setDistance(getDistance(trainingRecord, testingRecord));
            int maxIndex = 0;
            for (int j = 1; j < k; j++) {
                if (neighbours.get(j).getDistance() > neighbours.get(maxIndex).getDistance()) {
                    maxIndex = j;
                }
            }
            if (neighbours.get(maxIndex).getDistance() > trainingRecord.getDistance()) {
                neighbours.set(maxIndex, trainingRecord);
            }
        }
        return neighbours;
    }

    private double getDistance(Record trainingRecord, Record testingRecord) {
        double sum = 0;
        for (int i = 0; i < trainingRecord.getAttributes().length; i++) {
            sum += Math.pow(trainingRecord.getAttributes()[i] - testingRecord.getAttributes()[i], 2);
        }
        return Math.sqrt(sum);
    }

    private double getPrediction(List<Record> testingRecords) {
        int correctPredictions = 0;
        for (Record testingRecord : testingRecords) {
            if (testingRecord.getClassName().equals(testingRecord.getPredictedClassName())) {
                correctPredictions++;
            }
        }
        return (double) correctPredictions / testingRecords.size();
    }

    private List<Record> getTestingRecords(List<Record> records) {
        Random generator = new Random();
        Double testingSizeDouble = records.size() * TRAINING_PERCENTAGE;
        this.testingSize = testingSizeDouble.intValue();
        int counter = 0;
        int currentIndex;
        List<Record> testingRecords = new ArrayList<>();
        while (counter < testingSize) {
            while (true) {
                currentIndex = generator.nextInt(records.size());
                if (!usedIndexes.contains(currentIndex)) {
                    break;
                }
            }
            testingRecords.add(records.get(currentIndex));
            usedIndexes.add(currentIndex);
            counter++;
        }
        return testingRecords;
    }

    private List<Record> getTrainingRecords(List<Record> records) {
        List<Record> trainingRecords = new ArrayList<>();
        int index = 0;
        int estimatedTrainingSize = records.size() - testingSize;
        while (trainingRecords.size() < estimatedTrainingSize) {
            if (!usedIndexes.contains(index)) {
                trainingRecords.add(records.get(index));
                usedIndexes.add(index);
            }
            index++;
        }
        return trainingRecords;
    }

    private List<Record> readFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            String[] input;
            List<Record> records = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                input = line.split(",");
                Record record = new Record();
                double[] attributes = new double[4];
                for (int i = 0; i < 4; i++) {
                    attributes[i] = Double.parseDouble(input[i]);
                }
                record.setAttributes(attributes);
                record.setClassName(input[4]);
                records.add(record);
            }
            return records;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    class Record {
        private double distance;
        private double[] attributes;
        private int countSize;
        private String className;
        private String predictedClassName;

        public int getCountSize() {
            return countSize;
        }

        public void setCountSize(int countSize) {
            this.countSize = countSize;
        }

        double getDistance() {
            return distance;
        }

        void setDistance(double distance) {
            this.distance = distance;
        }

        double[] getAttributes() {
            return attributes;
        }

        void setAttributes(double[] attributes) {
            this.attributes = attributes;
        }

        String getClassName() {
            return className;
        }

        void setClassName(String className) {
            this.className = className;
        }

        String getPredictedClassName() {
            return predictedClassName;
        }

        void setPredictedClassName(String predictedClassName) {
            this.predictedClassName = predictedClassName;
        }
    }
}
