package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

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