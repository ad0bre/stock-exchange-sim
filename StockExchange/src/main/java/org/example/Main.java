package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private ArrayList<StockOffer> availableShares = generateShares();

    private ArrayList<StockOffer> generateShares() {
        Random random = new Random();
        ArrayList<String> stockTypes = new ArrayList<String>(Arrays.asList("Apple", "Microsoft", "Google", "Amazon"));
        ArrayList<StockOffer> offers = new ArrayList<>();
        for (String type : stockTypes) {
            var numberOfShares = random.nextInt(25);
            for (int i = 0; i < numberOfShares; i++) {
                offers.add(new StockOffer(type, random.nextDouble() % 100, random.nextInt(50)));
            }
        }

        return offers;
    }
;
    public static void main(String[] args) {
        System.out.println("THREAD SIMULATOR\nWrite the number of threads you want to create (default = 1):");
        Scanner scanner = new Scanner(System.in);
        int threads = 1;
        try {
            int input = Integer.parseInt(scanner.nextLine());
            threads = input > 0 ? input : threads;
            System.out.println("Creating " + threads + " threads...");
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid number");
            System.exit(1);
        }

        ExecutorService executor = Executors.newFixedThreadPool(threads);

        executor.shutdown();
    }

}