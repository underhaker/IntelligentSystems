import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Knapsack {
    private final static double CROSSOVER_PROBABILITY = 0.6;
    private final static double MUTATION_PROBABILITY = 0.2;
    private int populationSize;
    private int numberOfItems;
    private int knapsackCapacity;
    private int generationCounter;
    private double totalFitnessOfGeneration;
    private double[] weightOfItems;
    private double[] valueOfItems;
    private List<String> populationList;
    private List<String> breedPopulationList;
    private int maximumGenerations;
    private List<String> bestSolutionOfGenerationList;
    private List<Double> bestFitnessOfGenerationList;
    private List<Double> fitnessList;

    Knapsack(int n, int m, double[] weightOfItems, double[] valueOfItems) {
        this.populationSize = n * 2;
        this.numberOfItems = n;
        this.populationList = new ArrayList<>();
        this.breedPopulationList = new ArrayList<>();
        this.bestFitnessOfGenerationList = new ArrayList<>();
        this.bestSolutionOfGenerationList = new ArrayList<>();
        this.fitnessList = new ArrayList<>();
        this.weightOfItems = weightOfItems;
        this.valueOfItems = valueOfItems;
        this.knapsackCapacity = m;
        this.generationCounter = 1;
        this.maximumGenerations = 15;
    }

    public static void main(String[] args) {
        int n = 3;
        int m = 5;
        double[] weightOfItems = {3, 1, 2};
        double[] valueOfItems = {2, 5, 3};

        Knapsack knapsack = new Knapsack(n, m, weightOfItems, valueOfItems);
        knapsack.solveKnapsack();
    }

    public void solveKnapsack() {
        makePopulation();
        evaluatePopulation();
        bestSolutionOfGenerationList.add(populationList.get(getBestSolution()));
        bestFitnessOfGenerationList.add(getBestFitness());
        while (generationCounter <= maximumGenerations) {
            makeFurtherGenerations();
        }
        System.out.println("4th generation:" + bestFitnessOfGenerationList.get(3));
        System.out.println("7th generation:" + bestFitnessOfGenerationList.get(7));
        System.out.println("8th generation:" + bestFitnessOfGenerationList.get(8));
        System.out.println("10th generation:" + bestFitnessOfGenerationList.get(9));
        System.out.println("final generation solution:" + bestFitnessOfGenerationList.get(generationCounter - 1));
    }

    private void makeFurtherGenerations() {
        for (int i = 0; i < populationSize / 2; i++) {
            breedPopulation();
        }
        generationCounter++;
        fitnessList.clear();
        evaluateBreedPopulation();
        for (int i = 0; i < populationSize; i++) {
            populationList.set(i, breedPopulationList.get(i));
        }
        breedPopulationList.clear();
        bestSolutionOfGenerationList.add(populationList.get(getBestSolution()));
        bestFitnessOfGenerationList.add(getBestFitness());

    }

    private void makePopulation() {
        for (int i = 0; i < populationSize; i++) {
            populationList.add(makeGene());
        }
    }

    private String makeGene() {
        StringBuilder gene = new StringBuilder(numberOfItems);
        char c;
        for (int i = 0; i < numberOfItems; i++) {
            c = '0';
            double randomNumber = Math.random();
            if (randomNumber > 0.5) {
                c = '1';
            }
            gene.append(c);
        }
        return gene.toString();
    }

    private double calculateFitnessFunction(String gene) {
        double totalWeight = 0;
        double totalValue = 0;
        double fitnessValue = 0;
        double difference;
        char c = '0';
        for (int i = 0; i < numberOfItems; i++) {
            if (gene.charAt(i) == '1') {
                totalWeight = totalWeight + weightOfItems[i];
                totalValue = totalValue + valueOfItems[i];
            }
        }
        difference = knapsackCapacity - totalWeight;
        if (difference >= 0) {
            fitnessValue = totalValue;
        }
        return fitnessValue;
    }

    private void evaluatePopulation() {
        totalFitnessOfGeneration = 0;
        for (int i = 0; i < populationSize; i++) {
            double temporaryFitness = calculateFitnessFunction(populationList.get(i));
            fitnessList.add(temporaryFitness);
            totalFitnessOfGeneration = totalFitnessOfGeneration + temporaryFitness;
        }
    }

    private void evaluateBreedPopulation() {
        totalFitnessOfGeneration = 0;
        for (int i = 0; i < populationSize; i++) {
            double temporaryFitness = calculateFitnessFunction(breedPopulationList.get(i));
            fitnessList.add(temporaryFitness);
            totalFitnessOfGeneration = totalFitnessOfGeneration + temporaryFitness;
        }
    }

    private double getBestFitness() {
        double bestFitness = 0;
        for (Double currentFitness : fitnessList) {
            if (currentFitness > bestFitness) {
                bestFitness = currentFitness;
            }
        }
        return bestFitness;
    }

    private int getBestSolution() {
        int bestPosition = 0;
        double currentFitness;
        double bestFitness = 0;
        for (int i = 0; i < populationList.size(); i++) {
            currentFitness = calculateFitnessFunction(populationList.get(i));
            if (currentFitness > bestFitness) {
                bestFitness = currentFitness;
                bestPosition = i;
            }
        }
        return bestPosition;
    }

    private void breedPopulation() {
        int gene1;
        int gene2;
        if (populationSize % 2 == 1) {
            breedPopulationList.add(bestSolutionOfGenerationList.get(generationCounter - 1));
        }
        gene1 = selectGene();
        gene2 = selectGene();

        crossOverGenes(gene1, gene2);
    }


    private int selectGene() {
        double randomNumber = Math.random() * totalFitnessOfGeneration;
        for (int i = 0; i < populationSize; i++) {
            if (randomNumber <= fitnessList.get(i)) {
                return i;
            }
            randomNumber = randomNumber - fitnessList.get(i);
        }
        return 0;
    }

    private void crossOverGenes(int gene1, int gene2) {
        String newGene1;
        String newGene2;
        double randomCrossover = Math.random();
        if (randomCrossover <= CROSSOVER_PROBABILITY) {
            Random generator = new Random();
            int crossoverPoint = generator.nextInt(numberOfItems) + 1;
            newGene1 = populationList.get(gene1).substring(0, crossoverPoint) + populationList.get(gene2).substring(crossoverPoint);
            newGene2 = populationList.get(gene2).substring(0, crossoverPoint) + populationList.get(gene1).substring(crossoverPoint);
            breedPopulationList.add(newGene1);
            breedPopulationList.add(newGene2);
        } else {
            breedPopulationList.add(populationList.get(gene1));
            breedPopulationList.add(populationList.get(gene2));
        }
        mutateGene();
    }

    private void mutateGene() {
        double randomMutation = Math.random();
        if (randomMutation <= MUTATION_PROBABILITY) {
            String mutatedGene;
            String newMutatedGene;
            Random generator = new Random();
            int mutationPoint = 0;
            double whichGene = Math.random() * 100;
            if (whichGene <= 50) {
                mutatedGene = breedPopulationList.get(breedPopulationList.size() - 1);
                mutationPoint = generator.nextInt(numberOfItems);
                if (mutatedGene.substring(mutationPoint, mutationPoint + 1).equals("1")) {
                    newMutatedGene = mutatedGene.substring(0, mutationPoint) + "0" + mutatedGene.substring(mutationPoint);
                    breedPopulationList.set(breedPopulationList.size() - 1, newMutatedGene);
                }
                if (mutatedGene.substring(mutationPoint, mutationPoint + 1).equals("0")) {
                    newMutatedGene = mutatedGene.substring(0, mutationPoint) + "1" + mutatedGene.substring(mutationPoint);
                    breedPopulationList.set(breedPopulationList.size() - 1, newMutatedGene);
                }
            }
            if (whichGene > 50) {
                mutatedGene = breedPopulationList.get(breedPopulationList.size() - 2);
                mutationPoint = generator.nextInt(numberOfItems);
                if (mutatedGene.substring(mutationPoint, mutationPoint + 1).equals("1")) {
                    newMutatedGene = mutatedGene.substring(0, mutationPoint) + "0" + mutatedGene.substring(mutationPoint);
                    breedPopulationList.set(breedPopulationList.size() - 1, newMutatedGene);
                }
                if (mutatedGene.substring(mutationPoint, mutationPoint + 1).equals("0")) {
                    newMutatedGene = mutatedGene.substring(0, mutationPoint) + "1" + mutatedGene.substring(mutationPoint);
                    breedPopulationList.set(breedPopulationList.size() - 1, newMutatedGene);
                }
            }
        }
    }


}
