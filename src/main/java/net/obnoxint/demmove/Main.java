package net.obnoxint.demmove;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

public final class Main {

    private static final String PROPERTY_FILE = "demmove.properties";

    // properties
    static String exeName;
    static String demoSource;
    static String demoTarget;

    private static boolean quit = false;

    public static void main(final String[] args) {

        // copy properties
        final File propFile = new File(PROPERTY_FILE);
        if (!propFile.exists()) {
            try (InputStream in = Main.class.getResourceAsStream("/" + PROPERTY_FILE)) {
                Files.copy(in, propFile.toPath());
            } catch (final IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        // load properties
        try (FileInputStream in = new FileInputStream("demmove.properties")) {
            final Properties p = new Properties();
            p.load(in);
            exeName = p.getProperty("exeName");
            demoSource = p.getProperty("source");
            demoTarget = p.getProperty("target");
        } catch (final IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // initialize and start DemMove thread
        final DemMove demMove = new DemMove();
        demMove.start();

        // gracefully exit when JVM shuts down
        Runtime.getRuntime().addShutdownHook(new Thread(() -> quit = true));

        // main loop
        while (!quit) {
            try {
                Thread.sleep(10);
            } catch (final InterruptedException e) {}
        }

        // wait for DemMove thread to finish
        while (demMove.running) {};

    }

}
