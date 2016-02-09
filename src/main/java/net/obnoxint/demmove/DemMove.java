package net.obnoxint.demmove;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

final class DemMove extends Thread {

    private static final String DEMO_EXT = ".dem";
    private static final long SLEEP = 10_000;

    private final File demoSource, demoTarget;

    boolean running = false;

    DemMove() {
        demoSource = new File(Main.demoSource);
        demoTarget = new File(Main.demoTarget);
        demoTarget.mkdirs();
    }

    @Override
    public void run() {

        while (true) {

            running = true;

            final Tasklist tl = Tasklist.byName(Main.exeName);

            if (tl.isEmpty()) {
                final File[] demos = demoSource.listFiles((dir, name) -> name.endsWith(DEMO_EXT));

                for (int i = 0; i < demos.length; i++) {
                    try {
                        Files.move(demos[i].toPath(), new File(demoTarget, demos[i].getName()).toPath());
                    } catch (final IOException e) {}
                }
            }

            running = false;

            try {
                sleep(SLEEP);
            } catch (final InterruptedException e) {}

        }

    }

}
