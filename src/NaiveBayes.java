import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NaiveBayes {
    private static final int ATTRIBUTES_SIZE = 16;
    private static final int CROSS_FOLD_VALIDATION_SIZE = 10;
    private final static String DATA_SET = "resources/congress.data.txt";
    private int testingSize;
    private List<Integer> usedTestingIndexes;
    private List<Integer> usedIndexes;

    public static void main(String[] args) {
        NaiveBayes naiveBayer = new NaiveBayes();
        naiveBayer.solveNaiveBayer();
    }

    private void solveNaiveBayer() {
        double sumPredictions = 0;
        double predictions;
        List<Record> records = readFromFile(DATA_SET);
        this.usedTestingIndexes = new ArrayList<>();
        for (int i = 0; i < CROSS_FOLD_VALIDATION_SIZE; i++) {
            predictions = 0;
            this.usedIndexes = new ArrayList<>();
            List<Record> testingRecords = getTestingRecords(records);
            List<Record> trainingRecords = getTrainingRecords(records);
            for (Record testingRecord : testingRecords) {
                double posteriorDemocrat = findPosterior(trainingRecords, testingRecord, "democrat");
                double posteriorRepublican = findPosterior(trainingRecords, testingRecord, "republican");
                if (posteriorDemocrat >= posteriorRepublican) {
                    testingRecord.setPredictedClassName("democrat");
                } else {
                    testingRecord.setPredictedClassName("republican");
                }
                if (testingRecord.getClassName().equals(testingRecord.getPredictedClassName())) {
                    predictions++;
                }
            }
            sumPredictions += predictions / testingRecords.size();
        }
        System.out.println((sumPredictions / CROSS_FOLD_VALIDATION_SIZE) * 100 + "%");
    }


    private double findPosterior(List<Record> trainingRecords, Record testingRecord, String className) {
        long classCounter = trainingRecords.stream().filter(record -> record.getClassName().equals(className) && !record.isUnknown).count();
        long trainingSize = trainingRecords.stream().filter(record -> !record.isUnknown).count();
        double posterior = ((double) classCounter / trainingSize);
        double pConditional;
        for (int i = 0; i < ATTRIBUTES_SIZE; i++) {
            pConditional = getPConditional(trainingRecords, testingRecord, className, i);
            posterior = posterior * pConditional;
        }
        return posterior;
    }

    private double getPConditional(List<Record> trainingRecords, Record testingRecord, String className, int attributeIndex) {
        if (testingRecord.getAttributes()[attributeIndex] == 0) {
            return 1;
        }
        double pConditional;
        long classCount = trainingRecords.stream().filter(record -> record.getClassName().equals(className) && record.getAttributes()[attributeIndex] == testingRecord.getAttributes()[attributeIndex]).count();
        long classSize = trainingRecords.stream().filter(record -> record.getClassName().equals(className) && record.getAttributes()[attributeIndex] != 0).count();
        pConditional = (double) classCount / classSize;
        return pConditional;
    }

    private List<Record> getTestingRecords(List<Record> records) {
        Random generator = new Random();
        Double testingSizeDouble = records.size() * (CROSS_FOLD_VALIDATION_SIZE / 100.0);
        this.testingSize = testingSizeDouble.intValue();
        int counter = 0;
        int currentIndex;
        List<Record> testingRecords = new ArrayList<>();
        while (counter < testingSize) {
            while (true) {
                currentIndex = generator.nextInt(records.size());
                if (!usedTestingIndexes.contains(currentIndex)) {
                    break;
                }
            }
            testingRecords.add(records.get(currentIndex));
            usedTestingIndexes.add(currentIndex);
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
                input = line.replaceAll("'", "").split(",");
                Record record = new Record();
                boolean isUnknown = false;
                int[] attributes = new int[ATTRIBUTES_SIZE];
                for (int i = 0; i < ATTRIBUTES_SIZE; i++) {
                    switch (input[i]) {
                        case "?":
                            attributes[i] = 0;
                            isUnknown = true;
                            break;
                        case "n":
                            attributes[i] = 1;
                            break;
                        case "y":
                            attributes[i] = 2;
                            break;
                    }
                }
                record.setAttributes(attributes);
                record.setClassName(input[input.length - 1]);
                record.setUnknown(isUnknown);
                records.add(record);
            }
            return records;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    class Record {
        private String className;
        private String predictedClassName;
        private int[] attributes;
        private boolean isUnknown;

        public boolean isUnknown() {
            return isUnknown;
        }

        public void setUnknown(boolean unknown) {
            isUnknown = unknown;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getPredictedClassName() {
            return predictedClassName;
        }

        public void setPredictedClassName(String predictedClassName) {
            this.predictedClassName = predictedClassName;
        }

        public int[] getAttributes() {
            return attributes;
        }

        public void setAttributes(int[] attributes) {
            this.attributes = attributes;
        }
    }
}
