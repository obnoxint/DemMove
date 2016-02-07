package net.obnoxint.demmove;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

final class DemMove extends Thread {

    private static final String DEMO_SOURCE = "C:\\~\\Left 4 Dead 2\\left4dead2";
    private static final String DEMO_DIR = "dem";
    private static final String DEMO_EXT = ".dem";

    private static final String EXE = "leaft4dead2.exe";
    private static final long SLEEP = 10_000;

    private final File demoSource, demoDir;

    boolean running = false;

    DemMove() {
        demoSource = new File(DEMO_SOURCE);
        demoDir = new File(demoSource, DEMO_DIR);
        demoDir.mkdirs();
    }

    @Override
    public void run() {

        running = true;

        while (true) {

            final Tasklist tl = Tasklist.byName(EXE);

            if (tl.isEmpty()) {
                final File[] demos = demoSource.listFiles((dir, name) -> name.endsWith(DEMO_EXT));

                for (int i = 0; i < demos.length; i++) {
                    try {
                        Files.move(demos[i].toPath(), new File(demoDir, demos[i].getName()).toPath());
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
