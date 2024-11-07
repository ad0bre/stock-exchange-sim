package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

//    private CopyOnWriteArrayList<Transaction> transactions = new CopyOnWriteArrayList<>();
//
//    private ConcurrentHashMap<String, CopyOnWriteArrayList<StockOffer>> stockMarket = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        System.out.println("STOCK EXCHANGE SIMULATOR\nWrite the number of clients you want to create (default = 1):");
        Scanner scanner = new Scanner(System.in);
        int threads = 1;
        try {
            int input = Integer.parseInt(scanner.nextLine());
            threads = input > 0 ? input : threads;
            System.out.println("Creating " + threads + " threads...");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
            System.exit(1);
        }

        List<Client> clients = new ArrayList<>();
        Exchange exchange = new Exchange(clients);

        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            Client client = new Client(exchange);
            clients.add(client);
            executor.submit(client);
        }

        executor.shutdown();
    }

}