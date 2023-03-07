package tracker;

import tracker.input.MainLoopHandler;

import java.util.Scanner;

public class Main {
    private static final MainLoopHandler mainLoopHandler = MainLoopHandler.getInstance();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Learning Progress Tracker");

        // Do main loop.
        mainLoopHandler.doMainLoop(scanner);

        scanner.close();
    }
}
