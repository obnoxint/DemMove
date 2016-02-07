package net.obnoxint.demmove;

public final class Main {

    private static boolean quit = false;

    public static void main(final String[] args) {

        final DemMove demMove = new DemMove();
        demMove.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> quit = true));

        while (!quit) {
            try {
                Thread.sleep(10);
            } catch (final InterruptedException e) {}
        }

        while (demMove.running) {};

    }

}
